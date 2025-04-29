package com.vdt.vdt.dto;

import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.entity.KYC;
import com.vdt.vdt.entity.ERPServiceType;
import com.vdt.vdt.entity.SubscriptionService;
import lombok.Data;

import java.util.List;

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
    List<SubscriptionService> subscriptionServices;
    KYC kyc;

    public void addSubscriptionService(SubscriptionService subscriptionService){
        subscriptionServices.add(subscriptionService);
    }

    public static CustomerDTO getCustomerDTO(Customer customer) {
        System.out.println("Converting Customer to CustomerDTO: " + customer);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setCustomerTier(customer.getCustomerTier().toString());
        customerDTO.setAccountType(customer.getAccountType().toString());
        customerDTO.setUserAgentId(customer.getAgent().getId());
        customerDTO.setAccountNumber(customer.getAccountNumber());

        System.out.println("Converted CustomerDTO: " + customerDTO);
        return customerDTO;
    }
}
