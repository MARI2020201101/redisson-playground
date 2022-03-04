package com.mariworld.redis.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class SortedSetTest extends BaseTest{

    @Test
    public void sortedSetTest(){
        RScoredSortedSetReactive<String> sortedSet = this.redissonClient.getScoredSortedSet("student:score", StringCodec.INSTANCE);
        Mono<Void> mono = sortedSet.addScore("sam", 12.25)
                .then(sortedSet.add(23.33, "mike"))
                .then(sortedSet.addScore("jake", 33.44))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();

        sortedSet.entryRange(0,1)
                .flatMapIterable(Function.identity())
                .map(se->se.getScore() + " : " + se.getValue())
                .subscribe();

        sleep(1000);
    }
}
