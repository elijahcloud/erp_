package com.vdt.vdt.repository;

import com.vdt.vdt.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PriceRepository extends JpaRepository<Price,Long> {
}
