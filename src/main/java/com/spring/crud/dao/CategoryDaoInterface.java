package com.spring.crud.dao;

import com.spring.crud.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDaoInterface extends JpaRepository<CategoryEntity, Integer> {

    CategoryEntity findCategoryEntitiesById(Integer id);

    CategoryEntity findByName(String name);
}
