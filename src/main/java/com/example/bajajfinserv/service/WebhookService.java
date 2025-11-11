package com.example.bajajfinserv.service;

import com.example.bajajfinserv.model.WebhookResponse;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class WebhookService {

    public void executeFlow() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            JSONObject requestBody = new JSONObject();
            requestBody.put("name", "Sanika A M"); 
            requestBody.put("regNo", "PES2UG22CS500"); 
            requestBody.put("email", "sanikaamshetty@gmail.com"); 

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

            System.out.println("🌐 Sending registration request...");
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            String rawResponse = response.getBody();
            System.out.println("🌐 Raw Response from generateWebhook: " + rawResponse);

            
            JSONObject jsonResponse = new JSONObject(rawResponse);
            
            if (!jsonResponse.has("webhook") || !jsonResponse.has("accessToken")) {
                System.err.println("❌ Missing 'webhook' or 'accessToken' in response. Please check API request details.");
                return;
            }

            WebhookResponse webhookResponse = new WebhookResponse();
            webhookResponse.setWebhookUrl(jsonResponse.getString("webhook"));
            webhookResponse.setAccessToken(jsonResponse.getString("accessToken"));

            System.out.println("✅ Webhook URL: " + webhookResponse.getWebhookUrl());
            System.out.println("✅ Access Token: " + webhookResponse.getAccessToken());

        
            String finalQuery = "SELECT E1.EMP_ID, E1.FIRST_NAME, E1.LAST_NAME, D.DEPARTMENT_NAME, "
                  + "COUNT(E2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT "
                  + "FROM EMPLOYEE E1 "
                  + "JOIN DEPARTMENT D ON E1.DEPARTMENT = D.DEPARTMENT_ID "
                  + "LEFT JOIN EMPLOYEE E2 ON E1.DEPARTMENT = E2.DEPARTMENT "
                  + "AND E2.DOB > E1.DOB "
                  + "GROUP BY E1.EMP_ID, E1.FIRST_NAME, E1.LAST_NAME, D.DEPARTMENT_NAME "
                  + "ORDER BY E1.EMP_ID DESC;";


            
            JSONObject answerBody = new JSONObject();
            answerBody.put("finalQuery", finalQuery);

            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);

        
            headers2.set("Authorization", webhookResponse.getAccessToken());

            HttpEntity<String> entity2 = new HttpEntity<>(answerBody.toString(), headers2);

            System.out.println("📤 Submitting final query to webhook...");
            try {
                ResponseEntity<String> result = restTemplate.postForEntity(webhookResponse.getWebhookUrl(), entity2, String.class);
                System.out.println("📨 Submission Response: " + result.getBody());
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                System.err.println("⚠️ Submission failed: " + e.getStatusCode() + " - " + e.getStatusText());
                System.err.println("Possible cause: Invalid registration details or expired token.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error during execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
