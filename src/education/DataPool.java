package education;

public class DataPool {
    private Person[] people;
    private int count;

    public DataPool(int capacity) {
        this.people = new Person[capacity];
        this.count = 0;
    }

    public void add(Person p) {
        if (count < people.length) {
            people[count] = p;
            count++;
        } else {
            System.out.println("Error: Data Pool is full.");
        }
    }

    // 1. Sorting (Bubble Sort)
    public void sortByAge() {
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (people[j].getAge() > people[j+1].getAge()) {
                    Person temp = people[j];
                    people[j] = people[j+1];
                    people[j+1] = temp;
                }
            }
        }
        System.out.println("\n>> List sorted by age.");
    }

    // 2. Searching
    public void findByName(String name) {
        System.out.println("\n>> Searching for: " + name);
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (people[i].getName().equals(name)) {
                people[i].display();
                found = true;
            }
        }
        if (!found) System.out.println("Not found.");
    }

    // 3. Filtering
    public void showOnlyTeachers() {
        System.out.println("\n>> Filter: Showing only Teachers");
        for (int i = 0; i < count; i++) {
            if (people[i].getRole().equals("Teacher")) {
                people[i].display();
            }
        }
    }

    public void showAll() {
        for (int i = 0; i < count; i++) {
            people[i].display();
        }
    }
}