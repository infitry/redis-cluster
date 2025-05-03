package com.infitry.rediscluster.service.factory;

import com.infitry.rediscluster.model.Tester;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@UtilityClass
public class TesterFactory {
    public List<Tester> createTesters() {
        var testers = new ArrayList<Tester>();
        IntStream.range(0, 10000).forEach(i -> {
            var randomAge = new Random().nextInt(100);
            var tester = Tester.builder()
                    .id(java.util.UUID.randomUUID())
                    .name("Tester " + i)
                    .age(randomAge)
                    .address("Address " + i)
                    .phone("010-" + i)
                    .email("tester" + i + "@gmail.com")
                    .gender(Tester.Gender.MALE)
                    .nationality("Korea")
                    .build();

            testers.add(tester);
        });
        return testers;
    }
}
