import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class DepartmentDashboardView extends JFrame {

    // --- COLOR PALETTE (Sage Green Theme) ---
    private final Color CLR_BG = new Color(235, 233, 225);
    private final Color CLR_HEADER_BG = new Color(70, 75, 60);  // Dark Olive
    private final Color CLR_ACCENT = new Color(155, 150, 130);  // Sage
    private final Color CLR_NAV_BAR = new Color(225, 223, 215);
    private final Color CLR_SAVE_BTN = new Color(165, 82, 45);  // Terracotta

    // Fonts
    private final Font FONT_TITLE = new Font("Serif", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_CELL = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 13);

    // Data Components
    private DefaultTableModel departmentTableModel;
    private JTable departmentTable;

    // --- Constructor ---
    public DepartmentDashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Department Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        // Main container
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(CLR_BG);
        setContentPane(rootPanel);

        // 1. Add navigation bar
        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);

        // 2. Add departments content area
        rootPanel.add(createDepartmentsContent(), BorderLayout.CENTER);
    }

    // =========================================================
    // 1. NAVIGATION BAR
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

        // Navigation Buttons
        JButton btnStudents = createNavButton("Students");
        JButton btnLecturers = createNavButton("Lecturers");
        JButton btnDepartments = createNavButton("Departments");
        JButton btnDegrees = createNavButton("Degrees");
        JButton btnLogout = createNavButton("Logout");
        btnLogout.setForeground(new Color(180, 50, 50));

        // --- NAVIGATION LOGIC ---
        btnDegrees.addActionListener(e -> {
            new DegreeDashboardView().setVisible(true); // Open Degrees page
            this.dispose(); // Close current Department Dashboard
        });

        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.dispose(); // Close application or return to login
            }
        });

        // Add buttons to panel
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
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setForeground(CLR_ACCENT.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setForeground(CLR_HEADER_BG); }
        });
        return btn;
    }

    // =========================================================
    // 2. CONTENT AREA (DEPARTMENTS LOGIC)
    // =========================================================
    private JPanel createDepartmentsContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CLR_BG);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        // Title
        JLabel lblTitle = new JLabel("Departments", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_HEADER_BG);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(CLR_BG);

        // Controls (Add/Edit/Delete)
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

        // Table
        centerContainer.add(createDepartmentTable(), BorderLayout.CENTER);
        panel.add(centerContainer, BorderLayout.CENTER);

        // Save Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(CLR_BG);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnSave = new JButton("Save changes");
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(CLR_SAVE_BTN);
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(this, "Changes saved successfully!"));

        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================
    // 3. LOGIC IMPLEMENTATION
    // =========================================================
    private void deleteSelectedRow() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow != -1) {
            if (JOptionPane.showConfirmDialog(this, "Delete selected department?", "Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                departmentTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    private void showAddDepartmentDialog() {
        JTextField txtName = new JTextField();
        JTextField txtHOD = new JTextField();
        JTextField txtDegree = new JTextField();
        JTextField txtStaff = new JTextField();
        Object[] message = {"Department Name:", txtName, "Head of Dept (HOD):", txtHOD, "Related Degree:", txtDegree, "No of Staff:", txtStaff};

        if (JOptionPane.showConfirmDialog(this, message, "Add New Department", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (!txtName.getText().isEmpty()) {
                departmentTableModel.addRow(new Object[]{txtName.getText(), txtHOD.getText(), txtDegree.getText(), txtStaff.getText()});
            }
        }
    }

    private void showEditDepartmentDialog() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }
        JTextField txtName = new JTextField((String) departmentTableModel.getValueAt(selectedRow, 0));
        JTextField txtHOD = new JTextField((String) departmentTableModel.getValueAt(selectedRow, 1));
        JTextField txtDegree = new JTextField((String) departmentTableModel.getValueAt(selectedRow, 2));
        JTextField txtStaff = new JTextField((String) departmentTableModel.getValueAt(selectedRow, 3));
        Object[] message = {"Department Name:", txtName, "Head of Dept (HOD):", txtHOD, "Related Degree:", txtDegree, "No of Staff:", txtStaff};

        if (JOptionPane.showConfirmDialog(this, message, "Edit Department", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            departmentTableModel.setValueAt(txtName.getText(), selectedRow, 0);
            departmentTableModel.setValueAt(txtHOD.getText(), selectedRow, 1);
            departmentTableModel.setValueAt(txtDegree.getText(), selectedRow, 2);
            departmentTableModel.setValueAt(txtStaff.getText(), selectedRow, 3);
        }
    }

    // =========================================================
    // 4. TABLE CONFIGURATION
    // =========================================================
    private JScrollPane createDepartmentTable() {
        String[] columns = {"Name", "HOD", "Degree", "No of Staff"};
        Object[][] data = {
                {"Applied Computing", "Kumar Sanga", "Engineering Technology", "15"},
                {"Software Engineering", "Kumar Sanga", "Information Technology", "17"},
                {"Computer Systems Engineering", "Kumar Sanga", "Computer Science", "12"}
        };

        departmentTableModel = new DefaultTableModel(data, columns) {
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

        JScrollPane scrollPane = new JScrollPane(departmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(CLR_ACCENT, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private JButton createActionButton(String text, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isPrimary) {
            btn.setBackground(CLR_HEADER_BG);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        } else {
            btn.setBackground(CLR_ACCENT);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        }
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

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DepartmentDashboardView().setVisible(true));
    }
}
