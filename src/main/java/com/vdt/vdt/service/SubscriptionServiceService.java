package com.vdt.vdt.service;

import com.vdt.vdt.dto.ServiceSubscriptionRequestDto;
import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.entity.ERPServiceType;
import com.vdt.vdt.entity.SubscriptionService;
import com.vdt.vdt.entity.Tenant;
import com.vdt.vdt.repository.CustomerRepository;
import com.vdt.vdt.repository.ERPServiceTypeRepository;
import com.vdt.vdt.repository.SubscriptionServiceRepository;
import com.vdt.vdt.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionServiceService {
    @Autowired
    SubscriptionServiceRepository serviceRepository;

    @Autowired
    ERPServiceTypeRepository erpServiceTypeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TenantRepository tenantRepository;

    public SubscriptionService addServiceSubscription(Long customerId, ServiceSubscriptionRequestDto servSubRequestDto){
        //Fetch ERPServiceType
        ERPServiceType erpServiceType = erpServiceTypeRepository.findById(servSubRequestDto.getServiceTypeId()).orElseThrow(()->new EntityNotFoundException("Service Type With Id not found"));
        //Fetch Tenant
        Tenant tenant = tenantRepository.findById(servSubRequestDto.getTenantId()).orElseThrow(()->new EntityNotFoundException("Tenant With Id not found"));

        //Fetch Customer
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Tenant With Id not found"));

        //Create new Subscription Object
        SubscriptionService subscriptionService = new SubscriptionService();
        subscriptionService.setBillingCycle(servSubRequestDto.getBillingCycle());
        //Assign Tenant
        subscriptionService.setTenant(tenant);
        //Assign Customer
        subscriptionService.setCustomer(customer);
        //Assign ERP
        subscriptionService.setErpServiceType(erpServiceType);
        subscriptionService.setCreatedAt(LocalDateTime.now());
        //Save
        return serviceRepository.save(subscriptionService);
    }

    public List<SubscriptionService> getCustomerServiceSubscription(Long customerId){
        return serviceRepository.findByCustomerId(customerId);
    }



}
