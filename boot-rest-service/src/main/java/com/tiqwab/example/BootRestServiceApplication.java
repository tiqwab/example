package com.tiqwab.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class BootRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootRestServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(AccountRepository accountRepository, BookmarkRepository bookmarkdRepository) {
		return (evt) -> {
			Arrays.asList("jhoeller", "dsyer", "pwebb", "ogierke", "rwinch", "mfisher", "mpolack", "jlong")
					.stream()
					.forEach(person -> {
					    Account account = accountRepository.save(new Account(person, "password"));
					    bookmarkdRepository.save(new Bookmark(account, "http://bookmark.com/1/" + person, "A description"));
                        bookmarkdRepository.save(new Bookmark(account, "http://bookmark.com/2/" + person, "A description"));
					});
		};
	}

}
