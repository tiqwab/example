package com.tiqwab.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * TODO: What is the best way to disable @EnableRedisHttpSession?
 * The below does not work...
 * https://github.com/spring-projects/spring-boot/issues/6256
 */
@Configuration
@Profile("default")
@EnableRedisHttpSession
public class CacheConfig {
}
