package com.vdt.vdt.controller;

import com.vdt.vdt.dto.CustomerNoteDTO;
import com.vdt.vdt.service.CustomerNoteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customers/{customerId}/notes")
public class CustomerNoteController {

    @Autowired
    private CustomerNoteService noteService;

    @GetMapping
    public ResponseEntity<?> getNotes(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            return ResponseEntity.ok(noteService.getNotesByCustomer(customerId, pageable));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/pinned")
    public ResponseEntity<?> getPinnedNotes(@PathVariable Long customerId) {
        try {
            return ResponseEntity.ok(noteService.getPinnedNotes(customerId));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createNote(
            @PathVariable Long customerId,
            @RequestParam Long userId,
            @RequestBody CustomerNoteDTO noteDTO) {
        try {
            return ResponseEntity.ok(noteService.createNote(customerId, userId, noteDTO));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PatchMapping("/{noteId}")
    public ResponseEntity<?> updateNote(
            @PathVariable Long noteId,
            @RequestBody CustomerNoteDTO noteDTO) {
        try {
            return ResponseEntity.ok(noteService.updateNote(noteId, noteDTO));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable Long noteId) {
        try {
            noteService.deleteNote(noteId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
    }
}
