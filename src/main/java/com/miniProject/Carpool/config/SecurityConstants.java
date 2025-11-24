package com.miniProject.Carpool.config;

public class SecurityConstants {
    // รวม Path ที่อนุญาตให้เข้าถึงได้โดยไม่ต้อง Login
    public static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/register",
    };
}
