package education;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO implements IRepository<Student> {
    private DatabaseManager dbManager = new DatabaseManager();

    // 1. CREATE
    @Override
    public void add(Student s) {
        String sql = "INSERT INTO students (id, name, age) VALUES (?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, s.getID());
            pstmt.setString(2, s.getName());
            pstmt.setInt(3, s.getAge());

            pstmt.executeUpdate();
            System.out.println("Student " + s.getName() + " successfully saved to database!");

        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error saving student: " + e.getMessage());
        }
    }

    // 2. READ
    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>(); // Creates new empty list
        String sql = "SELECT * FROM students";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");

                students.add(new Student(name, age, id));
            }

        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error reading data: " + e.getMessage());
        }
        return students;
    }

    // 3. UPDATE
    @Override
    public void update(int id, int newValue) {
        String sql = "UPDATE students SET age = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newValue);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new StudentNotFoundException("Update failed: Student with ID " + id + " not found!");
            }

            System.out.println("Age of student with ID " + id + " updated to " + newValue);

        } catch (SQLException | DatabaseConnectionException | StudentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 4. DELETE
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new StudentNotFoundException("Delete failed: Student with ID " + id + " not found!");
            }

            System.out.println("Student with ID " + id + " successfully deleted.");

        } catch (SQLException | DatabaseConnectionException | StudentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 5. FIND BY ID (Optional)
    @Override
    public Optional<Student> findById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Student(rs.getString("name"), rs.getInt("age"), rs.getInt("id")));
            }
        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Search error: " + e.getMessage());
        }
        return Optional.empty();
    }
}