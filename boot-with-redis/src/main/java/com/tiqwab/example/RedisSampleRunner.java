package com.tiqwab.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
// @Component
public class RedisSampleRunner implements CommandLineRunner {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        /*
        // save STRING type
        redisTemplate.opsForValue().set("str1", "val1");

        // Save LIST type
        redisTemplate.opsForList().leftPush("list1", "1");
        redisTemplate.opsForList().leftPushAll("list1", "1", "2", "3");

        // Save SET type
        redisTemplate.opsForSet().add("set1", "1", "2", "3");
        redisTemplate.opsForSet().add("set1", "1");

        // Save HASH type
        redisTemplate.opsForHash().put("hash1", "firstName", "Taro");
        redisTemplate.opsForHash().put("hash1", "lastName", "Yamada");

        // Save ZSET type
        redisTemplate.opsForZSet().add("zset1", "history1", 1);
        redisTemplate.opsForZSet().add("zset1", "history2", 2);
        redisTemplate.opsForZSet().add("zset1", "history1", 3);
        */

        /*
        log.info("get str1: {}", redisTemplate.opsForValue().get("str1"));
        log.info("range list1 0 -1: {}", redisTemplate.opsForList().range("list1", 0, -1));
        log.info("smembers set1: {}", redisTemplate.opsForSet().members("set1"));
        log.info("hget hash1 firstname: {}", redisTemplate.opsForHash().get("hash1", "firstName"));
        // How to specify '+inf' and '-inf'?
        log.info("zrangebyscore zset1 -inf +inf withscores:");
        redisTemplate.opsForZSet().rangeByScoreWithScores("zset1", 0, Integer.MAX_VALUE)
                .stream()
                .forEach(t -> log.info("({},{})", t.getValue(), t.getScore()));
        */
    }

}
