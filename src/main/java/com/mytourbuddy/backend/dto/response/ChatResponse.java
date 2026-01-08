package com.mytourbuddy.backend.dto.response;

import java.util.List;

import com.mytourbuddy.backend.model.Package;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private String response;
    private List<Package> suggestedPackages;
}