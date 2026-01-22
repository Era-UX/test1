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

            // ВОТ ОНА СВЯЗЬ: Если 0 строк изменено, кидаем исключение
            if (rowsAffected == 0) {
                throw new StudentNotFoundException("Update failed: Student with ID " + id + " not found!");
            }

            System.out.println("Age of student with ID " + id + " updated to " + newAge);

        } catch (SQLException | DatabaseConnectionException | StudentNotFoundException e) {
            // Теперь здесь ловятся ВСЕ ТРИ типа ошибок
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 4. DELETE
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            // ВОТ ОНА СВЯЗЬ: Если удалять некого, кидаем исключение
            if (rowsAffected == 0) {
                throw new StudentNotFoundException("Delete failed: Student with ID " + id + " not found!");
            }

            System.out.println("Student with ID " + id + " successfully deleted.");

        } catch (SQLException | DatabaseConnectionException | StudentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}