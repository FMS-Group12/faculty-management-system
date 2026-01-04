public class Student {

    private String studentId;
    private String fullName;
    private String degree;
    private String email;
    private String mobile;

    public Student(String studentId, String fullName, String degree, String email, String mobile) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.degree = degree;
        this.email = email;
        this.mobile = mobile;
    }

    public String getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public String getDegree() { return degree; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
}
