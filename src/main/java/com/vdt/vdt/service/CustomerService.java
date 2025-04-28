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
import com.vdt.vdt.entity.AccountType; // Ensure this import matches the actual package of AccountType
import com.vdt.vdt.entity.CustomerTier; // Ensure this import matches the actual package of CustomerTier

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    public Customer addNewCustomer(CustomerDTO customerDTO) {
        System.out.println("Starting addNewCustomer with CustomerDTO: " + customerDTO);

        Customer customer = new Customer();

        // Fetch Tenant User agent
        Optional<User> userOptional = userRepository.findById(customerDTO.getUserAgentId().toString());
        System.out.println("User present: " + userOptional.isPresent());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            customer.setAgent(user);
            customer.setName(customerDTO.getName());
            customer.setEmail(customerDTO.getEmail());
            customer.setPhoneNumber(customerDTO.getPhoneNumber());
            customer.setCustomerTier(CustomerTier.valueOf(customerDTO.getCustomerTier()));
            customer.setAccountType(AccountType.valueOf(customerDTO.getAccountType()));
            customer.setAccountNumber(customerDTO.getAccountNumber());
        } else {
            String errorMessage = "User Agent with ID " + customerDTO.getUserAgentId() + " not found.";
            System.out.println(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }

        Customer savedCustomer = customerRepository.save(customer);
        System.out.println("Customer saved successfully: " + savedCustomer);
        return savedCustomer;
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        System.out.println("Fetching all customers with pageable: " + pageable);
        return customerRepository.findAll(pageable);
    }

    public Customer getCustomerById(Long id) {
        System.out.println("Fetching customer by ID: " + id);

        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            System.out.println("Customer found: " + customer);
            return customer;
        } else {
            String errorMessage = "Customer with ID " + id + " not found.";
            System.out.println(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }
    }

    public Customer updateCustomer(CustomerDTO customerDTO) throws UsernameNotFoundException {
        System.out.println("Starting updateCustomer with CustomerDTO: " + customerDTO);

        // Fetch existing customer by ID
        Customer existingCustomer = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> {
                    String errorMessage = "Customer not found with ID: " + customerDTO.getId();
                    System.out.println(errorMessage);
                    return new UsernameNotFoundException(errorMessage);
                });

        if (customerDTO.getUserAgentId() != null) {
            User userAgent = userRepository.findById(customerDTO.getUserAgentId().toString())
                    .orElseThrow(() -> {
                        String errorMessage = "User not found with ID: " + customerDTO.getUserAgentId();
                        System.out.println(errorMessage);
                        return new UsernameNotFoundException(errorMessage);
                    });
            existingCustomer.setAgent(userAgent);
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
            existingCustomer.setAccountType(AccountType.valueOf(customerDTO.getAccountType()));
        }
        if (customerDTO.getCustomerTier() != null) {
            existingCustomer.setCustomerTier(CustomerTier.valueOf(customerDTO.getCustomerTier()));
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        System.out.println("Customer updated successfully: " + updatedCustomer);
        return updatedCustomer;
    }
}
