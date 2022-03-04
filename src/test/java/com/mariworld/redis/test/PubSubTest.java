package com.mariworld.redis.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.client.codec.StringCodec;

public class PubSubTest extends BaseTest{

    @Test
    public void subscriber1(){
        RTopicReactive topic = this.redissonClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();

        sleep(6000000);
    }
    @Test
    public void subscriber2(){
        RTopicReactive topic = this.redissonClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();

        sleep(6000000);
    }
    @Test
    public void subscriber3(){
        RTopicReactive topic = this.redissonClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();

        sleep(6000000);
    }

    @Test
    public void subscriber4(){
        RPatternTopicReactive topic = this.redissonClient.getPatternTopic("slack-room*", StringCodec.INSTANCE);

        topic.addListener(String.class, new PatternMessageListener<String>() {
            @Override
            public void onMessage(CharSequence pattern, CharSequence topic, String msg) {
                System.out.println("pattern : " + pattern);
                System.out.println("topic : " + topic);
                System.out.println("msg : "+ msg);
            }
        }).subscribe();

        sleep(6000000);
    }
}
