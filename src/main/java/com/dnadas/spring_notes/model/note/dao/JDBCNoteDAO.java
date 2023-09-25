package com.dnadas.spring_notes.model.note.dao;

import com.dnadas.spring_notes.model.note.dto.NotePatchDTO;
import com.dnadas.spring_notes.model.note.dto.NotePostDTO;
import com.dnadas.spring_notes.model.note.dto.NoteResponseDTO;
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

  private NoteResponseDTO getNoteObject(ResultSet rs) throws SQLException {
    return new NoteResponseDTO(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getDate("created_at"));
  }

  @Override
  public List<NoteResponseDTO> findAll() {
    List<NoteResponseDTO> notes = new ArrayList<>();
    String sql = "SELECT * FROM notes ORDER BY CREATED_AT";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        notes.add(getNoteObject(rs));
      }
      logger.info(notes.size() + " notes read");
    } catch (SQLException e) {
      logger.error("Failed to read notes", e);
    }
    return notes;
  }

  @Override
  public Optional<NoteResponseDTO> findById(Long id) {
    String sql = "SELECT * FROM notes WHERE ID = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        NoteResponseDTO note = getNoteObject(rs);
        logger.info("Read note: " + note.title());
        return Optional.of(note);
      }
    } catch (SQLException e) {
      logger.error("Failed to read note with ID " + id, e);
    }
    return Optional.empty();
  }

  @Override
  public boolean create(NotePostDTO note) {
    String sql = "INSERT INTO notes(TITLE,CONTENT) VALUES(?,?)";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, note.title());
      ps.setString(2, note.content());
      ps.executeUpdate();
      logger.info("Created note: " + note.title());
      return true;
    } catch (SQLException e) {
      if (e.getSQLState().equals(UNIQUE_CONSTRAINT_VIOLATION)) {
        logger.error(note.title() + " violates unique constraint", e);
      } else {
        logger.error("Failed to create note", e);
      }
      return false;
    }
  }

  @Override
  public boolean update(NotePatchDTO note) {
    String sql = "UPDATE notes SET TITLE = ?, CONTENT = ?  WHERE ID = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, note.title());
      ps.setString(2, note.content());
      ps.setLong(3, note.id());
      ps.executeUpdate();
      logger.info("Updated note: " + note.title());
      return true;
    } catch (SQLException e) {
      if (e.getSQLState().equals(UNIQUE_CONSTRAINT_VIOLATION)) {
        logger.error(note.title() + " violates unique constraint", e);
      } else {
        logger.error("Failed to create note", e);
      }
      return false;
    }
  }

  @Override
  public boolean delete(Long id) {
    String sql = "DELETE FROM NOTES WHERE ID = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);
      ps.executeUpdate();
      logger.info("Deleted note with ID " + id);
      return true;
    } catch (SQLException e) {
      logger.error("Failed to delete note with ID " + id, e);
      return false;
    }
  }
}
