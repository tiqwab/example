package com.tiqwab.example.app;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CartForm {

    @NotEmpty
    @NotNull
    private Set<Integer> lineNo;

}
