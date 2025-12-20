package education;

public class Main {
    public static void main(String[] args) {

        Student student1 = new Student("Jake", 18, 360);
        Student student2 = new Student("Anna", 20, 405);

        student1.display();
        student2.display();

        if (student1.getAge() > student2.getAge()) {
            System.out.println("Jake is older than Anna");
        } else if (student1.getAge() < student2.getAge()) {
            System.out.println("Anna is older than Jake");
        } else {
            System.out.println("Jake and Anna are the same age");
        }

        System.out.println();

        Teacher teacher1 = new Teacher("Dr. Brown", "Math", 10);
        Teacher teacher2 = new Teacher("Dr. Smith", "Physics", 15);

        teacher1.display();
        teacher2.display();

        if (teacher1.getExperienceYears() > teacher2.getExperienceYears()) {
            System.out.println("Dr. Brown has more experience than Dr. Smith");
        } else if (teacher1.getExperienceYears() < teacher2.getExperienceYears()) {
            System.out.println("Dr. Smith has more experience than Dr. Brown");
        } else {
            System.out.println("Both teachers have the same experience");
        }

        System.out.println();

        Institution institution = new Institution(
                "Tech University",
                "New York",
                1998
        );
        institution.display();
    }
}