package com.miniProject.Carpool.dto;

import lombok.Data;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role; // รับเป็น String แล้วค่อยแปลงเป็น Enum ใน Service
}
