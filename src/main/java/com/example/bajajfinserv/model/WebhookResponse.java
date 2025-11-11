package com.example.bajajfinserv.model;

import lombok.Data;

@Data
public class WebhookResponse {
    private String webhookUrl;
    private String accessToken;
}
