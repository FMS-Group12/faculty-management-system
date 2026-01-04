public class Lecturer {

    private int lecturerId; // Added to match the degreeId pattern
    private String fullName;
    private String departmentName;
    private String courses;
    private String email;
    private String mobileNo;

    // Default no-argument constructor (Added to match Degree pattern)
    public Lecturer() {
    }

    // Full constructor
    public Lecturer(int lecturerId, String fullName, String departmentName, String courses, String email, String mobileNo) {
        this.lecturerId = lecturerId;
        this.fullName = fullName;
        this.departmentName = departmentName;
        this.courses = courses;
        this.email = email;
        this.mobileNo = mobileNo;
    }

    // Getters and Setters (Updated to match Degree pattern)
    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}