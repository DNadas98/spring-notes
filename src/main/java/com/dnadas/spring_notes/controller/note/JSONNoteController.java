package com.dnadas.spring_notes.controller.note;

import com.dnadas.spring_notes.controller.dto.NotePatchDTO;
import com.dnadas.spring_notes.controller.dto.NotePostDTO;
import com.dnadas.spring_notes.controller.dto.NoteResponseDTO;
import com.dnadas.spring_notes.exception.DaoException;
import com.dnadas.spring_notes.exception.UniqueConstraintException;
import com.dnadas.spring_notes.service.note.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
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
    try {
      List<NoteResponseDTO> notes = noteService.findAll();
      return ResponseEntity.ok(Map.of("data", notes));
    } catch (SQLException e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
        Map.of("error", "Failed " + "to read notes"));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getNoteById(@PathVariable Long id) {
    try {
      Optional<NoteResponseDTO> note = noteService.findById(id);
      if (note.isPresent()) {
        return ResponseEntity.ok(Map.of("data", note.get()));
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          Map.of("error", "Note with ID " + id + " not found"));
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
        Map.of("error", "Failed " + "to read note"));
    }
  }

  @PostMapping
  public ResponseEntity<?> createNote(@RequestBody NotePostDTO note) {
    try {
      noteService.create(note);
      return ResponseEntity.ok(Map.of("message", "Note created successfully"));
    } catch (UniqueConstraintException e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
        Map.of("error", "Note with" + e.getFieldName() + " " + e.getValue() + " already exists"));
    } catch (SQLException | DaoException e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
        Map.of("error", "Failed " + "to create note"));
    }
  }

  @PatchMapping
  public ResponseEntity<?> updateNote(@RequestBody NotePatchDTO note) {
    try {
      noteService.update(note);
      return ResponseEntity.ok(Map.of("message", "Note updated successfully"));
    } catch (UniqueConstraintException e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
        Map.of("error", "Note with " + e.getFieldName() + " " + e.getValue() + " already exists"));
    } catch (SQLException | DaoException e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
        Map.of("error", "Failed " + "to update note"));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteNote(@PathVariable Long id) {
    try {
      noteService.delete(id);
      return ResponseEntity.ok(Map.of("message", "Note with ID " + id + " deleted successfully"));
    } catch (SQLException | DaoException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        Map.of("error", "Failed to delete note with ID " + id));
    }
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  private ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
    String message = "Invalid format";
    logger.error(message, e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(Map.of("error", message));
  }
}