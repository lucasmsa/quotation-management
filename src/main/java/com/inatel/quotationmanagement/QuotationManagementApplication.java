package com.inatel.quotationmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.context.event.EventListener;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.inatel.quotationmanagement.quotationmanagement.models.Notification;
import com.inatel.quotationmanagement.quotationmanagement.repository.CacheRepository;

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
