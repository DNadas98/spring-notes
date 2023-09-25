package com.dnadas.spring_notes.controller.note;

import com.dnadas.spring_notes.model.note.dto.NotePatchDTO;
import com.dnadas.spring_notes.model.note.dto.NotePostDTO;
import com.dnadas.spring_notes.model.note.dto.NoteResponseDTO;
import com.dnadas.spring_notes.service.note.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class JSONNoteController {

  private final NoteService noteService;
  private final Logger logger;

  public JSONNoteController(NoteService noteService) {
    this.noteService = noteService;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping
  public ResponseEntity<?> getAllNotes() {
    List<NoteResponseDTO> notes = noteService.findAll();
    return ResponseEntity.ok(Map.of("data", notes));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getNoteById(@PathVariable Long id) {
    Optional<NoteResponseDTO> note = noteService.findById(id);
    if (note.isPresent()) {
      return ResponseEntity.ok(Map.of("data", note.get()));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", "Note with ID " + id + " not found"));
    }
  }

  @PostMapping
  public ResponseEntity<?> createNote(@RequestBody NotePostDTO note) {
    if (noteService.create(note)) {
      return ResponseEntity.ok(Map.of("message", "Note created successfully"));
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Failed to create note"));
    }
  }

  @PatchMapping
  public ResponseEntity<?> updateNote(@RequestBody NotePatchDTO note) {
    if (noteService.update(note)) {
      return ResponseEntity.ok(Map.of("message", "Note updated successfully"));
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Failed to update note"));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteNote(@PathVariable Long id) {
    if (noteService.delete(id)) {
      return ResponseEntity.ok(Map.of("message", "Note with ID " + id + " deleted successfully"));
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Failed to delete note with ID " + id));
    }
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  private ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
    logger.error("Invalid format", e);
    String message = "Invalid format";
    int status = HttpStatus.BAD_REQUEST.value();
    return ResponseEntity.status(status).body(Map.of("error", message, "status", status));
  }
}