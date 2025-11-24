package com.miniProject.Carpool.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSearchRequest extends BaseSearchRequest{
    private String role;
    private Boolean isActive;
}