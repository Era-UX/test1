package education.domain;

public class Student extends Person {
    //2. Encapsulation
    private Student(Builder builder) {
        super(builder.id, builder.name, builder.age);
    }

    private String teacherName;

    public static class Builder {
        private int id;
        private String name;
        private int age;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setAge(int age) {
            this.age = age;
            return this;
        }
        public Student build() {
            return new Student(this);
        }
    }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    @Override
    public String getRole() {
        return "Student";
    }
}