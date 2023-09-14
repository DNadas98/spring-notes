package com.dnadas.spring_notes.model.note.dao;

import com.dnadas.spring_notes.model.note.dto.Note;

import java.util.List;
import java.util.Optional;

public interface NoteDAO {
  List<Note> findAll();

  Optional<Note> findById(Long id);

  boolean create(Note note);

  boolean update(Note note);

  boolean delete(Long id);
}
