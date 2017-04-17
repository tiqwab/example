package com.tiqwab.example.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderLine implements Serializable {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    private DemoOrder order;
    @OneToOne
    private Goods goods;
    private Integer quantity;
    private Integer lineNo;

    public long getSubtotal() {
        return this.goods.getPrice() * quantity;
    }

}
