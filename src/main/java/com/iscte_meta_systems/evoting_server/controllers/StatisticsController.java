package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.model.DistrictStatisticsDTO;
import com.iscte_meta_systems.evoting_server.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/districts/{districtName}/statistics")
    public DistrictStatisticsDTO getDistrictStatistics(@PathVariable String districtName) {
        return statisticsService.getDistrictStatistics(districtName);
    }
}
