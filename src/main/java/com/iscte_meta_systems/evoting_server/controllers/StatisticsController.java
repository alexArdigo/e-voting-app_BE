package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.model.DistrictStatisticsDTO;
import com.iscte_meta_systems.evoting_server.model.PartyVoteStatsDTO;
import com.iscte_meta_systems.evoting_server.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/vote-percentages") //api/statistics/vote-percentages?electionId=1&districtName=Aveiro
    public List<PartyVoteStatsDTO> getStats(
            /**
             * Retrieves the vote percentages by PARTY for a specific (electoralCircle) election and DISTRICT.
             */
            @RequestParam Long electionId,
            @RequestParam String districtName) {
        return statisticsService.getVotePercentagesByPartyByDistrict(electionId, districtName);
    }

    @GetMapping("/districts/{districtName}/statistics")
    public DistrictStatisticsDTO getDistrictStatistics(@PathVariable String districtName) {
        return statisticsService.getDistrictStatistics(districtName);
    }

    @GetMapping("/total-votes-by-election") //total-votes-by-election?electionId=1
    public int getTotalVotes(Long electionId) {
        /**
         * Retrieves the total number of votes for a specific election
         */
        return statisticsService.getTotalVotesByElection(electionId);
    }

    @GetMapping("/total-votes-by-party") //total-votes-by-party?electionId=1&partyName=IniciativaLiberal
    public int getNumberOfVotesByParty(
            @RequestParam Long electionId,
            @RequestParam String partyName) {
        /**
         * Retrieves the total number of votes for a specific party in a specific election
         */
        return statisticsService.getVotesByPartyByElectoralCircle(partyName, electionId);
    }
}
