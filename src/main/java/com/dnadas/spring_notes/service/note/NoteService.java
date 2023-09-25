package com.dnadas.spring_notes.service.note;

import com.dnadas.spring_notes.model.note.dto.NotePatchDTO;
import com.dnadas.spring_notes.model.note.dto.NotePostDTO;
import com.dnadas.spring_notes.model.note.dto.NoteResponseDTO;

import java.util.List;
import java.util.Optional;

public interface NoteService {
  List<NoteResponseDTO> findAll();

  Optional<NoteResponseDTO> findById(Long id);

  boolean create(NotePostDTO note);

  boolean update(NotePatchDTO note);

  boolean delete(Long id);
}