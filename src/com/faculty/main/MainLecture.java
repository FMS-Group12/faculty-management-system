import javax.swing.SwingUtilities;

public class MainLecture {
    public static void main(String[] args) {

        // Always start Swing apps on EDT
        SwingUtilities.invokeLater(() -> {

            // Start with Login screen
            // new SignInView().setVisible(true);

            // OR directly open Lecturer Dashboard (for testing)
            // Matches the simplified Degree pattern
            new LecturerDashboardView().setVisible(true);
        });
    }
}