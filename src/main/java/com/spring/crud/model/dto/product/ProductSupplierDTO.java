package com.spring.crud.model.dto.product;

import com.spring.crud.model.entity.SupplierEntity;
import lombok.Data;

@Data
public class ProductSupplierDTO {
    private Integer id;
    private SupplierEntity supplier;
    private Double price;
    private Integer quantity;
}