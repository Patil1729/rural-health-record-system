package com.ruralHealth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VaccinationStatus {
    SCHEDULED,
    COMPLETED,
    MISSED;

    @JsonCreator
    public static VaccinationStatus fromValue(String value) {
        return VaccinationStatus.valueOf(value.toUpperCase());
    }

    }
