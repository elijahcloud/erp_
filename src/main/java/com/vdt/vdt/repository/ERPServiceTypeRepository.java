package com.vdt.vdt.repository;


import com.vdt.vdt.entity.ERPServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ERPServiceTypeRepository extends JpaRepository<ERPServiceType, Long> {

    // Find services by serviceType
    List<ERPServiceType> findByServiceName(String serviceType);

    // You can define more methods depending on what you need
}
