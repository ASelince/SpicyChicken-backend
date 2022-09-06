package com.spicy.data;

import org.springframework.lang.Nullable;

public interface Persistable<ID> {

    @Nullable
    ID getId();

    boolean isNew();
}
