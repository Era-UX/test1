package education;

import java.sql.*;

public class TeacherDAO {
    private DatabaseManager dbManager = new DatabaseManager();

    // 1. CREATE
    public void addTeacher(Teacher t) {
        String sql = "INSERT INTO teachers (name, age, subject, exp_years) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getName());
            pstmt.setInt(2, t.getAge());
            pstmt.setString(3, t.getSubject());
            pstmt.setInt(4, t.getExperienceYears());

            pstmt.executeUpdate();
            System.out.println("Teacher " + t.getName() + " successfully added to database!");
        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error adding teacher: " + e.getMessage());
        }
    }

    // 2. READ
    public void readTeachers() {
        String sql = "SELECT * FROM teachers";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- List of Teachers from Database ---");
            while (rs.next()) {
                Teacher t = new Teacher(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("subject"),
                        rs.getInt("exp_years")
                );
                System.out.println(t);
            }
        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error reading teachers: " + e.getMessage());
        }
    }

    // 3. UPDATE
    public void updateTeacherExperience(String name, int newExp) {
        String sql = "UPDATE teachers SET exp_years = ? WHERE name = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newExp);
            pstmt.setString(2, name);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Experience of teacher " + name + " updated to " + newExp + " years.");
            }
        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error updating teacher: " + e.getMessage());
        }
    }

    // 4. DELETE
    public void deleteTeacher(String name) {
        String sql = "DELETE FROM teachers WHERE name = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Teacher " + name + " successfully deleted from database.");
            }
        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error deleting teacher: " + e.getMessage());
        }
    }
}