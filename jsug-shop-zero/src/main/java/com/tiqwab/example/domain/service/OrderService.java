package com.tiqwab.example.domain.service;

import com.tiqwab.example.domain.model.*;
import com.tiqwab.example.domain.repository.OrderLineRepository;
import com.tiqwab.example.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderLineRepository orderLineRepository;

    public String calcSignature(Cart cart) {
        byte[] serialized = SerializationUtils.serialize(cart.getOrderLines());
        byte[] signature = null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            signature = messageDigest.digest(serialized);
        } catch(NoSuchAlgorithmException ignored) {

        }
        return Base64.getEncoder().encodeToString(signature);
    }

    public DemoOrder purchase(Account account, Cart cart, String signature) {
        if (cart.isEmpty()) {
            throw new EmptyCartOrderException("買い物カゴが空です");
        }
        if (!Objects.equals(calcSignature(cart), signature)) {
            throw new InvalidCartOrderException("買い物カゴの状態が変わっています");
        }

        // To deepCopy
        OrderLines orderLines = (OrderLines) SerializationUtils.deserialize(
                SerializationUtils.serialize(cart.getOrderLines())
        );

        DemoOrder order = DemoOrder.builder()
                .email(account.getEmail())
                .orderDate(LocalDate.now())
                .build();
        orderRepository.save(order);

        orderLines.stream().forEach(line -> {
            line.setOrder(order);
            orderLineRepository.save(line);
        });

        cart.clear();
        return order;
    }

}
