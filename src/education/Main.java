package education;

public class Main {
    public static void main(String[] args) {
        Institution uni = new Institution("Tech University", "New York", 1998);
        uni.displayInfo();
        System.out.println();

        DataPool pool = new DataPool(10);

        Student student1 = new Student("Jake", 18, 360);
        Student student2 = new Student("Anna", 20, 405);

        Teacher teacher1 = new Teacher("Dr. Brown", 45, "Math", 10);
        Teacher teacher2 = new Teacher("Dr. Smith", 50, "Physics", 15);

        pool.add(student1);
        pool.add(student2);
        pool.add(teacher1);
        pool.add(teacher2);

        System.out.println("--- All Members (Initial List) ---");
        pool.showAll();

        pool.sortByAge();
        pool.showAll();

        pool.findByName("Anna");

        pool.showOnlyTeachers();

        // Equals and HashCode demonstration
        Student jakeClone = new Student("Jake", 18, 360);
        System.out.println("\nChecking equals(): " + student1.equals(jakeClone));
        System.out.println("HashCode of Student 1: " + student1.hashCode());
        System.out.println("HashCode of Clone: " + jakeClone.hashCode());

        // Overloading demonstration
        System.out.println("\nTesting Overload for " + teacher1.getName() + ":");
        System.out.println("Current experience: " + teacher1.getExperienceYears() + " years");

        System.out.println("Adding 1 year (default)...");
        teacher1.addExperience();
        System.out.println("Adding 5 years (overloaded)...");
        teacher1.addExperience(5);
        System.out.println("Experience after Overload: " + teacher1.getExperienceYears());
    }
}