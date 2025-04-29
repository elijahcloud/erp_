package com.vdt.vdt.repository;

import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> , JpaSpecificationExecutor<Ticket> {

    List<Ticket> findByCustomerId(Long customerId);

    @Query("SELECT t FROM Ticket t WHERE " +
            "(t.customer.id = :customerId OR :customerId IS NULL) " +
            "AND (t.id = :ticketId OR :ticketId IS NULL) " +
            "AND (t.status = :status OR :status IS NULL) " +
            "AND (t.priority = :priority OR :priority IS NULL)")
    Page<Ticket> searchTickets(Long customerId, Long ticketId, TicketStatus status, TicketPriority priority, Pageable pageable);

    long countByStatus(TicketStatus ticketStatus);

    List<Ticket> findByStatus(TicketStatus ticketStatus);

    @Query("SELECT t.id as id, t.createdAt as createdAt, t.slaResponseDueAt as slaResponseDueAt, t.slaResolutionDueAt as slaResolutionDueAt, t.priority as priority, t.firstResponseAt as firstResponseAt, t.assignedAgent as assignedAgent FROM Ticket t WHERE t.status <> 'CLOSED' AND t.status <> 'RESOLVED'")
    Page<Ticket> findOpenTickets(Pageable pageable);


    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.resolutionSlaBreached = false AND t.resolvedAt IS NOT NULL")
    long countResolvedWithinSla();


    @Query("SELECT COUNT(t) FROM Ticket t")
    long countTotalTickets();


    @Query("SELECT t FROM Ticket t WHERE t.firstResponseAt IS NOT NULL")
    List<Ticket> findTicketsWithResponseTime();


    @Query("SELECT t.assignedAgent.id, COUNT(t) FROM Ticket t WHERE t.responseSlaBreached = true OR t.resolutionSlaBreached = true GROUP BY t.assignedAgent.id")
    List<Object[]> countSlaBreachesPerAgent();


    @Query("SELECT t.type, COUNT(t), SUM(CASE WHEN t.resolutionSlaBreached = false THEN 1 ELSE 0 END) FROM Ticket t GROUP BY t.type")
    List<Object[]> countSlaComplianceByTicketType();


    @Query("SELECT t.assignedAgent.id, COUNT(t) FROM Ticket t WHERE t.resolutionSlaBreached = true GROUP BY t.assignedAgent.id ORDER BY COUNT(t) DESC")
    List<Object[]> findTopSlaViolators();

    @Query("""
    SELECT 
        t.id,
        t.ticketTitle,
        t.type,
        t.priority,
        t.status,
        t.createdAt,
        t.firstResponseAt,
        t.resolvedAt,
        t.slaResponseDueAt,
        t.slaResolutionDueAt,
        t.responseSlaBreached,
        t.resolutionSlaBreached,
        t.assignedAgent.id
    FROM Ticket t
    WHERE t.deleted = false
""")
    List<Object[]> getSlaComplianceData();

    @Query("SELECT t FROM Ticket t WHERE t.slaResolutionDueAt IS NOT NULL AND t.slaResolutionDueAt BETWEEN :startTime AND :endTime")
    List<Ticket> findTicketsNearSlaBreach(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
