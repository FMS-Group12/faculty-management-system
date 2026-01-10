import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;


public class StudentProfileView extends JFrame {
    
    private final Color CLR_BG_START = new Color(20, 24, 42);
    private final Color CLR_BG_END   = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT   = new Color(212, 175, 55);
    private final Color CLR_WHITE    = new Color(245, 245, 245);
    private final Color CLR_FIELD_BG = new Color(45, 50, 75);
    private final Color CLR_NAV_BAR  = new Color(15, 18, 32);
    private final Color CLR_LOGOUT   = new Color(255, 80, 80);
    
    private final Font FONT_TITLE = new Font("Inter", Font.ITALIC | Font.BOLD, 34);
    private final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV   = new Font("SansSerif", Font.BOLD, 14);
    
    private JTextField txtStudentId, txtName, txtEmail, txtMobile, txtDegree, txtUserId;
    private JLabel lblWelcome;

    private StudentDAO1 dao = new StudentDAO1();
    private String currentUsername;
    private String studentDisplayName = "Student";

    public StudentProfileView(String username) {
        this.currentUsername = username;
        initializeUI();
        autoLoadProfile();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Student Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, CLR_BG_START, 0, getHeight(), CLR_BG_END);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(rootPanel);

        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);
        rootPanel.add(createProfilePanel(), BorderLayout.CENTER);
    }

    private JPanel createTopNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(CLR_NAV_BAR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 70));
        navPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 30)));

        lblWelcome = new JLabel("  Welcome, " + studentDisplayName);
        lblWelcome.setFont(FONT_NAV);
        lblWelcome.setForeground(CLR_ACCENT);
        navPanel.add(lblWelcome, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 22));
        buttonPanel.setOpaque(false);

        JButton btnProfile   = createNavButton("Profile");
        JButton btnTimetable = createNavButton("Timetable");
        JButton btnCourses   = createNavButton("Courses");
        JButton btnLogout    = createNavButton("Logout");

        btnProfile.setForeground(CLR_ACCENT); 

        btnTimetable.addActionListener(e -> { new TimeTableView(currentUsername).setVisible(true); dispose(); });
        btnCourses.addActionListener(e -> { new CourseEnrolled(currentUsername).setVisible(true); dispose(); });
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", 0) == 0) {
                new SignInView().setVisible(true);
                dispose();
            }
        });

        buttonPanel.add(btnProfile); buttonPanel.add(btnTimetable);
        buttonPanel.add(btnCourses); buttonPanel.add(btnLogout);
        navPanel.add(buttonPanel, BorderLayout.EAST);
        return navPanel;
    }

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

        txtStudentId = createField(false); 
        txtName      = createField(true);
        txtEmail     = createField(true);
        txtMobile    = createField(true);
        txtDegree    = createField(false); 
        txtUserId    = createField(false); 

        addRow(formCard, gbc, 0, "Student ID", txtStudentId);
        addRow(formCard, gbc, 1, "Full Name", txtName);
        addRow(formCard, gbc, 2, "Email Address", txtEmail);
        addRow(formCard, gbc, 3, "Mobile No", txtMobile);
        addRow(formCard, gbc, 4, "Enrolled Degree", txtDegree);
        addRow(formCard, gbc, 5, "System User ID", txtUserId);

        panel.add(formCard, BorderLayout.CENTER);

        JButton btnSave = new JButton("SAVE CHANGES");
        btnSave.setPreferredSize(new Dimension(220, 45));
        btnSave.setBackground(CLR_ACCENT);
        btnSave.setForeground(CLR_BG_START);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSave.addActionListener(e -> updateProfile());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(CLR_WHITE); lbl.setFont(FONT_LABEL);
        p.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        p.add(field, gbc);
    }

    private JTextField createField(boolean editable) {
        JTextField txt = new JTextField(30);
        txt.setFont(FONT_FIELD);
        txt.setBackground(editable ? CLR_FIELD_BG : new Color(30, 35, 55));
        txt.setForeground(editable ? CLR_WHITE : new Color(150, 150, 150));
        txt.setCaretColor(CLR_WHITE);
        txt.setEditable(editable);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,255,255,20)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return txt;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);
        btn.setForeground(text.equals("Logout") ? CLR_LOGOUT : CLR_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

   
    private void autoLoadProfile() {
        Student1 s = dao.getStudentByUsername(currentUsername);
        if (s != null) {
            studentDisplayName = s.fullname;
            lblWelcome.setText("  Welcome, " + studentDisplayName);

            txtStudentId.setText(s.student_id);
            txtName.setText(s.fullname);
            txtEmail.setText(s.email);
            txtMobile.setText(s.mobile_no);
            txtDegree.setText(s.degree_id); 
            txtUserId.setText(s.user_id);
        }
    }

    private void updateProfile() {
        Student1 s = new Student1(
                txtStudentId.getText().trim(),
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtMobile.getText().trim(),
                txtDegree.getText().trim(),
                txtUserId.getText().trim()
        );

        if (dao.updateStudent(s)) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            lblWelcome.setText("  Welcome, " + s.fullname);
        } else {
            JOptionPane.showMessageDialog(this, "Error: Could not update profile.");
        }
    }
}
