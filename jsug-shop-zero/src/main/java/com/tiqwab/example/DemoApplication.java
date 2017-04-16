package com.tiqwab.example;

import com.tiqwab.example.domain.model.Cart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
@EntityScan(basePackageClasses = {DemoApplication.class, Jsr310JpaConverters.class})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	// TODO: Check ScopeSeccion
    // Cart is created when necessary (not when user's access executes controller methods)
	@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
	Cart cart() {
		return new Cart();
	}

}
