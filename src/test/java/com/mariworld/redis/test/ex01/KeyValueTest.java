package com.mariworld.redis.test.ex01;

import com.mariworld.redis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class KeyValueTest extends BaseTest {

    @Test
    public void KeyValueAccessTest(){
        RBucketReactive<String> bucket = this.redissonClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

    }
}
