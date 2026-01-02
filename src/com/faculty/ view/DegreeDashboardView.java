import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Vector;

public class DegreeDashboardView extends JFrame {

    // Inside DegreeDashboardView class
    private DegreeDAO degreeDAO = new DegreeDAO();
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

    private DefaultTableModel degreeTableModel;
    private JTable degreeTable;

    public DegreeDashboardView() {
        initializeUI();
    }
    private void initializeUI() {
        setTitle("Faculty Management System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        // Main Container
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(CLR_BG);
        setContentPane(rootPanel);

        // 1. ADD NAVIGATION BAR
        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);

        // 2. ADD DEPARTMENTS CONTENT (This View acts as the Departments Page)
        rootPanel.add(createDegreesContent(), BorderLayout.CENTER);
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
        JButton btnDegrees = createNavButton("Degrees");
        JButton btnDepartments = createNavButton("Departments");
        JButton btnLogout = createNavButton("Logout");
        btnLogout.setForeground(new Color(180, 50, 50));
        btnDepartments.addActionListener(e -> {
            // Logic to open Degrees JFrame would go here
            new DepartmentDashboardView().setVisible(true);
            this.dispose();
            //JOptionPane.showMessageDialog(this, "Navigate to Degrees Page");
        });

        btnLogout.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.dispose(); // Close Application or return to Login
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
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setForeground(CLR_ACCENT.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setForeground(CLR_HEADER_BG); }
        });
        return btn;
    }
    private JPanel createDegreesContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CLR_BG);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitle = new JLabel("Degrees", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_HEADER_BG);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(CLR_BG);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setBackground(CLR_BG);
        controlsPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton btnAdd = createActionButton("Add", true);
        JButton btnEdit = createActionButton("Edit", true);
        JButton btnDelete = createActionButton("Delete", true);

        btnAdd.addActionListener(e -> showAddDegreeDialog());
        btnEdit.addActionListener(e -> showEditDegreeDialog());
        btnDelete.addActionListener(e -> deleteSelectedRow());

        controlsPanel.add(btnAdd);
        controlsPanel.add(btnEdit);
        controlsPanel.add(btnDelete);
        centerContainer.add(controlsPanel, BorderLayout.NORTH);

        centerContainer.add(createDegreeTable(), BorderLayout.CENTER);
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
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(this, "Changes saved successfully!"));

        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
    private void showAddDegreeDialog() {
        JTextField txtDegree = new JTextField();
        JTextField txtDepartment = new JTextField();
        JTextField txtNoOfStudents = new JTextField();
        Object[] message = {"Degree Name:", txtDegree, "Department:", txtDepartment, "No of Students:", txtNoOfStudents};

        if (JOptionPane.showConfirmDialog(this, message, "Add Degree", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String name = txtDegree.getText();
            String dept = txtDepartment.getText();
            int students = Integer.parseInt(txtNoOfStudents.getText().trim());

            if (degreeDAO.addDegree(name, dept, students)) {
                degreeTableModel.addRow(new Object[]{name, dept, String.valueOf(students)});
            }
        }
    }

    private void showEditDegreeDialog() {
        int selectedRow = degreeTable.getSelectedRow();
        if (selectedRow == -1) return;

        String oldName = (String) degreeTableModel.getValueAt(selectedRow, 0);
        JTextField txtDegree = new JTextField(oldName);
        JTextField txtDept = new JTextField((String) degreeTableModel.getValueAt(selectedRow, 1));
        JTextField txtStudents = new JTextField(degreeTableModel.getValueAt(selectedRow, 2).toString());

        Object[] message = {"Degree Name:", txtDegree, "Department:", txtDept, "Students:", txtStudents};

        if (JOptionPane.showConfirmDialog(this, message, "Edit Degree", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (degreeDAO.updateDegree(oldName, txtDegree.getText(), txtDept.getText(), Integer.parseInt(txtStudents.getText()))) {
                degreeTableModel.setValueAt(txtDegree.getText(), selectedRow, 0);
                degreeTableModel.setValueAt(txtDept.getText(), selectedRow, 1);
                degreeTableModel.setValueAt(txtStudents.getText(), selectedRow, 2);
            }
        }
    }
    private void deleteSelectedRow() {
        int selectedRow = degreeTable.getSelectedRow();
        if (selectedRow != -1) {
            String degreeName = (String) degreeTableModel.getValueAt(selectedRow, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete " + degreeName + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (degreeDAO.deleteDegree(degreeName)) {
                    degreeTableModel.removeRow(selectedRow);
                }
            }
        }
    }
    private JScrollPane createDegreeTable() {
        String[] columns = {"Degree", "Department", "No of Students"};

        // Fetch data from Database instead of hardcoded array
        Vector<Vector<Object>> data = degreeDAO.getAllDegrees();

        degreeTableModel = new DefaultTableModel(data, new Vector<>(java.util.Arrays.asList(columns))) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        degreeTable = new JTable(degreeTableModel);
        degreeTable.setRowHeight(45);
        degreeTable.setFont(FONT_CELL);
        degreeTable.setShowGrid(false);
        degreeTable.setIntercellSpacing(new Dimension(0, 0));
        degreeTable.setBackground(Color.WHITE);
        degreeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        degreeTable.setFillsViewportHeight(true);

        JTableHeader header = degreeTable.getTableHeader();
        header.setDefaultRenderer(new DegreeDashboardView.HeaderRenderer());
        header.setBackground(CLR_BG);
        header.setPreferredSize(new Dimension(0, 50));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < degreeTable.getColumnCount(); i++) {
            degreeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(degreeTable);
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
    private class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBackground(CLR_BG);
            return new DegreeDashboardView.PillHeaderPanel(value.toString());
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
        SwingUtilities.invokeLater(() -> {
            new DegreeDashboardView().setVisible(true);
        });
    }
}
