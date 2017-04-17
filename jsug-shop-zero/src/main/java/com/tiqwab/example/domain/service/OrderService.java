package com.tiqwab.example.domain.service;

import com.tiqwab.example.domain.model.Account;
import com.tiqwab.example.domain.model.Cart;
import com.tiqwab.example.domain.model.DemoOrder;
import com.tiqwab.example.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

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

    public List<DemoOrder> purchase(Account account, Cart cart, String signature) {
        /*
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

        List<DemoOrder> ordered = new ArrayList<>();
        for (int i = 0; i < orderLines.size(); i++) {
            DemoOrder order = DemoOrder.builder()
                               .lineNo(orderLines.getList().get(i).getLineNo())
                               .goods(orderLines.getList().get(i).getGoods())
                               .quantity(orderLines.getList().get(i).getQuantity())
                               .email(account.getEmail())
                               .orderDate(LocalDate.now())
                               .build();
            ordered.add(orderRepository.save(order));
        }
        */
        cart.clear();
        // return ordered;
        return null;
    }

}
