package com.vdt.vdt.service;

import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.dto.CustomerBillingDTO;
import com.vdt.vdt.entity.CustomerBilling;
import com.vdt.vdt.repository.CustomerBillingRepository;
import com.vdt.vdt.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerBillingService {

    @Autowired
    private CustomerBillingRepository billingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerBillingDTO getBillingByCustomerId(Long customerId) {
        CustomerBilling billing = billingRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new UsernameNotFoundException("Billing info not found for customer ID: " + customerId));
        return convertToDTO(billing);
    }

    public CustomerBillingDTO updateBilling(Long customerId, CustomerBillingDTO dto) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new UsernameNotFoundException("Customer not found with ID: " + customerId));

        CustomerBilling billing = billingRepository.findByCustomerId(customerId)
            .orElse(new CustomerBilling());
        billing.setCustomer(customer);

        if (dto.getPaymentMethod() != null) billing.setPaymentMethod(dto.getPaymentMethod());
        if (dto.getBillingCycle() != null) billing.setBillingCycle(dto.getBillingCycle());
        if (dto.getCurrency() != null) billing.setCurrency(dto.getCurrency());
        if (dto.getTaxId() != null) billing.setTaxId(dto.getTaxId());

        billingRepository.save(billing);
        return convertToDTO(billing);
    }

    private CustomerBillingDTO convertToDTO(CustomerBilling billing) {
        CustomerBillingDTO dto = new CustomerBillingDTO();
        dto.setPaymentMethod(billing.getPaymentMethod());
        dto.setBillingCycle(billing.getBillingCycle());
        dto.setCurrency(billing.getCurrency());
        dto.setTaxId(billing.getTaxId());
        return dto;
    }
}
