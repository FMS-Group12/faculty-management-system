import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class DepartmentDashboardView extends JFrame {

    // --- DATABASE CONFIGURATION ---
    private final String DB_URL = "jdbc:mysql://localhost:3306/faculty_management_system";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    // --- COLOR PALETTE (Sage Green Theme) ---
    private final Color CLR_BG = new Color(235, 233, 225);
    private final Color CLR_HEADER_BG = new Color(70, 75, 60);
    private final Color CLR_ACCENT = new Color(155, 150, 130);
    private final Color CLR_NAV_BAR = new Color(225, 223, 215);
    private final Color CLR_SAVE_BTN = new Color(165, 82, 45);

    // Fonts
    private final Font FONT_TITLE = new Font("Serif", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_CELL = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 13);

    // Data Components
    private DefaultTableModel departmentTableModel;
    private JTable departmentTable;

    public DepartmentDashboardView() {
        initializeUI();
        loadDepartmentsFromDB(); // <--- Loads data immediately on startup
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Department Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(CLR_BG);
        setContentPane(rootPanel);

        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);
        rootPanel.add(createDepartmentsContent(), BorderLayout.CENTER);
    }

    // =========================================================
    // 1. BACKEND LOGIC (DATABASE OPERATIONS)
    // =========================================================

    // --- LOAD (READ) ---
    private void loadDepartmentsFromDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM departments")) {

            departmentTableModel.setRowCount(0); // Clear current table data

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("department_id")); // ID (Hidden or visible)
                row.add(rs.getString("name"));
                row.add(rs.getString("hod"));
                row.add(rs.getInt("no_of_staff"));
                departmentTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    // --- ADD (INSERT) ---
    private void addDepartmentToDB(String name, String hod, int staff) {
        String sql = "INSERT INTO departments (name, hod, no_of_staff) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, hod);
            pstmt.setInt(3, staff);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Department Added Successfully!");
            loadDepartmentsFromDB(); // Refresh the table

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding: " + e.getMessage());
        }
    }

    // --- EDIT (UPDATE) ---
    private void updateDepartmentInDB(int id, String name, String hod, int staff) {
        String sql = "UPDATE departments SET name=?, hod=?, no_of_staff=? WHERE department_id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, hod);
            pstmt.setInt(3, staff);
            pstmt.setInt(4, id);

            int rows = pstmt.executeUpdate();
            if(rows > 0) {
                JOptionPane.showMessageDialog(this, "Department Updated Successfully!");
                loadDepartmentsFromDB(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating: " + e.getMessage());
        }
    }

    // --- DELETE (DELETE) ---
    private void deleteDepartmentFromDB(int id) {
        String sql = "DELETE FROM departments WHERE department_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Department Deleted!");
            loadDepartmentsFromDB(); // Refresh the table

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting: " + e.getMessage());
        }
    }

    // =========================================================
    // 2. UI ACTIONS (Dialogs & Buttons)
    // =========================================================

    private void showAddDepartmentDialog() {
        JTextField txtName = new JTextField();
        JTextField txtHOD = new JTextField();
        JTextField txtStaff = new JTextField();

        Object[] message = {
                "Department Name:", txtName,
                "Head of Dept (HOD):", txtHOD,
                "No of Staff:", txtStaff
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Department", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                if (txtName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name is required!");
                    return;
                }
                String name = txtName.getText();
                String hod = txtHOD.getText();
                int staff = Integer.parseInt(txtStaff.getText()); // Converts text to number

                addDepartmentToDB(name, hod, staff); // <--- CALL BACKEND

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Staff count must be a valid number!");
            }
        }
    }

    private void showEditDepartmentDialog() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }

        // Get current values from the table to fill the inputs
        int id = (int) departmentTable.getValueAt(selectedRow, 0); // ID is in column 0
        String currentName = (String) departmentTable.getValueAt(selectedRow, 1);
        String currentHOD = (String) departmentTable.getValueAt(selectedRow, 2);
        String currentStaff = String.valueOf(departmentTable.getValueAt(selectedRow, 3));

        JTextField txtName = new JTextField(currentName);
        JTextField txtHOD = new JTextField(currentHOD);
        JTextField txtStaff = new JTextField(currentStaff);

        Object[] message = {
                "Department Name:", txtName,
                "Head of Dept (HOD):", txtHOD,
                "No of Staff:", txtStaff
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Department", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = txtName.getText();
                String hod = txtHOD.getText();
                int staff = Integer.parseInt(txtStaff.getText());

                updateDepartmentInDB(id, name, hod, staff); // <--- CALL BACKEND

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Staff count must be a valid number!");
            }
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get the ID from column 0
            int id = (int) departmentTable.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Delete Department ID: " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteDepartmentFromDB(id); // <--- CALL BACKEND
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    // =========================================================
    // 3. UI LAYOUT
    // =========================================================

    private JPanel createDepartmentsContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CLR_BG);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitle = new JLabel("Departments", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_HEADER_BG);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(CLR_BG);

        // Control Buttons
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setBackground(CLR_BG);
        controlsPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton btnAdd = createActionButton("Add new", true);
        JButton btnEdit = createActionButton("Edit", false);
        JButton btnDelete = createActionButton("Delete", false);

        btnAdd.addActionListener(e -> showAddDepartmentDialog());
        btnEdit.addActionListener(e -> showEditDepartmentDialog());
        btnDelete.addActionListener(e -> deleteSelectedRow());

        controlsPanel.add(btnAdd);
        controlsPanel.add(btnEdit);
        controlsPanel.add(btnDelete);
        centerContainer.add(controlsPanel, BorderLayout.NORTH);

        centerContainer.add(createDepartmentTable(), BorderLayout.CENTER);
        panel.add(centerContainer, BorderLayout.CENTER);

        // Save Button (Not strictly needed since Add/Edit save instantly, but kept for UI consistency)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(CLR_BG);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        JButton btnSave = new JButton("Refresh Data");
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(CLR_SAVE_BTN);
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> loadDepartmentsFromDB());
        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JScrollPane createDepartmentTable() {
        // Updated Columns: "Degree" is removed, "ID" is added
        String[] columns = {"ID", "Name", "HOD", "No of Staff"};

        departmentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        departmentTable = new JTable(departmentTableModel);
        departmentTable.setRowHeight(45);
        departmentTable.setFont(FONT_CELL);
        departmentTable.setShowGrid(false);
        departmentTable.setIntercellSpacing(new Dimension(0, 0));
        departmentTable.setBackground(Color.WHITE);
        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        departmentTable.setFillsViewportHeight(true);

        JTableHeader header = departmentTable.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setBackground(CLR_BG);
        header.setPreferredSize(new Dimension(0, 50));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < departmentTable.getColumnCount(); i++) {
            departmentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Make ID column small
        departmentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        departmentTable.getColumnModel().getColumn(1).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(departmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(CLR_ACCENT, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    // --- NAVIGATION & STYLING ---
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
        btnLogout.setForeground(new Color(180, 50, 50));

        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION) dispose();
        });

        // Navigation logic (Add your other page classes here)
        btnDegrees.addActionListener(e -> {
            // new DegreeDashboardView().setVisible(true);
            // dispose();
        });

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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createActionButton(String text, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isPrimary) {
            btn.setBackground(CLR_HEADER_BG);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(CLR_ACCENT);
            btn.setForeground(Color.WHITE);
        }
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    // --- RENDERERS ---
    private class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBackground(CLR_BG);
            return new PillHeaderPanel(value.toString());
        }
    }

    private class PillHeaderPanel extends JPanel {
        private String text;
        public PillHeaderPanel(String text) { this.text = text; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CLR_HEADER_BG);
            g2.fillRoundRect(2, 5, getWidth() - 4, getHeight() - 10, 20, 20);
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics(FONT_HEADER);
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.setFont(FONT_HEADER);
            g2.drawString(text, x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DepartmentDashboardView().setVisible(true));
    }
}
