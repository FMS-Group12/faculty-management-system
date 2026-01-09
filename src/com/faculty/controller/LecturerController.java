import java.util.Vector;
import java.util.Map;

public class LecturerController {

    private LectureDAO dao;

   
    public LecturerController() {
        this.dao = new LectureDAO();
    }

  
    public Vector<Vector<Object>> getTableData() {
        return dao.getAllLecturers();
    }

    
    public boolean addLecturer(String name, String deptId, String courses, String email, String mobile) {
        // Business logic or validation can go here
        return dao.addLecturer(name, deptId, courses, email, mobile);
    }

   
    public boolean updateLecturer(String name, String deptId, String courses, String email, String mobile, String originalEmail) {
        return dao.updateLecturer(name, deptId, courses, email, mobile, originalEmail);
    }

   
    public boolean deleteLecturer(String email) {
        return dao.deleteLecturer(email);
    }

   
    public Map<String, Integer> getDepartmentMap() {
        return dao.getDepartmentMap();
    }

}
