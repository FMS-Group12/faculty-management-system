import java.util.List;

public class CourseController {
    private CourseDAO dao;

    public CourseController() {
        this.dao = new CourseDAO(); // Initialize the DAO here
    }

    // Proxy methods to talk to the DAO
    public List<Object[]> getAllCourses() {
        return dao.getAllCourses();
    }

    public boolean addCourse(String code, String name, int credits, String lecturer) {
        return dao.insertCourse(code, name, credits, lecturer);
    }

    public boolean updateCourse(String oldCode, String newCode, String name, String lecturer, int credits) {
        return dao.updateCourse(oldCode, newCode, name, lecturer, credits);
    }

    public boolean deleteCourse(String code) {
        return dao.deleteCourse(code);
    }
}
