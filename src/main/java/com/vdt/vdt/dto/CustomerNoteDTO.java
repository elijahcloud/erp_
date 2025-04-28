package com.vdt.vdt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerNoteDTO {
    private Long id;
    private String noteContent;
    private LocalDateTime dateCreated;
    private String userName;
    private boolean pinned;
    private LocalDateTime createdAt; // Add this field

    // Getter and setter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
