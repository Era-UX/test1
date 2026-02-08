package education.domain;

public class Institution {
    private String name;
    private String address;
    private String rectorName;

    private int totalStudents;
    private int totalTeachers;

    public Institution(String name, String address, String rectorName) {
        this.name = name;
        this.address = address;
        this.rectorName = rectorName;
    }

    public void updateStatistics(int studentCount, int teacherCount) {
        this.totalStudents = studentCount;
        this.totalTeachers = teacherCount;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getRectorName() { return rectorName; }
    public int getTotalStudents() { return totalStudents; }
    public int getTotalTeachers() { return totalTeachers; }
}