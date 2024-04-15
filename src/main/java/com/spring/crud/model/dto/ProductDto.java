package com.spring.crud.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String image;
    private String name;
    private String description;
    private Integer categoryId;
    private List<SupplierInfo> supplierInfos;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SupplierInfo {
        private Integer supplierId;
        private double price;
        private int quantity;
    }
}

