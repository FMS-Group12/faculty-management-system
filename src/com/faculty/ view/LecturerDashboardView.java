

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class LecturerDashboardView extends JFrame {

    private final Color CLR_BG = new Color(235, 233, 225);
    private final Color CLR_HEADER_BG = new Color(70, 75, 60);
    private final Color CLR_ACCENT = new Color(155, 150, 130);
    private final Color CLR_NAV_BAR = new Color(225, 223, 215);
    private final Color CLR_SAVE_BTN = new Color(165, 82, 45);

    private final Font FONT_TITLE = new Font("Serif", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_CELL = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 13);

    private DefaultTableModel lecturerTableModel;
    private JTable lecturerTable;
    private LectureDAO lectureDAO = new LectureDAO(); // Initialize DAO

    public LecturerDashboardView() {
        initializeUI();
        refreshTable(); // Load data on startup
    }

    private void refreshTable() {
        lectureDAO.getAllLecturers(lecturerTableModel);
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Lecturer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        // Header and Nav
        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);

        // Center Content (Table)
        JPanel centerPanel = createLecturersContent();
        rootPanel.add(centerPanel, BorderLayout.CENTER);

        // --- SAVE CHANGES BUTTON (PURPLE THEME) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(235, 233, 225));
        bottomPanel.setBorder(new javax.swing.border.EmptyBorder(10, 0, 40, 0));

        JButton btnSave = new JButton("Save changes");
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.setBackground(new Color(162, 82, 45)); // Purple theme
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(this, "Data Saved Successfully!"));

        bottomPanel.add(btnSave);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH); // Positioned at the bottom
    }

    private JPanel createTopNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(CLR_NAV_BAR);
        navPanel.setBorder(new MatteBorder(0, 0, 1, 0, CLR_ACCENT));
        navPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel lblWelcome = new JLabel("  Welcome, Admin");
        lblWelcome.setFont(FONT_NAV);
        lblWelcome.setForeground(CLR_HEADER_BG);
        navPanel.add(lblWelcome, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(CLR_NAV_BAR);

        JButton btnStudents = createNavButton("Students");
        JButton btnLecturers = createNavButton("Lecturers");
        JButton btnDepartments = createNavButton("Departments");
        JButton btnDegrees = createNavButton("Degrees");
        JButton btnLogout = createNavButton("Logout");

        btnStudents.addActionListener(e -> { new StudentDashboardView().setVisible(true); this.dispose(); });
        btnDepartments.addActionListener(e -> { new DepartmentDashboardView().setVisible(true); this.dispose(); });
        btnDegrees.addActionListener(e -> { new DegreeDashboardView().setVisible(true); this.dispose(); });

        buttonPanel.add(btnStudents);
        buttonPanel.add(btnLecturers);
        buttonPanel.add(btnDepartments);
        buttonPanel.add(btnDegrees);
        buttonPanel.add(btnLogout);

        navPanel.add(buttonPanel, BorderLayout.EAST);
        return navPanel;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);
        btn.setForeground(CLR_HEADER_BG);
        btn.setBackground(CLR_NAV_BAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        return btn;
    }

    private JPanel createLecturersContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CLR_BG);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitle = new JLabel("Lecturers", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_HEADER_BG);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(CLR_BG);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controls.setBackground(CLR_BG);

        JButton btnAdd = createActionButton("Add new", true);
        JButton btnEdit = createActionButton("Edit", false);
        JButton btnDelete = createActionButton("Delete", false);

        btnAdd.addActionListener(e -> showAddLecturerDialog());
        btnEdit.addActionListener(e -> showEditLecturerDialog());
        btnDelete.addActionListener(e -> deleteSelectedRow());

        controls.add(btnAdd); controls.add(btnEdit); controls.add(btnDelete);
        centerContainer.add(controls, BorderLayout.NORTH);
        centerContainer.add(createLecturerTable(), BorderLayout.CENTER);

        panel.add(centerContainer, BorderLayout.CENTER);
        return panel;
    }

    private void showAddLecturerDialog() {
        JTextField nameF = new JTextField();
        JTextField deptIdF = new JTextField(); // Input the ID (e.g., 1, 2, or 3)
        JTextField courF = new JTextField();
        JTextField emailF = new JTextField();
        JTextField mobF = new JTextField();

        Object[] fields = {
                "Full Name:", nameF,
                "Department:", deptIdF, // User enters the ID here
                "Courses:", courF,
                "Email:", emailF,
                "Mobile No:", mobF
        };

        if (JOptionPane.showConfirmDialog(null, fields, "Add New Lecturer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // Send data to DAO
            boolean success = lectureDAO.addLecturer(
                    nameF.getText(),
                    deptIdF.getText(),
                    courF.getText(),
                    emailF.getText(),
                    mobF.getText()
            );

            if (success) {
                refreshTable(); // The Join in the DAO will now show the Name for this ID
                JOptionPane.showMessageDialog(this, "Lecturer added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error: Check if Department ID exists.");
            }
        }
    }

    private void showEditLecturerDialog() {
        int row = lecturerTable.getSelectedRow();
        if (row == -1) return;

        String oldEmail = (String) lecturerTableModel.getValueAt(row, 3);
        JTextField nameF = new JTextField((String) lecturerTableModel.getValueAt(row, 0));
        JTextField deptF = new JTextField((String) lecturerTableModel.getValueAt(row, 1));
        JTextField courF = new JTextField((String) lecturerTableModel.getValueAt(row, 2));
        JTextField emailF = new JTextField(oldEmail);
        JTextField mobF = new JTextField((String) lecturerTableModel.getValueAt(row, 4));

        Object[] fields = {"Name:", nameF, "Dept:", deptF, "Course:", courF, "Email:", emailF, "Mobile:", mobF};

        if (JOptionPane.showConfirmDialog(null, fields, "Edit Lecturer", 2) == 0) {
            if (lectureDAO.updateLecturer(nameF.getText(), deptF.getText(), courF.getText(), emailF.getText(), mobF.getText(), oldEmail)) {
                refreshTable();
            }
        }
    }

    private void deleteSelectedRow() {
        int row = lecturerTable.getSelectedRow();
        if (row == -1) return;
        String email = (String) lecturerTableModel.getValueAt(row, 3);
        if (JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", 0) == 0) {
            if (lectureDAO.deleteLecturer(email)) refreshTable();
        }
    }

    // =========================================================
    // UPDATED TABLE METHOD
    // =========================================================
    private JScrollPane createLecturerTable() {
        String[] columns = {"Full Name", "Department", "Courses teaching", "Email", "Mobile Number"};

        // 1. Initialize the model with column names
        lecturerTableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // 2. Link the model to the table
        lecturerTable = new JTable(lecturerTableModel);
        lecturerTable.setRowHeight(45);
        lecturerTable.setFont(FONT_CELL);
        lecturerTable.setShowGrid(false);
        lecturerTable.setBackground(Color.WHITE);

        // 3. Apply Rounded Header (Pill Style)
        JTableHeader header = lecturerTable.getTableHeader();
        header.setPreferredSize(new Dimension(0, 50));
        header.setDefaultRenderer(new HeaderRenderer());

        // 4. Center align text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < lecturerTable.getColumnCount(); i++) {
            lecturerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return new JScrollPane(lecturerTable);
    }
    // =========================================================
    // CUSTOM ROUNDED HEADER CLASSES
    // =========================================================
    private class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // Returns the rounded panel instead of the default label
            return new PillHeaderPanel(value.toString());
        }
    }

    private class PillHeaderPanel extends JPanel {
        private String text;

        PillHeaderPanel(String text) {
            this.text = text;
            setOpaque(false); // Make background transparent to show the "pill"
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the Dark Olive Rounded Box
            g2.setColor(CLR_HEADER_BG);
            // 5px margin from top/bottom and 2px from sides
            g2.fillRoundRect(2, 5, getWidth() - 4, getHeight() - 10, 20, 20);

            // Draw the White Text
            g2.setColor(Color.WHITE);
            g2.setFont(FONT_HEADER);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, x, y);
        }
    }

    private JButton createActionButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setBackground(primary ? CLR_HEADER_BG : CLR_ACCENT);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LecturerDashboardView().setVisible(true));
    }

}

