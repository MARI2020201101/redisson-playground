package com.mariworld.redis.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RTransactionReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TransactionTest extends BaseTest{

    private RBucketReactive<Long> bucket1;
    private RBucketReactive<Long> bucket2;

    @BeforeAll
    public void accountSetup(){
        bucket1= this.redissonClient.getBucket("user:1:balance", LongCodec.INSTANCE);
        bucket2 = this.redissonClient.getBucket("user:2:balance", LongCodec.INSTANCE);

        Mono<Void> mono = bucket1.set(100L)
                .then(bucket2.set(0L))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }

    @AfterAll
    public void accountStatus(){
        Mono<Void> mono = Flux.zip(this.bucket1.get(), this.bucket2.get())
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void nonTransactionTest(){
        this.transfer(bucket1, bucket2,50)
                .thenReturn(0)
                .map(i-> (5/i))
                .doOnError(System.out::println)
                .subscribe();
        sleep(10000);
    }

    @Test
    public void transactionTest(){
        RTransactionReactive transaction = this.redissonClient.createTransaction(TransactionOptions.defaults());
        RBucketReactive<Long> user1Bal = transaction.getBucket("user:1:balance", LongCodec.INSTANCE);
        RBucketReactive<Long> user2Bal = transaction.getBucket("user:2:balance", LongCodec.INSTANCE);

        this.transfer(user1Bal, user2Bal,50)
                .thenReturn(0)
                .map(i-> (5/i))
                .then(transaction.commit())
                .doOnError(System.out::println)
                .doOnError(ex->transaction.rollback())
                .subscribe();
        sleep(10000);
    }

    @Test
    private  Mono<Void> transfer(RBucketReactive<Long> from, RBucketReactive<Long> to , int amount){
        return Flux.zip(from.get(), to.get())
                .filter(t -> t.getT1() >= amount)
                .flatMap(t -> from.set(t.getT1() - amount).thenReturn(t))
                .flatMap(t -> to.set(t.getT2() + amount))
                .then();
    }
}
