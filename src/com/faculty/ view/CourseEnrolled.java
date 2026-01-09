import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class CourseEnrolled extends JFrame {

    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    private final Color CLR_BG_START = new Color(20, 24, 42);
    private final Color CLR_BG_END   = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT   = new Color(212, 175, 55); // Gold
    private final Color CLR_WHITE    = new Color(245, 245, 245);
    private final Color CLR_NAV_BAR  = new Color(15, 18, 32);
    private final Color CLR_LOGOUT   = new Color(255, 80, 80);

    private final Font FONT_NAV     = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_TITLE   = new Font("Inter", Font.BOLD | Font.ITALIC, 36);
    private final Font FONT_SUB     = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_HEADER  = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_CELL    = new Font("SansSerif", Font.PLAIN, 14);

    private String studentName;

  
    public CourseEnrolled(String studentName) {
        this.studentName = studentName;
        initializeUI();
    }

  
    private void initializeUI() {
        setTitle("Faculty Management System - Courses Enrolled");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, CLR_BG_START, 0, getHeight(), CLR_BG_END));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(rootPanel);

        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);
        rootPanel.add(createMainPanel(), BorderLayout.CENTER);
    }

 
    private JPanel createTopNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(CLR_NAV_BAR);
        nav.setPreferredSize(new Dimension(1000, 70));
        nav.setBorder(new MatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 30)));

        JLabel lblWelcome = new JLabel("  Welcome, " + studentName);
        lblWelcome.setFont(FONT_NAV);
        lblWelcome.setForeground(CLR_ACCENT);
        nav.add(lblWelcome, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 22));
        buttons.setOpaque(false);

        buttons.add(createNavButton("Profile"));
        buttons.add(createNavButton("Timetable"));
        buttons.add(createNavButton("Courses"));
        buttons.add(createNavButton("Logout"));

        nav.add(buttons, BorderLayout.EAST);
        return nav;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);
        btn.setForeground(text.equals("Logout") ? CLR_LOGOUT : CLR_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            switch (text) {
                case "Logout":
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        new SignInView().setVisible(true);
                        this.dispose();
                    }
                    break;

                case "Profile":
                    new StudentProfileView().setVisible(true);
                    this.dispose();
                    break;

                case "Timetable":
                    new TimeTableView("Student").setVisible(true); 
                    this.dispose();
                    break;

                case "Courses":
                    break;
            }
        });


        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(CLR_ACCENT);
            }
            public void mouseExited(MouseEvent e) {
                if (text.equals("Logout")) btn.setForeground(CLR_LOGOUT);
                else if (text.equals("Courses")) btn.setForeground(CLR_ACCENT);
                else btn.setForeground(CLR_WHITE);
            }
        });
        return btn;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 40, 40, 40));

        JLabel title = new JLabel("Courses Enrolled");
        title.setFont(FONT_TITLE);
        title.setForeground(CLR_WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("COMPUTER SCIENCE - SEMESTER 1");
        subtitle.setFont(FONT_SUB);
        subtitle.setForeground(CLR_ACCENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(30));

        JPanel tableGlass = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_GLASS_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        tableGlass.setOpaque(false);
        tableGlass.setBorder(new EmptyBorder(20, 20, 20, 20));
        tableGlass.add(createTable(), BorderLayout.CENTER);

        panel.add(tableGlass);
        return panel;
    }

    private JScrollPane createTable() {

        String[] columns = {"Course Code", "Course Name", "Credits", "Grade"};

        Vector<Vector<Object>> data =
                enrollmentDAO.getEnrollmentsByUsername(studentName);

        DefaultTableModel model =
                new DefaultTableModel(data, new Vector<>(java.util.Arrays.asList(columns))) {
                    public boolean isCellEditable(int r, int c) {
                        return false;
                    }
                };

        JTable table = new JTable(model);
        table.setRowHeight(55);
        table.setFont(FONT_CELL);
        table.setForeground(CLR_WHITE);
        table.setBackground(new Color(0, 0, 0, 0));
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new Dimension(0, 50));

        DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        cell.setForeground(CLR_WHITE);
        cell.setBackground(new Color(0, 0, 0, 0));
        cell.setFont(FONT_CELL);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cell);
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        return sp;
    }

    private class HeaderRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {

            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
            lbl.setForeground(CLR_ACCENT);
            lbl.setFont(FONT_HEADER);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setBackground(new Color(255, 255, 255, 10));
            return lbl;
        }
    }

}
