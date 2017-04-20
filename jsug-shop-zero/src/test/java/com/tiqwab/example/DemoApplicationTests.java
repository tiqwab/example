package com.tiqwab.example;

import com.tiqwab.example.domain.model.Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	Environment env;

	@Autowired
	ApplicationContext context;

	@Test
	public void contextLoads() {
	    System.out.println("spring.session.store-type: " + env.getProperty("spring.session.store-type"));
	    System.out.println("Cart: " + context.getBean(Cart.class).getClass());
		System.out.println("CacheManager: " + context.getBean(CacheManager.class).getClass());
	}

}
