package com.tiqwab.example;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class Item {

    private String name;
    private Integer price;
    private LocalDateTime producedAt;

}
