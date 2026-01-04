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

    // ---------- CONTROLLER ----------
    private StudentController controller = new StudentController();

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

    // ---------- UI ----------
    private void initializeUI() {
        setTitle("Faculty Management System - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, CLR_BG_START, 0, getHeight(), CLR_BG_END));
                g2.fillRect(0, 0, getWidth(), getHeight());
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

    // ---------- NAV BAR ----------
    private JPanel createTopNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(CLR_NAV_BAR);
        nav.setPreferredSize(new Dimension(getWidth(), 70));
        nav.setBorder(new MatteBorder(0, 0, 1, 0, new Color(255,255,255,30)));

        JLabel lbl = new JLabel("  Welcome, Admin");
        lbl.setFont(FONT_NAV);
        lbl.setForeground(CLR_ACCENT);
        nav.add(lbl, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 22));
        buttons.setOpaque(false);

        JButton btnStudents = createNavButton("Students");
        JButton btnLecturers = createNavButton("Lecturers");
        JButton btnCourses = createNavButton("Courses");
        JButton btnDepartments = createNavButton("Departments");
        JButton btnDegrees = createNavButton("Degrees");
        JButton btnLogout = createNavButton("Logout");

        btnStudents.setForeground(CLR_ACCENT);
        btnStudents.setBorder(new MatteBorder(0,0,2,0,CLR_ACCENT));

        btnLecturers.addActionListener(e -> { new LecturerDashboardView().setVisible(true); dispose(); });
        btnCourses.addActionListener(e -> { new CourseDashboardView().setVisible(true); dispose(); });
        btnDepartments.addActionListener(e -> { new DepartmentDashboardView().setVisible(true); dispose(); });
        btnDegrees.addActionListener(e -> { new DegreeDashboardView().setVisible(true); dispose(); });

        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", 0) == 0) {
                new SignInView().setVisible(true);
                dispose();
            }
        });

        buttons.add(btnStudents);
        buttons.add(btnLecturers);
        buttons.add(btnCourses);
        buttons.add(btnDepartments);
        buttons.add(btnDegrees);
        buttons.add(btnLogout);

        nav.add(buttons, BorderLayout.EAST);
        return nav;
    }

    // ---------- CONTENT ----------
    private JPanel createStudentsContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(60, 50, 30, 50));

        JLabel title = new JLabel("Students", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(CLR_WHITE);
        panel.add(title, BorderLayout.NORTH);

        JPanel glass = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(CLR_GLASS_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        glass.setOpaque(false);
        glass.setBorder(new EmptyBorder(20,20,20,20));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        controls.setOpaque(false);

        JButton add = createRoundedButton("Add New", new Dimension(100,35), CLR_ACCENT, 11);
        JButton edit = createRoundedButton("Edit", new Dimension(100,35), CLR_ACCENT, 11);
        JButton del = createRoundedButton("Delete", new Dimension(100,35), CLR_ACCENT, 11);

        add.addActionListener(e -> addStudent());
        edit.addActionListener(e -> editStudent());
        del.addActionListener(e -> deleteStudent());

        controls.add(add);
        controls.add(edit);
        controls.add(del);

        glass.add(controls, BorderLayout.NORTH);
        glass.add(createStudentTable(), BorderLayout.CENTER);
        panel.add(glass, BorderLayout.CENTER);
        return panel;
    }

    // ---------- TABLE ----------
    private JScrollPane createStudentTable() {
        String[] cols = {"Full Name", "Student ID", "Degree", "Email", "Mobile"};
        studentTableModel = new DefaultTableModel(null, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(45);
        studentTable.setFont(FONT_CELL);
        studentTable.setForeground(CLR_WHITE);
        studentTable.setOpaque(false);
        studentTable.setSelectionBackground(new Color(212,175,55,60));
        studentTable.setSelectionForeground(CLR_ACCENT);
        studentTable.setShowGrid(false);
        studentTable.setRowSelectionAllowed(true);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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



        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

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


    // ---------- CRUD ----------
    private void loadStudentsFromDatabase() {
        try (Connection con = dbc.getConnection()) {
            studentTableModel.setRowCount(0);
            ResultSet rs = controller.getAllStudents(con);
            while (rs.next()) {
                studentTableModel.addRow(new Object[]{
                        rs.getString("fullname"),
                        rs.getString("student_id"),
                        rs.getString("degree"),
                        rs.getString("email"),
                        rs.getString("mobile_no")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addStudent() {
        JTextField n = createStyledField(), id = createStyledField(),
                d = createStyledField(), e = createStyledField(), m = createStyledField();

        Object[] f = {"Full Name:",n,"Student ID:",id,"Degree:",d,"Email:",e,"Mobile:",m};

        if (JOptionPane.showConfirmDialog(this,f,"New Student",2)==0) {
            try (Connection con = dbc.getConnection()) {
                controller.addStudent(con,id.getText(),n.getText(),d.getText(),e.getText(),m.getText());
                loadStudentsFromDatabase();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void editStudent() {
        int r = studentTable.getSelectedRow();
        if (r==-1) { JOptionPane.showMessageDialog(this,"Select student"); return; }

        JTextField n = createStyledField(), id = createStyledField(),
                d = createStyledField(), e = createStyledField(), m = createStyledField();

        n.setText(studentTableModel.getValueAt(r,0).toString());
        id.setText(studentTableModel.getValueAt(r,1).toString());
        d.setText(studentTableModel.getValueAt(r,2).toString());
        e.setText(studentTableModel.getValueAt(r,3).toString());
        m.setText(studentTableModel.getValueAt(r,4).toString());

        Object[] f = {"Full Name:",n,"Student ID:",id,"Degree:",d,"Email:",e,"Mobile:",m};

        if (JOptionPane.showConfirmDialog(this,f,"Edit Student",2)==0) {
            try (Connection con = dbc.getConnection()) {
                controller.updateStudent(con,id.getText(),n.getText(),d.getText(),e.getText(),m.getText());
                loadStudentsFromDatabase();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void deleteStudent() {
        int r = studentTable.getSelectedRow();
        if (r==-1) { JOptionPane.showMessageDialog(this,"Select student"); return; }

        String id = studentTableModel.getValueAt(r,1).toString();
        if (JOptionPane.showConfirmDialog(this,"Delete student "+id+"?","Confirm",0)==0) {
            try (Connection con = dbc.getConnection()) {
                controller.deleteStudent(con,id);
                loadStudentsFromDatabase();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    // ---------- HELPERS ----------
    private JButton createNavButton(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("SansSerif",Font.BOLD,14));
        b.setForeground(t.equals("Logout")?CLR_LOGOUT:CLR_WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){ b.setForeground(CLR_ACCENT); }
            public void mouseExited(MouseEvent e){ b.setForeground(t.equals("Logout")?CLR_LOGOUT:CLR_WHITE); }
        });
        return b;
    }

    private JButton createRoundedButton(String t, Dimension s, Color bg, int fs) {
        JButton b = new JButton(t) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g;
                g2.setColor(bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),15,15);
                g2.setColor(bg==CLR_ACCENT?CLR_BG_START:CLR_WHITE);
                g2.setFont(new Font("SansSerif",Font.BOLD,fs));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(t,(getWidth()-fm.stringWidth(t))/2,(getHeight()+fm.getAscent())/2-2);
            }
        };
        b.setPreferredSize(s);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        return b;
    }

    private JTextField createStyledField() {
        JTextField f = new JTextField();
        f.setBackground(CLR_FIELD_BG);
        f.setForeground(CLR_WHITE);
        f.setCaretColor(CLR_WHITE);
        return f;
    }


}
