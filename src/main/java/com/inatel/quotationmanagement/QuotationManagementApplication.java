package com.inatel.quotationmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.context.event.EventListener;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import com.inatel.quotationmanagement.quotationmanagement.models.Notification;
import com.inatel.quotationmanagement.quotationmanagement.repository.NotificationRepository;

@SpringBootApplication
@EnableCaching
public class QuotationManagementApplication {
	private NotificationRepository notificationRepository;

	public QuotationManagementApplication() {
		this.notificationRepository = new NotificationRepository();
	}

	public static void main(String[] args) {
		SpringApplication.run(QuotationManagementApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
    public void notifyHost() {
        // this.notificationRepository.saveHost(new Notification("localhost", "8081"));
    }
}
