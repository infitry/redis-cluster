## Redis cluster 를 테스트 합니다.

---

- dockerc-compose/redis-cluster.yml 를 실행하여 레디스 컨테이너를 실행 합니다.

- 다음 명령어를 실행하여 cluster 구성을 완료 합니다.
``` linux
docker exec -it redis-node-1 redis-cli --cluster create \
  redis-node-1:6379 \
  redis-node-2:6380 \
  redis-node-3:6381 \
  redis-node-4:6382 \
  redis-node-5:6383 \
  redis-node-6:6384 \
  --cluster-replicas 1
```

- 다음 명령어를 실행하여 클러스터 구성을 확인 합니다.
``` linux
docker exec -it redis-node-1 redis-cli -p 6379 cluster nodes
```