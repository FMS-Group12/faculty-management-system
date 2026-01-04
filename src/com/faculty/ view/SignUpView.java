

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SignUpView extends JFrame {

    // --- REFINED PREMIUM PALETTE ---
    private final Color CLR_BG_START   = new Color(20, 24, 42);
    private final Color CLR_BG_END     = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG   = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT     = new Color(212, 175, 55);
    private final Color CLR_WHITE      = new Color(245, 245, 245);
    private final Color CLR_FIELD_BG   = new Color(45, 50, 75);

    private JTextField txtUser;
    private JPasswordField txtPass, txtConfirm;
    private JButton[] buttons;
    private int mouseX, mouseY;
    private String selectedRole = "";

    public SignUpView() {
        initializeUI();
    }

    private void initializeUI() {
        setUndecorated(true);
        setSize(550, 850);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

        // --- MAIN BACKGROUND PANEL ---
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, CLR_BG_START, 0, getHeight(), CLR_BG_END);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        setContentPane(mainPanel);

        // --- 1. TITLE BAR ---
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setPreferredSize(new Dimension(getWidth(), 60));

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });

        JPanel windowControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        windowControls.setOpaque(false);

        JButton btnMin = createControlBtn("—");
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton btnClose = createControlBtn("✕");
        btnClose.addActionListener(e -> System.exit(0));

        windowControls.add(btnMin);
        windowControls.add(btnClose);
        titleBar.add(windowControls, BorderLayout.EAST);
        mainPanel.add(titleBar, BorderLayout.NORTH);

        // --- 2. CENTERED GLASS CARD ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel customGlass = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                g2.setColor(new Color(0,0,0,80));
                g2.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 40, 40);
                // Glass effect
                g2.setColor(CLR_GLASS_BG);
                g2.fillRoundRect(0, 0, getWidth()-10, getHeight()-10, 40, 40);
            }
        };
        customGlass.setOpaque(false);
        customGlass.setPreferredSize(new Dimension(460, 720));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Content
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        title.setForeground(CLR_WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        txtUser = (JTextField) addLabeledField(contentPanel, "Username", false);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        txtPass = (JPasswordField) addLabeledField(contentPanel, "Password", true);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        txtConfirm = (JPasswordField) addLabeledField(contentPanel, "Confirm Password", true);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel lblRole = new JLabel("Select Your Role");
        lblRole.setForeground(new Color(200, 200, 200));
        lblRole.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblRole);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        contentPanel.add(createModernRolePanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton btnSignUp = createRoundedButton("REGISTER");
        btnSignUp.addActionListener(e -> handleSignUp());
        contentPanel.add(btnSignUp);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnLogin = new JButton("Already have an account? Login");
        btnLogin.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnLogin.setForeground(CLR_ACCENT);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> {
            new SignInView().setVisible(true);
            this.dispose();
        });
        contentPanel.add(btnLogin);

        // --- ASSEMBLY ---
        customGlass.add(contentPanel, BorderLayout.CENTER); // Add content to glass
        centerWrapper.add(customGlass);                    // Put glass in center wrapper
        mainPanel.add(centerWrapper, BorderLayout.CENTER); // Put wrapper in main background
    }

    private void handleSignUp() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        if (user.isEmpty() || pass.isEmpty() || selectedRole.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a role.");
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        SignUpDAO dao = new SignUpDAO();
        if (dao.isUsernameTaken(user)) {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (dao.registerUser(user, pass, selectedRole)) {
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Database error.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtUser.setText("");
        txtPass.setText("");
        txtConfirm.setText("");
        selectedRole = "";
        if (buttons != null) {
            for (JButton btn : buttons) {
                btn.setBackground(new Color(255, 255, 255, 10));
                btn.setForeground(CLR_WHITE);
            }
        }
        repaint();
    }

    private JPanel createModernRolePanel() {
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        rolePanel.setOpaque(false);
        rolePanel.setMaximumSize(new Dimension(450, 50));

        String[] roles = {"Admin", "Student", "Lecturer"};
        buttons = new JButton[roles.length];

        for (int i = 0; i < roles.length; i++) {
            final int index = i;
            buttons[i] = new JButton(roles[i]);
            buttons[i].setPreferredSize(new Dimension(110, 40));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(new Color(255,255,255,10));
            buttons[i].setForeground(CLR_WHITE);
            buttons[i].setBorder(new RoundBorder(new Color(255,255,255,40), 15));
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            buttons[i].addActionListener(e -> {
                for (JButton btn : buttons) {
                    btn.setBackground(new Color(255,255,255,10));
                    btn.setForeground(CLR_WHITE);
                }
                buttons[index].setBackground(CLR_ACCENT);
                buttons[index].setForeground(CLR_BG_START);
                selectedRole = roles[index];
            });
            rolePanel.add(buttons[i]);
        }
        return rolePanel;
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
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(new Color(255,255,255,40), 15),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));

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
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        private Color color; private int radius;
        RoundBorder(Color color, int radius) { this.color = color; this.radius = radius; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUpView().setVisible(true));
    }
}

// --- Placeholder Classes to ensure the code compiles ---


