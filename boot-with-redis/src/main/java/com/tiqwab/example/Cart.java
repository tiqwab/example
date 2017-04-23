package com.tiqwab.example;

import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@ToString
public class Cart implements Serializable {

    private OrderLines orderLines;

    public Cart() {
        this.orderLines = new OrderLines();
    }

    public List<OrderLine> getLines() {
        return orderLines.getLines();
    }

}
