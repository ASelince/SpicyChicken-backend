package com.spicy.rule;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Operator {

    EQ("EQUAL"), NOT_EQUAL("NOT_EQUAL"),
    LESS_THAN, LESS_EQUAL,
    GREATER_THAN, GREATER_EQUAL,
    LIKE, NOT_LIKE,
    START_WITH, END_WITH,
    NOT_START_WITH, NOT_END_WITH,
    IN, NOT_IN;


    private String key;

    private Operator(String key) {

        this.key = key;
    }

    public String toString() {

        return this.key;
    }

    public static Operator operatorOf(String key) {

        for (Operator op : Operator.values()) {

            if (op.key.equalsIgnoreCase(key)) {
                return op;
            }
        }

        return EQ;
    }
}
