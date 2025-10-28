package com.surya.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOfferingDto {

    private Long id;
    private String name;
    private String image;
    private String description;
    private int price;
    private int duration;
    private Long saloonId;
    private Long categoryId;
}
