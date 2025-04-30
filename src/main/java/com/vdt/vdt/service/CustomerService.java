package com.vdt.vdt.service;

import com.vdt.vdt.dto.CustomerRequestDTO;
import com.vdt.vdt.entity.*;
import com.vdt.vdt.dto.CustomerDTO;
import com.vdt.vdt.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ERPServiceTypeRepository erpServiceTypeRepository;

    @Autowired
    SubscriptionServiceRepository subscriptionServiceRepository;

    @Autowired
    KYCRepository kycRepository;

    @Autowired
    TenantRepository tenantRepository;

    public CustomerDTO addNewCustomer(CustomerRequestDTO customerDTO) {
        System.out.println("Starting addNewCustomer with CustomerDTO: " + customerDTO);

        Customer customer = new Customer();
        KYC kyc = new KYC();
        SubscriptionService subscriptionService = new SubscriptionService();

        //Fetch Tenant
        Tenant tenant = tenantRepository.findById(customerDTO.getTenantId()).orElseThrow(()->new EntityNotFoundException("Tenant With Id not found"));

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
            customer.setTenant(tenant);

        } else {
            String errorMessage = "User Agent with ID " + customerDTO.getUserAgentId() + " not found.";
            System.out.println(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        Customer savedCustomer = customerRepository.save(customer);

        kyc.setKycPackage(customerDTO.getKycPackageType());
        kyc.setCustomer(savedCustomer);
        kyc.setKycStatus(KYCStatus.PENDING);
        kycRepository.save(kyc);

        //Fetch all ERPServiceTypes
        customerDTO.getServiceTypeId().forEach((erpServiceTypeId)->{
            ERPServiceType erpServiceType = erpServiceTypeRepository.findById(erpServiceTypeId).orElseThrow(()->new EntityNotFoundException("Service Type Not Found"));
            SubscriptionService subscriptionService1 = new SubscriptionService();
            subscriptionService1.setCreatedAt(LocalDateTime.now());
            subscriptionService1.setErpServiceType(erpServiceType);
            subscriptionService1.setCustomer(savedCustomer);
            subscriptionService1.setTenant(tenant);
            subscriptionService1.setBillingCycle(customerDTO.getBillingCycleType());
            subscriptionServiceRepository.save(subscriptionService1);
        });

        System.out.println("Customer saved successfully: ");
        return getCustomerDTO(savedCustomer);
    }

    public Page<CustomerDTO> getAllCustomers(Long tenantId, Pageable pageable) {
        System.out.println("Fetching all customers with pageable: " + pageable);
        return customerRepository.findByTenantId(tenantId,pageable).map(this::getCustomerDTO);
    }

    public CustomerDTO getCustomerById(Long id) {
        System.out.println("Fetching customer by ID: " + id);

        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            return getCustomerDTO(customer);
        } else {
            String errorMessage = "Customer with ID " + id + " not found.";
            System.out.println(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }
    }

    public CustomerDTO updateCustomer(Long id, CustomerRequestDTO customerDTO) throws UsernameNotFoundException {
        System.out.println("Starting updateCustomer with CustomerDTO: " + customerDTO);

        // Fetch existing customer by ID
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Customer not found with ID: " + id;
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
        return getCustomerDTO(updatedCustomer);
    }

    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    }

    public CustomerDTO getCustomerDTO(Customer customer) {
        //System.out.println("Converting Customer to CustomerDTO: " + customer);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setCustomerTier(customer.getCustomerTier().toString());
        customerDTO.setAccountType(customer.getAccountType().toString());
        customerDTO.setUserAgentId(customer.getAgent().getId());
        customerDTO.setAccountNumber(customer.getAccountNumber());
        customerDTO.setKyc(kycRepository.findByCustomerId(customerDTO.getId()).get());

        customerDTO.setSubscriptionServices(subscriptionServiceRepository.findByCustomerId(customer.getId()));



        System.out.println("Converted CustomerDTO: " + customerDTO);
        return customerDTO;
    }
}
