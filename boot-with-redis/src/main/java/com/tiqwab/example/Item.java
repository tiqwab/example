package com.tiqwab.example;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class Item implements Serializable {

    private String name;
    private Integer price;
    private LocalDateTime producedAt;

}
