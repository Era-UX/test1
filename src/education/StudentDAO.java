package education;

import java.sql.*;

public class StudentDAO {
    private DatabaseManager dbManager = new DatabaseManager();

    // 1. CREATE
    public void addStudent(Student s) {
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
    public void readStudents() {
        String sql = "SELECT * FROM students";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("--- List of Students from Database ---");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");

                Student s = new Student(name, age, id);
                System.out.println(s);
            }

        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error reading data: " + e.getMessage());
        }
    }

    // 3. UPDATE
    public void updateStudentAge(int id, int newAge) {
        String sql = "UPDATE students SET age = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newAge);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Age of student with ID " + id + " updated to " + newAge);
            }
        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error updating data: " + e.getMessage());
        }
    }

    // 4. DELETE
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student with ID " + id + " successfully deleted.");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error deleting data: " + e.getMessage());
        }
    }
}