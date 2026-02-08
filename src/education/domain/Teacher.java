package education.domain;

public class Teacher extends Person {
    private String subject;
    private int experience;

    private Teacher(Builder builder) {
        super(builder.id, builder.name, builder.age);
        this.subject = builder.subject;
        this.experience = builder.experience;
    }

    public String getSubject() { return subject; }
    public int getExperience() { return experience; }

    @Override
    public String getRole() {
        return "Teacher";
    }

    @Override
    public String toString() {
        return String.format("%s | Subject: %-10s | Experience: %d years",
                super.toString(), subject, experience);
    }

    public static class Builder {
        private int id;
        private String name;
        private int age;
        private String subject;
        private int experience;

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
        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }
        public Builder setExperience(int experience) {
            this.experience = experience;
            return this;
        }
        public Teacher build() {
            return new Teacher(this);
        }
    }
}