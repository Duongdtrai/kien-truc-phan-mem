package com.spring.crud.model.dto.product;

import com.spring.crud.model.entity.CategoryEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProductEntityDTO {
    private Integer id;
    private String image;
    private String name;
    private String description;
    private CategoryEntity category;
    private List<ProductSupplierDTO> productSuppliers;
}