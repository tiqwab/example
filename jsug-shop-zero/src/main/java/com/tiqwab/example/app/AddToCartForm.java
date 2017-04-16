package com.tiqwab.example.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartForm {

    private Long goodsId;
    private Integer quantity;

}
