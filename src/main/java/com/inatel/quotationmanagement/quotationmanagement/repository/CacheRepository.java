package com.inatel.quotationmanagement.quotationmanagement.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import com.inatel.quotationmanagement.quotationmanagement.models.Notification;

@Repository
public class CacheRepository {

    private String url = "http://localhost:8080/notification/";

    @CacheEvict(value = "stocks", allEntries = true)
    public void saveHost(Notification notification) {
        System.out.println("Saving... " + notification.getHost() + " " + notification.getPort() + " on Stock-Manager");
        RestTemplate restTemplate = new RestTemplate();
        
        HttpEntity<Notification[]> response = restTemplate.postForEntity(this.url, notification, Notification[].class);
        System.out.println("Saved " + response.getBody()[0].getHost() + ":" + response.getBody()[0].getPort() + "!");
        System.out.println("Application registered on Stock-manager");
    }
}