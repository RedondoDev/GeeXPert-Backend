package com.redondodev.GeeXPert_back.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RecommendationRequest {

    private String model;
    private String prompt;
    private boolean stream;

}
