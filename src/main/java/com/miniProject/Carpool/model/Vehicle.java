package com.miniProject.Carpool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String licensePlate;

    private String vehicleModel;
    private String vehicleType;
    private String color;
    private Integer seatCapacity;

    @JdbcTypeCode(SqlTypes.ARRAY) // บอก Hibernate ว่านี่คือ ARRAY นะ
    @Column(columnDefinition = "text[]") // บังคับประเภทใน DB ให้ตรงกับ Postgres
    private List<String> amenities;

    @JdbcTypeCode(SqlTypes.JSON) // บอก Hibernate ว่านี่คือ JSON นะ (จะแปลง List <-> JSON อัตโนมัติ)
    @Column(columnDefinition = "jsonb") // แนะนำ jsonb เพราะเร็วกว่า json ธรรมดา
    private List<String> photos;

    @Builder.Default
    private Boolean isDefault = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id" , unique = true)
    private User user;
}
