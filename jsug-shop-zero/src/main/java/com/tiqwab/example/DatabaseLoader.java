package com.tiqwab.example;

import com.tiqwab.example.domain.model.*;
import com.tiqwab.example.domain.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class DatabaseLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderLineRepository orderLineRepository;

    @Override
    public void run(String... args) throws Exception {
        final Account user = Account.builder()
                .name("user")
                .password(Account.PASSWORD_ENCODER.encode("user"))
                .email("user@user.com")
                .birthDay(LocalDate.of(2017, 4, 1))
                .zip("111-1111")
                .address("Tokyo")
                .age(20)
                .roles(new String[]{"ROLE_USER"})
                .build();
        accountRepository.save(user);

        final Category bookCategory = Category.builder().categoryName("book").build();
        categoryRepository.save(bookCategory);

        final Category cdCategory = Category.builder().categoryName("cd").build();
        categoryRepository.save(cdCategory);

        log.info(bookCategory.toString());

        final Goods book1 = Goods.builder()
                .goodsName("book1")
                .description("this is book1")
                .price(100)
                .category(bookCategory)
                .build();
        goodsRepository.save(book1);

        final Goods cd1 = Goods.builder()
                .goodsName("cd1")
                .description("this is cd1")
                .price(200)
                .category(cdCategory)
                .build();
        goodsRepository.save(cd1);

        final DemoOrder order = DemoOrder.builder()
                .email(user.getEmail())
                .orderDate(LocalDate.now())
                .build();
        orderRepository.save(order);

        final OrderLine orderLine = OrderLine.builder()
                .lineNo(1)
                .order(order)
                .goods(book1)
                .quantity(1)
                .build();
        orderLineRepository.save(orderLine);
    }
}
