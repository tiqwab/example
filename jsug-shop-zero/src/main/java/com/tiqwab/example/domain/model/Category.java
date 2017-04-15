package com.tiqwab.example.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {

    @Id @GeneratedValue
    private Long id;
    private String categoryName;

}
