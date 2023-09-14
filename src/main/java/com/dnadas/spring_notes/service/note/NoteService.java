package com.dnadas.spring_notes.service.note;

import com.dnadas.spring_notes.model.note.dto.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {
  List<Note> findAll();

  Optional<Note> findById(Long id);

  boolean create(Note note);

  boolean update(Note note);

  boolean delete(Long id);
}