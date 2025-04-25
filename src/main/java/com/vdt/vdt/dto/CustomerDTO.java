package com.vdt.vdt.dto;

import com.vdt.vdt.entity.Customer;
import lombok.Data;

@Data
public class CustomerDTO {
    Long id;
    Long userAgentId;
    String name;
    String phoneNumber;
    String email;
    String accountType;
    String customerTier;
    String accountNumber;

    public static CustomerDTO getCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setCustomerTier(customer.getCustomerTier());
        customerDTO.setAccountType(customer.getAccountType());
        customerDTO.setUserAgentId(customer.getAgent().getId());
        customerDTO.setAccountNumber(customer.getAccountNumber());
        return customerDTO;
    }
}
