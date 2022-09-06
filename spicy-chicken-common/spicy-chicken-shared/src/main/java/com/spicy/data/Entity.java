package com.spicy.data;

import java.io.Serializable;

public interface Entity<ID extends Serializable> extends Persistable<ID> {

    ID getId();

    void setId(ID id);
}
