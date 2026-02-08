package education.repository;

import education.db.PostgresDB;
import education.domain.Teacher;
import education.exception.DatabaseException;
import education.exception.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherDAO implements IRepository<Teacher> {

    private Connection getConnection() throws DatabaseException {
        return PostgresDB.getInstance().getConnection();
    }

    @Override
    public void add(Teacher t) throws DatabaseException {
        String sql = "INSERT INTO teachers (name, age, subject, experience) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getName());
            pstmt.setInt(2, t.getAge());
            pstmt.setString(3, t.getSubject());
            pstmt.setInt(4, t.getExperience());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding teacher", e);
        }
    }

    @Override
    public List<Teacher> getAll() throws DatabaseException {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY id ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                teachers.add(new Teacher.Builder()
                        .setId(rs.getInt("id"))
                        .setName(rs.getString("name"))
                        .setAge(rs.getInt("age"))
                        .setSubject(rs.getString("subject"))
                        .setExperience(rs.getInt("experience"))
                        .build());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error loading teachers", e);
        }
        return teachers;
    }

    @Override
    public void update(int id, int newExperience) throws DatabaseException, EntityNotFoundException {
        // Обновляем опыт работы, например
        String sql = "UPDATE teachers SET experience = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newExperience);
            pstmt.setInt(2, id);
            if (pstmt.executeUpdate() == 0) {
                throw new EntityNotFoundException("Teacher not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error updating teacher", e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseException, EntityNotFoundException {
        String sql = "DELETE FROM teachers WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            if (pstmt.executeUpdate() == 0) {
                throw new EntityNotFoundException("Teacher not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting teacher", e);
        }
    }

    @Override
    public Optional<Teacher> findById(int id) throws DatabaseException {
        String sql = "SELECT * FROM teachers WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Teacher.Builder()
                        .setId(rs.getInt("id"))
                        .setName(rs.getString("name"))
                        .setAge(rs.getInt("age"))
                        .setSubject(rs.getString("subject"))
                        .setExperience(rs.getInt("experience"))
                        .build());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching teacher", e);
        }
        return Optional.empty();
    }
}