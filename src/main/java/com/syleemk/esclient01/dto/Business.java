package com.syleemk.esclient01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {
    private Long businessId;
    private String name;
    private String desc;
}
