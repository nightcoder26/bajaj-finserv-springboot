package com.example.springboot_webhook.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class TaskService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void runTask() {
        try {
            // 1. Call generateWebhook API
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", "John Doe");        // <-- Replace with your details
            requestBody.put("regNo", "REG12347");       // <-- Replace with your regNo
            requestBody.put("email", "john@example.com"); // <-- Replace with your email

            ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String webhook = jsonNode.get("webhook").asText();
            String accessToken = jsonNode.get("accessToken").asText();

            System.out.println("Webhook: " + webhook);
            System.out.println("AccessToken: " + accessToken);

            // 2. Execute your SQL query (replace this with your actual solution)
            String finalQuery = "SELECT * FROM your_table;";

            // 3. Submit final query
            String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            Map<String, String> queryBody = new HashMap<>();
            queryBody.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(queryBody, headers);
            ResponseEntity<String> submitResponse = restTemplate.postForEntity(submitUrl, entity, String.class);

            System.out.println("Submission Response: " + submitResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
