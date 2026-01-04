import javax.swing.*;

public class mainCourse {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CourseDashboardView().setVisible(true));
    }
}
