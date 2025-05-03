package com.infitry.rediscluster.config;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        var nodes = redisProperties.getCluster().getNodes();
        var configuration = new RedisClusterConfiguration(nodes);

        // topology refresh option
        var topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(60)) // 주기적으로 토폴로지 정보를 새로고침하여 얻어온다.
                .enableAllAdaptiveRefreshTriggers() // 모든 refresh 이벤트에 대해 토폴로지 갱신을 실행한다.
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30))
                .build();

        // clusterClientOptions
        var clientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .build();

        var clientConfig = LettuceClientConfiguration
                .builder()
                .clientOptions(clientOptions)
                /**
                 MASTER: 마스터에서만 읽기 (최신 데이터 일관성 보장)
                 MASTER_PREFERRED: 마스터 우선, 마스터가 없을 경우 복제본에서 읽기
                 UPSTREAM: 업스트림에서만 읽기
                 UPSTREAM_PREFERRED: 업스트림 우선, 업스트림이 없을 경우 복제본에서 읽기
                 REPLICA_PREFERRED: 복제본 우선, 복제본이 없을 경우 마스터에서 읽기
                 REPLICA: 복제본에서만 읽기
                 LOWEST_LATENCY: 최소 지연 시간 우선 (지연 시간이 가장 낮은 노드에서 읽기)
                 ANY: 가용한 모든 노드에서 읽기
                 ANY_REPLICA: 복제본에서만 읽기 없거나 비정상이면 마스터에서 읽을 수도 있다.
                 */
                .readFrom(ReadFrom.REPLICA)
                .build();

        return new LettuceConnectionFactory(configuration, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
