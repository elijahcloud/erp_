package com.vdt.vdt.controller;

import com.vdt.vdt.dto.ERPServiceDTO;
import com.vdt.vdt.service.ERPServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ERPServiceTypeController {
    @Autowired
    ERPServiceTypeService erpServiceTypeService;

    @PostMapping("api/service")
    ResponseEntity<?> createService(@RequestBody ERPServiceDTO erpServiceDTO){
        return ResponseEntity.ok(erpServiceTypeService.createService(erpServiceDTO));
    }

    @PutMapping("/api/service")
    ResponseEntity<?> updateService(ERPServiceDTO erpServiceDTO){

        return ResponseEntity.ok(erpServiceTypeService.updateService(erpServiceDTO));
    }

}
