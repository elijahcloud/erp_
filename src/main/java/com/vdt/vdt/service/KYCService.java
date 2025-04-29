package com.vdt.vdt.service;

import com.vdt.vdt.dto.KYCResponseDTO;
import com.vdt.vdt.entity.KYC;
import com.vdt.vdt.entity.KYCStatus;
import com.vdt.vdt.repository.KYCRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class KYCService {
    @Autowired
    KYCRepository kycRepository;


    public KYCResponseDTO updateKYCDocument(Long customerId, MultipartFile multipartFile, KYCStatus status) throws IOException {
        //Fetch KYC of customer
        KYC kyc = kycRepository.findByCustomerId(customerId).orElseThrow(EntityNotFoundException::new);

        if(multipartFile != null) kyc.setDocument(multipartFile.getBytes());
        if(status != null) kyc.setKycStatus(status);
        kyc.setLastUpdated(LocalDateTime.now());

        kyc = kycRepository.save(kyc);

        KYCResponseDTO kycResponseDTO = new KYCResponseDTO();
        kycResponseDTO.setKycStatus(kyc.getKycStatus());
        kycResponseDTO.setUpdatedTime(kyc.getLastUpdated());
        return kycResponseDTO;
    }
}
