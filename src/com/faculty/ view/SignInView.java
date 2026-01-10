import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

// --- SIGN IN PAGE ---
public class SignInView extends JFrame {

    // --- COLORS ---
    private final Color CLR_BG_START = new Color(20, 24, 42);
    private final Color CLR_BG_END = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT = new Color(212, 175, 55);
    private final Color CLR_WHITE = new Color(245, 245, 245);
    private final Color CLR_FIELD_BG = new Color(45, 50, 75);

    private JTextField txtUser;
    private JPasswordField txtPass;
    private int mouseX, mouseY;
    private String selectedRole = "";

    public SignInView() {
        initializeUI();
    }

    private void initializeUI() {
        setUndecorated(true);
        setSize(550, 670);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, CLR_BG_START, 0, getHeight(), CLR_BG_END);
                g2d.setPaint(gp);
                int arc = (getExtendedState() == JFrame.MAXIMIZED_BOTH) ? 0 : 40;
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // --- TITLE BAR ---
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setPreferredSize(new Dimension(getWidth(), 60));
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (getExtendedState() != JFrame.MAXIMIZED_BOTH)
                    setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        controls.setOpaque(false);
        JButton btnMin = createControlBtn("—");
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
        JButton btnMax = createControlBtn("⬜");
        btnMax.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) setExtendedState(JFrame.NORMAL);
            else setExtendedState(JFrame.MAXIMIZED_BOTH);
            repaint();
        });
        JButton btnClose = createControlBtn("✕");
        btnClose.addActionListener(e -> System.exit(0));
        controls.add(btnMin);
        controls.add(btnMax);
        controls.add(btnClose);
        titleBar.add(controls, BorderLayout.EAST);
        mainPanel.add(titleBar, BorderLayout.NORTH);

        // --- CENTER GLASS PANEL ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel glass = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 40, 40);
                g2.setColor(CLR_GLASS_BG);
                g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 40, 40);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(0, 0, getWidth() - 11, getHeight() - 11, 40, 40);
            }
        };
        glass.setOpaque(false);
        glass.setPreferredSize(new Dimension(460, 550));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        title.setForeground(CLR_WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 40)));

        txtUser = (JTextField) addLabeledField(content, "Username", false);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        txtPass = (JPasswordField) addLabeledField(content, "Password", true);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        // --- ROLE SELECTION ---
        JLabel lblRole = new JLabel("Select Your Role");
        lblRole.setForeground(new Color(200, 200, 200));
        lblRole.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblRole);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(createRolePanel());
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnLogin = createRoundedButton("SIGN IN");
        btnLogin.addActionListener(e -> handleLogin());
        content.add(btnLogin);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnSignUp = new JButton("Don't have an account? Register");
        btnSignUp.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnSignUp.setForeground(CLR_ACCENT);
        btnSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSignUp.setContentAreaFilled(false);
        btnSignUp.setBorderPainted(false);
        btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignUp.addActionListener(e -> {
            new SignUpView().setVisible(true);
            this.dispose();
        });
        content.add(btnSignUp);

        glass.add(content);
        centerWrapper.add(glass);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
    }

    private JPanel createRolePanel() {
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        rolePanel.setOpaque(false);
        rolePanel.setMaximumSize(new Dimension(450, 50));

        String[] roles = {"Admin", "Student", "Lecturer"};
        JButton[] buttons = new JButton[roles.length];

        for (int i = 0; i < roles.length; i++) {
            final int index = i;
            buttons[i] = new JButton(roles[i]);
            buttons[i].setPreferredSize(new Dimension(110, 40));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(new Color(255, 255, 255, 10));
            buttons[i].setForeground(CLR_WHITE);
            buttons[i].setBorder(new RoundBorder(new Color(255, 255, 255, 40), 15));
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttons[i].setContentAreaFilled(true);
            buttons[i].addActionListener(e -> {

                for (JButton btn : buttons) {
                    btn.setBackground(new Color(255, 255, 255, 10));
                    btn.setForeground(CLR_WHITE);
                }
                buttons[index].setBackground(CLR_ACCENT);
                buttons[index].setForeground(CLR_BG_START);
                selectedRole = roles[index];


                rolePanel.repaint();
            });
            rolePanel.add(buttons[i]);
        }

        return rolePanel;
    }

    private void handleLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty() || selectedRole.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a role.");
            return;
        }

        SignUpDAO dao = new SignUpDAO();
        boolean exists = dao.loginUserWithRole(user, pass, selectedRole);

        if (exists) {
            //JOptionPane.showMessageDialog(this,"Login successful as "+selectedRole+"!");

            // --- ONLY FOR ADMIN ---
            if (selectedRole.equalsIgnoreCase("Admin")) {
                AdminView adminview = new AdminView();
                adminview.setVisible(true);
                this.dispose(); // Close login
            } else if (selectedRole.equalsIgnoreCase("Student")) {
                String username = txtUser.getText().trim();
                StudentView studentview = new StudentView(username);
                studentview.setVisible(true);
                this.dispose();

            } else if ((selectedRole.equalsIgnoreCase("Lecturer"))) {
                JOptionPane.showMessageDialog(this, "Login successful as " + selectedRole + "!");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials or role!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JComponent addLabeledField(JPanel container, String labelText, boolean isPassword) {
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(lbl);
        container.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextField field = isPassword ? new JPasswordField() : new JTextField();
        field.setMaximumSize(new Dimension(380, 45));
        field.setBackground(CLR_FIELD_BG);
        field.setForeground(CLR_WHITE);
        field.setCaretColor(CLR_WHITE);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setOpaque(true);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(new Color(255, 255, 255, 40), 15),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        container.add(field);
        return field;
    }

    private JButton createRoundedButton(String text) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        b.setMaximumSize(new Dimension(380, 55));
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setForeground(CLR_BG_START);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    private JButton createControlBtn(String text) {
        JButton b = new JButton(text);
        b.setForeground(CLR_WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    class RoundBorder extends javax.swing.border.AbstractBorder {
        private Color color;
        private int radius;

        RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}