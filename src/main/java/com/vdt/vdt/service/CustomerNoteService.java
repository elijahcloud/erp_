
package com.vdt.vdt.service;

import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.dto.CustomerNoteDTO;
import com.vdt.vdt.entity.CustomerNote;
import com.vdt.vdt.repository.CustomerNoteRepository;
import com.vdt.vdt.repository.CustomerRepository;
import com.vdt.vdt.entity.User;
import com.vdt.vdt.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerNoteService {

    @Autowired
    private CustomerNoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Page<CustomerNoteDTO> getNotesByCustomer(Long customerId, Pageable pageable) {
        validateCustomerExists(customerId);
        return noteRepository.findByCustomerId(customerId, pageable)
                .map(this::toDTO);
    }

    public List<CustomerNoteDTO> getPinnedNotes(Long customerId) {
        validateCustomerExists(customerId);
        return noteRepository.findByCustomerIdAndPinnedTrue(customerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CustomerNoteDTO createNote(Long customerId, Long userId, CustomerNoteDTO dto) {
        if (dto == null || dto.getNoteContent() == null || dto.getNoteContent().isBlank()) {
            throw new IllegalArgumentException("Note content must not be null or blank");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
        User user = userRepository.findById(userId.toString())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        CustomerNote note = new CustomerNote();
        note.setNoteContent(dto.getNoteContent());
        note.setCustomer(customer);
        note.setUser(user);
        note.setPinned(dto.isPinned());
        note.setDateCreated(LocalDateTime.now());

        return toDTO(noteRepository.save(note));
    }

    public CustomerNoteDTO updateNote(Long noteId, CustomerNoteDTO dto) {
        if (dto == null || dto.getNoteContent() == null || dto.getNoteContent().isBlank()) {
            throw new IllegalArgumentException("Note content must not be null or blank");
        }

        CustomerNote note = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with ID: " + noteId));
        note.setNoteContent(dto.getNoteContent());
        note.setPinned(dto.isPinned());
        return toDTO(noteRepository.save(note));
    }

    public void deleteNote(Long noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new EntityNotFoundException("Note not found with ID: " + noteId);
        }
        noteRepository.deleteById(noteId);
    }

    private void validateCustomerExists(Long customerId) {
        if (customerId == null || !customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Customer not found with ID: " + customerId);
        }
    }

    private CustomerNoteDTO toDTO(CustomerNote note) {
        CustomerNoteDTO dto = new CustomerNoteDTO();
        dto.setId(note.getId());
        dto.setNoteContent(note.getNoteContent());
        dto.setDateCreated(note.getDateCreated());
        dto.setPinned(note.isPinned());
        dto.setUserName(note.getUser().getEmail());
        return dto;
    }
}
