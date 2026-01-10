import java.util.List;

public class CourseController {

    private CourseDAO dao;

    public CourseController() {
        this.dao = new CourseDAO();
    }

    public List<Object[]> getAllCourses() {
        return dao.getAllCourses();
    }

    public boolean addCourse(String code, String name, int credits, int lecturerId) {
        return dao.insertCourse(code, name, credits, lecturerId);
    }

    public boolean updateCourse(String oldCode, String newCode, String name, int lecturerId, int credits) {
        return dao.updateCourse(oldCode, newCode, name, lecturerId, credits);
    }

    public boolean deleteCourse(String code) {
        return dao.deleteCourse(code);
    }
    public int getLecturerIdByName(String name) {
        return dao.getLecturerIdByName(name);
    }
}
