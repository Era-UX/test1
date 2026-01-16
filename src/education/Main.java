package education;

public class Main {
    public static void main(String[] args) {
        // DAO Objects
        StudentDAO studentDao = new StudentDAO();
        TeacherDAO teacherDao = new TeacherDAO();

        System.out.println("=== WORKING WITH STUDENTS ===");

        // Add
        Student s = new Student("Alex", 19, 105);
        studentDao.addStudent(s);

        // Update
        studentDao.updateStudentAge(105, 20);

        // Read
        studentDao.readStudents();

        // Delete (optional check)
        studentDao.deleteStudent(105);


        System.out.println("\n=== WORKING WITH TEACHERS ===");

        // Add
        Teacher t = new Teacher("Dr. Smith", 45, "Java Programming", 10);
        teacherDao.addTeacher(t);

        // Update
        teacherDao.updateTeacherExperience("Dr. Smith", 11);

        // Read
        teacherDao.readTeachers();

        // Delete
        teacherDao.deleteTeacher("Dr. Smith");

        System.out.println("\nCheck completed. All data operations finished.");
    }
}