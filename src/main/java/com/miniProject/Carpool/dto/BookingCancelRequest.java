package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.CancelReason;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingCancelRequest {
    @NotNull
    private CancelReason reason;
}