package com.avenuecode.orders.domain;

import lombok.Getter;

@Getter
public class SearchCriteria {
    String key;
    String operation;
    String value;

    public SearchCriteria(String key, String operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}