package com.tiqwab.example.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class DemoOrder {

    @Id @GeneratedValue
    private Long id;
    private String email;
    private LocalDate orderDate;

}
