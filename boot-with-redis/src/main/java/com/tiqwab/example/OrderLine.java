package com.tiqwab.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
public class OrderLine {

    private Item item;
    @Getter
    private Integer price;
    @Getter
    private Integer quantity;

    public String getName() {
        return this.item.getName();
    }

}
