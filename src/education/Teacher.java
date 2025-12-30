package education;

public class Teacher extends Person {
    private String subject;
    private int experienceYears;

    public Teacher(String name, int age, String subject, int experienceYears) {
        super(name, age);
        this.subject = subject;
        this.experienceYears = experienceYears;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    // Overloading
    public void addExperience() { this.experienceYears++; }
    public void addExperience(int years) { this.experienceYears += years; }

    @Override
    public String getRole() { return "Teacher"; }

    @Override
    public void display() {
        System.out.println("Teacher: " + getName() + ", Age: " + getAge() + ", Subject: " + subject + ", Experience: " + experienceYears + " years");
    }
}