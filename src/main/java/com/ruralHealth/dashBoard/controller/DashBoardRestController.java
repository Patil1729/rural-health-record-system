package com.ruralHealth.dashBoard.controller;

import com.ruralHealth.dashBoard.payload.DashboardDto;
import com.ruralHealth.dashBoard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/dashboard")
public class DashBoardRestController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/patient/{id}")
    public ResponseEntity<DashboardDto> getDashboard(@PathVariable Long id) {

        return ResponseEntity.ok(dashboardService.getDashboard(id));
    }
}

