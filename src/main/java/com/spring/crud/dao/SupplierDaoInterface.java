package com.spring.crud.dao;

import com.spring.crud.model.entity.SupplierEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Repository
public interface SupplierDaoInterface extends JpaRepository<SupplierEntity, Integer> {
//    List<SupplierEntity> findAll(Pageable pageable);
    @Query("SELECT s FROM SupplierEntity s WHERE s.id IN :ids")
    List<SupplierEntity> findSuppliersByIds(@Param("ids") List<Integer> ids);
    SupplierEntity findSupplierById(Integer id);

    List<SupplierEntity> findByNameContainingIgnoreCase(String name);
}
