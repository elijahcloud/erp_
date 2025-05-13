package com.vdt.vdt.repository;

import com.vdt.vdt.entity.Case;
import com.vdt.vdt.entity.Ticket;
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

    Page<Ticket> findByCustomerId(Long customerId, Pageable pageable);
    List<Ticket> findByCustomerId(Long customerId);

    long countByStatus(TicketStatus ticketStatus);

    @Query("SELECT t.id as id, t.createdAt as createdAt, t.slaResponseDueAt as slaResponseDueAt, t.slaResolutionDueAt as slaResolutionDueAt, t.priority as priority, t.firstResponseAt as firstResponseAt, t.assignedAgent as assignedAgent FROM Ticket t WHERE t.status <> 'CLOSED' AND t.status <> 'RESOLVED'")
    Page<Ticket> findOpenTickets(Pageable pageable);


    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.resolutionSlaBreached = false AND t.status ='CLOSED'")
    long countResolvedWithinSla();


    @Query("SELECT COUNT(t) FROM Ticket t")
    long countTotalTickets();

    @Query("SELECT t FROM Ticket t WHERE t.firstResponseAt IS NOT NULL")
    List<Ticket> findTicketsWithResponseTime();

    @Query("SELECT u.email, COUNT(t) FROM Ticket t JOIN User u ON t.assignedAgent.id = u.id WHERE t.responseSlaBreached = true OR t.resolutionSlaBreached = true GROUP BY u.email")
    List<Object[]> countSlaBreachesPerAgent();


    @Query("SELECT t.ticketType, COUNT(t), SUM(CASE WHEN t.resolutionSlaBreached = false THEN 1 ELSE 0 END) FROM Ticket t GROUP BY t.ticketType")
    List<Object[]> countSlaComplianceByTicketType();


    @Query(value = "SELECT u.user_email, COUNT(t.ticket_id) " +
            "FROM tickets t " +
            "JOIN users u ON t.ticket_assigned_agent = u.user_id " +
            "WHERE t.is_resolution_sla_breached = true " +
            "GROUP BY u.user_email " +
            "ORDER BY COUNT(t.ticket_id) DESC",
            countQuery = "SELECT COUNT(DISTINCT t.ticket_assigned_agent) FROM tickets t WHERE t.is_resolution_sla_breached = true",
            nativeQuery = true)
    Page<Object[]> findTopSlaViolators(Pageable pageable);



    @Query("""
    SELECT 
        t.id,
        t.ticketTitle,
        t.ticketType,
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

    @Query("SELECT t.assignedDepartment.name, COUNT(t) FROM Ticket t WHERE t.ticketSlaStatus = 'BREACHED' GROUP BY t.assignedDepartment.name")
    List<Ticket[]> countSlaBreachesPerDepartment();

    List<Ticket> findByTicketCase(Case ticketCase);


    List<Ticket> findByTicketCaseAndStatus(Case foundCase, TicketStatus ticketStatus);
}
