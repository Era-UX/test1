package education;

public class Institution {

    private String name;
    private String address;
    private int foundedYear;

    public Institution(String name, String address, int foundedYear) {
        this.name = name;
        this.address = address;
        this.foundedYear = foundedYear;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getFoundedYear() {
        return foundedYear;
    }
    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }

    public void display() {
        System.out.println("Institution:");
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Founded: " + foundedYear);
    }
}