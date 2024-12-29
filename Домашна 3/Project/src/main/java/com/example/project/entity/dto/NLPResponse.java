package com.example.project1.entity.dto;

import lombok.Data;

@Data
public class NLPResponse {
    public String recommendation;
    public Double sentimentScore;
}
