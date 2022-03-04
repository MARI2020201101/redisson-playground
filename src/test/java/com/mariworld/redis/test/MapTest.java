package com.mariworld.redis.test;

import com.mariworld.redis.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

public class MapTest extends BaseTest{

    @Test
    public void mapTest(){
        RMapReactive<String, String> map = this.redissonClient.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "sam");
        Mono<String> city = map.put("city", "london");
        Mono<String> age = map.put("age", "83");
        StepVerifier.create(name.concatWith(age).concatWith(city))
                .verifyComplete();
    }

    @Test
    public void mapTest2(){
        RMapReactive<String, String> map = this.redissonClient.getMap("user:2", StringCodec.INSTANCE);

        Map<String, String> javamap = Map.of(
                "name", "john",
                "age", "30",
                "city", "paris"
        );

        StepVerifier.create(map.putAll(javamap).then())
                .verifyComplete();
    }

    @Test
    public void mapTest3(){
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = this.redissonClient.getMap("users",codec );

        Student student1 = new Student("kim1",31,"london1");
        Student student2 = new Student("kim2",32,"london2");
        Student student3 = new Student("kim3",33,"london3");
        Mono<Student> mono1 = map.put(1,student1);
        Mono<Student> mono2 = map.put(2,student2);
        Mono<Student> mono3 = map.put(3, student3);
        StepVerifier.create(mono1.concatWith(mono2).concatWith(mono3).then())
                .verifyComplete();

    }
}
