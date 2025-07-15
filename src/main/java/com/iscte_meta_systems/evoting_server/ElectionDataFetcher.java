package com.iscte_meta_systems.evoting_server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

//Criado com a ajuda de AI.

public class ElectionDataFetcher {

    private static final String BASE_URL = "https://www.legislativas2025.mai.gov.pt/frontend/data/PartiesCandidates";
    private static final String ELECTION_ID = "AR";
    private static final String IMAGE_BASE_URL = "https://www.legislativas2025.mai.gov.pt/images/";
    private static final String PLACEHOLDER_IMAGE = "https://via.placeholder.com/150x150/cccccc/000000?text=Candidate";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Predefined party colors mapping
    private static final Map<String, String> PARTY_COLORS = new HashMap<>();
    static {
        PARTY_COLORS.put("ADN", "#800080");
        PARTY_COLORS.put("B.E.", "#FF0000");
        PARTY_COLORS.put("CH", "#000080");
        PARTY_COLORS.put("E", "#8B4513");
        PARTY_COLORS.put("IL", "#00CED1");
        PARTY_COLORS.put("JPP", "#FFD700");
        PARTY_COLORS.put("L", "#32CD32");
        PARTY_COLORS.put("MPT", "#8FBC8F");
        PARTY_COLORS.put("NC", "#4169E1");
        PARTY_COLORS.put("ND", "#2F4F4F");
        PARTY_COLORS.put("PAN", "#90EE90");
        PARTY_COLORS.put("PCP-PEV", "#FF0000");
        PARTY_COLORS.put("PCTP/MRPP", "#DC143C");
        PARTY_COLORS.put("PLS", "#4682B4");
        PARTY_COLORS.put("PPD/PSD.CDS-PP", "#FF8C00");
        PARTY_COLORS.put("PPD/PSD.CDS-PP.PPM", "#FF8C00");
        PARTY_COLORS.put("PPM", "#9932CC");
        PARTY_COLORS.put("PS", "#FF69B4");
        PARTY_COLORS.put("PTP", "#CD853F");
        PARTY_COLORS.put("R.I.R.", "#20B2AA");
        PARTY_COLORS.put("VP", "#9370DB");
    }

    /**
     * Fetches all election data and transforms it to match your JSON structure
     *
     * @return JSON organized by territory with party and candidate details
     * @throws IOException if there's an error fetching or parsing data
     * @throws InterruptedException if the HTTP request is interrupted
     */
    public static ObjectNode fetchAllElectionData() throws IOException, InterruptedException {
        ObjectNode result = objectMapper.createObjectNode();
        Map<String, String> partyNamesMap = new HashMap<>();
        int totalPages = 1;

        // First, get party names mapping from the API
        for (int page = 1; page <= totalPages; page++) {
            System.out.println("Fetching page " + page + " of " + totalPages);

            String url = String.format("%s?electionId=%s&page=%d", BASE_URL, ELECTION_ID, page);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Accept", "application/json")
                    .header("User-Agent", "Java/ElectionDataFetcher")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP error: " + response.statusCode() +
                        " for page " + page);
            }

            JsonNode pageData = objectMapper.readTree(response.body());

            // Update total pages from first response
            if (page == 1) {
                totalPages = pageData.get("numberOfPages").asInt();
                System.out.println("Total pages to fetch: " + totalPages);

                // Build party names mapping
                JsonNode parties = pageData.get("parties");
                if (parties != null && parties.isArray()) {
                    for (JsonNode party : parties) {
                        String acronym = party.get("acronym").asText();
                        String fullName = party.get("name").asText();
                        partyNamesMap.put(acronym, fullName);
                    }
                }
            }

            // Process election candidates for this page
            JsonNode electionCandidates = pageData.get("electionCandidates");
            if (electionCandidates != null && electionCandidates.isArray()) {
                for (JsonNode territory : electionCandidates) {
                    String territoryName = territory.get("territoryName").asText();

                    // Get or create territory parties array
                    ArrayNode territoryParties;
                    if (result.has(territoryName)) {
                        // Territory already exists, get existing parties array
                        territoryParties = (ArrayNode) result.get(territoryName);
                    } else {
                        // New territory, create new parties array
                        territoryParties = objectMapper.createArrayNode();
                        result.set(territoryName, territoryParties);
                    }

                    JsonNode candidates = territory.get("candidates");
                    if (candidates != null && candidates.isArray()) {
                        for (JsonNode partyData : candidates) {
                            String partyAcronym = partyData.get("party").asText();
                            String partyFullName = partyNamesMap.getOrDefault(partyAcronym, partyAcronym);
                            String imageKey = partyData.get("imageKey").asText();

                            // Check if this party already exists for this territory
                            boolean partyExists = false;
                            for (JsonNode existingParty : territoryParties) {
                                if (existingParty.get("partyName").asText().equals(partyFullName)) {
                                    partyExists = true;
                                    break;
                                }
                            }

                            // Only add party if it doesn't already exist
                            if (!partyExists) {
                                // Create party object
                                ObjectNode partyObject = objectMapper.createObjectNode();
                                partyObject.put("partyName", partyFullName);
                                partyObject.put("color", PARTY_COLORS.getOrDefault(partyAcronym, "#808080"));
                                partyObject.put("logoUrl", IMAGE_BASE_URL + imageKey + ".png");
                                partyObject.put("description", partyFullName + " - Partido político português");

                                // Create candidates array
                                ArrayNode candidatesArray = objectMapper.createArrayNode();
                                JsonNode effectiveCandidates = partyData.get("effectiveCandidates");

                                if (effectiveCandidates != null && effectiveCandidates.isArray()) {
                                    // Fetch ALL candidates (removed the 5-candidate limit)
                                    for (JsonNode candidateName : effectiveCandidates) {
                                        ObjectNode candidate = objectMapper.createObjectNode();
                                        candidate.put("name", candidateName.asText());
                                        candidate.put("imageUrl", PLACEHOLDER_IMAGE);
                                        candidatesArray.add(candidate);
                                    }
                                }

                                partyObject.set("candidates", candidatesArray);
                                territoryParties.add(partyObject);
                            }
                        }
                    }
                }
            }

            // Add small delay to be respectful to the server
            if (page < totalPages) {
                Thread.sleep(100);
            }
        }

        return result;
    }

    /**
     * Alternative method that returns the data as a JSON string
     */
    public static String fetchAllElectionDataAsString() throws IOException, InterruptedException {
        ObjectNode data = fetchAllElectionData();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    }

    /**
     * Method to fetch data and save to the default PartiesAndCandidates.json file
     */
    public static void fetchAndSaveToResourcesFile() throws IOException, InterruptedException {
        fetchAndSaveToFile("src/main/resources/PartiesAndCandidates.json");
    }

    /**
     * Method to fetch data and save to file
     */
    public static void fetchAndSaveToFile(String filename) throws IOException, InterruptedException {
        String jsonData = fetchAllElectionDataAsString();

        // Ensure directory exists
        java.nio.file.Path filePath = java.nio.file.Paths.get(filename);
        java.nio.file.Files.createDirectories(filePath.getParent());

        java.nio.file.Files.write(filePath,
                jsonData.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("Data saved to: " + filename);
    }

    /**
     * Method to read existing data from PartiesAndCandidates.json if it exists
     */
    public static ObjectNode readExistingData() throws IOException {
        java.nio.file.Path resourcePath = java.nio.file.Paths.get("src/main/resources/PartiesAndCandidates.json");

        if (java.nio.file.Files.exists(resourcePath)) {
            String existingData = java.nio.file.Files.readString(resourcePath);
            if (!existingData.trim().isEmpty()) {
                return (ObjectNode) objectMapper.readTree(existingData);
            }
        }

        // Return empty object if file doesn't exist or is empty
        return objectMapper.createObjectNode();
    }

    /**
     * Method to merge new data with existing data and save to PartiesAndCandidates.json
     */
    public static void fetchAndMergeWithExistingData() throws IOException, InterruptedException {
        System.out.println("Reading existing data from PartiesAndCandidates.json...");
        ObjectNode existingData = readExistingData();

        System.out.println("Fetching new data from API...");
        ObjectNode newData = fetchAllElectionData();

        // Merge strategy: new data overwrites existing data for same territories
        existingData.setAll(newData);

        // Save merged data
        String mergedJsonData = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(existingData);

        java.nio.file.Path resourcePath = java.nio.file.Paths.get("src/main/resources/PartiesAndCandidates.json");
        java.nio.file.Files.createDirectories(resourcePath.getParent());
        java.nio.file.Files.write(resourcePath,
                mergedJsonData.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("Merged data saved to: src/main/resources/PartiesAndCandidates.json");
        System.out.println("Total territories in file: " + existingData.size());
    }

    /**
     * Method to get data for a specific territory only
     */
    public static ArrayNode fetchTerritoryData(String territoryName) throws IOException, InterruptedException {
        ObjectNode allData = fetchAllElectionData();
        return (ArrayNode) allData.get(territoryName);
    }

    public static void main(String[] args) {
        try {
            System.out.println("Starting to fetch Portuguese election data...");

            // Option 1: Fetch and save directly to PartiesAndCandidates.json
            fetchAndSaveToResourcesFile();

            // Option 2: Or merge with existing data (uncomment to use)
            // fetchAndMergeWithExistingData();

            // Read the final data to show summary
            ObjectNode finalData = readExistingData();

            // Print summary
            System.out.println("\n=== OPERATION COMPLETE ===");
            System.out.println("Total territories in PartiesAndCandidates.json: " + finalData.size());

            // Print territory names
            if (finalData.size() > 0) {
                System.out.println("Territories found:");
                finalData.fieldNames().forEachRemaining(territory -> {
                    ArrayNode parties = (ArrayNode) finalData.get(territory);
                    System.out.println("  - " + territory + " (" + parties.size() + " parties)");
                });
            }

        } catch (Exception e) {
            System.err.println("Error processing election data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}