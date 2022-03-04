package com.mariworld.redis.test;

import com.mariworld.redis.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class MapCacheTest extends BaseTest{

    @Test
    public void mapcacheTest(){
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> map = this.redissonClient.getMapCache("users:cache", codec);

        Student student1 = new Student("jade",31,"london");
        Student student2 = new Student("lucy",32,"paris");
        Student student3 = new Student("kim",33,"tokyo");

        Mono<Student> mono1 = map.put(1,student1,5, TimeUnit.SECONDS);
        Mono<Student> mono2 = map.put(2,student2,10, TimeUnit.SECONDS);
        Mono<Student> mono3 = map.put(3, student3,15, TimeUnit.SECONDS);

        StepVerifier.create(mono1.concatWith(mono2).concatWith(mono3).then())
                .verifyComplete();

        sleep(3000);

        map.get(1).doOnNext(System.out::println).subscribe();
        map.get(2).doOnNext(System.out::println).subscribe();
        map.get(3).doOnNext(System.out::println).subscribe();

        sleep(3000);

        System.out.println("-------");

        map.get(1).doOnNext(System.out::println).subscribe();
        map.get(2).doOnNext(System.out::println).subscribe();
        map.get(3).doOnNext(System.out::println).subscribe();
    }
}
