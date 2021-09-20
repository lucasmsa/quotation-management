package com.inatel.quotationmanagement.quotationmanagement.models;

public class Stock {
    private String id;
    
    private String description;

    public Stock(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public Stock() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

