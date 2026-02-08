package education.repository;

import education.db.PostgresDB;
import education.domain.Student;
import education.exception.DatabaseException;
import education.exception.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO implements IRepository<Student> {

    private Connection getConnection() throws DatabaseException {
        return PostgresDB.getInstance().getConnection();
    }

    @Override
    public void add(Student s) throws DatabaseException {
        String sql = "INSERT INTO students (name, age) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, s.getName());
            pstmt.setInt(2, s.getAge());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding student", e);
        }
    }

    @Override
    public List<Student> getAll() throws DatabaseException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, t.name as curator_name " +
                "FROM students s " +
                "LEFT JOIN teachers t ON s.teacher_id = t.id " +
                "ORDER BY s.id ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student student = new Student.Builder()
                        .setId(rs.getInt("id"))
                        .setName(rs.getString("name"))
                        .setAge(rs.getInt("age"))
                        .build();

                String curatorName = rs.getString("curator_name");
                student.setTeacherName(curatorName != null ? curatorName : "None");

                students.add(student);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting students", e);
        }
        return students;
    }

    @Override
    public void update(int id, int newValue) throws DatabaseException, EntityNotFoundException {
        String sql = "UPDATE students SET age = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newValue);
            pstmt.setInt(2, id);
            if (pstmt.executeUpdate() == 0) {
                throw new EntityNotFoundException("Student not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error updating student", e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseException, EntityNotFoundException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            if (pstmt.executeUpdate() == 0) {
                throw new EntityNotFoundException("Student not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting student", e);
        }
    }

    @Override
    public Optional<Student> findById(int id) throws DatabaseException {
        String sql = "SELECT s.*, t.name as curator_name " +
                "FROM students s " +
                "LEFT JOIN teachers t ON s.teacher_id = t.id " +
                "WHERE s.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Student student = new Student.Builder()
                        .setId(rs.getInt("id"))
                        .setName(rs.getString("name"))
                        .setAge(rs.getInt("age"))
                        .build();

                String curatorName = rs.getString("curator_name");
                student.setTeacherName(curatorName != null ? curatorName : "None");

                return Optional.of(student);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching student", e);
        }
        return Optional.empty();
    }
}