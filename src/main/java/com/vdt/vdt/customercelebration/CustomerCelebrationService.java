package com.vdt.vdt.customercelebration;

import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCelebrationService {
    @Autowired
    CustomerCelebrationRepository celebrationRepository;

    @Autowired
    CustomerRepository customerRepository;

    CustomerCelebration addCelebration(Long customerId, CelebrationRequestDto celebrationRequestDto) {
        // Fetch Customer
        Customer customerDb = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer With Id Not Found"));

        // Create a new celebration entity
        CustomerCelebration newCelebration = new CustomerCelebration();
        newCelebration.setCustomer(customerDb); // Relate customer to the celebration
        newCelebration.setCelebrationType(celebrationRequestDto.getCelebrationType());
        newCelebration.setCustomLabel(celebrationRequestDto.getCustomLabel());
        newCelebration.setDate(celebrationRequestDto.getCelebrationDate());
        newCelebration.setNote(celebrationRequestDto.getNote());
        newCelebration.setReminderInDays(celebrationRequestDto.getReminderDays());

        // Save the celebration
        return celebrationRepository.save(newCelebration);
    }

    public List<CustomerCelebration> getAllCelebrationsForCustomer(Long customerId) {
        // Fetch customer to ensure it exists
        Customer customerDb = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer With Id Not Found"));

        // Fetch celebrations for the customer
        return celebrationRepository.findAllByCustomer(customerDb);
    }

    public CustomerCelebration updateCelebration(Long customerId, Long celebrationId, CelebrationRequestDto celebrationRequestDto) {
        // Fetch Customer
        Customer customerDb = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer With Id Not Found"));
    
        // Fetch Celebration
        CustomerCelebration celebrationDb = celebrationRepository.findById(celebrationId)
                .orElseThrow(() -> new EntityNotFoundException("Celebration With Id Not Found"));
    
        // Validate that the celebration is associated with the correct customer
        if (!celebrationDb.getCustomer().getId().equals(customerDb.getId())) {
            throw new IllegalArgumentException("Celebration does not belong to the specified customer");
        }
    
        // Update the fields of the celebration only if they are not null
        if (celebrationRequestDto.getCelebrationType() != null) {
            celebrationDb.setCelebrationType(celebrationRequestDto.getCelebrationType());
        }
        if (celebrationRequestDto.getCelebrationDate() != null) {
            celebrationDb.setDate(celebrationRequestDto.getCelebrationDate());
        }
        if (celebrationRequestDto.getNote() != null) {
            celebrationDb.setNote(celebrationRequestDto.getNote());
        }
    
        // Save and return the updated celebration
        return celebrationRepository.save(celebrationDb);
    }
}
