package com.iscte_meta_systems.evoting_server.model;

import java.util.List;

public class DistrictStatisticsDTO {
    private String districtName;
    private int totalVotes;
    private List<MunicipalityStatisticsDTO> municipalities;

    public DistrictStatisticsDTO() {}

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public List<MunicipalityStatisticsDTO> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(List<MunicipalityStatisticsDTO> municipalities) {
        this.municipalities = municipalities;
    }
}
