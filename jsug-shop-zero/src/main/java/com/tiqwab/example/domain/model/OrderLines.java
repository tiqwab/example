package com.tiqwab.example.domain.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class OrderLines implements Serializable {

    @Getter
    private List<OrderLine> list;
    private int lineCount;

    public OrderLines() {
        this.list = new ArrayList<>();
        this.lineCount = 0;
    }

    public void add(OrderLine orderLine) {
        this.lineCount++;
        orderLine.setLineNo(lineCount);
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

    public void remove(Set<Integer> lineNo) {
        List<OrderLine> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!lineNo.contains(i)) {
                newList.add(this.list.get(i));
            }
        }
        this.list = newList;
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public void clear() {
        this.list.clear();
        this.lineCount = 0;
    }

    public int size() {
        return this.list.size();
    }

}
