package com.example.springboot_webhook.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class StartupService implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 1. Generate webhook and get token
        Map<String, String> result = generateWebhook();

        if (result == null) {
            System.err.println("❌ Could not generate webhook");
            return;
        }

        String webhookUrl = result.get("webhookUrl");
        String accessToken = result.get("accessToken");

        System.out.println("✅ Got Webhook URL: " + webhookUrl);
        System.out.println("✅ Got Access Token: " + accessToken);

        // 2. Submit final SQL query using that token
        submitFinalQuery(accessToken);
    }

    private Map<String, String> generateWebhook() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("name", "M BHAVITHA");
        body.put("regNo", "22BCB7204");
        body.put("email", "bhavitha961@gmail.com");
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            System.out.println("✅ generateWebhook response: " + response.getBody());


            Map<String, String> result = new HashMap<>();
            result.put("webhookUrl", (String) response.getBody().get("webhook"));
            result.put("accessToken", (String) response.getBody().get("accessToken"));

            return result;
        } catch (Exception e) {
            System.err.println("❌ Error generating webhook: " + e.getMessage());
            return null;
        }
    }

    // Step 2: Submit query
    private void submitFinalQuery(String accessToken) {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

        // ⚡ Replace this with your actual final SQL query
        String finalQuery = "SELECT * FROM employee;";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            System.out.println("✅ Final submission response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("❌ Error submitting query: " + e.getMessage());
        }
    }
}
