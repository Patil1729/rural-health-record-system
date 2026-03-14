package com.ruralHealth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Specialization {
    GENERAL_PHYSICIAN,
    CARDIOLOGIST,
    PEDIATRICIAN,
    DERMATOLOGIST,
    GYNECOLOGIST,
    ORTHOPEDIC,
    NEUROLOGIST;

    //this is to capitalize
    @JsonCreator
    public static Specialization fromValue(String value) {
        return Specialization.valueOf(value.toUpperCase());
    }
}
