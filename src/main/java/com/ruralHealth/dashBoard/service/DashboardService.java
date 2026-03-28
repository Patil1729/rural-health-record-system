package com.ruralHealth.dashBoard.service;

import com.ruralHealth.dashBoard.payload.DashboardDto;

public interface DashboardService {
    DashboardDto getDashboard(Long patientId);
}
