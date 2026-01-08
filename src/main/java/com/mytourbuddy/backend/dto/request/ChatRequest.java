package com.mytourbuddy.backend.dto.request;

import lombok.Data;

@Data
public class ChatRequest {
    private String userId;
    private String message;
}
