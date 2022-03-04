package com.mariworld.redis.test;

import com.mariworld.redis.test.BaseTest;
import com.mariworld.redis.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class KeyValueObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest(){
        Student student = new Student("marsha", 33, "seoul");
        RBucketReactive<Object> bucket = this.redissonClient.getBucket("student:1" , JsonJacksonCodec.INSTANCE);
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueObjectTestV2(){
        Student student = new Student("marsha", 33, "seoul", List.of(1,2,3));
        RBucketReactive<Object> bucket = this.redissonClient.getBucket("student:1" , new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
