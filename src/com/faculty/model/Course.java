public class Course {
    private String courseCode;
    private String courseName;
    private String lecturerName;
    private int credits;

    public Course(String courseCode, String courseName, String lecturerName, int credits) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.lecturerName = lecturerName;
        this.credits = credits;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String code) { this.courseCode = code; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String name) { this.courseName = name; }
    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String name) { this.lecturerName = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
}
