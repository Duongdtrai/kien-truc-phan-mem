package com.spring.crud.dao;

import com.spring.crud.model.entity.ProductSupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSupplierDaoInterface extends JpaRepository<ProductSupplierEntity, Integer> {
    List<ProductSupplierEntity> findAllByProductId(Integer productId);

    void deleteByProductId(Integer productId);
}
