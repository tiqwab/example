package com.tiqwab.example.app;

import com.tiqwab.example.domain.model.OrderLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartForm {

    @NotNull
    private Long goodsId;
    @NotNull
    private Long categoryId;
    @NotNull
    @Min(1)
    private Integer quantity;

}
