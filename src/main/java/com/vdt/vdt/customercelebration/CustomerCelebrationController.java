package com.vdt.vdt.customercelebration;

import com.vdt.vdt.customercelebration.CelebrationRequestDto;
import com.vdt.vdt.customercelebration.CustomerCelebration;
import com.vdt.vdt.customercelebration.CustomerCelebrationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/{customerId}/celebration")
public class CustomerCelebrationController {

    @Autowired
    CustomerCelebrationService celebrationService;

    @PostMapping()
    public ResponseEntity<?> createCelebration(
            @PathVariable Long customerId,
            @RequestBody CelebrationRequestDto celebrationRequestDto) {
        try {
            // Call service to add celebration
            CustomerCelebration createdCelebration = celebrationService.addCelebration(customerId, celebrationRequestDto);

            // Return success response
            return new ResponseEntity<>(createdCelebration, HttpStatus.CREATED);
        } catch (EntityNotFoundException ex) {
            // Handle case when customer is not found
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            // Handle other unexpected errors
            return new ResponseEntity<>("An error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllCelebrations(@PathVariable Long customerId) {
        try {
            // Call the service to fetch all celebrations for the customer
            List<CustomerCelebration> celebrations = celebrationService.getAllCelebrationsForCustomer(customerId);

            // Return celebrations in the response
            return new ResponseEntity<>(celebrations, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            // Handle case when customer is not found
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            // Handle other unexpected errors
            return new ResponseEntity<>("An error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @PutMapping("/{celebrationId}")
    public ResponseEntity<?> updateCelebration(
            @PathVariable Long customerId,
            @PathVariable Long celebrationId,
            @RequestBody CelebrationRequestDto celebrationRequestDto) {
        try {
            // Call service to update celebration
            CustomerCelebration updatedCelebration = celebrationService.updateCelebration(customerId, celebrationId, celebrationRequestDto);

            // Return success response
            return new ResponseEntity<>(updatedCelebration, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            // Handle case when customer or celebration is not found
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            // Handle other unexpected errors
            return new ResponseEntity<>("An error occurred while updating the celebration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}