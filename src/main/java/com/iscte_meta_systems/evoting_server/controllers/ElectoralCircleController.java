package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.District;
import com.iscte_meta_systems.evoting_server.entities.Municipality;
import com.iscte_meta_systems.evoting_server.entities.Parish;
import com.iscte_meta_systems.evoting_server.repositories.DistrictRepository;
import com.iscte_meta_systems.evoting_server.repositories.MunicipalityRepository;
import com.iscte_meta_systems.evoting_server.repositories.ParishesRepository;
import com.iscte_meta_systems.evoting_server.services.ElectoralCircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ElectoralCircleController {

    @Autowired
    private ElectoralCircleService electoralCircleService;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private ParishesRepository parishesRepository;

    @GetMapping("/territory")
    public Map<String, Object> getTerritoryData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalDistricts", electoralCircleService.getDistrictsCount());
        data.put("totalMunicipalities", electoralCircleService.getMunicipalitiesCount());
        data.put("totalParishes", electoralCircleService.getParishesCount());
        return data;
    }

    @GetMapping("districts")
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    @GetMapping("districts/{districtName}")
    public District getDistrictByName(@PathVariable String districtName) {
        return districtRepository.findByDistrictName(districtName);
    }

    @GetMapping("/districts/{districtName}/municipalities")
    public List<Municipality> getAllMunicipalitiesByDistrict(@PathVariable String districtName) {
        return municipalityRepository.findByDistrictName(districtName);
    }

    @GetMapping("/municipalities/{municipalityName}/parishes")
    public List<Parish> getParishesByMunicipality(@PathVariable String municipalityName) {
        return parishesRepository.findByMunicipalityName(municipalityName);
    }

    @GetMapping("/parishes/search")
    public List<Parish> searchParishes(@RequestParam String name) {
        return parishesRepository.findByParishNameContaining(name);
    }

}


