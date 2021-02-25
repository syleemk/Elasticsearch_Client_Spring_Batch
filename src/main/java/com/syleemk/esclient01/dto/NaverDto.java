package com.syleemk.esclient01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaverDto {
    private Business business = new Business();
    private Booking booking = new Booking();
}
