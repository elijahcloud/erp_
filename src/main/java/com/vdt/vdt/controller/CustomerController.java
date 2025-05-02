package com.vdt.vdt.controller;

import com.vdt.vdt.dto.CustomerRequestDTO;
import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.dto.CustomerDTO;
import com.vdt.vdt.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

@PostMapping
@Operation(summary = "Create a new customer", description = "Creates a new customer based on the provided data")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Customer created successfully", content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
    @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content(schema = @Schema(implementation = String.class)))
})
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequestDTO customerDTO){
        try {
            CustomerDTO customer = customerService.addNewCustomer(customerDTO);
            return ResponseEntity.ok(customer);
        }catch (EntityNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

@GetMapping
@Operation(summary = "Retrieve all customers", description = "Retrieves a paginated list of all customers for a specific tenant")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customer list"),
    @ApiResponse(responseCode = "400", description = "Invalid input parameters")
})
@Parameter(name = "tenantId", description = "The ID of the tenant whose customers are to be fetched", required = true)
@Parameter(name = "pageable", description = "Pagination details")
    public Page<CustomerDTO> getCustomers(@RequestParam Long tenantId, Pageable pageable) {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        return customerService.getAllCustomers(tenantId,pageable);
    }

@GetMapping("/{id}")
@Operation(summary = "Retrieve a single customer by ID", description = "Fetches details of a single customer by their unique ID")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customer", content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = String.class)))
})
@Parameter(name = "id", description = "The unique ID of the customer to fetch", required = true)
    public ResponseEntity<?> getCustomer(@PathVariable Long id){
        try {
            CustomerDTO customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        }catch (UsernameNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Customer not found.");
        }
    }

@PatchMapping("/{id}")
@Operation(summary = "Update an existing customer", description = "Updates details of an existing customer based on their unique ID")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Customer updated successfully", content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error")
})
@Parameter(name = "id", description = "The unique ID of the customer to update", required = true)
@Parameter(name = "customerDTO", description = "Updated data for the customer", required = true)
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO customerDTO) {
        try {
            // Ensure the ID from path is set in DTO (in case it's not passed in the body)
            //customerDTO.setId(id);

            CustomerDTO updatedCustomer = customerService.updateCustomer(id,customerDTO);
            return ResponseEntity.ok(updatedCustomer);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating customer: " + ex.getMessage());
        }
    }

}
