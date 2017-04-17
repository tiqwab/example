package com.tiqwab.example.domain.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class Cart {

    @Getter
    private OrderLines orderLines;

    public Cart() {
        log.info("Create Cart");
        this.orderLines = new OrderLines();
    }

    public void add(OrderLine orderLine) {
        log.info("Add a new orderLine: {}", orderLine);
        this.orderLines.add(orderLine);
    }

    public void remove(Set<Integer> lineNo) {
        log.info("Remove the specified lines: {}", lineNo);
        this.orderLines.remove(lineNo);
    }

    public boolean isEmpty() {
        return orderLines.isEmpty();
    }

}
