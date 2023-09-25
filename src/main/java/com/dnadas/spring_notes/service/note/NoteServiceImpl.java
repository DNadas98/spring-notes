package com.dnadas.spring_notes.service.note;

import com.dnadas.spring_notes.model.note.dao.NoteDAO;
import com.dnadas.spring_notes.model.note.dto.NotePatchDTO;
import com.dnadas.spring_notes.model.note.dto.NotePostDTO;
import com.dnadas.spring_notes.model.note.dto.NoteResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {
  private final NoteDAO noteDAO;

  @Autowired
  public NoteServiceImpl(NoteDAO noteDAO) {
    this.noteDAO = noteDAO;
  }

  @Override
  public List<NoteResponseDTO> findAll() {
    return noteDAO.findAll();
  }

  @Override
  public Optional<NoteResponseDTO> findById(Long id) {
    return noteDAO.findById(id);
  }

  @Override
  public boolean create(NotePostDTO note) {
    return noteDAO.create(note);
  }

  @Override
  public boolean update(NotePatchDTO note) {
    return noteDAO.update(note);
  }

  @Override
  public boolean delete(Long id) {
    return noteDAO.delete(id);
  }
}
