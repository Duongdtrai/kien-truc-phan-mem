package com.spring.crud.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDto {
    private Integer id;

    private String email;

    private String name;

    private String address;

    private String phoneNumber;

    private String note;
}
