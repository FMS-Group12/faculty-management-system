import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentProfileView extends JFrame {

    // ===== COLOR PALETTE (MIDNIGHT THEME) =====
    private final Color CLR_BG_START = new Color(20, 24, 42);
    private final Color CLR_BG_END   = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT   = new Color(212, 175, 55);
    private final Color CLR_WHITE    = new Color(245, 245, 245);
    private final Color CLR_FIELD_BG = new Color(45, 50, 75);
    private final Color CLR_NAV_BAR  = new Color(15, 18, 32);
    private final Color CLR_LOGOUT   = new Color(255, 80, 80);

    // ===== FONTS =====
    private final Font FONT_TITLE = new Font("Inter", Font.ITALIC | Font.BOLD, 34);
    private final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV   = new Font("SansSerif", Font.BOLD, 14);

    // ===== FIELDS =====
    private JTextField txtStudentId, txtName, txtEmail, txtMobile, txtDegree, txtUserId;

    private StudentDAO dao = new StudentDAO();
    private String currentStudentName = "Student";

    public StudentProfileView() {
        initializeUI();
    }

    // =====================================================
    // MAIN UI
    // =====================================================
    private void initializeUI() {

        setTitle("Faculty Management System - Student Profile");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, CLR_BG_START,
                        0, getHeight(), CLR_BG_END
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(rootPanel);

        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);
        rootPanel.add(createProfilePanel(), BorderLayout.CENTER);
    }

    // =====================================================
    // NAV BAR (SAME AS TIMETABLE)
    // =====================================================
    private JPanel createTopNavBar() {

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(CLR_NAV_BAR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 70));
        navPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(255,255,255,30)));

        JLabel lblWelcome = new JLabel("  Welcome, " + currentStudentName);
        lblWelcome.setFont(FONT_NAV);
        lblWelcome.setForeground(CLR_ACCENT);
        navPanel.add(lblWelcome, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 22));
        buttonPanel.setOpaque(false);

        JButton btnProfile   = createNavButton("Profile");
        JButton btnTimetable = createNavButton("Timetable");
        JButton btnCourses   = createNavButton("Courses");
        JButton btnLogout    = createNavButton("Logout");

        // Active page
        btnProfile.setForeground(CLR_ACCENT);
        btnProfile.setBorder(new MatteBorder(0, 0, 2, 0, CLR_ACCENT));

        btnTimetable.addActionListener(e -> {
            new TimeTableView(currentStudentName).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new SignInView().setVisible(true);
                dispose();
            }
        });

        buttonPanel.add(btnProfile);
        buttonPanel.add(btnTimetable);
        buttonPanel.add(btnCourses);
        buttonPanel.add(btnLogout);

        navPanel.add(buttonPanel, BorderLayout.EAST);
        return navPanel;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);

        btn.setForeground(text.equals("Logout") ? CLR_LOGOUT : CLR_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(CLR_ACCENT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (text.equals("Logout")) btn.setForeground(CLR_LOGOUT);
                else if (text.equals("Profile")) btn.setForeground(CLR_ACCENT);
                else btn.setForeground(CLR_WHITE);
            }
        });
        return btn;
    }

    // =====================================================
    // PROFILE FORM
    // =====================================================
    private JPanel createProfilePanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(40, 120, 40, 120));

        JLabel lblTitle = new JLabel("My Profile", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_WHITE);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formCard = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_GLASS_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        formCard.setOpaque(false);
        formCard.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtStudentId = createField();
        txtName      = createField();
        txtEmail     = createField();
        txtMobile    = createField();
        txtDegree    = createField();
        txtUserId    = createField();

        addRow(formCard, gbc, 0, "Student ID", txtStudentId);
        addRow(formCard, gbc, 1, "Full Name", txtName);
        addRow(formCard, gbc, 2, "Email", txtEmail);
        addRow(formCard, gbc, 3, "Mobile No", txtMobile);
        addRow(formCard, gbc, 4, "Degree ID", txtDegree);
        addRow(formCard, gbc, 5, "User ID", txtUserId);

        panel.add(formCard, BorderLayout.CENTER);

        JButton btnFetch = createActionButton("FETCH DATA");
        JButton btnSave  = createActionButton("SAVE CHANGES");

        btnFetch.addActionListener(e -> fetchStudentFromDB());
        btnSave.addActionListener(e -> updateStudentInDB());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnFetch);
        bottomPanel.add(btnSave);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String label, JTextField field) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(CLR_WHITE);
        lbl.setFont(FONT_LABEL);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JTextField createField() {
        JTextField txt = new JTextField(30);
        txt.setFont(FONT_FIELD);
        txt.setBackground(CLR_FIELD_BG);
        txt.setForeground(CLR_WHITE);
        txt.setCaretColor(CLR_WHITE);
        return txt;
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBackground(CLR_ACCENT);
        btn.setForeground(CLR_BG_START);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    // =====================================================
    // DATABASE ACTIONS
    // =====================================================
    private void fetchStudentFromDB() {
        String id = txtStudentId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Student ID!");
            return;
        }

        Student s = dao.getStudentById(id);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }

        txtName.setText(s.fullname);
        txtEmail.setText(s.email);
        txtMobile.setText(s.mobile_no);
        txtDegree.setText(s.degree_id);
        txtUserId.setText(s.user_id);
    }

    private void updateStudentInDB() {
        Student s = new Student(
                txtStudentId.getText().trim(),
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtMobile.getText().trim(),
                txtDegree.getText().trim(),
                txtUserId.getText().trim()
        );

        if (dao.updateStudent(s)) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Update failed! Check database.");
        }
    }

    // =====================================================
    // MAIN
    // =====================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentProfileView().setVisible(true));
    }
}
