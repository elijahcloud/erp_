package com.vdt.vdt.controller;

import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.dto.CustomerDTO;
import com.vdt.vdt.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/api/customer")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO customerDTO){
        try {
            Customer customer = customerService.addNewCustomer(customerDTO);
            return ResponseEntity.ok(CustomerDTO.getCustomerDTO(customer));
        }catch (UsernameNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Agent with ID " + customerDTO.getUserAgentId() + " not found.");
        }
    }

    @GetMapping("/api/customer")
    public Page<CustomerDTO> getCustomers(Pageable pageable) {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        return customerService.getAllCustomers(pageable).map(CustomerDTO::getCustomerDTO);
    }

    @GetMapping("/api/customer/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id){
        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(CustomerDTO.getCustomerDTO(customer));
        }catch (UsernameNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Customer not found.");
        }
    }

    @PatchMapping("/api/customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        try {
            // Ensure the ID from path is set in DTO (in case it's not passed in the body)
            customerDTO.setId(id);

            Customer updatedCustomer = customerService.updateCustomer(customerDTO);
            return ResponseEntity.ok(CustomerDTO.getCustomerDTO(updatedCustomer));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating customer: " + ex.getMessage());
        }
    }



}
