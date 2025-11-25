package com.miniProject.Carpool.dto.search;

import lombok.Data;

import lombok.Data;

@Data
public class BaseSearchRequest {
    private int page = 1;
    private int limit = 20;
    private String q; // Keyword search
}