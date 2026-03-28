package com.ruralHealth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "notification_log")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // SMS OR EMAIL
    private String recipient;
    private String message;
    private String status; // SENT OR FAILED

    private LocalDateTime createdAt;
}
