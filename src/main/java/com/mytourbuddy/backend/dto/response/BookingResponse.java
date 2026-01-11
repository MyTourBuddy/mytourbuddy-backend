package com.mytourbuddy.backend.dto.response;

import java.time.Instant;

import com.mytourbuddy.backend.model.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private String id;
    private String touristId;
    private String pkgId;
    private Double totalPrice;
    private Integer totalCount;
    private BookingStatus bookingStatus;
    private Instant createdAt;
}
