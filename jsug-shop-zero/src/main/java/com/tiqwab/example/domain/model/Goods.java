package com.tiqwab.example.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Goods implements Serializable {

    @Id @GeneratedValue
    private Long id;
    private String goodsName;
    private String description;
    @ManyToOne
    private Category category;
    private Integer price;

}
