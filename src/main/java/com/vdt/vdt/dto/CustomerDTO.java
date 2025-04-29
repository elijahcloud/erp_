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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserAgentId() {
        return userAgentId;
    }

    public void setUserAgentId(Long userAgentId) {
        this.userAgentId = userAgentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCustomerTier() {
        return customerTier;
    }

    public void setCustomerTier(String customerTier) {
        this.customerTier = customerTier;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
