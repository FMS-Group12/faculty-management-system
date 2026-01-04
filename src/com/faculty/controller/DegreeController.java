import java.util.List;
import java.util.Vector;

public class DegreeController {

    private DegreeDAO degreeDAO;

    public DegreeController() {
        degreeDAO = new DegreeDAO();
    }

    public Vector<Vector<Object>> getAllDegreesForTable() {
        return degreeDAO.getAllDegrees();
    }

    public boolean addDegree(String degreeName, String departmentName, int numberOfStudents) {

        // Basic business validation
        if (degreeName == null || degreeName.isEmpty()) {
            return false;
        }
        if (departmentName == null || departmentName.isEmpty()) {
            return false;
        }
        if (numberOfStudents < 0) {
            return false;
        }

        return degreeDAO.addDegree(degreeName, departmentName, numberOfStudents);
    }
    
    public boolean updateDegree(String oldDegreeName,
                                String newDegreeName,
                                String departmentName,
                                int numberOfStudents) {

        if (newDegreeName == null || newDegreeName.isEmpty()) {
            return false;
        }

        return degreeDAO.updateDegree(
                oldDegreeName,
                newDegreeName,
                departmentName,
                numberOfStudents
        );
    }

    public boolean deleteDegree(String degreeName) {
        if (degreeName == null || degreeName.isEmpty()) {
            return false;
        }
        return degreeDAO.deleteDegree(degreeName);
    }
}
