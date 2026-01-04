import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Vector;

public class DepartmentDashboardView extends JFrame {

    // --- CONNECT TO DAO ---
    private DepartmentDAO dao = new DepartmentDAO();

    // --- COLORS & FONTS ---
    private final Color CLR_BG = new Color(235, 233, 225);
    private final Color CLR_HEADER_BG = new Color(70, 75, 60);
    private final Color CLR_ACCENT = new Color(155, 150, 130);
    private final Color CLR_NAV_BAR = new Color(225, 223, 215);

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
        refreshTableData();
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

    // --- HELPER: REFRESH TABLE ---
    private void refreshTableData() {
        departmentTableModel.setRowCount(0);
        Vector<Vector<Object>> data = dao.getAllDepartments();
        for (Vector<Object> row : data) {
            departmentTableModel.addRow(row);
        }
    }

    // =========================================================
    // UI ACTIONS
    // =========================================================

    private void showAddDepartmentDialog() {
        JTextField txtName = new JTextField();
        JTextField txtHOD = new JTextField();
        JTextField txtStaff = new JTextField();

        Object[] message = { "Department Name:", txtName, "Head of Dept (HOD):", txtHOD, "No of Staff:", txtStaff };

        if (JOptionPane.showConfirmDialog(this, message, "Add Department", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                if (txtName.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Name is required!"); return; }

                boolean success = dao.addDepartment(txtName.getText(), txtHOD.getText(), Integer.parseInt(txtStaff.getText()));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Added Successfully!");
                    refreshTableData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Staff count must be a number!");
            }
        }
    }

    private void showEditDepartmentDialog() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Please select a row to edit."); return; }

        int id = (int) departmentTable.getValueAt(selectedRow, 0);
        String currentName = (String) departmentTable.getValueAt(selectedRow, 1);
        String currentHOD = (String) departmentTable.getValueAt(selectedRow, 2);
        String currentStaff = String.valueOf(departmentTable.getValueAt(selectedRow, 3));

        JTextField txtName = new JTextField(currentName);
        JTextField txtHOD = new JTextField(currentHOD);
        JTextField txtStaff = new JTextField(currentStaff);

        Object[] message = { "Department Name:", txtName, "Head of Dept (HOD):", txtHOD, "No of Staff:", txtStaff };

        if (JOptionPane.showConfirmDialog(this, message, "Edit Department", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                boolean success = dao.updateDepartment(id, txtName.getText(), txtHOD.getText(), Integer.parseInt(txtStaff.getText()));
                if(success) {
                    JOptionPane.showMessageDialog(this, "Updated Successfully!");
                    refreshTableData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Number format!");
            }
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) departmentTable.getValueAt(selectedRow, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete Department ID: " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                boolean success = dao.deleteDepartment(id);
                if(success) {
                    JOptionPane.showMessageDialog(this, "Deleted Successfully!");
                    refreshTableData();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    // =========================================================
    // UI LAYOUT
    // =========================================================

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

        // --- NAVIGATION BUTTONS ---
        JButton btnStudents = createNavButton("Students");
        JButton btnLecturers = createNavButton("Lecturers");
        JButton btnCourses = createNavButton("Courses");
        JButton btnDepartments = createNavButton("Departments");
        JButton btnDegrees = createNavButton("Degrees");
        JButton btnLogout = createNavButton("Logout");
        btnLogout.setForeground(new Color(180, 50, 50));

        // --- NAVIGATION LOGIC ---
        btnStudents.addActionListener(e -> {
            new StudentDashboardView().setVisible(true);
            dispose();
        });

        btnLecturers.addActionListener(e -> {
            new LecturerDashboardView().setVisible(true);
            dispose();
        });

        btnCourses.addActionListener(e -> {
            new CourseDashboardView().setVisible(true);
            dispose();
        });

        btnDepartments.addActionListener(e -> refreshTableData());

        btnDegrees.addActionListener(e -> {
            new DegreeDashboardView().setVisible(true);
            dispose();
        });

        // --- UPDATED LOGOUT LOGIC: GO TO SIGN IN VIEW ---
        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                new SignInView().setVisible(true); // <--- Opens your SignInView
                dispose();
            }
        });

        // Add to panel
        buttonPanel.add(btnStudents);
        buttonPanel.add(btnLecturers);
        buttonPanel.add(btnCourses);
        buttonPanel.add(btnDepartments);
        buttonPanel.add(btnDegrees);
        buttonPanel.add(btnLogout);

        navPanel.add(buttonPanel, BorderLayout.EAST);
        return navPanel;
    }

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
        return panel;
    }

    private JScrollPane createDepartmentTable() {
        String[] columns = {"ID", "Name", "HOD", "No of Staff"};
        departmentTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
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

        departmentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        departmentTable.getColumnModel().getColumn(1).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(departmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(CLR_ACCENT, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
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
        btn.setBackground(isPrimary ? CLR_HEADER_BG : CLR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

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
