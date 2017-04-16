package com.tiqwab.example.domain.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class OrderLines {

    @Getter
    private List<OrderLine> list;

    public OrderLines() {
        this.list = new ArrayList<>();
    }

    public void add(OrderLine orderLine) {
        this.list.add(orderLine);
    }

    public Stream<OrderLine> stream() {
        return list.stream();
    }

    public long getTotal() {
        return list.stream()
                .mapToLong(OrderLine::getSubtotal)
                .sum();
    }

}
