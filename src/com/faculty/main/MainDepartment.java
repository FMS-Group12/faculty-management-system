import javax.swing.SwingUtilities;

public class MainDepartment {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DepartmentDashboardView().setVisible(true);
        });
    }
}
