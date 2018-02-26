package com.snorlacs.newse.domain;

import org.hibernate.validator.constraints.NotBlank;

public class Author {

    @NotBlank(message = "author name must not be blank")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
