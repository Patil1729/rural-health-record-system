package com.ruralHealth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AppointmentStatus {
    BOOKED,
    COMPLETED,
    CANCELLED,
    CONFIRMED,;

    @JsonCreator
    public static Specialization fromValue(String value) {
        return Specialization.valueOf(value.toUpperCase());
    }
}
