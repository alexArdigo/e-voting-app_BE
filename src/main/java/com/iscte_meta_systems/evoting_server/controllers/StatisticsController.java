package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.model.PartyVoteStatsDTO;
import com.iscte_meta_systems.evoting_server.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/vote-percentages") //vote-percentages?electionId=1&districtName=Aveiro
    public List<PartyVoteStatsDTO> getStats(
            @RequestParam Long electionId,
            @RequestParam String districtName) {
        return statisticsService.getVotePercentagesByPartyByDistrict(electionId, districtName);
    }

}
