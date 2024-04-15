package com.spring.crud.controller;


import com.spring.crud.dao.CategoryDaoInterface;
import com.spring.crud.dao.ProductDaoInterface;
import com.spring.crud.dao.ProductSupplierDaoInterface;
import com.spring.crud.dao.SupplierDaoInterface;
import com.spring.crud.model.dto.ProductDto;
import com.spring.crud.model.dto.product.ProductEntityDTO;
import com.spring.crud.model.dto.product.ProductSupplierDTO;
import com.spring.crud.model.entity.CategoryEntity;
import com.spring.crud.model.entity.ProductEntity;
import com.spring.crud.model.entity.ProductSupplierEntity;
import com.spring.crud.model.entity.SupplierEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
@Controller
public class ProductController {
    @Autowired
    private final ProductDaoInterface productDao;
    @Autowired
    private final CategoryDaoInterface categoryDao;
    @Autowired
    private final SupplierDaoInterface supplierDao;
    @Autowired
    private final ProductSupplierDaoInterface productSupplierDao;
    public ProductController(ProductDaoInterface productDao, CategoryDaoInterface categoryDao, SupplierDaoInterface supplierDao, ProductSupplierDaoInterface productSupplierDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.supplierDao = supplierDao;
        this.productSupplierDao = productSupplierDao;
    }

    @GetMapping("/product")
    public ModelAndView getListProduct(@RequestParam(name = "keySearch", required = false) String keySearch) {
        List<ProductEntity> data;
        if (keySearch != null && !keySearch.isEmpty()) {
            data = productDao.findByNameContainingIgnoreCase(keySearch);
        } else {
            data = productDao.findAll();
        }
        List<ProductEntityDTO> response = new ArrayList<>();
        for (ProductEntity value : data) {
            ProductEntityDTO productWithSupplierDTO = new ProductEntityDTO();
            productWithSupplierDTO.setId(value.getId());
            productWithSupplierDTO.setImage(value.getImage());
            productWithSupplierDTO.setName(value.getName());
            productWithSupplierDTO.setDescription(value.getDescription());
            productWithSupplierDTO.setCategory(value.getCategory());
            List<ProductSupplierEntity> productSuppliers = this.productSupplierDao.findAllByProductId(value.getId());
            List<ProductSupplierDTO> ProductSupplierDTOs = new ArrayList<>();
            for (ProductSupplierEntity valueProductSupplier : productSuppliers) {
                ProductSupplierDTO productSupplierDTO = new ProductSupplierDTO();
                SupplierEntity suppliers = this.supplierDao.findSupplierById(valueProductSupplier.getSupplierId());
                productSupplierDTO.setId(valueProductSupplier.getId());
                productSupplierDTO.setSupplier(suppliers);
                productSupplierDTO.setPrice(valueProductSupplier.getPrice());
                productSupplierDTO.setQuantity(valueProductSupplier.getQuantity());
                ProductSupplierDTOs.add(productSupplierDTO);
            }
            productWithSupplierDTO.setProductSuppliers(ProductSupplierDTOs);
            response.add(productWithSupplierDTO);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listProducts", response);
        modelAndView.setViewName("product/index");
        return modelAndView;
    }

    @GetMapping("/create_product")
    public ModelAndView createProduct() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("productDTO", new ProductDto());
        modelAndView.addObject("categories", this.categoryDao.findAll());
        modelAndView.addObject("suppliers", this.supplierDao.findAll());
        modelAndView.setViewName("product/new_product");
        return modelAndView;
    }

    @PostMapping("/createProduct")
    public String createProduct(@ModelAttribute("productDTO") ProductDto productDTO) {
        ProductEntity productNew = new ProductEntity();
        CategoryEntity categoryExist = categoryDao.findCategoryEntitiesById(productDTO.getCategoryId());
        productNew.setImage(productDTO.getImage());
        productNew.setName(productDTO.getName());
        productNew.setDescription(productDTO.getDescription());
        productNew.setCategory(categoryExist);
        List<ProductSupplierEntity> productSuppliers = new ArrayList<>();
        productNew = productDao.save(productNew);
        for (int i = 0; i < productDTO.getSupplierInfos().size(); i++) {
            if (productDTO.getSupplierInfos().get(i).getSupplierId() != null) {
                ProductSupplierEntity productSupplier = new ProductSupplierEntity();
                SupplierEntity supplier = this.supplierDao.findSupplierById(productDTO.getSupplierInfos().get(i).getSupplierId());
                productSupplier.setSupplierId(supplier.getId());
                productSupplier.setProductId(productNew.getId());
                productSupplier.setPrice(productDTO.getSupplierInfos().get(i).getPrice());
                productSupplier.setQuantity(productDTO.getSupplierInfos().get(i).getQuantity());
                productSuppliers.add(productSupplier);
            }
        }
        productSupplierDao.saveAll(productSuppliers);
        return "redirect:/product";
    }

    @PostMapping("/saveProduct/{id}")
    @Transactional
    public String updateProduct(@PathVariable("id") Integer id, @ModelAttribute("productDto") ProductDto productDto) {
        this.productSupplierDao.deleteByProductId(id);
        ProductEntity productExist = productDao.findProductEntitiesById(id);
        CategoryEntity categoryExist = categoryDao.findCategoryEntitiesById(productDto.getCategoryId());
        productExist.setImage(productDto.getImage());
        productExist.setName(productDto.getName());
        productExist.setDescription(productDto.getDescription());
        productExist.setCategory(categoryExist);

        List<ProductSupplierEntity> productSuppliers = new ArrayList<>();
        for (int i = 0; i < productDto.getSupplierInfos().size(); i++) {
            if (productDto.getSupplierInfos().get(i).getSupplierId() != null) {
                ProductSupplierEntity productSupplier = new ProductSupplierEntity();
                SupplierEntity supplier = this.supplierDao.findSupplierById(productDto.getSupplierInfos().get(i).getSupplierId());
                productSupplier.setSupplierId(supplier.getId());
                productSupplier.setProductId(id);
                productSupplier.setPrice(productDto.getSupplierInfos().get(i).getPrice());
                productSupplier.setQuantity(productDto.getSupplierInfos().get(i).getQuantity());
                productSuppliers.add(productSupplier);
            }
        }
        this.productSupplierDao.saveAll(productSuppliers);
        this.productDao.save(productExist);
        return "redirect:/product";
    }

    @GetMapping("/update_product/{id}")
    public ModelAndView getProductById(@PathVariable(value = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();

        // get by id
        ProductEntity data = productDao.findProductEntitiesById(id);
        ProductEntityDTO productWithSupplierDTO = new ProductEntityDTO();
        productWithSupplierDTO.setId(data.getId());
        productWithSupplierDTO.setImage(data.getImage());
        productWithSupplierDTO.setName(data.getName());
        productWithSupplierDTO.setDescription(data.getDescription());
        productWithSupplierDTO.setCategory(data.getCategory());
        List<ProductSupplierEntity> productSuppliers = this.productSupplierDao.findAllByProductId(data.getId());
        List<ProductSupplierDTO> ProductSupplierDTOs = new ArrayList<>();
        for (ProductSupplierEntity valueProductSupplier : productSuppliers) {
            ProductSupplierDTO productSupplierDTO = new ProductSupplierDTO();
            SupplierEntity suppliers = this.supplierDao.findSupplierById(valueProductSupplier.getSupplierId());
            productSupplierDTO.setId(valueProductSupplier.getId());
            productSupplierDTO.setSupplier(suppliers);
            productSupplierDTO.setPrice(valueProductSupplier.getPrice());
            productSupplierDTO.setQuantity(valueProductSupplier.getQuantity());
            ProductSupplierDTOs.add(productSupplierDTO);
        }
        productWithSupplierDTO.setProductSuppliers(ProductSupplierDTOs);
        ProductEntityDTO productDto = productWithSupplierDTO;

        List<Integer> supplierIds = new ArrayList<Integer>();
        for (ProductSupplierDTO value : productDto.getProductSuppliers()) {
            if (value.getSupplier() != null) {
                supplierIds.add(value.getSupplier().getId());
            }
        }
        modelAndView.addObject("productDto", productDto);
        modelAndView.addObject("categories", categoryDao.findAll());
        modelAndView.addObject("suppliers", supplierDao.findAll());
        modelAndView.addObject("supplierIds", supplierIds);
        modelAndView.setViewName("product/update_product");
        return modelAndView;
    }

    @GetMapping("/deleteProduct/{id}")
    @Transactional
    public String deleteProduct(@PathVariable(value = "id") Integer id) {
        System.out.println("id: "+ id);
        this.productSupplierDao.deleteByProductId(id);
        this.productDao.deleteById(id);
        return "redirect:/product";
    }
}
