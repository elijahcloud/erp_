package com.vdt.vdt.service;

import com.vdt.vdt.dto.ERPServiceDTO;
import com.vdt.vdt.entity.ERPServiceType;
import com.vdt.vdt.entity.Price;
import com.vdt.vdt.entity.Tenant;
import com.vdt.vdt.repository.ERPServiceRepository;
import com.vdt.vdt.repository.PriceRepository;
import com.vdt.vdt.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ERPServiceTypeService {
    @Autowired
    ERPServiceRepository erpServiceRepository;
    @Autowired
    TenantRepository tenantRepository;
    @Autowired
    PriceRepository priceRepository;


    public ERPServiceType createService(ERPServiceDTO erpServiceDTO){
        System.out.println(erpServiceDTO);
        //Fetch Tenant
        Tenant tenant = tenantRepository.findById(erpServiceDTO.getTenantId()).orElseThrow(()->new EntityNotFoundException("Tenant With Id Not Found"));

        ERPServiceType erpServiceType = new ERPServiceType();
        erpServiceType.setServiceName(erpServiceDTO.getServiceName());
        erpServiceType.setLinkedBranch(erpServiceDTO.getLinkedBranch());
        erpServiceType.setLinkedZone(erpServiceDTO.getLinkedZone());
        erpServiceType.setTenant(tenant);

        erpServiceType = erpServiceRepository.save(erpServiceType);

        //create price
        Price price = new Price();
        price.setAmount(erpServiceDTO.getPrice());
        price.setErpServiceType(erpServiceType);
        price.setCreatedAt(LocalDateTime.now());
        priceRepository.save(price);

        return erpServiceRepository.findById(erpServiceType.getId()).orElseThrow(EntityNotFoundException::new);
    }

    public ERPServiceType updateService(ERPServiceDTO erpServiceDTO){
        //Fetch ERP service
        ERPServiceType erpServiceType = erpServiceRepository.findById(erpServiceDTO.getId()).orElseThrow(EntityNotFoundException::new);

        if(erpServiceDTO.getLinkedBranch()!= null) erpServiceType.setLinkedBranch(erpServiceType.getLinkedBranch());
        if(erpServiceDTO.getLinkedZone()!= null) erpServiceType.setLinkedZone(erpServiceType.getLinkedBranch());
        erpServiceType = erpServiceRepository.save(erpServiceType);

        if(erpServiceDTO.getPrice() != null) {
            Price price = new Price();
            price.setAmount(erpServiceDTO.getPrice());
            price.setCreatedAt(LocalDateTime.now());
            price.setErpServiceType(erpServiceType);
            priceRepository.save(price);
        }

        return erpServiceRepository.findById(erpServiceDTO.getId()).orElseThrow(EntityNotFoundException::new);
    }




}
