package com.vdt.vdt.service;

import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.dto.CustomerDTO;
import com.vdt.vdt.repository.CustomerRepository;
import com.vdt.vdt.entity.User;
import com.vdt.vdt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    public Customer addNewCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();

        //Fetch Tenant User agent
        User user = null;
        Optional<User> userOptional =  userRepository.findById(customerDTO.getUserAgentId().toString());
        System.out.println("user present: "+userOptional.isPresent());

        if(userOptional.isPresent()){
            user = userOptional.get();
            customer.setAgent(user);
            customer.setName(customerDTO.getName());
            customer.setEmail(customerDTO.getEmail());
            customer.setPhoneNumber(customerDTO.getPhoneNumber());
            customer.setCustomerTier(customerDTO.getCustomerTier());
            customer.setAccountType(customerDTO.getAccountType());
            customer.setAccountNumber(customerDTO.getAccountNumber());
        }else{
            throw  new UsernameNotFoundException("User Agent with id"  + customerDTO.getUserAgentId());
        }

        return customerRepository.save(customer);
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Customer getCustomerById(Long id){

        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isPresent()){
            return customerOptional.get();
        }else{
            throw  new UsernameNotFoundException("User Agent with id"  + id);
        }
    }

    public Customer updateCustomer(CustomerDTO customerDTO) throws UsernameNotFoundException {
        // Fetch existing customer by ID
        Customer existingCustomer = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with ID: " + customerDTO.getId()));

        if (customerDTO.getUserAgentId() != null) {
            User userAgent = userRepository.findById(customerDTO.getUserAgentId().toString())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + customerDTO.getUserAgentId()));
            existingCustomer.setAgent(userAgent); // assuming there's a setUser() or similar method
        }
        if (customerDTO.getName() != null) {
            existingCustomer.setName(customerDTO.getName());
        }
        if (customerDTO.getPhoneNumber() != null) {
            existingCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
        }
        if (customerDTO.getEmail() != null) {
            existingCustomer.setEmail(customerDTO.getEmail());
        }
        if (customerDTO.getAccountType() != null) {
            existingCustomer.setAccountType(customerDTO.getAccountType());
        }
        if (customerDTO.getCustomerTier() != null) {
            existingCustomer.setCustomerTier(customerDTO.getCustomerTier());
        }

        // Save and return updated customer
        return customerRepository.save(existingCustomer);
    }



}
