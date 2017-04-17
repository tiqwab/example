package com.tiqwab.example.domain.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderLine implements Serializable {

    private Goods goods;
    private Integer quantity;
    private Integer lineNo;

    public long getSubtotal() {
        return this.goods.getPrice() * quantity;
    }

}
