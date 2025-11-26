package com.miniProject.Carpool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Double lat;
    private Double lng;
    private String address;
    private String placeId;
    private String name;
}