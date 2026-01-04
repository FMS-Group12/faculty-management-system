import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;
import java.util.Map;
import java.util.LinkedHashMap;

public class LecturerDashboardView extends JFrame {

    private LectureDAO lectureDAO = new LectureDAO();

    // --- MIDNIGHT GLASS PALETTE ---
    private final Color CLR_BG_START   = new Color(20, 24, 42);
    private final Color CLR_BG_END     = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG   = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT     = new Color(212, 175, 55);
    private final Color CLR_WHITE      = new Color(245, 245, 245);
    private final Color CLR_FIELD_BG   = new Color(45, 50, 75);
    private final Color CLR_NAV_BAR    = new Color(15, 18, 32);
    private final Color CLR_LOGOUT     = new Color(255, 80, 80);

    // Fonts
    private final Font FONT_TITLE  = new Font("Inter", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 16);
    private final Font FONT_CELL   = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV    = new Font("SansSerif", Font.BOLD, 18);

    private DefaultTableModel tableModel;
    private JTable lecturerTable;

    public LecturerDashboardView() {
        initializeUI();
        refreshTableData();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Lecturer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

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
        mainPanel.add(createLecturerContent(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 40, 0));

        JButton btnSave = createRoundedButton("Save Changes", new Dimension(220, 45), CLR_ACCENT, 12);
        btnSave.addActionListener(e -> {
            refreshTableData();
            JOptionPane.showMessageDialog(this, "Data Synchronized with Database!");
        });
        bottomPanel.add(btnSave);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        Vector<Vector<Object>> data = lectureDAO.getAllLecturers();
        for (Vector<Object> row : data) {
            tableModel.addRow(row);
        }
    }

    private void showAddLecturerDialog() {
        JTextField txtName = createStyledField();
        JTextField txtCourses = createStyledField();
        JTextField txtEmail = createStyledField();
        JTextField txtMobile = createStyledField();

        Map<String, Integer> deptMap = lectureDAO.getDepartmentMap();
        JComboBox<String> deptCombo = new JComboBox<>(deptMap.keySet().toArray(new String[0]));

        Object[] message = {
                "Full Name:", txtName,
                "Department:", deptCombo,
                "Courses:", txtCourses,
                "Email:", txtEmail,
                "Mobile:", txtMobile
        };

        if (JOptionPane.showConfirmDialog(this, message, "Add Lecturer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String selectedName = (String) deptCombo.getSelectedItem();
            String deptId = String.valueOf(deptMap.get(selectedName));

            if (lectureDAO.addLecturer(txtName.getText(), deptId, txtCourses.getText(), txtEmail.getText(), txtMobile.getText())) {
                JOptionPane.showMessageDialog(this, "Successfully Saved to Database!");
                refreshTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save. Check Database connection.");
            }
        }
    }

    private void showEditLecturerDialog() {
        int selectedRow = lecturerTable.getSelectedRow();
        if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Select a row!"); return; }

        String oldEmail = (String) lecturerTable.getValueAt(selectedRow, 3);

        JTextField txtName = createStyledField(); txtName.setText((String) lecturerTable.getValueAt(selectedRow, 0));
        JTextField txtCourses = createStyledField(); txtCourses.setText((String) lecturerTable.getValueAt(selectedRow, 2));
        JTextField txtEmail = createStyledField(); txtEmail.setText(oldEmail);
        JTextField txtMobile = createStyledField(); txtMobile.setText((String) lecturerTable.getValueAt(selectedRow, 4));

        Map<String, Integer> deptMap = lectureDAO.getDepartmentMap();
        JComboBox<String> deptCombo = new JComboBox<>(deptMap.keySet().toArray(new String[0]));
        deptCombo.setSelectedItem(lecturerTable.getValueAt(selectedRow, 1));

        Object[] message = {
                "Full Name:", txtName,
                "Department:", deptCombo,
                "Courses:", txtCourses,
                "Email:", txtEmail,
                "Mobile:", txtMobile
        };

        if (JOptionPane.showConfirmDialog(this, message, "Edit Lecturer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String selectedName = (String) deptCombo.getSelectedItem();
            String deptId = String.valueOf(deptMap.get(selectedName));

            if (lectureDAO.updateLecturer(txtName.getText(), deptId, txtCourses.getText(), txtEmail.getText(), txtMobile.getText(), oldEmail)) {
                JOptionPane.showMessageDialog(this, "Updated!");
                refreshTableData();
            }
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = lecturerTable.getSelectedRow();
        if (selectedRow != -1) {
            String email = (String) lecturerTable.getValueAt(selectedRow, 3);
            if (JOptionPane.showConfirmDialog(this, "Delete " + email + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if(lectureDAO.deleteLecturer(email)) {
                    JOptionPane.showMessageDialog(this, "Deleted!");
                    refreshTableData();
                }
            }
        } else { JOptionPane.showMessageDialog(this, "Select a row!"); }
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

        btnStudents.addActionListener(e -> { new StudentDashboardView().setVisible(true); dispose(); });
        btnLecturers.addActionListener(e -> refreshTableData());
        btnCourses.addActionListener(e -> { new CourseDashboardView().setVisible(true); dispose(); });
        btnDepartments.addActionListener(e -> { new DepartmentDashboardView().setVisible(true); dispose(); });
        btnDegrees.addActionListener(e -> { new DegreeDashboardView().setVisible(true); dispose(); });

        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new SignInView().setVisible(true);
                dispose();
            }
        });

        buttonPanel.add(btnStudents); buttonPanel.add(btnLecturers); buttonPanel.add(btnCourses);
        buttonPanel.add(btnDepartments); buttonPanel.add(btnDegrees); buttonPanel.add(btnLogout);

        navPanel.add(buttonPanel, BorderLayout.EAST);
        return navPanel;
    }

    private JPanel createLecturerContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(60, 50, 30, 50));

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
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        lecturerTable = new JTable(tableModel);
        lecturerTable.setRowHeight(45);
        lecturerTable.setFont(FONT_CELL);
        lecturerTable.setForeground(CLR_WHITE);
        lecturerTable.setBackground(new Color(0,0,0,0));
        lecturerTable.setOpaque(false);

        // Configuration for Full Row Selection
        lecturerTable.setRowSelectionAllowed(true);
        lecturerTable.setColumnSelectionAllowed(false);
        lecturerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lecturerTable.setShowGrid(false);
        lecturerTable.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = lecturerTable.getTableHeader();
        header.setBackground(new Color(255,255,255,10));
        header.setForeground(CLR_ACCENT);
        header.setFont(FONT_HEADER);
        header.setPreferredSize(new Dimension(0, 45));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Row Selection Renderer
        DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                if (isSelected) {
                    setBackground(new Color(255, 255, 255, 30));
                    setForeground(CLR_ACCENT);
                } else {
                    setBackground(new Color(0, 0, 0, 0));
                    setForeground(CLR_WHITE);
                }
                return this;
            }
        };

        for (int i = 0; i < lecturerTable.getColumnCount(); i++) {
            lecturerTable.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }

        JScrollPane scroll = new JScrollPane(lecturerTable);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        if (text.equals("Logout")) btn.setForeground(CLR_LOGOUT);
        else btn.setForeground(CLR_WHITE);

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setForeground(CLR_ACCENT); }
            @Override public void mouseExited(MouseEvent e) {
                if (text.equals("Logout")) btn.setForeground(CLR_LOGOUT);
                else if (text.equals("Lecturers")) btn.setForeground(CLR_ACCENT);
                else btn.setForeground(CLR_WHITE);
            }
        });
        return btn;
    }

    private JButton createRoundedButton(String text, Dimension size, Color bg, int fontSize) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
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
        btn.setPreferredSize(size); btn.setBackground(bg); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField(); field.setBackground(CLR_FIELD_BG);
        field.setForeground(CLR_WHITE); field.setCaretColor(CLR_WHITE);
        return field;
    }
}