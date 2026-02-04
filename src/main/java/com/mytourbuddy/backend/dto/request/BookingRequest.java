package com.mytourbuddy.backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    @NotBlank(message = "Package ID is required")
    private String pkgId;

    @NotNull(message = "Total price is required")
    @Positive(message = "Price must be greater than 0")
    private Double totalPrice;

    @NotNull(message = "Total count is required")
    @Positive(message = "Count must be greater than 0")
    private Integer totalCount;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;
}
