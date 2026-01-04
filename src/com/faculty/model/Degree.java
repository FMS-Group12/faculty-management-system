public class Degree {

    private int degreeId;
    private String degreeName;
    private String departmentName;
    private int numberOfStudents;
    
    public Degree() {
    }
    
    public Degree(int degreeId, String degreeName, String departmentName, int numberOfStudents) {
        this.degreeId = degreeId;
        this.degreeName = degreeName;
        this.departmentName = departmentName;
        this.numberOfStudents = numberOfStudents;
    }
    
    public int getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(int degreeId) {
        this.degreeId = degreeId;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

}

