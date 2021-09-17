package com.inatel.quotationmanagement.quotationmanagement.models;

import java.math.BigDecimal;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@Table(name = "quotes")
public class Quote {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String quoteDate;

    private BigDecimal value;

    public Quote(String quoteDate, BigDecimal value) {
        this.quoteDate = quoteDate;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return quoteDate;
    }

    public void setDate(String quoteDate) {
        this.quoteDate = quoteDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

