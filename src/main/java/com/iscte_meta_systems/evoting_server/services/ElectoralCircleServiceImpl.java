package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.District;
import com.iscte_meta_systems.evoting_server.entities.Municipality;
import com.iscte_meta_systems.evoting_server.entities.Parish;
import com.iscte_meta_systems.evoting_server.repositories.DistrictRepository;
import com.iscte_meta_systems.evoting_server.repositories.MunicipalityRepository;
import com.iscte_meta_systems.evoting_server.repositories.ParishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElectoralCircleServiceImpl implements ElectoralCircleService {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private ParishRepository parishRepository;

    @PostConstruct
    public void loadData() {
        if (districtRepository.count() > 0) {
            return;
        }


        try {
            readCSVFile();
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @Override
    public long getDistrictsCount() {
        return districtRepository.count();
    }

    @Override
    public long getMunicipalitiesCount() {
        return municipalityRepository.count();
    }

    @Override
    public long getParishesCount() {
        return parishRepository.count();
    }

    private void readCSVFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("DiscticsMunicipalitiesParishesPortugal.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        Map<String, District> districtsMap = new HashMap<>();
        Map<String, Municipality> municipalitiesMap = new HashMap<>();

        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;


            if (lineNumber == 1) continue;


            if (line.trim().isEmpty()) continue;


            String[] parts = line.split(";");
            if (parts.length < 3) continue;

            String districtName = cleanText(parts[0]);
            String municipalityName = cleanText(parts[1]);
            String parishName = cleanText(parts[2]);


            parishName = removeParentheses(parishName);


            if (isEmpty(districtName) || isEmpty(municipalityName) || isEmpty(parishName)) {
                continue;
            }


            District district = districtsMap.get(districtName);
            if (district == null) {
                district = new District();
                district.setDistrictName(districtName);
                district.setMunicipalities(new ArrayList<>());
                district = districtRepository.save(district);
                districtsMap.put(districtName, district);
            }


            String municipalityKey = districtName + "_" + municipalityName;
            Municipality municipality = municipalitiesMap.get(municipalityKey);
            if (municipality == null) {
                municipality = new Municipality();
                municipality.setMunicipalityName(municipalityName);
                municipality.setDistrict(district);
                municipality.setParishes(new ArrayList<>());
                municipality = municipalityRepository.save(municipality);
                municipalitiesMap.put(municipalityKey, municipality);

                district.getMunicipalities().add(municipality);
            }


            String finalParishName = parishName;
            boolean parishExists = parishRepository.findByMunicipalityName(municipalityName)
                    .stream()
                    .anyMatch(parishes -> parishes.getParishName().equals(finalParishName));

            if (!parishExists) {
                Parish parish = new Parish();
                parish.setParishName(parishName);
                parish.setMunicipality(municipality);
                parishRepository.save(parish);

                municipality.getParishes().add(parish);
            }
        }

        reader.close();


        System.out.println("Distritos: " + getDistrictsCount());
        System.out.println("Municípios: " + getMunicipalitiesCount());
        System.out.println("Freguesias: " + getParishesCount());
    }

    private String cleanText(String text) {
        if (text == null) return null;
        return text.replace("\"", "").trim();
    }

    private String removeParentheses(String text) {
        if (text == null) return null;
        return text.replaceAll("\\s*\\([^)]*\\)", "").trim();
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}