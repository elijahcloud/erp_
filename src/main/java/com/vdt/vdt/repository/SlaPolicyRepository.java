package com.vdt.vdt.repository;

import com.vdt.vdt.entity.CustomerAccountType;
import com.vdt.vdt.entity.SlaPolicy;
import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlaPolicyRepository extends JpaRepository<SlaPolicy, Long> {

    @Query("SELECT p FROM SlaPolicy p WHERE p.ticketType = :ticketType " +
            "AND p.priority = :priority " +
            "AND p.customerAccountType = :customerAccountType " )
    Optional<SlaPolicy> findMatchingPolicy(
            @Param("ticketType") TicketType ticketType,
            @Param("priority") TicketPriority priority,
            @Param("customerAccountType") CustomerAccountType customerAccountType
    );

    SlaPolicy findByCustomerAccountTypeAndTicketType(CustomerAccountType accountType, TicketType type);

    Page<SlaPolicy> findAllByDeletedFalse(Pageable pageable);

}


