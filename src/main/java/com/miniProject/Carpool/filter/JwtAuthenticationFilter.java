package com.miniProject.Carpool.filter;

import com.miniProject.Carpool.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;
        final String role;

        // เช็ค Header ว่ามี Bearer ไหม
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // ตัดคำว่า "Bearer " ออก

        try {
            // ดึง User ID และ Role จาก Token
            userId = jwtUtil.extractUserId(jwt);
            role = jwtUtil.extractClaim(jwt, claims -> claims.get("role", String.class));

            // ถ้ามี ID และยังไม่ได้ Authenticate ใน Context
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtUtil.isTokenValid(jwt)) {
                    // สร้าง Authentication Token ของ Spring Security
                    // ใช้ userId เป็น Principal แทน username เพื่อให้ getMyUser ทำงานง่าย

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userId, // Principal (User ID)
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)) // Role
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // set เข้า Context (เหมือน req.user = ...)
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {

        }

        filterChain.doFilter(request, response);
    }
}
