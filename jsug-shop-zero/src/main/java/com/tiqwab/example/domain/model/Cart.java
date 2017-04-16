package com.tiqwab.example.domain.model;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Cart {

    private List<OrderLine> orderLines;

    public Cart() {
        log.info("Create Cart");
        this.orderLines = new ArrayList<>();
    }

    public void add(OrderLine orderLine) {
        log.info("Add a new orderLine: {}", orderLine);
        this.orderLines.add(orderLine);
    }

}
