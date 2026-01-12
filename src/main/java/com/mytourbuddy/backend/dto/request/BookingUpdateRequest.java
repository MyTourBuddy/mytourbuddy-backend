package com.mytourbuddy.backend.dto.request;

import com.mytourbuddy.backend.model.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingUpdateRequest {
    private BookingStatus bookingStatus;
}
