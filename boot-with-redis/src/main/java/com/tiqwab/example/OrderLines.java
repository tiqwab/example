package com.tiqwab.example;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
public class OrderLines {

    @Getter
    private List<OrderLine> lines;

    public OrderLines() {
        this.lines = new ArrayList<>();
        this.lines.add(
                OrderLine.builder()
                        .item(Item.builder().name("Book1").price(100).producedAt(LocalDateTime.now()).build())
                        .quantity(1)
                        .price(100)
                        .build()
        );
        this.lines.add(
                OrderLine.builder()
                        .item(Item.builder().name("Book2").price(250).producedAt(LocalDateTime.now()).build())
                        .quantity(2)
                        .price(250)
                        .build()
        );
    }

}
