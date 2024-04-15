package com.spring.crud.dao;

import com.spring.crud.model.entity.CategoryEntity;
import com.spring.crud.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDaoInterface extends JpaRepository<ProductEntity, Integer> {
    Page<ProductEntity> findAll(Pageable pageable);
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
    ProductEntity findProductEntitiesById(Integer id);
}