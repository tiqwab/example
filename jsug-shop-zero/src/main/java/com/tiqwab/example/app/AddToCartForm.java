package com.tiqwab.example.app;

import com.tiqwab.example.domain.model.OrderLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartForm {

    private Long goodsId;
    private Long categoryId;
    private Integer quantity;
    private List<OrderLine> orderLines = new ArrayList<>();

    public void add(OrderLine orderLine) {
        orderLines.add(orderLine);
    }

}
