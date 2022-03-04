package com.mariworld.redis.test;

import com.mariworld.redis.test.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private final RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonReactiveClient redissonClient;

    @BeforeAll
    public void setClient(){
        redissonClient = redissonConfig.getReactiveClient();
    }

    @AfterAll
    public void shutdown(){
        redissonClient.shutdown();
    }

    protected void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {

        }
    }
}
