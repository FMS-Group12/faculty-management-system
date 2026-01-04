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
import java.util.Vector;

public class DegreeDashboardView extends JFrame {

    private DegreeController degreeController = new DegreeController();

    private final Color CLR_BG_START   = new Color(20, 24, 42);
    private final Color CLR_BG_END     = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG   = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT     = new Color(212, 175, 55); // Gold
    private final Color CLR_WHITE      = new Color(245, 245, 245);
    private final Color CLR_FIELD_BG   = new Color(45, 50, 75);
    private final Color CLR_NAV_BAR    = new Color(15, 18, 32);
    private final Color CLR_LOGOUT     = new Color(255, 80, 80);

    private final Font FONT_TITLE  = new Font("Inter", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 16);
    private final Font FONT_CELL   = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV    = new Font("SansSerif", Font.BOLD, 18);

    private DefaultTableModel degreeTableModel;
    private JTable degreeTable;

    public DegreeDashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Degrees Dashboard");
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
        mainPanel.add(createDegreesContent(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 40, 0));

        JButton btnSave = createRoundedButton("SAVE CHANGES", new Dimension(200, 40), CLR_ACCENT, 12);
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(this, "Changes saved successfully!"));

        bottomPanel.add(btnSave);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(CLR_NAV_BAR);
        navPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 30)));
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
        JButton btnDegrees = createNavButton("Degrees");
        JButton btnDepartments = createNavButton("Departments");
        JButton btnLogout = createNavButton("Logout");

        btnDegrees.setForeground(CLR_WHITE);
        btnDegrees.setBorder(new MatteBorder(0, 0, 2, 0, CLR_ACCENT));

        btnLecturers.addActionListener(e ->
        {
            new LecturerDashboardView().setVisible(true);
            this.dispose();
        });
        btnStudents.addActionListener(e ->
        {
            new StudentDashboardView().setVisible(true);
            this.dispose();
        });
        btnDepartments.addActionListener(e ->
        {
            new DepartmentDashboardView().setVisible(true);
            this.dispose();
        });
        btnCourses.addActionListener(e ->
        {
            new CourseDashboardView().setVisible(true);
            this.dispose();
        });
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

    private JPanel createDegreesContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(60, 50, 30, 50));

        JLabel lblTitle = new JLabel("Degree Programs", SwingConstants.CENTER);
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

        btnAdd.addActionListener(e -> showAddDegreeDialog());
        btnEdit.addActionListener(e -> showEditDegreeDialog());
        btnDelete.addActionListener(e -> deleteSelectedRow());

        controls.add(btnAdd); controls.add(btnEdit); controls.add(btnDelete);
        tableContainer.add(controls, BorderLayout.NORTH);
        tableContainer.add(createDegreeTable(), BorderLayout.CENTER);

        panel.add(tableContainer, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane createDegreeTable() {
        String[] columns = {"Degree", "Department", "No of Students"};
        Vector<Vector<Object>> data = degreeController.getAllDegreesForTable();

        degreeTableModel = new DefaultTableModel(data, new Vector<>(java.util.Arrays.asList(columns))) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        degreeTable = new JTable(degreeTableModel);
        degreeTable.setRowHeight(45);
        degreeTable.setFont(FONT_CELL);
        degreeTable.setForeground(CLR_WHITE);
        degreeTable.setBackground(new Color(0,0,0,0));
        degreeTable.setOpaque(false);
        degreeTable.setSelectionBackground(new Color(212, 175, 55, 60));
        degreeTable.setSelectionForeground(CLR_ACCENT);
        degreeTable.setShowGrid(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setOpaque(false);
        for (int i = 0; i < degreeTable.getColumnCount(); i++) {
            degreeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = degreeTable.getTableHeader();
        header.setBackground(new Color(255, 255, 255, 10));
        header.setForeground(CLR_ACCENT);
        header.setFont(FONT_HEADER);
        header.setPreferredSize(new Dimension(0, 45));

        JScrollPane scroll = new JScrollPane(degreeTable);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(text.equals("Logout") ? CLR_LOGOUT : CLR_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setForeground(CLR_ACCENT); }
            @Override public void mouseExited(MouseEvent e) {
                if (text.equals("Degrees")) btn.setForeground(CLR_ACCENT);
                else btn.setForeground(text.equals("Logout") ? CLR_LOGOUT : CLR_WHITE);
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
        btn.setPreferredSize(size);
        btn.setBackground(bg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField();
        field.setBackground(CLR_FIELD_BG);
        field.setForeground(CLR_WHITE);
        field.setCaretColor(CLR_WHITE);
        return field;
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
            if (degreeController.updateDegree(oldName, txtDegree.getText(), txtDept.getText(), Integer.parseInt(txtStudents.getText()))) {
                degreeTableModel.setValueAt(txtDegree.getText(), selectedRow, 0);
                degreeTableModel.setValueAt(txtDept.getText(), selectedRow, 1);
                degreeTableModel.setValueAt(txtStudents.getText(), selectedRow, 2);
            }
        }
    }

    private void showAddDegreeDialog() {
        JTextField nameF = createStyledField();
        JTextField deptF = createStyledField();
        JTextField countF = createStyledField();
        Object[] fields = { "Degree Name:", nameF, "Department:", deptF, "Students Count:", countF };
        if (JOptionPane.showConfirmDialog(this, fields, "Add New Degree", 2) == 0) {
            // Controller logic remains the same
            if (degreeController.addDegree(nameF.getText(), deptF.getText(), Integer.parseInt(countF.getText()))) {
                degreeTableModel.addRow(new Object[]{nameF.getText(), deptF.getText(), countF.getText()});
            }
        }
    }
    private void deleteSelectedRow() {
        int selectedRow = degreeTable.getSelectedRow();
        if (selectedRow != -1) {
            String degreeName = (String) degreeTableModel.getValueAt(selectedRow, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete " + degreeName + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (degreeController.deleteDegree(degreeName)) {
                    degreeTableModel.removeRow(selectedRow);
                }
            }
        }
    }
}
