package com.dnadas.spring_notes.service.note;

import com.dnadas.spring_notes.controller.dto.NotePatchDTO;
import com.dnadas.spring_notes.controller.dto.NotePostDTO;
import com.dnadas.spring_notes.controller.dto.NoteResponseDTO;
import com.dnadas.spring_notes.exception.DaoException;
import com.dnadas.spring_notes.model.note.Note;
import com.dnadas.spring_notes.model.note.dao.NoteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {
  private final NoteDAO noteDAO;

  @Autowired
  public NoteService(NoteDAO noteDAO) {
    this.noteDAO = noteDAO;
  }

  public List<NoteResponseDTO> findAll() throws SQLException {
    return noteDAO.findAll().stream().map(note -> new NoteResponseDTO(note.getId(),
      note.getTitle(), note.getContent(), note.getCreatedAt())).collect(Collectors.toList());
  }

  public Optional<NoteResponseDTO> findById(Long id) throws SQLException {
    Optional<Note> note = noteDAO.findById(id);
    if (note.isPresent()){
      return Optional.of( new NoteResponseDTO(note.get().getId(),note.get().getTitle(),
        note.get().getContent()
        ,note.get().getCreatedAt()));
    }
    return Optional.empty();
  }

  public void create(NotePostDTO note) throws SQLException, DaoException {
    noteDAO.create(note.title(),note.content());
  }

  public void update(NotePatchDTO note) throws SQLException, DaoException {
    noteDAO.update(note.id(),note.title(),note.content());
  }

  public boolean delete(Long id) throws SQLException, DaoException {
    return noteDAO.delete(id);
  }
}
