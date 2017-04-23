package com.tiqwab.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@ToString
public class OrderLine implements Serializable {

    private Item item;
    @Getter
    private Integer price;
    @Getter
    private Integer quantity;

    public String getName() {
        return this.item.getName();
    }

}
