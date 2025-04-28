package com.vdt.vdt.specification;

import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecification {

    public static Specification<Ticket> hasCustomerId(Long customerId) {
        return (Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (customerId != null) {
                return criteriaBuilder.equal(root.get("customer").get("id"), customerId);
            }
            return null;
        };
    }

    public static Specification<Ticket> hasTicketId(Long ticketId) {
        return (Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (ticketId != null) {
                return criteriaBuilder.equal(root.get("id"), ticketId);
            }
            return null;
        };
    }

    public static Specification<Ticket> hasStatus(TicketStatus status) {
        return (Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (status != null) {
                return criteriaBuilder.equal(root.get("status"), status);
            }
            return null;
        };
    }

    public static Specification<Ticket> hasPriority(TicketPriority priority) {
        return (Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (priority != null) {
                return criteriaBuilder.equal(root.get("priority"), priority);
            }
            return null;
        };
    }

    public static Specification<Ticket> combineSpecifications(Long customerId, Long ticketId, TicketStatus status, TicketPriority priority) {
        Specification<Ticket> specification = Specification.where(hasCustomerId(customerId));
        specification = specification.and(hasTicketId(ticketId));
        specification = specification.and(hasStatus(status));
        specification = specification.and(hasPriority(priority));

        return specification;
    }
}

