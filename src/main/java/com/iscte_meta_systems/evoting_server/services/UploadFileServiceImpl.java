package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV format required:
 * For Presidential elections: organisationName,imageUrl
 * For Legislative elections: organisationName,color,logoUrl,description,candidateName,candidateImageUrl
 * The first row should be a header and will be skipped.
 */
@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public List<Organisation> parceCSVFile(MultipartFile file, Election election) {

        if (election.getType() == ElectionType.PRESIDENTIAL) {
            return parsePresidentialCSV(file);
        } else if (election.getType() == ElectionType.LEGISLATIVE) {
            // For legislative elections, we need to parse and return organisations
            // that will be added to ElectoralCircles, not the base Legislative election
            return parseElectoralCircleCSV(file);
        } else {
            throw new IllegalArgumentException("Unsupported election type for CSV upload: " + election.getType());
        }
    }

    private List<Organisation> parsePresidentialCSV(MultipartFile file) {
        List<Organisation> organisations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null) throw new RuntimeException("CSV file is empty");

            String[] headers = headerLine.split(",");
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                idx.put(headers[i].trim().toLowerCase(), i);
            }

            int iOrg = idx.get("organisationname");
            int iImg = idx.getOrDefault("imageurl", -1);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");
                String candName = cols.length > iOrg ? cols[iOrg].trim() : "";
                if (!candName.isEmpty()) {
                    UniParty up = new UniParty();
                    up.setOrganisationName(candName);
                    up.setName(candName);
                    if (iImg >= 0 && cols.length > iImg) up.setImageUrl(cols[iImg].trim());
                    organisations.add(up);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV: " + e.getMessage(), e);
        }
        return organisations;
    }

    public List<Organisation> parseElectoralCircleCSV(MultipartFile file) {
        List<Organisation> organisations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null) throw new RuntimeException("CSV file is empty");

            String[] headers = headerLine.split(",");
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                idx.put(headers[i].trim().toLowerCase(), i);
            }

            int iOrg = idx.get("organisationname");
            int iColor = idx.getOrDefault("color", -1);
            int iLogo = idx.getOrDefault("logourl", -1);
            int iDesc = idx.getOrDefault("description", -1);
            int iCand = idx.getOrDefault("candidatename", -1);
            int iImg = idx.getOrDefault("candidateimageurl", -1);

            Map<String, Party> partyMap = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");
                String orgName = cols.length > iOrg ? cols[iOrg].trim() : "";
                if (orgName.isEmpty()) continue;

                Party party = partyMap.computeIfAbsent(orgName, k -> {
                    Party p = new Party();
                    p.setOrganisationName(k);
                    if (iColor >= 0 && cols.length > iColor) p.setColor(cols[iColor].trim());
                    if (iLogo >= 0 && cols.length > iLogo) p.setLogoUrl(cols[iLogo].trim());
                    if (iDesc >= 0 && cols.length > iDesc) p.setDescription(cols[iDesc].trim());
                    p.setCandidates(new ArrayList<>());
                    return p;
                });

                String candName = iCand >= 0 && cols.length > iCand ? cols[iCand].trim() : "";
                if (!candName.isEmpty()) {
                    Candidate c = new Candidate();
                    c.setName(candName);
                    if (iImg >= 0 && cols.length > iImg) c.setImageUrl(cols[iImg].trim());
                    // Don't save candidate here - it will be saved when copied to electoral circles
                    party.getCandidates().add(c);
                }
            }
            organisations.addAll(partyMap.values());
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV: " + e.getMessage(), e);
        }
        return organisations;
    }
}
