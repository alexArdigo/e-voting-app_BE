package com.iscte_meta_systems.evoting_server.services;

public interface StatisticsService {

    public List<PartyVoteStats> getVotePercentagesByPartyByDistrict(Long electionId, String districtName);

    int getTotalVotes(Long electionId);

    int getTotalVoters(Long electionId);

    int getTotalElections();

    int getTotalVotersByMunicipality(Long electionId, String municipality);

    int getTotalVotersByDistrict(Long electionId, String district);

    int getTotalVotersByParish(Long electionId, String parish);

    int getTotalVotersByUniparty(Long electionId, String unipartyName);

    int getTotalVotersByParty(Long electionId, String partyName);
}
