package view; // Matches your folder 'view'

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LecturerDashboardView extends JFrame {

    // --- YOUR COLOR PALETTE (Sage Green Theme) ---
    private final Color CLR_BG = new Color(235, 233, 225);
    private final Color CLR_HEADER_BG = new Color(70, 75, 60);  // Dark Olive
    private final Color CLR_ACCENT = new Color(155, 150, 130);  // Sage
    private final Color CLR_NAV_BAR = new Color(225, 223, 215);
    private final Color CLR_SAVE_BTN = new Color(165, 82, 45);  // Terracotta

    // --- YOUR FONTS ---
    private final Font FONT_TITLE = new Font("Serif", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_CELL = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 13);

    private DefaultTableModel lecturerTableModel;
    private JTable lecturerTable;

    public LecturerDashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Lecturer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(CLR_BG);
        setContentPane(rootPanel);

        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);
        rootPanel.add(createLecturersContent(), BorderLayout.CENTER);
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

        buttonPanel.add(createNavButton("Students"));
        buttonPanel.add(createNavButton("Lecturers"));
        buttonPanel.add(createNavButton("Departments"));
        buttonPanel.add(createNavButton("Degrees"));

        JButton btnLogout = createNavButton("Logout");
        btnLogout.setForeground(new Color(180, 50, 50));
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });
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

    private JPanel createLecturersContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CLR_BG);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitle = new JLabel("Lecturers", SwingConstants.CENTER);
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

        btnAdd.addActionListener(e -> showAddLecturerDialog());
        btnEdit.addActionListener(e -> showEditLecturerDialog());
        btnDelete.addActionListener(e -> deleteSelectedRow());

        controlsPanel.add(btnAdd);
        controlsPanel.add(btnEdit);
        controlsPanel.add(btnDelete);
        centerContainer.add(controlsPanel, BorderLayout.NORTH);

        centerContainer.add(createLecturerTable(), BorderLayout.CENTER);
        panel.add(centerContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(CLR_BG);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnSave = new JButton("Save changes");
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(CLR_SAVE_BTN);
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> {
            if (lecturerTable.isEditing()) lecturerTable.getCellEditor().stopCellEditing();
            JOptionPane.showMessageDialog(this, "Changes saved successfully!");
        });

        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showEditLecturerDialog() {
        int row = lecturerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lecturer to edit.");
            return;
        }

        JTextField txtName = new JTextField((String) lecturerTableModel.getValueAt(row, 0));
        JTextField txtDept = new JTextField((String) lecturerTableModel.getValueAt(row, 1));
        JTextField txtCourse = new JTextField((String) lecturerTableModel.getValueAt(row, 2));
        JTextField txtEmail = new JTextField((String) lecturerTableModel.getValueAt(row, 3));
        JTextField txtMobile = new JTextField((String) lecturerTableModel.getValueAt(row, 4));

        Object[] message = {"Full Name:", txtName, "Department:", txtDept, "Courses:", txtCourse, "Email:", txtEmail, "Mobile:", txtMobile};

        if (JOptionPane.showConfirmDialog(this, message, "Edit Lecturer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            lecturerTableModel.setValueAt(txtName.getText(), row, 0);
            lecturerTableModel.setValueAt(txtDept.getText(), row, 1);
            lecturerTableModel.setValueAt(txtCourse.getText(), row, 2);
            lecturerTableModel.setValueAt(txtEmail.getText(), row, 3);
            lecturerTableModel.setValueAt(txtMobile.getText(), row, 4);
        }
    }

    private void showAddLecturerDialog() {
        JTextField f1 = new JTextField(); JTextField f2 = new JTextField();
        JTextField f3 = new JTextField(); JTextField f4 = new JTextField();
        JTextField f5 = new JTextField();
        Object[] fields = {"Full Name:", f1, "Department:", f2, "Courses:", f3, "Email:", f4, "Mobile:", f5};
        if (JOptionPane.showConfirmDialog(this, fields, "Add Lecturer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            lecturerTableModel.addRow(new Object[]{f1.getText(), f2.getText(), f3.getText(), f4.getText(), f5.getText()});
        }
    }

    private void deleteSelectedRow() {
        int row = lecturerTable.getSelectedRow();
        if (row != -1 && JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            lecturerTableModel.removeRow(row);
        }
    }

    private JScrollPane createLecturerTable() {
        String[] columns = {"Full Name", "Department", "Courses teaching", "Email", "Mobile Number"};
        Object[][] data = {
                {"Kumar Sangakkara", "Software Engineering", "ETEC 21062", "kumars@kln.ac.lk", "0123456789"},
                {"Kumar Sangakkara", "Software Engineering", "CSCI 21052", "kumars@kln.ac.lk", "0123456789"},
                {"Kumar Sangakkara", "Software Engineering", "CSCI 21042", "kumars@kln.ac.lk", "0123456789"},
                {"Mithali Raj", "Applied Computing", "ETEC 21022", "mithalir@kln.ac.lk", "9876543210"}
        };

        lecturerTableModel = new DefaultTableModel(data, columns);
        lecturerTable = new JTable(lecturerTableModel);
        lecturerTable.setRowHeight(45);
        lecturerTable.setFont(FONT_CELL);
        lecturerTable.setBackground(Color.WHITE);

        // --- FIXED BORDER COLORS HERE ---
        lecturerTable.setShowGrid(false);            // Make grid visible
        lecturerTable.setGridColor(CLR_ACCENT);    // Set grid line color to Sage
        lecturerTable.setIntercellSpacing(new Dimension(1, 1)); // Set gap between cells

        JTableHeader header = lecturerTable.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new Dimension(0, 50));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < lecturerTable.getColumnCount(); i++) {
            lecturerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return new JScrollPane(lecturerTable);
    }

    private JButton createActionButton(String text, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(isPrimary ? CLR_HEADER_BG : CLR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
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
        SwingUtilities.invokeLater(() -> new LecturerDashboardView().setVisible(true));
    }
}