package com.mariworld.redis.test;

import com.mariworld.redis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class HyperLogLogTest extends BaseTest {

    @Test
    public void count(){
        RHyperLogLogReactive<Long> counter = this.redissonClient.getHyperLogLog("user:visits", LongCodec.INSTANCE);
        List<Long> longList = LongStream.rangeClosed(1, 25)
                .boxed()
                .collect(Collectors.toList());

        StepVerifier.create(counter.addAll(longList).then())
                .verifyComplete();

        counter.count()
                .doOnNext(System.out::println)
                .subscribe();
    }
}
