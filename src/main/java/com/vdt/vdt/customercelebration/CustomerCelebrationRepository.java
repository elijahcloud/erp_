package com.vdt.vdt.customercelebration;

import com.vdt.vdt.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerCelebrationRepository extends JpaRepository<CustomerCelebration,Long> {

    List<CustomerCelebration> findAllByCustomer(Customer customerDb);
}
