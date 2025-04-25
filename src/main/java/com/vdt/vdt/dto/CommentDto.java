package com.vdt.vdt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String comment;
    private String author;
    private LocalDateTime timestamp;


}

