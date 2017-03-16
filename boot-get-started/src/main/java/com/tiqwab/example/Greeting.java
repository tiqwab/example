package com.tiqwab.example;

import lombok.Getter;

/**
 * Created by nm on 3/12/17.
 */
public class Greeting {

    @Getter
    private final long id;
    @Getter
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

}
