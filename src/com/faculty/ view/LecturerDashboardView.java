

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LecturerDashboardView extends JFrame {

    // --- MIDNIGHT GLASS PALETTE (Matching SignUpView) ---
    private final Color CLR_BG_START   = new Color(20, 24, 42);
    private final Color CLR_BG_END     = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG   = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT     = new Color(212, 175, 55); // Gold
    private final Color CLR_WHITE      = new Color(245, 245, 245);
    private final Color CLR_FIELD_BG   = new Color(45, 50, 75);
    private final Color CLR_NAV_BAR    = new Color(15, 18, 32);
    private final Color CLR_LOGOUT     = new Color(255, 80, 80);

    // Fonts
    private final Font FONT_TITLE  = new Font("Inter", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_BTN    = new Font("SansSerif", Font.BOLD, 11);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_CELL   = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV    = new Font("SansSerif", Font.BOLD, 18);

    private DefaultTableModel lecturerTableModel;
    private JTable lecturerTable;
    private LectureDAO lectureDAO = new LectureDAO();

    public LecturerDashboardView() {
        initializeUI();
        refreshTable();
    }

    private void refreshTable() {
        lectureDAO.getAllLecturers(lecturerTableModel);
    }

    private void initializeUI() {
        setTitle("Faculty Management System ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        // Main Gradient Background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, CLR_BG_START, 0, getHeight(), CLR_BG_END);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(mainPanel);

        mainPanel.add(createTopNavBar(), BorderLayout.NORTH);
        mainPanel.add(createLecturersContent(), BorderLayout.CENTER);

        // Bottom Save Area
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 40, 0));

        JButton btnSave = createRoundedButton("SAVE CHANGES", new Dimension(200, 40), CLR_ACCENT, 16);
        btnSave.addActionListener(e -> {
            refreshTable();
            JOptionPane.showMessageDialog(this, "Data Synchronized Successfully!");
        });

        bottomPanel.add(btnSave);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(CLR_NAV_BAR);
        navPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(255,255,255,30)));
        navPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel lblWelcome = new JLabel("  Welcome, Admin");
        lblWelcome.setFont(FONT_NAV);
        lblWelcome.setForeground(CLR_ACCENT);
        navPanel.add(lblWelcome, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 22));
        buttonPanel.setOpaque(false);

        JButton btnStudents = createNavButton("Students");
        JButton btnLecturers = createNavButton("Lecturers");
        JButton btnCourses = createNavButton("Courses");
        JButton btnDepartments = createNavButton("Departments");
        JButton btnDegrees = createNavButton("Degrees");
        JButton btnLogout = createNavButton("Logout");

        btnLecturers.setForeground(CLR_ACCENT);
        btnLecturers.setBorder(new MatteBorder(0, 0, 2, 0, CLR_ACCENT));

        btnStudents.addActionListener(e -> { new StudentDashboardView().setVisible(true); this.dispose(); });
        btnCourses.addActionListener(e -> { new CourseDashboardView().setVisible(true); this.dispose(); });
        btnDepartments.addActionListener(e -> { new DepartmentDashboardView().setVisible(true); this.dispose(); });
        btnDegrees.addActionListener(e -> { new DegreeDashboardView().setVisible(true); this.dispose(); });

        btnLogout.setForeground(CLR_LOGOUT);
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", 0) == 0) {
                new SignInView().setVisible(true);
                this.dispose();
            }
        });

        buttonPanel.add(btnStudents);
        buttonPanel.add(btnLecturers);
        buttonPanel.add(btnCourses);
        buttonPanel.add(btnDepartments);
        buttonPanel.add(btnDegrees);
        buttonPanel.add(btnLogout);

        navPanel.add(buttonPanel, BorderLayout.EAST);
        return navPanel;
    }

    private JPanel createLecturersContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(60, 50, 30, 50));

        // UPDATED: Title to "Lecturers"
        JLabel lblTitle = new JLabel("Lecturers", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_WHITE);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel tableContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_GLASS_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        tableContainer.setOpaque(false);
        tableContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        controls.setOpaque(false);

        JButton btnAdd = createRoundedButton("Add New", new Dimension(100, 35), CLR_ACCENT, 11);
        JButton btnEdit = createRoundedButton("Edit", new Dimension(100, 35), CLR_ACCENT, 11);
        JButton btnDelete = createRoundedButton("Delete", new Dimension(100, 35), CLR_ACCENT, 11);

        btnAdd.addActionListener(e -> showAddLecturerDialog());
        btnEdit.addActionListener(e -> showEditLecturerDialog());
        btnDelete.addActionListener(e -> deleteSelectedRow());

        controls.add(btnAdd); controls.add(btnEdit); controls.add(btnDelete);
        tableContainer.add(controls, BorderLayout.NORTH);
        tableContainer.add(createLecturerTable(), BorderLayout.CENTER);

        panel.add(tableContainer, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane createLecturerTable() {
        String[] columns = {"Full Name", "Department", "Courses", "Email", "Mobile"};
        lecturerTableModel = new DefaultTableModel(null, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        lecturerTable = new JTable(lecturerTableModel);
        lecturerTable.setRowHeight(45);
        lecturerTable.setFont(FONT_CELL);
        lecturerTable.setForeground(CLR_WHITE);
        lecturerTable.setBackground(new Color(0,0,0,0));
        lecturerTable.setOpaque(false);
        lecturerTable.setSelectionBackground(new Color(212, 175, 55, 60));
        lecturerTable.setSelectionForeground(CLR_ACCENT);
        lecturerTable.setShowGrid(false);

        // --- UPDATED: CENTER CONTENT RENDERING ---
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setOpaque(false);

        for (int i = 0; i < lecturerTable.getColumnCount(); i++) {
            lecturerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = lecturerTable.getTableHeader();
        header.setBackground(new Color(255,255,255,10));
        header.setForeground(CLR_ACCENT);
        header.setFont(FONT_HEADER);
        header.setPreferredSize(new Dimension(0, 45));

        // Center the Header Text
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scroll = new JScrollPane(lecturerTable);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private void showAddLecturerDialog() {
        JTextField nameF = createStyledField();
        JTextField courF = createStyledField();
        JTextField emailF = createStyledField();
        JTextField mobF = createStyledField();
        java.util.Map<String, Integer> deptMap = lectureDAO.getDepartmentMap();
        JComboBox<String> deptCombo = new JComboBox<>(deptMap.keySet().toArray(new String[0]));
        deptCombo.setBackground(CLR_FIELD_BG);
        deptCombo.setForeground(CLR_WHITE);

        Object[] fields = { "Full Name:", nameF, "Department:", deptCombo, "Courses:", courF, "Email:", emailF, "Mobile:", mobF };
        if (JOptionPane.showConfirmDialog(this, fields, "New Lecturer", 0) == 0) {
            String deptIdStr = String.valueOf(deptMap.get((String) deptCombo.getSelectedItem()));
            if (lectureDAO.addLecturer(nameF.getText(), deptIdStr, courF.getText(), emailF.getText(), mobF.getText())) {
                refreshTable();
            }
        }
    }

    private void showEditLecturerDialog() {
        int row = lecturerTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a lecturer to edit."); return; }

        String originalEmail = (String) lecturerTableModel.getValueAt(row, 3);
        JTextField nameF = createStyledField();
        nameF.setText((String) lecturerTableModel.getValueAt(row, 0));
        JTextField courF = createStyledField();
        courF.setText((String) lecturerTableModel.getValueAt(row, 2));
        JTextField emailF = createStyledField();
        emailF.setText(originalEmail);
        JTextField mobF = createStyledField();
        mobF.setText((String) lecturerTableModel.getValueAt(row, 4));

        java.util.Map<String, Integer> deptMap = lectureDAO.getDepartmentMap();
        JComboBox<String> deptCombo = new JComboBox<>(deptMap.keySet().toArray(new String[0]));
        deptCombo.setSelectedItem(lecturerTableModel.getValueAt(row, 1));
        deptCombo.setBackground(CLR_FIELD_BG);
        deptCombo.setForeground(CLR_WHITE);

        Object[] fields = { "Full Name:", nameF, "Department:", deptCombo, "Courses:", courF, "Email:", emailF, "Mobile:", mobF };
        if (JOptionPane.showConfirmDialog(this, fields, "Edit Lecturer", 0) == 0) {
            String deptIdStr = String.valueOf(deptMap.get((String) deptCombo.getSelectedItem()));
            if (lectureDAO.updateLecturer(nameF.getText(), deptIdStr, courF.getText(), emailF.getText(), mobF.getText(), originalEmail)) {
                refreshTable();
            }
        }
    }

    private void deleteSelectedRow() {
        int row = lecturerTable.getSelectedRow();
        if (row != -1) {
            String email = (String) lecturerTableModel.getValueAt(row, 3);
            if (JOptionPane.showConfirmDialog(this, "Delete " + email + "?", "Confirm", 0) == 0) {
                if (lectureDAO.deleteLecturer(email)) refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
        }
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(CLR_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createRoundedButton(String text, Dimension size, Color bg, int fontSize) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2.setColor(getBackground() == CLR_ACCENT ? CLR_BG_START : CLR_WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent())/2-2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(size);
        btn.setBackground(bg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField();
        field.setBackground(CLR_FIELD_BG);
        field.setForeground(CLR_WHITE);
        field.setCaretColor(CLR_WHITE);
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LecturerDashboardView().setVisible(true));
    }

}
