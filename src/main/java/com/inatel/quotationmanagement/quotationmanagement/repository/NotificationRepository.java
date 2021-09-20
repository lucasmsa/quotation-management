package com.inatel.quotationmanagement.quotationmanagement.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.inatel.quotationmanagement.quotationmanagement.models.Notification;

@Repository
public class NotificationRepository {
    private String host = "localhost";
    private String port = "8081";
    private String url = "http://localhost:8080/notification/";

    @Transactional
    @CacheEvict(value = {"stocks", "stockQuotes"}, allEntries = true)
    public void saveHost(Notification notification) {
        if (!this.checkIfHostIsAlreadyRegistered()) {
            System.out.println("Saving... " + notification.getHost() + " " + notification.getPort() + " on Stock-Manager");
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Notification[]> response = restTemplate.postForEntity(this.url, notification, Notification[].class);
            System.out.println("Saved " + response.getBody()[0].getHost() + ":" + response.getBody()[0].getPort() + "!");
            System.out.println("Application registered on Stock-manager");
        }
    }

    private boolean checkIfHostIsAlreadyRegistered() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Notification[]> response = restTemplate.getForEntity(this.url, Notification[].class);

        for (Notification notification : response.getBody()) {
            if (notification.getHost().equals(this.host) && notification.getPort().equals(this.port)) {
                System.out.println("Application already registered on Stock-manager");
                return true;
            }
        }   

        return false;
    }
}
