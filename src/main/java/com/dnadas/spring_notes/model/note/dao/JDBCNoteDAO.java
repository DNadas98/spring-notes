package com.dnadas.spring_notes.model.note.dao;

import com.dnadas.spring_notes.exception.DaoException;
import com.dnadas.spring_notes.exception.UniqueConstraintException;
import com.dnadas.spring_notes.model.note.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JDBCNoteDAO implements NoteDAO {

  //https://stackoverflow.com/questions/39557914/how-to-get-uniqueviolationexception-instead-of-org-postgresql-util-psqlexception
  private static final String UNIQUE_CONSTRAINT_VIOLATION = "23505";
  private final DataSource dataSource;
  private final Logger logger;

  @Autowired
  public JDBCNoteDAO(DataSource dataSource) {
    this.dataSource = dataSource;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  private Note getNoteObject(ResultSet rs) throws SQLException {
    return new Note(
      rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getTimestamp(
      "created_at").toLocalDateTime());
  }

  @Override
  public List<Note> findAll() throws SQLException {
    List<Note> notes = new ArrayList<>();
    String sql = "SELECT * FROM notes ORDER BY CREATED_AT";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        notes.add(getNoteObject(rs));
      }
      logger.info(notes.size() + " notes read");
    }
    return notes;
  }

  @Override
  public Optional<Note> findById(Long id) throws SQLException {
    String sql = "SELECT * FROM notes WHERE ID = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Note note = getNoteObject(rs);
          logger.info("Read note: " + note.getTitle());
          return Optional.of(note);
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public void create(String title, String content) throws SQLException, DaoException {
    String sql = "INSERT INTO notes(TITLE,CONTENT) VALUES(?,?)";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, title);
      ps.setString(2, content);
      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new DaoException("Failed to create note");
      }
      logger.info("Created note: " + title);
    } catch (SQLException e) {
      if (e.getSQLState().equals(UNIQUE_CONSTRAINT_VIOLATION)) {
        throw new UniqueConstraintException(e.getMessage(),"title"
          ,title);
      } else {
        throw new SQLException(e);
      }
    }
  }

  @Override
  public void update(Long id, String title, String content)
    throws SQLException, DaoException {
    String sql = "UPDATE notes SET TITLE = ?, CONTENT = ?  WHERE ID = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, title);
      ps.setString(2, content);
      ps.setLong(3, id);
      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new DaoException("Failed to update note");
      }
      logger.info("Updated note: " + title);
    } catch (SQLException e) {
      if (e.getSQLState().equals(UNIQUE_CONSTRAINT_VIOLATION)) {
        throw new UniqueConstraintException(e.getMessage(),"title",title);
      } else {
        throw new SQLException(e);
      }
    }
  }

  @Override
  public boolean delete(Long id) throws SQLException, DaoException {
    String sql = "DELETE FROM NOTES WHERE ID = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);
      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new DaoException("Failed to create note");
      }
      logger.info("Deleted note with ID " + id);
      return true;
    }
  }
}
