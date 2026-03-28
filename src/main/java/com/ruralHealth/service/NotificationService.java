package com.ruralHealth.service;

public interface NotificationService {

    void sendReminder(String phone, String message);

    void sendEmail(String email, String subject, String body);
}
