package com.vdt.vdt.repository;

import com.vdt.vdt.entity.CustomerNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerNoteRepository extends JpaRepository<CustomerNote, Long> {

    Page<CustomerNote> findByCustomerId(Long customerId, Pageable pageable);

    List<CustomerNote> findByCustomerIdAndPinnedTrue(Long customerId);
}
