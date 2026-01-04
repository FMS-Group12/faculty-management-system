public class Department {
    private int id;
    private String name;
    private String hod;
    private int noOfStaff;

    public Department(int id, String name, String hod, int noOfStaff) {
        this.id = id;
        this.name = name;
        this.hod = hod;
        this.noOfStaff = noOfStaff;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getHod() { return hod; }
    public int getNoOfStaff() { return noOfStaff; }
}
