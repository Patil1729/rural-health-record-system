package com.ruralHealth.dashBoard.service;

import com.ruralHealth.dashBoard.payload.DashboardDto;
import com.ruralHealth.dashBoard.payload.DashboardSummaryDTO;

public interface DashboardService {
    DashboardDto getDashboard(Long patientId);

    DashboardSummaryDTO getDashboardSummary();
}
