package com.dnadas.spring_notes.model.note.dao;

import com.dnadas.spring_notes.model.note.dto.NotePatchDTO;
import com.dnadas.spring_notes.model.note.dto.NotePostDTO;
import com.dnadas.spring_notes.model.note.dto.NoteResponseDTO;

import java.util.List;
import java.util.Optional;

public interface NoteDAO {
  List<NoteResponseDTO> findAll();

  Optional<NoteResponseDTO> findById(Long id);

  boolean create(NotePostDTO note);

  boolean update(NotePatchDTO note);

  boolean delete(Long id);
}
