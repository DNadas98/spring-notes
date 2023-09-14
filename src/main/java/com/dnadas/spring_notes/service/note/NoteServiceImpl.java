package com.dnadas.spring_notes.service.note;

import com.dnadas.spring_notes.model.note.dao.NoteDAO;
import com.dnadas.spring_notes.model.note.dto.Note;
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
  public List<Note> findAll() {
    return noteDAO.findAll();
  }

  @Override
  public Optional<Note> findById(Long id) {
    return noteDAO.findById(id);
  }

  @Override
  public boolean create(Note note) {
    return noteDAO.create(note);
  }

  @Override
  public boolean update(Note note) {
    return noteDAO.update(note);
  }

  @Override
  public boolean delete(Long id) {
    return noteDAO.delete(id);
  }
}
