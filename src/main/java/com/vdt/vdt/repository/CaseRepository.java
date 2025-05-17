package com.vdt.vdt.repository;


import com.vdt.vdt.entity.Case;
import com.vdt.vdt.entity.IssueType;
import com.vdt.vdt.entity.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CaseRepository extends JpaRepository<Case, Long> {



    Page<Case> findByCaseStatus(TicketStatus status, Pageable pageable);


    @Query("SELECT c FROM Case c WHERE c.issueType = :issueType AND c.caseStatus = 'OPEN'")
    Optional<Case> findOpenCaseByIssueType(@Param("issueType") IssueType issueType);


}


