package com.mariworld.redis.test;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ListStackQueueTest extends BaseTest{

    @Test
    @Order(1)
    public void listTest(){
        RListReactive<Long> list = this.redissonClient.getList("number-input", LongCodec.INSTANCE);


        List<Long> longList = LongStream.rangeClosed(1, 10)
                .boxed()
                .collect(Collectors.toList());

        list.addAll(longList);
//      순서를 보장하지 않는다
//        Mono<Void> listAdd = Flux.range(1, 10)
//                .map(Long::valueOf)
//                .flatMap(i -> list.add(i))
//                .then();
        StepVerifier.create(list.addAll(longList).then())
                .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void queueTest(){
        RQueueReactive<Long> queue = this.redissonClient.getQueue("number-input", LongCodec.INSTANCE);
        Mono<Void> queuepoll = queue.poll()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(queuepoll)
                .verifyComplete();
        StepVerifier.create(queue.size())
                .expectNext(6)
                .verifyComplete();
    }

    @Test
    @Order(3)
    public void stackTest(){
        RDequeReactive<Long> dequeue = this.redissonClient.getDeque("number-input", LongCodec.INSTANCE);
        Mono<Void> stackPoll = dequeue.pollLast()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(stackPoll)
                .verifyComplete();
    }
}
