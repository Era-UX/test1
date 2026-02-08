package education;

import education.domain.Student;
import education.domain.Teacher;
import education.exception.ProjectException;
import education.repository.IRepository;
import education.repository.RepositoryFactory;
import education.server.EduServer;
import education.util.DataPool;

import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        IRepository<Student> studentDao = RepositoryFactory.createStudentRepository();
        IRepository<Teacher> teacherDao = RepositoryFactory.createTeacherRepository();

        Thread serverThread = new Thread(() -> {
            try {
                new EduServer(studentDao, teacherDao).start();
            } catch (Exception e) {
                System.err.println("Server failed: " + e.getMessage());
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        while (true) {
            System.out.println("\n===== EDUCATION MANAGEMENT SYSTEM =====");
            System.out.println("1. Student Management");
            System.out.println("2. Teacher Management");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int mainChoice = readInt();

            if (mainChoice == 0) {
                System.out.println("Exiting system...");
                break;
            }

            if (mainChoice == 1) {
                System.out.println("\n--- Student CRUD Operations ---");
                System.out.println("1. Create (Add Student)");
                System.out.println("2. Read (View All / Sort)");
                System.out.println("3. Update (Change Age)");
                System.out.println("4. Delete (Remove Student)");
                System.out.print("Action: ");

                int action = readInt();

                try {
                    switch (action) {
                        case 1:
                            System.out.print("Enter Name: ");
                            String name = readStringName();

                            System.out.print("Enter Age: ");
                            int age = readInt();

                            Student s = new Student.Builder()
                                    .setName(name)
                                    .setAge(age)
                                    .build();

                            studentDao.add(s);
                            System.out.println("[SUCCESS] Student added!");
                            break;

                        case 2:
                            var list = studentDao.getAll();
                            System.out.println("1. Simple List | 2. Sort by Name");
                            int viewMode = readInt();

                            if (viewMode == 2) {
                                DataPool<Student> pool = new DataPool<>(list);
                                pool.sort(Comparator.comparing(Student::getName))
                                        .forEach(System.out::println);
                            } else {
                                list.forEach(System.out::println);
                            }
                            break;

                        case 3:
                            System.out.print("Enter Student ID to update: ");
                            int upId = readInt();
                            System.out.print("Enter New Age: ");
                            int newAge = readInt();

                            studentDao.update(upId, newAge);
                            System.out.println("[SUCCESS] Student updated.");
                            break;

                        case 4:
                            System.out.print("Enter Student ID to delete: ");
                            int delId = readInt();

                            studentDao.delete(delId);
                            System.out.println("[SUCCESS] Student deleted.");
                            break;

                        default:
                            System.out.println("[ERROR] Invalid action number!");
                    }
                } catch (ProjectException e) {
                    System.err.println("[ERROR] " + e.getMessage());
                }

            } else if (mainChoice == 2) {
                System.out.println("\n--- Teacher CRUD Operations ---");
                System.out.println("1. Create (Add Teacher)");
                System.out.println("2. Read (View All)");
                System.out.println("3. Update (Change Experience)");
                System.out.println("4. Delete (Remove Teacher)");
                System.out.print("Action: ");

                int action = readInt();

                try {
                    switch (action) {
                        case 1:
                            System.out.print("Enter Name: ");
                            String name = readStringName();

                            System.out.print("Enter Subject: ");
                            String subject = scanner.nextLine();

                            System.out.print("Enter Experience Years: ");
                            int exp = readInt();

                            Teacher t = new Teacher.Builder()
                                    .setName(name)
                                    .setAge(35)
                                    .setSubject(subject)
                                    .setExperience(exp)
                                    .build();

                            teacherDao.add(t);
                            System.out.println("[SUCCESS] Teacher added!");
                            break;

                        case 2:
                            teacherDao.getAll().forEach(System.out::println);
                            break;

                        case 3:
                            System.out.print("Enter Teacher ID to update: ");
                            int upId = readInt();
                            System.out.print("Enter New Experience: ");
                            int newExp = readInt();

                            teacherDao.update(upId, newExp);
                            System.out.println("[SUCCESS] Teacher updated.");
                            break;

                        case 4:
                            System.out.print("Enter Teacher ID to delete: ");
                            int delId = readInt();

                            teacherDao.delete(delId);
                            System.out.println("[SUCCESS] Teacher deleted.");
                            break;

                        default:
                            System.out.println("[ERROR] Invalid action number!");
                    }
                } catch (ProjectException e) {
                    System.err.println("[ERROR] " + e.getMessage());
                }
            } else {
                System.out.println("[ERROR] Invalid option. Please select 1, 2 or 0.");
            }
        }
    }

    private static int readInt() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a number: ");
            }
        }
    }

    private static String readStringName() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[a-zA-Z\\s]+")) {
                return input;
            } else {
                System.out.print("Invalid name! Use only letters. Try again: ");
            }
        }
    }
}