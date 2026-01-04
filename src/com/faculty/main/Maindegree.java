import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        // Always start Swing apps on EDT
        SwingUtilities.invokeLater(() -> {

            // Start with Login screen
            //new SignInView().setVisible(true);

            // OR directly open Degree Dashboard (for testing)
             new DegreeDashboardView().setVisible(true);
        });
    }
}
