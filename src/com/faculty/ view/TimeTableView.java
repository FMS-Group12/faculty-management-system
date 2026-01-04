import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TimeTableView extends JFrame {

    // --- MIDNIGHT GLASS PALETTE ---
    private final Color CLR_BG_START   = new Color(20, 24, 42);
    private final Color CLR_BG_END     = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG   = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT     = new Color(212, 175, 55); // Gold
    private final Color CLR_WHITE      = new Color(245, 245, 245);
    private final Color CLR_NAV_BAR    = new Color(15, 18, 32);
    private final Color CLR_LOGOUT     = new Color(255, 80, 80); // Red

    // Fonts
    private final Font FONT_NAV      = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_TITLE    = new Font("Inter", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_HEADER   = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_CELL     = new Font("SansSerif", Font.PLAIN, 14);

    private String currentStudentName;

    public TimeTableView(String studentName) {
        this.currentStudentName = studentName;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Faculty Management System - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        // Root Panel with Gradient
        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, CLR_BG_START, 0, getHeight(), CLR_BG_END);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(rootPanel);

        // 1. ADD NAVIGATION BAR
        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);

        // 2. ADD TIMETABLE CONTENT
        rootPanel.add(createTimetablePanel(), BorderLayout.CENTER);
    }

    // =========================================================
    // 1. NAVIGATION BAR
    // =========================================================
    private JPanel createTopNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(CLR_NAV_BAR);
        navPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 30)));
        navPanel.setPreferredSize(new Dimension(getWidth(), 70));

        // Welcome Message
        JLabel lblWelcome = new JLabel("  Welcome, " + currentStudentName);
        lblWelcome.setFont(FONT_NAV);
        lblWelcome.setForeground(CLR_ACCENT);
        navPanel.add(lblWelcome, BorderLayout.WEST);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 22));
        buttonPanel.setOpaque(false);

        JButton btnProfile = createNavButton("Profile");
        JButton btnTimetable = createNavButton("Timetable");
        JButton btnCourses = createNavButton("Courses");
        JButton btnLogout = createNavButton("Logout");

        // Set Active State
        btnTimetable.setForeground(CLR_ACCENT);
        btnTimetable.setBorder(new MatteBorder(0, 0, 2, 0, CLR_ACCENT));

        // --- NAVIGATION LOGIC ---
        // btnProfile.addActionListener(e -> { new StudentProfileView().setVisible(true); dispose(); });
        // btnCourses.addActionListener(e -> { new StudentCoursesView().setVisible(true); dispose(); });

        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION) {
                new SignInView().setVisible(true);
                dispose();
            }
        });

        buttonPanel.add(btnProfile);
        buttonPanel.add(btnTimetable);
        buttonPanel.add(btnCourses);
        buttonPanel.add(btnLogout);

        navPanel.add(buttonPanel, BorderLayout.EAST);
        return navPanel;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);
        if (text.equals("Logout")) btn.setForeground(CLR_LOGOUT);
        else btn.setForeground(CLR_WHITE);

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setForeground(CLR_ACCENT);
            }
            public void mouseExited(MouseEvent evt) {
                if (text.equals("Logout")) btn.setForeground(CLR_LOGOUT);
                else if (text.equals("Timetable")) btn.setForeground(CLR_ACCENT);
                else btn.setForeground(CLR_WHITE);
            }
        });
        return btn;
    }

    // =========================================================
    // 2. TIMETABLE PAGE
    // =========================================================
    private JPanel createTimetablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 40, 40));

        JLabel lblTitle = new JLabel("class schedule");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("COMPUTER SCIENCE - SEMESTER 1");
        lblSubtitle.setFont(FONT_SUBTITLE);
        lblSubtitle.setForeground(CLR_ACCENT);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblSubtitle);
        panel.add(Box.createVerticalStrut(30));

        // Glass Table Container
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
        tableContainer.add(createTable(), BorderLayout.CENTER);

        panel.add(tableContainer);

        return panel;
    }

    // =========================================================
    // 3. TABLE HELPERS
    // =========================================================
    private JScrollPane createTable() {
        String[] columns = {"Time", "MON", "TUE", "WED", "THU", "FRI"};
        Object[][] data = {
                {"08.00", "OOP", "OOP", "OOP", "OOP", "OOP"},
                {"10.00", "OOP", "OOP", "OOP", "OOP", "OOP"},
                {"Interval", "", "", "", "", ""},
                {"01.00", "SE", "OOP", "SE", "SE", "SE"},
                {"03.00", "SE", "OOP", "SE", "SE", "SE"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(55);
        table.setFont(FONT_CELL);
        table.setForeground(CLR_WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(0,0,0,0));
        table.setOpaque(false);
        table.setFillsViewportHeight(true);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setBackground(new Color(255, 255, 255, 10));
        header.setPreferredSize(new Dimension(0, 50));

        // Cell Renderer
        MidnightCellRenderer cellRenderer = new MidnightCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    // --- RENDERERS ---

    private class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBackground(new Color(255,255,255,10));
            label.setForeground(CLR_ACCENT); // Gold Text for Headers
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(FONT_HEADER);
            label.setText(value.toString());
            return label;
        }
    }

    private class MidnightCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String timeColumnValue = (String) table.getValueAt(row, 0);

            if ("Interval".equalsIgnoreCase(timeColumnValue)) {
                // Highlight Interval Row
                c.setBackground(CLR_ACCENT);
                c.setForeground(CLR_BG_START); // Dark text on Gold background
                setFont(new Font("SansSerif", Font.BOLD, 16));
                setHorizontalAlignment(SwingConstants.CENTER);
                if (column == 3) setText("INTERVAL"); else setText("");
            } else {
                // Standard Cells
                c.setBackground(new Color(0,0,0,0)); // Transparent
                c.setForeground(CLR_WHITE);
                setFont(FONT_CELL);
                setHorizontalAlignment(SwingConstants.CENTER);

                if (column == 0) {
                    // Time Column Border
                    ((JComponent)c).setBorder(new MatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 50)));
                } else {
                    ((JComponent)c).setBorder(null);
                }
            }
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TimeTableView("Kumar").setVisible(true);
        });
    }
}
