package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.model.DistrictStatisticsDTO;

public interface StatisticsService {

    DistrictStatisticsDTO getDistrictStatistics(String districtName);
}