package com.miniProject.Carpool.dto;

import lombok.Data;

@Data
public class BaseSearchRequest {
    private int page = 0;
    private int size = 10;
    private String keyword;
}