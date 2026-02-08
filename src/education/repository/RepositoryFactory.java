package education.repository;

import education.domain.Student;
import education.domain.Teacher;

public class RepositoryFactory {

    public static IRepository<Student> createStudentRepository() {
        return new StudentDAO();
    }

    public static IRepository<Teacher> createTeacherRepository() {
        return new TeacherDAO();
    }
}