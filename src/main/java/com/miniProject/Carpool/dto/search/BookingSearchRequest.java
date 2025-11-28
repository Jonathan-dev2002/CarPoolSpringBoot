package com.miniProject.Carpool.dto.search;

import com.miniProject.Carpool.model.BookingStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingSearchRequest extends BaseSearchRequest {
    private BookingStatus status;
    private String routeId;
    private String passengerId;
    private String driverId;
    private String createdFrom;
    private String createdTo;
}