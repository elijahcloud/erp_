package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class DepartmentSlaBreachDTO {
    private String department;
    private Long breachedTicketCount;

    public DepartmentSlaBreachDTO(String department, Long breachedTicketCount) {
        this.department = department;
        this.breachedTicketCount = breachedTicketCount;
    }

    public String getDepartment() {
        return department;
    }

    public Long getBreachedTicketCount() {
        return breachedTicketCount;
    }
}

