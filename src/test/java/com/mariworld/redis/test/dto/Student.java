package com.mariworld.redis.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student{

    private String name;
    private int age;
    private String city;
    private List<Integer> marks;

    public Student(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
}
