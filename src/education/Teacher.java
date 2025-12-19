package education;

public class Teacher {

    private String name;
    private String subject;
    private int experienceYears;

    public Teacher(String name, String subject, int experienceYears) {
        this.name = name;
        this.subject = subject;
        this.experienceYears = experienceYears;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getExperienceYears() {
        return experienceYears;
    }
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void display() {
        System.out.println("Teacher:");
        System.out.println("Name: " + name);
        System.out.println("Subject: " + subject);
        System.out.println("Experience: " + experienceYears + " years");
    }
}