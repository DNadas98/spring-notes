package com.dnadas.spring_notes.model.note.dao;

import com.dnadas.spring_notes.exception.DaoException;
import com.dnadas.spring_notes.model.note.Note;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface NoteDAO {
  List<Note> findAll() throws SQLException;

  Optional<Note> findById(Long id) throws SQLException;

  void create(String title, String content) throws SQLException, DaoException;

  void update(Long id, String title, String content) throws SQLException, DaoException;

  boolean delete(Long id) throws SQLException;
}
