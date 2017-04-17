package com.tiqwab.example.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category implements Serializable {

    @Id @GeneratedValue
    private Long id;
    private String categoryName;

}
