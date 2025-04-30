package com.vdt.vdt.dto;

import com.vdt.vdt.entity.KYCStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KYCResponseDTO {
    KYCStatus kycStatus;
    LocalDateTime updatedTime;
}
