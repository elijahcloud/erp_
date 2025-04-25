package com.vdt.vdt.repository;

import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> , JpaSpecificationExecutor<Ticket> {
    @Query("SELECT t FROM Ticket t WHERE t.deleted = false")
    Page<Ticket> findAllActive(Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.id = :id AND t.deleted = false")
    Optional<Ticket> findActiveById(Long id);

    List<Ticket> findByCustomerId(Long customerId);

    @Query("SELECT t FROM Ticket t WHERE " +
            "(t.customer.id = :customerId OR :customerId IS NULL) " +
            "AND (t.id = :ticketId OR :ticketId IS NULL) " +
            "AND (t.status = :status OR :status IS NULL) " +
            "AND (t.priority = :priority OR :priority IS NULL)")
    Page<Ticket> searchTickets(Long customerId, Long ticketId, TicketStatus status, TicketPriority priority, Pageable pageable);

    long countByStatus(TicketStatus ticketStatus);

}
