import java.util.Vector;
import java.util.Map;

public class LecturerController {

    private LectureDAO dao;

    /**
     * Standard Constructor
     * Matches the DepartmentController pattern.
     */
    public LecturerController() {
        this.dao = new LectureDAO();
    }

    // =========================================================
    // DATA ACCESS METHODS (Degree/Department Pattern)
    // =========================================================

    /**
     * Fetches raw data from the DAO.
     */
    public Vector<Vector<Object>> getTableData() {
        return dao.getAllLecturers();
    }

    /**
     * Logic for adding a new lecturer.
     */
    public boolean addLecturer(String name, String deptId, String courses, String email, String mobile) {
        // Business logic or validation can go here
        return dao.addLecturer(name, deptId, courses, email, mobile);
    }

    /**
     * Logic for updating an existing lecturer.
     */
    public boolean updateLecturer(String name, String deptId, String courses, String email, String mobile, String originalEmail) {
        return dao.updateLecturer(name, deptId, courses, email, mobile, originalEmail);
    }

    /**
     * Logic for deleting a lecturer.
     */
    public boolean deleteLecturer(String email) {
        return dao.deleteLecturer(email);
    }

    /**
     * Fetches department mapping for the UI ComboBox.
     */
    public Map<String, Integer> getDepartmentMap() {
        return dao.getDepartmentMap();
    }
}