package com.spicy.data;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spicy.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public abstract class DataEntity<ID extends Serializable> implements Entity<ID>, Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    private String createdBy;

    @CreatedByName
    private String createdByName;

    @CreatedTime
    private Date createdTime;

    @UpdatedBy
    private String updatedBy;

    @UpdatedByName
    private String updatedByName;

    @UpdatedTime
    private Date updatedTime;

    @Version
    private Integer dataVersion = -1;

    @TableLogic
    @JsonIgnore
    private String delFlag;

    public DataEntity() {

    }

    @Override
    public boolean isNew() {

        ID id = getId();

        if (id == null) {

            return true;
        }

        if (id instanceof CharSequence) {

            return ((CharSequence) id).length() == 0;
        }

        if (id instanceof Number) {

            return ((Number) id).longValue() == 0;
        }

        return false;
    }

    @Override
    public boolean equals(Object that) {

        if (that instanceof DataEntity) {

            return Objects.equals(((DataEntity<?>) that).getId(), this.getId());
        }

        return false;
    }
}
