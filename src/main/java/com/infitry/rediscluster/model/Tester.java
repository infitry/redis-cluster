package com.infitry.rediscluster.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Tester {
    UUID id;
    String name;
    int age;
    String address;
    String phone;
    String email;
    Gender gender;
    String nationality;

    public enum Gender {
        MALE, FEMALE;
    }
}
