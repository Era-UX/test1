package education;

public class Student extends Person {
    private int id;

    public Student(String name, int age, int id) {
        super(name, age);
        this.id = id;
    }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    @Override
    public String getRole() { return "Student"; }

    @Override
    public void display() {
        System.out.println("Student: " + getName() + ", Age: " + getAge() + ", ID: " + id);
    }
}