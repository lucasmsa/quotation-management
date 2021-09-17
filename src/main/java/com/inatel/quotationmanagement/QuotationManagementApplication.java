package com.inatel.quotationmanagement;

import com.inatel.quotationmanagement.quotationmanagement.models.Notification;
import com.inatel.quotationmanagement.quotationmanagement.repository.CacheRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableCaching
public class QuotationManagementApplication {
	private CacheRepository cacheRepository;

	public QuotationManagementApplication() {
		this.cacheRepository = new CacheRepository();
	}

	public static void main(String[] args) {
		SpringApplication.run(QuotationManagementApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
    public void notifyHost() {
        this.cacheRepository.saveHost(new Notification("localhost", "8081"));
    }
}
