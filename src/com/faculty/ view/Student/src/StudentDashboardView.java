import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class StudentDashboardView extends JFrame {

    // --- COLOR PALETTE ---
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

    // Table
    private DefaultTableModel studentTableModel;
    private JTable studentTable;

    public StudentDashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(CLR_BG);
        setContentPane(rootPanel);

        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);
        rootPanel.add(createStudentsContent(), BorderLayout.CENTER);
    }

    // =========================================================
    // NAV BAR
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

        buttonPanel.add(createNavButton("Students"));
        buttonPanel.add(createNavButton("Lecturers"));
        buttonPanel.add(createNavButton("Departments"));
        buttonPanel.add(createNavButton("Degrees"));

        // Active Logout button
        JButton btnLogout = createNavButton("Logout");
        btnLogout.addActionListener(e -> logoutAction());
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

    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            // If you have SignIn page, uncomment next line:
            // new SignInPage().setVisible(true);
        }
    }

    // =========================================================
    // STUDENT CONTENT
    // =========================================================
    private JPanel createStudentsContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CLR_BG);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitle = new JLabel("Students", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_HEADER_BG);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Center panel with BoxLayout
        Box centerBox = Box.createVerticalBox();
        centerBox.setBackground(CLR_BG);

        // Buttons panel
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(CLR_BG);
        controls.setBorder(new EmptyBorder(0, 0, 20, 0));

        JButton btnAdd = createActionButton("Add new", true);
        JButton btnEdit = createActionButton("Edit", false);
        JButton btnDelete = createActionButton("Delete", false);

        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        controls.add(btnAdd);
        controls.add(btnEdit);
        controls.add(btnDelete);

        centerBox.add(controls);
        centerBox.add(createStudentTable());

        panel.add(centerBox, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(CLR_BG);
        bottom.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnSave = new JButton("Save changes");
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(CLR_SAVE_BTN);
        btnSave.setPreferredSize(new Dimension(200, 40));
        bottom.add(btnSave);

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // =========================================================
    // TABLE
    // =========================================================
    private JScrollPane createStudentTable() {
        String[] columns = {"Full Name", "Student ID", "Degree", "Email", "Mobile Number"};

        Object[][] data = {
                {"Kumar Sangakkara", "ET/2022/007", "Engineering Technology", "kumars@kln.ac.lk", "0123456789"},
                {"Kumar Sangakkara", "ET/2022/007", "Engineering Technology", "kumars@kln.ac.lk", "0123456789"},
                {"Kumar Sangakkara", "ET/2022/007", "Engineering Technology", "kumars@kln.ac.lk", "0123456789"},
        };

        studentTableModel = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(45);
        studentTable.setFont(FONT_CELL);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setShowGrid(false);
        studentTable.setBackground(Color.WHITE);

        JTableHeader header = studentTable.getTableHeader();
        header.setPreferredSize(new Dimension(0, 50));
        header.setDefaultRenderer(new HeaderRenderer());

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(studentTable);
        scroll.setBorder(BorderFactory.createLineBorder(CLR_ACCENT));
        return scroll;
    }

    // =========================================================
    // CRUD METHODS
    // =========================================================
    private void addStudent() {
        JTextField n = new JTextField();
        JTextField id = new JTextField();
        JTextField d = new JTextField();
        JTextField e = new JTextField();
        JTextField m = new JTextField();

        Object[] msg = {"Full Name:", n, "Student ID:", id, "Degree:", d, "Email:", e, "Mobile:", m};

        if (JOptionPane.showConfirmDialog(this, msg, "Add Student",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            studentTableModel.addRow(new Object[]{
                    n.getText(), id.getText(), d.getText(), e.getText(), m.getText()
            });
        }
    }

    private void editStudent() {
        int r = studentTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Current values
        String currentName = (String) studentTableModel.getValueAt(r, 0);
        String currentID = (String) studentTableModel.getValueAt(r, 1);
        String currentDegree = (String) studentTableModel.getValueAt(r, 2);
        String currentEmail = (String) studentTableModel.getValueAt(r, 3);
        String currentMobile = (String) studentTableModel.getValueAt(r, 4);

        // Text fields prefilled
        JTextField n = new JTextField(currentName);
        JTextField id = new JTextField(currentID);
        JTextField d = new JTextField(currentDegree);
        JTextField e = new JTextField(currentEmail);
        JTextField m = new JTextField(currentMobile);

        Object[] msg = {"Full Name:", n, "Student ID:", id, "Degree:", d, "Email:", e, "Mobile:", m};

        if (JOptionPane.showConfirmDialog(this, msg, "Edit Student",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // Update table
            studentTableModel.setValueAt(n.getText(), r, 0);
            studentTableModel.setValueAt(id.getText(), r, 1);
            studentTableModel.setValueAt(d.getText(), r, 2);
            studentTableModel.setValueAt(e.getText(), r, 3);
            studentTableModel.setValueAt(m.getText(), r, 4);
        }
    }

    private void deleteStudent() {
        int r = studentTable.getSelectedRow();
        if (r != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                studentTableModel.removeRow(r);
            }
        }
    }

    private JButton createActionButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(primary ? CLR_HEADER_BG : CLR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    // =========================================================
    // TABLE HEADER RENDERER
    // =========================================================
    private class HeaderRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean s, boolean f, int r, int c) {
            return new PillHeaderPanel(value.toString());
        }
    }

    private class PillHeaderPanel extends JPanel {
        String text;
        PillHeaderPanel(String t) { text = t; setOpaque(false); }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(CLR_HEADER_BG);
            g2.fillRoundRect(5, 8, getWidth() - 10, getHeight() - 16, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setFont(FONT_HEADER);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text,
                    (getWidth() - fm.stringWidth(text)) / 2,
                    (getHeight() + fm.getAscent()) / 2 - 2);
        }
    }

    // =========================================================
    // MAIN
    // =========================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboardView().setVisible(true));
    }
}
