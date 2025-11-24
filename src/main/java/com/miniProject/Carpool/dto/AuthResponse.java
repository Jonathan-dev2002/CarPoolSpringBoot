package com.miniProject.Carpool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // สร้าง constructor เปล่า
@AllArgsConstructor // สร้าง constructor ที่รับ field ทั้งหมด (jwt)
public class AuthResponse {
    private String jwt;
}
