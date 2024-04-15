package com.spring.crud.controller;

import com.spring.crud.dao.SupplierDaoInterface;
import com.spring.crud.model.dto.SupplierDto;
import com.spring.crud.model.entity.ProductEntity;
import com.spring.crud.model.entity.SupplierEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SupplierController {
    @Autowired
    private final SupplierDaoInterface supplierDao;

    @Autowired
    public SupplierController(SupplierDaoInterface supplierDao) {
        this.supplierDao = supplierDao;
    }

    @GetMapping("/supplier")
    public ModelAndView getAllSuppliers(@RequestParam(name = "keySearch", required = false) String keySearch) {
        System.out.println("keySearch: "+ keySearch);
        List<SupplierEntity> data;
        if (keySearch != null && !keySearch.isEmpty()) {
            data = supplierDao.findByNameContainingIgnoreCase(keySearch);
        } else {
            data = supplierDao.findAll();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listSuppliers", data);
        modelAndView.setViewName("supplier/index");
        return modelAndView;
    }

    @GetMapping("/showNewSupplierForm")
    public ModelAndView showNewSupplierForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("supplierDTO", new SupplierDto());
        modelAndView.setViewName("supplier/new_supplier");
        return modelAndView;
    }

    @PostMapping("/createSupplier")
    public String createSupplier(@ModelAttribute("supplierDTO") SupplierDto supplierDto) {
        SupplierEntity newSupplier = new SupplierEntity();
        newSupplier.setEmail(supplierDto.getEmail());
        newSupplier.setNote(supplierDto.getNote());
        newSupplier.setName(supplierDto.getName());
        newSupplier.setAddress(supplierDto.getAddress());
        newSupplier.setPhoneNumber(supplierDto.getPhoneNumber());
        this.supplierDao.save(newSupplier);
        return "redirect:/supplier";
    }

    @PostMapping("/updateSupplier")
    public String updateSupplier(@ModelAttribute("supplierDTO") SupplierDto supplierDto) {
        SupplierEntity existingSupplier = supplierDao.findSupplierById(supplierDto.getId());
        existingSupplier.setEmail(supplierDto.getEmail());
        existingSupplier.setNote(supplierDto.getNote());
        existingSupplier.setName(supplierDto.getName());
        existingSupplier.setAddress(supplierDto.getAddress());
        existingSupplier.setPhoneNumber(supplierDto.getPhoneNumber());
        supplierDao.save(existingSupplier);
        return "redirect:/supplier";
    }

    @GetMapping("/showFormForUpdateSupplier/{id}")
    public ModelAndView getSupplierById(@PathVariable(value = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        SupplierEntity supplierDto = this.supplierDao.findSupplierById(id);
        modelAndView.addObject("supplierDto", supplierDto);
        modelAndView.setViewName("supplier/update_supplier");
        return modelAndView;
    }

    @GetMapping("/deleteSupplier/{id}")
    public String deleteSupplier(@PathVariable(value = "id") Integer id) {
        SupplierEntity supplier = this.supplierDao.findSupplierById(id);
        this.supplierDao.delete(supplier);
        return "redirect:/supplier";
    }
}
