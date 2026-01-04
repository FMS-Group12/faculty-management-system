import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CourseDashboardView extends JFrame {

    private final Color CLR_BG = new Color(235, 233, 225);
    private final Color CLR_HEADER_BG = new Color(70, 75, 60);
    private final Color CLR_ACCENT = new Color(155, 150, 130);
    private final Color CLR_NAV_BAR = new Color(225, 223, 215);

    private final Font FONT_TITLE = new Font("Serif", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_CELL = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 13);

    private JTable courseTable;
    private DefaultTableModel courseTableModel;
    private CourseController courseController = new CourseController();

    public CourseDashboardView() {
        initializeUI();
        loadCoursesFromDatabase();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Courses");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CLR_BG);
        setContentPane(root);

        root.add(createTopNavBar(), BorderLayout.NORTH);
        root.add(createCourseContent(), BorderLayout.CENTER);

        // ADDED: Bottom panel for the Save Changes button
        root.add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(CLR_NAV_BAR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 55));
        navPanel.setBorder(new MatteBorder(0, 0, 1, 0, CLR_ACCENT));

        JLabel lblWelcome = new JLabel("  Welcome, Admin");
        lblWelcome.setFont(FONT_NAV);
        lblWelcome.setForeground(CLR_HEADER_BG);
        navPanel.add(lblWelcome, BorderLayout.WEST);

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 15));
        menuPanel.setBackground(CLR_NAV_BAR);
        menuPanel.add(createNavLink("Students"));
        menuPanel.add(createNavLink("Lecturers"));
        menuPanel.add(createNavLink("Departments"));
        menuPanel.add(createNavLink("Degrees"));

        JButton btnLogout = createNavLink("Logout");
        btnLogout.setForeground(new Color(180, 50, 50));
        btnLogout.addActionListener(e -> dispose());
        menuPanel.add(btnLogout);

        navPanel.add(menuPanel, BorderLayout.EAST);
        return navPanel;
    }

    private JButton createNavLink(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);
        btn.setForeground(CLR_HEADER_BG);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createCourseContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CLR_BG);
        panel.setBorder(new EmptyBorder(30, 50, 10, 50)); // Adjusted bottom margin

        JLabel title = new JLabel("Courses", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(CLR_HEADER_BG);
        panel.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(CLR_BG);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controls.setBackground(CLR_BG);

        JButton btnAdd = createButton("Add", true);
        JButton btnEdit = createButton("Edit", false);
        JButton btnDelete = createButton("Delete", false);

        btnAdd.addActionListener(e -> addCourse());
        btnEdit.addActionListener(e -> editCourse());
        btnDelete.addActionListener(e -> deleteCourse());

        controls.add(btnAdd);
        controls.add(btnEdit);
        controls.add(btnDelete);

        center.add(controls, BorderLayout.NORTH);
        center.add(createCourseTable(), BorderLayout.CENTER);

        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    // NEW: Bottom Panel Method
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(CLR_BG);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 30, 0));

        JButton btnSaveAll = new JButton("SAVE CHANGES");
        btnSaveAll.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSaveAll.setBackground(new Color(60, 100, 60)); // Success Green
        btnSaveAll.setForeground(Color.WHITE);
        btnSaveAll.setPreferredSize(new Dimension(200, 45));
        btnSaveAll.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // This button acts as a global "Finish Editing" or triggers the edit for selected row
        btnSaveAll.addActionListener(e -> {
            int row = courseTable.getSelectedRow();
            if (row != -1) {
                editCourse();
            } else {
                JOptionPane.showMessageDialog(this, "Select a course record to update and save.");
            }
        });

        bottomPanel.add(btnSaveAll);
        return bottomPanel;
    }

    private JScrollPane createCourseTable() {
        String[] cols = {"Course Code", "Course Name", "Lecturer Name", "Credits"};
        courseTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        courseTable = new JTable(courseTableModel);
        courseTable.setRowHeight(45);
        courseTable.setFont(FONT_CELL);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < courseTable.getColumnCount(); i++) {
            courseTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return new JScrollPane(courseTable);
    }

    private void loadCoursesFromDatabase() {
        courseTableModel.setRowCount(0);
        for (Object[] row : courseController.getAllCourses()) {
            courseTableModel.addRow(row);
        }
    }

    private void addCourse() {
        JTextField code = new JTextField();
        JTextField name = new JTextField();
        JTextField lecturer = new JTextField();
        JTextField credits = new JTextField();

        Object[] msg = {"Code:", code, "Name:", name, "Lecturer:", lecturer, "Credits:", credits};

        if (JOptionPane.showConfirmDialog(this, msg, "Add Course", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                if (courseController.addCourse(code.getText(), name.getText(), Integer.parseInt(credits.getText()), lecturer.getText())) {
                    loadCoursesFromDatabase();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding course.");
            }
        }
    }

    private void editCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row from the table first!");
            return;
        }

        String oldCode = courseTableModel.getValueAt(row, 0).toString();
        String currentName = courseTableModel.getValueAt(row, 1).toString();
        String currentLec = courseTableModel.getValueAt(row, 2).toString();
        String currentCredits = courseTableModel.getValueAt(row, 3).toString();

        JTextField codeField = new JTextField(oldCode);
        JTextField nameField = new JTextField(currentName);
        JTextField lecturerField = new JTextField(currentLec);
        JTextField creditsField = new JTextField(currentCredits);

        Object[] msg = {
                "Course Code:", codeField,
                "Course Name:", nameField,
                "Lecturer Name:", lecturerField,
                "Credits:", creditsField
        };

        int result = JOptionPane.showConfirmDialog(this, msg, "Edit Course Details", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean success = courseController.updateCourse(
                        oldCode,
                        codeField.getText(),
                        nameField.getText(),
                        lecturerField.getText(),
                        Integer.parseInt(creditsField.getText())
                );

                if (success) {
                    loadCoursesFromDatabase();
                    JOptionPane.showMessageDialog(this, "Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed in Database.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Credits must be a number!");
            }
        }
    }

    private void deleteCourse() {
        int row = courseTable.getSelectedRow();
        if (row != -1 && JOptionPane.showConfirmDialog(this, "Delete selected course?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (courseController.deleteCourse(courseTableModel.getValueAt(row, 0).toString())) {
                loadCoursesFromDatabase();
            }
        } else if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
        }
    }

    private JButton createButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(primary ? CLR_HEADER_BG : CLR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }


}
