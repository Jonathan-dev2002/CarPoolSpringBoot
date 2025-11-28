package com.miniProject.Carpool.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationSearchRequest extends BaseSearchRequest {
    private String type;
    private Boolean read;
    private Boolean adminReviewed;

    private String userId; // Admin filter by user

    private String createdFrom;
    private String createdTo;
}