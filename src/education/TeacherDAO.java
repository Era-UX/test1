package education;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherDAO implements IRepository<Teacher> {
    private DatabaseManager dbManager = new DatabaseManager();

    // 1. CREATE (add)
    @Override
    public void add(Teacher t) {
        // ИСПРАВЛЕНО: колонка называется exp_years
        String sql = "INSERT INTO teachers (name, age, subject, exp_years) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getName());
            pstmt.setInt(2, t.getAge());
            pstmt.setString(3, t.getSubject());
            pstmt.setInt(4, t.getExperienceYears()); // Берем данные из объекта

            pstmt.executeUpdate();
            System.out.println("Teacher " + t.getName() + " successfully saved!");

        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error saving teacher: " + e.getMessage());
        }
    }

    // 2. READ (getAll)
    @Override
    public List<Teacher> getAll() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String subject = rs.getString("subject");
                // ИСПРАВЛЕНО: читаем из колонки exp_years
                int exp = rs.getInt("exp_years");

                // Создаем учителя
                Teacher t = new Teacher(name, age, subject, exp);

                // Печатаем ID для удобства (чтобы знать, кого удалять)
                System.out.print("[ID: " + id + "] ");

                teachers.add(t);
            }

        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Error reading teachers: " + e.getMessage());
        }
        return teachers;
    }

    // 3. UPDATE
    @Override
    public void update(int id, int newExperience) {
        // ИСПРАВЛЕНО: обновляем колонку exp_years
        String sql = "UPDATE teachers SET exp_years = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newExperience);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new StudentNotFoundException("Update failed: Teacher with ID " + id + " not found!");
            }

            System.out.println("Experience of teacher with ID " + id + " updated to " + newExperience);

        } catch (SQLException | DatabaseConnectionException | StudentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 4. DELETE
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM teachers WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new StudentNotFoundException("Delete failed: Teacher with ID " + id + " not found!");
            }

            System.out.println("Teacher with ID " + id + " successfully deleted.");

        } catch (SQLException | DatabaseConnectionException | StudentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 5. FIND BY ID
    @Override
    public Optional<Teacher> findById(int id) {
        String sql = "SELECT * FROM teachers WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // ИСПРАВЛЕНО: читаем exp_years
                Teacher t = new Teacher(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("subject"),
                        rs.getInt("exp_years")
                );
                return Optional.of(t);
            }
        } catch (SQLException | DatabaseConnectionException e) {
            System.out.println("Search error: " + e.getMessage());
        }
        return Optional.empty();
    }
}