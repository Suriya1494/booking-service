package com.surya.Dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;
@Data
public class SaloonDto {

    private Long id;
    private String name;
    private List<String> images;
    private String mail;
    private String phnNumber;
    private String adress;
    private String city;
    private Long ownerId;
    private LocalTime openTime;
    private  LocalTime closeTime;

}
