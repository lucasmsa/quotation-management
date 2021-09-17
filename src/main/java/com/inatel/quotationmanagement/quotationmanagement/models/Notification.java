package com.inatel.quotationmanagement.quotationmanagement.models;

public class Notification {
    private String host;
    private String port;

    public Notification(String host, String port) {
        this.host = host;
        this.port = port;
    }
    
    public Notification() {}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}