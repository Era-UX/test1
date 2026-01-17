package education;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentDAO studentDao = new StudentDAO();
        TeacherDAO teacherDao = new TeacherDAO();

        while (true) {
            System.out.println("\n===== EDUCATION MANAGEMENT SYSTEM =====");
            System.out.println("1. Student Management");
            System.out.println("2. Teacher Management");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int mainChoice = scanner.nextInt();
            if (mainChoice == 0) break;

            if (mainChoice == 1) {
                // STUDENT SUB-MENU
                System.out.println("\n--- Student CRUD Operations ---");
                System.out.println("1. Create (Add Student)");
                System.out.println("2. Read (View All)");
                System.out.println("3. Update (Change Age)");
                System.out.println("4. Delete (Remove Student)");
                System.out.print("Action: ");
                int action = scanner.nextInt();

                switch (action) {
                    case 1: // CREATE
                        System.out.print("Enter ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Age: ");
                        int age = scanner.nextInt();
                        studentDao.addStudent(new Student(name, age, id));
                        break;
                    case 2: // READ
                        studentDao.readStudents();
                        break;
                    case 3: // UPDATE
                        System.out.print("Enter Student ID to update: ");
                        int upId = scanner.nextInt();
                        System.out.print("Enter New Age: ");
                        int newAge = scanner.nextInt();
                        studentDao.updateStudentAge(upId, newAge);
                        break;
                    case 4: // DELETE
                        System.out.print("Enter Student ID to delete: ");
                        int delId = scanner.nextInt();
                        studentDao.deleteStudent(delId);
                        break;
                }
            } else if (mainChoice == 2) {
                // TEACHER SUB-MENU
                System.out.println("\n--- Teacher CRUD Operations ---");
                System.out.println("1. Create (Add Teacher)");
                System.out.println("2. Read (View All)");
                System.out.println("3. Update (Change Experience)");
                System.out.println("4. Delete (Remove Teacher)");
                System.out.print("Action: ");
                int action = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (action) {
                    case 1: // CREATE
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Age: ");
                        int age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Subject: ");
                        String subject = scanner.nextLine();
                        System.out.print("Enter Experience Years: ");
                        int exp = scanner.nextInt();
                        teacherDao.addTeacher(new Teacher(name, age, subject, exp));
                        break;
                    case 2: // READ
                        teacherDao.readTeachers();
                        break;
                    case 3: // UPDATE
                        System.out.print("Enter Teacher Name to update: ");
                        String upName = scanner.nextLine();
                        System.out.print("Enter New Experience: ");
                        int newExp = scanner.nextInt();
                        teacherDao.updateTeacherExperience(upName, newExp);
                        break;
                    case 4: // DELETE
                        System.out.print("Enter Teacher Name to delete: ");
                        String delName = scanner.nextLine();
                        teacherDao.deleteTeacher(delName);
                        break;
                }
            }
        }
        System.out.println("Program closed.");
        scanner.close();
    }
}