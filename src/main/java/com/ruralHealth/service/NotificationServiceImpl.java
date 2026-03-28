package com.ruralHealth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Async
    @Override
    public void sendReminder(String phone, String message) {
        log.info("Sending SMS to {}: {}", phone, message);

        // simulate delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("Error in sending SMS", e);
        }
    }

    @Async
    @Override
    public void sendEmail(String email, String subject, String body) {
        log.info("Sending Email to {}: {}", email, subject);
    }
}
