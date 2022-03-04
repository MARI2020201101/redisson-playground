package com.mariworld.redis.test;

import com.mariworld.redis.test.config.RedissonConfig;
import com.mariworld.redis.test.dto.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.redisson.api.LocalCachedMapOptions.defaults;

public class LocalCachedMapTest extends BaseTest{

    private RLocalCachedMap<Integer, Student> studentMap;

    @BeforeAll
    public void setupClient(){
        RedissonConfig config = new RedissonConfig();
        RedissonClient client = config.getClient();


        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.<Integer,Student>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.NONE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.LOAD);
        studentMap = client.getLocalCachedMap(
                "students",
                new TypedJsonJacksonCodec(Integer.class, Student.class),
                mapOptions
        );

    }


    @Test
    public void appServer1(){
        Student student1 = new Student("jade",31,"london");
        Student student2 = new Student("lucy",32,"paris");

        this.studentMap.put(1,student1);
        this.studentMap.put(2,student2);

        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(i-> System.out.println("[ " +i +" ] : " + studentMap.get(1)))
                .subscribe();

        sleep(6000000);

    }

    @Test
    public void appServer2(){
        Student student1 = new Student("jade-updated",31,"london");

        this.studentMap.put(1,student1);

    }
}
