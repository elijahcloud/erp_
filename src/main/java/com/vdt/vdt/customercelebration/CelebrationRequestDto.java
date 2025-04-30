package com.vdt.vdt.customercelebration;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CelebrationRequestDto {
    private CelebrationType celebrationType;
    private String customLabel;
    private Long reminderDays;
    private String note;
    private LocalDate celebrationDate;

}
