package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Districts;
import com.iscte_meta_systems.evoting_server.entities.Municipalities;
import com.iscte_meta_systems.evoting_server.entities.Parishes;
import com.iscte_meta_systems.evoting_server.repositories.DistrictsRepository;
import com.iscte_meta_systems.evoting_server.repositories.MunicipalitiesRepository;
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
import java.util.Objects;

@RestController
public class ElectoralCircleController {

    @Autowired
    private ElectoralCircleService electoralCircleService;

    @Autowired
    private DistrictsRepository districtsRepository;

    @Autowired
    private MunicipalitiesRepository municipalitiesRepository;

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
    public List<Districts> getAllDistricts() {
        return districtsRepository.findAll();
    }

    @GetMapping("districts/{districtName}")
    public Districts getDistrictByName(@PathVariable String districtName) {
        return districtsRepository.findByDistrictName(districtName).orElse(null);
    }

    @GetMapping("/districts/{districtName}/municipalities")
    public List<Municipalities> getAllMunicipalitiesByDistrict(@PathVariable String districtName) {
        return municipalitiesRepository.findByDistrictName(districtName);
    }

    @GetMapping("/municipalities/{municipalityName}/parishes")
    public List<Parishes> getParishesByMunicipality(@PathVariable String municipalityName) {
        return parishesRepository.findByMunicipalityName(municipalityName);
    }

    @GetMapping("/parishes/search")
    public List<Parishes> searchParishes(@RequestParam String name) {
        return parishesRepository.findByParishNameContaining(name);
    }

}


