package com.miniProject.Carpool.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSearchRequest extends BaseSearchRequest {
    private String role;
    private Boolean isActive;
    private Boolean isVerified;

    private String createdFrom;
    private String createdTo;
}
