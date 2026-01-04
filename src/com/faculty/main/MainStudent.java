import javax.swing.SwingUtilities;

public class MainStudent {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentDashboardView().setVisible(true);
        });
    }
}
