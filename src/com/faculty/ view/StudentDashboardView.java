import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.ResultSet;

public class StudentDashboardView extends JFrame {

    // --- MIDNIGHT GLASS PALETTE ---
    private final Color CLR_BG_START   = new Color(20, 24, 42);
    private final Color CLR_BG_END     = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG   = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT     = new Color(212, 175, 55);
    private final Color CLR_WHITE      = new Color(245, 245, 245);
    private final Color CLR_FIELD_BG   = new Color(45, 50, 75);
    private final Color CLR_NAV_BAR    = new Color(15, 18, 32);
    private final Color CLR_LOGOUT     = new Color(255, 80, 80);

    private final Font FONT_TITLE  = new Font("Inter", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 16);
    private final Font FONT_CELL   = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV    = new Font("SansSerif", Font.BOLD, 18);

    private DefaultTableModel studentTableModel;
    private JTable studentTable;

    public StudentDashboardView() {
        initializeUI();
        loadStudentsFromDatabase();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Student Dashboard");
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
        mainPanel.add(createStudentsContent(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 40, 0));

        JButton btnSave = createRoundedButton("SAVE CHANGES", new Dimension(200, 40), CLR_ACCENT, 12);
        btnSave.addActionListener(e -> {
            loadStudentsFromDatabase();
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

        // Set Active Page Styling for Students
        btnStudents.setForeground(CLR_ACCENT);
        btnStudents.setBorder(new MatteBorder(0, 0, 2, 0, CLR_ACCENT));

        // NAVIGATION ACTIONS
        btnStudents.addActionListener(e -> loadStudentsFromDatabase());
        btnLecturers.addActionListener(e -> { new LecturerDashboardView().setVisible(true); this.dispose(); });
        btnCourses.addActionListener(e -> { new CourseDashboardView().setVisible(true); this.dispose(); });
        btnDepartments.addActionListener(e -> { new DepartmentDashboardView().setVisible(true); this.dispose(); });
        btnDegrees.addActionListener(e -> { new DegreeDashboardView().setVisible(true); this.dispose(); });

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

    private JPanel createStudentsContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(60, 50, 30, 50));

        JLabel lblTitle = new JLabel("Students", SwingConstants.CENTER);
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

        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        controls.add(btnAdd); controls.add(btnEdit); controls.add(btnDelete);
        tableContainer.add(controls, BorderLayout.NORTH);
        tableContainer.add(createStudentTable(), BorderLayout.CENTER);

        panel.add(tableContainer, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane createStudentTable() {
        String[] columns = {"Full Name", "Student ID", "Degree", "Email", "Mobile"};
        studentTableModel = new DefaultTableModel(null, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(45);
        studentTable.setFont(FONT_CELL);
        studentTable.setForeground(CLR_WHITE);
        studentTable.setBackground(new Color(0,0,0,0));
        studentTable.setOpaque(false);

        // Highlight colors
        studentTable.setSelectionBackground(new Color(212, 175, 55, 60)); // Transparent Gold
        studentTable.setSelectionForeground(CLR_ACCENT); // Gold Text

        studentTable.setShowGrid(false);
        studentTable.setRowSelectionAllowed(true); // Ensures the whole row is selectable
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- UPDATED CENTER RENDERER ---
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                // Let the super class handle the alignment and value
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(JLabel.CENTER);

                // This logic ensures the "Glass" look stays when NOT selected,
                // but shows the highlight color when SELECTED.
                if (!isSelected) {
                    setOpaque(false);
                } else {
                    setOpaque(true);
                }

                return this;
            }
        };

        // Apply the renderer to all columns
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Header Styling
        JTableHeader header = studentTable.getTableHeader();
        header.setBackground(new Color(255, 255, 255, 10));
        header.setForeground(CLR_ACCENT);
        header.setFont(FONT_HEADER);
        header.setPreferredSize(new Dimension(0, 45));

        // Center the Header text
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scroll = new JScrollPane(studentTable);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }
    // --- CRUD METHODS ---
    private void loadStudentsFromDatabase() {
        try (Connection con = dbc.getConnection()) {
            studentTableModel.setRowCount(0);
            ResultSet rs = StudentDAO.getAllStudents(con);
            while (rs.next()) {
                studentTableModel.addRow(new Object[]{
                        rs.getString("fullname"), rs.getString("student_id"),
                        rs.getString("degree"), rs.getString("email"), rs.getString("mobile_no")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void addStudent() {
        JTextField n = createStyledField(); JTextField id = createStyledField();
        JTextField d = createStyledField(); JTextField e = createStyledField();
        JTextField m = createStyledField();
        Object[] fields = {"Full Name:", n, "Student ID:", id, "Degree:", d, "Email:", e, "Mobile:", m};

        if (JOptionPane.showConfirmDialog(this, fields, "New Student", 2) == 0) {
            try (Connection con = dbc.getConnection()) {
                StudentDAO.insertStudent(con, id.getText(), n.getText(), d.getText(), e.getText(), m.getText());
                loadStudentsFromDatabase();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void editStudent() {
        int r = studentTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Select a student to edit."); return; }

        JTextField n = createStyledField(); n.setText((String) studentTableModel.getValueAt(r, 0));
        String currentID = (String) studentTableModel.getValueAt(r, 1);
        JTextField idField = createStyledField(); idField.setText(currentID);
        JTextField d = createStyledField(); d.setText((String) studentTableModel.getValueAt(r, 2));
        JTextField e = createStyledField(); e.setText((String) studentTableModel.getValueAt(r, 3));
        JTextField m = createStyledField(); m.setText((String) studentTableModel.getValueAt(r, 4));

        Object[] fields = {"Full Name:", n, "Student ID:", idField, "Degree:", d, "Email:", e, "Mobile:", m};
        if (JOptionPane.showConfirmDialog(this, fields, "Edit Student", 2) == 0) {
            try (Connection con = dbc.getConnection()) {
                StudentDAO.updateStudent(con, idField.getText(), n.getText(), d.getText(), e.getText(), m.getText());
                loadStudentsFromDatabase();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void deleteStudent() {
        int r = studentTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }

        String id = (String) studentTableModel.getValueAt(r, 1);
        if (JOptionPane.showConfirmDialog(this, "Delete student " + id + "?", "Confirm", 0) == 0) {
            try (Connection con = dbc.getConnection()) {
                StudentDAO.deleteStudent(con, id);
                loadStudentsFromDatabase();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    // --- REUSABLE FACTORIES ---
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(text.equals("Logout") ? CLR_LOGOUT : CLR_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setForeground(CLR_ACCENT); }
            @Override
            public void mouseExited(MouseEvent e) {
                if (text.equals("Logout")) btn.setForeground(CLR_LOGOUT);
                else if (text.equals("Students")) btn.setForeground(CLR_ACCENT);
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
        JTextField field = new JTextField();
        field.setBackground(CLR_FIELD_BG);
        field.setForeground(CLR_WHITE);
        field.setCaretColor(CLR_WHITE);
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboardView().setVisible(true));
    }
}