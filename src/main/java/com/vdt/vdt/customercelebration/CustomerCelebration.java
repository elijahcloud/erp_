package com.vdt.vdt.customercelebration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vdt.vdt.entity.Customer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "customer_celebrations")
@Getter
@Setter
@NoArgsConstructor
public class CustomerCelebration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celebration_id")
    Long id;

    CelebrationType celebrationType;

    String customLabel;

    LocalDate date;

    @Column
    Long reminderInDays;

    String note;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id_ref", referencedColumnName = "customer_id")
    Customer customer;
}
