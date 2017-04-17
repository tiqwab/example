package com.tiqwab.example.domain.service;

import com.tiqwab.example.domain.model.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Transactional
public class OrderService {

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

}
