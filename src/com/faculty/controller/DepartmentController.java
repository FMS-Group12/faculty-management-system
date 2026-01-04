import java.util.Vector;

public class DepartmentController {

    private DepartmentDAO dao;

    public DepartmentController() {
        this.dao = new DepartmentDAO();
    }

    public Vector<Vector<Object>> getTableData() {
        return dao.getAllDepartments();
    }

    public boolean addDepartment(String name, String hod, int staff) {
        return dao.addDepartment(name, hod, staff);
    }

    public boolean updateDepartment(int id, String name, String hod, int staff) {
        return dao.updateDepartment(id, name, hod, staff);
    }

    public boolean deleteDepartment(int id) {
        return dao.deleteDepartment(id);
    }
}
