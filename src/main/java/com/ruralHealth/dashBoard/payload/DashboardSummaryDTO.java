package com.ruralHealth.dashBoard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {

    private long totalPatients;
    private long totalDoctors;
    private long totalAppointments;
}
