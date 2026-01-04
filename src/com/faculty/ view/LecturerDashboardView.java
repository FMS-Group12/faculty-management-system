

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Map;

public class LecturerDashboardView extends JFrame {

    // --- COLOR PALETTE (Sage Green Theme) ---
    private final Color CLR_BG = new Color(235, 233, 225);
    private final Color CLR_HEADER_BG = new Color(70, 75, 60);  // Dark Olive
    private final Color CLR_ACCENT = new Color(155, 150, 130);  // Sage
    private final Color CLR_NAV_BAR = new Color(225, 223, 215);
    private final Color CLR_SAVE_BTN = new Color(165, 82, 45);  // Terracotta
    private final Color CLR_LOGOUT = new Color(180, 40, 40);    // Red

    // Fonts
    private final Font FONT_TITLE = new Font("Serif", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_CELL = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 13);

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
        setTitle("Faculty Management System - Lecturer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        // 1. Header and Nav
        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);

        // 2. Center Content (Table)
        JPanel centerPanel = createLecturersContent();
        rootPanel.add(centerPanel, BorderLayout.CENTER);

        // 3. Bottom Save Area
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(CLR_BG);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 40, 0));

        JButton btnSave = new JButton("Save changes");
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.setBackground(CLR_SAVE_BTN);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(this, "Data Saved Successfully!"));

        bottomPanel.add(btnSave);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);
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

        // Highlight Active Tab
        btnLecturers.setBorder(new MatteBorder(0, 0, 2, 0, CLR_HEADER_BG));

        // Style Logout Button
        btnLogout.setForeground(CLR_LOGOUT);

        // Navigation Actions
        btnStudents.addActionListener(e -> { new StudentDashboardView().setVisible(true); this.dispose(); });
        btnDepartments.addActionListener(e -> { new DepartmentDashboardView().setVisible(true); this.dispose(); });
        btnDegrees.addActionListener(e -> { new DegreeDashboardView().setVisible(true); this.dispose(); });

        // LOGOUT LOGIC: Return to Sign In View
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new SignInView().setVisible(true); // Switches to SignIn form
                this.dispose();                   // Closes current dashboard
            }
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
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        // UNIFORM BUTTON SIZES
        JButton btnAdd = createActionButton("Add new", true);
        JButton btnEdit = createActionButton("Edit", true);
        JButton btnDelete = createActionButton("Delete", true);

        btnAdd.addActionListener(e -> showAddLecturerDialog());
        btnEdit.addActionListener(e -> showEditLecturerDialog());
        btnDelete.addActionListener(e -> deleteSelectedRow());

        controls.add(btnAdd); controls.add(btnEdit); controls.add(btnDelete);
        centerContainer.add(controls, BorderLayout.NORTH);
        centerContainer.add(createLecturerTable(), BorderLayout.CENTER);

        panel.add(centerContainer, BorderLayout.CENTER);
        return panel;
    }

    // Placeholder Logic for Table/Dialogs
    private void showAddLecturerDialog() { /* logic here */ }
    private void showEditLecturerDialog() { /* logic here */ }

    private void deleteSelectedRow() {
        int selectedRow = lecturerTable.getSelectedRow();
        if (selectedRow != -1) {
            if (JOptionPane.showConfirmDialog(this, "Delete selected lecturer?", "Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                lecturerTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    private JScrollPane createLecturerTable() {
        String[] columns = {"Full Name", "Department", "Courses teaching", "Email", "Mobile Number"};
        lecturerTableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        lecturerTable = new JTable(lecturerTableModel);
        lecturerTable.setRowHeight(45);
        lecturerTable.setFont(FONT_CELL);
        lecturerTable.setShowGrid(false);
        lecturerTable.setFillsViewportHeight(true);

        JTableHeader header = lecturerTable.getTableHeader();
        header.setPreferredSize(new Dimension(0, 50));
        header.setDefaultRenderer(new HeaderRenderer());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < lecturerTable.getColumnCount(); i++) {
            lecturerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return new JScrollPane(lecturerTable);
    }

    private JButton createActionButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        // FIXED UNIFORM SIZE FOR ALL ACTION BUTTONS
        btn.setPreferredSize(new Dimension(110, 35));
        btn.setFont(FONT_BTN);
        btn.setBackground(primary ? CLR_HEADER_BG : CLR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return new PillHeaderPanel(value.toString());
        }
    }

    private class PillHeaderPanel extends JPanel {
        private String text;
        PillHeaderPanel(String text) { this.text = text; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CLR_HEADER_BG);
            g2.fillRoundRect(2, 5, getWidth() - 4, getHeight() - 10, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setFont(FONT_HEADER);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LecturerDashboardView().setVisible(true));
    }
}
