import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class TimeTableView extends JFrame {

    // --- COLOR PALETTE (Sage Green Theme) ---
    private final Color CLR_BG = new Color(235, 233, 225);
    private final Color CLR_HEADER_BG = new Color(70, 75, 60);
    private final Color CLR_ACCENT = new Color(155, 150, 130);
    private final Color CLR_TEXT_DARK = new Color(60, 60, 60);
    private final Color CLR_TEXT_LIGHT = new Color(255, 255, 255);
    private final Color CLR_NAV_BAR = new Color(225, 223, 215);

    // Fonts
    private final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 13);
    private final Font FONT_TITLE = new Font("Serif", Font.ITALIC | Font.BOLD, 36);
    private final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private final Font FONT_CELL = new Font("SansSerif", Font.PLAIN, 14);

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

        // Root Panel
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(CLR_BG);
        setContentPane(rootPanel);

        // 1. ADD NAVIGATION BAR
        rootPanel.add(createTopNavBar(), BorderLayout.NORTH);

        // 2. ADD TIMETABLE CONTENT (Directly to Center, removed CardLayout)
        rootPanel.add(createTimetablePanel(), BorderLayout.CENTER);
    }

    // =========================================================
    // 1. NAVIGATION BAR (CONNECTS THE PAGES)
    // =========================================================
    private JPanel createTopNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(CLR_NAV_BAR);
        navPanel.setBorder(new MatteBorder(0, 0, 1, 0, CLR_ACCENT));
        navPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // Welcome Message
        JLabel lblWelcome = new JLabel("  Welcome, " + currentStudentName);
        lblWelcome.setFont(FONT_NAV);
        lblWelcome.setForeground(CLR_HEADER_BG);
        navPanel.add(lblWelcome, BorderLayout.WEST);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        buttonPanel.setBackground(CLR_NAV_BAR);

        JButton btnProfile = createNavButton("Profile");
        JButton btnTimetable = createNavButton("Timetable");
        JButton btnCourses = createNavButton("Courses");
        JButton btnLogout = createNavButton("Logout");
        btnLogout.setForeground(new Color(180, 50, 50));

        // --- NAVIGATION LOGIC ---
        
        // 1. Go to Profile
        btnProfile.addActionListener(e -> {
            new StudentProfileView().setVisible(true); 
            dispose(); 
        });

        // 2. Stay on Timetable (Already here)
        btnTimetable.addActionListener(e -> {
            // Optional: Refresh data if connected to DB
        });

        // 3. Go to Courses
        btnCourses.addActionListener(e -> {
            new StudentCoursesView().setVisible(true); 
            dispose(); 
        });

        // 4. Logout
        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION) dispose();
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
        btn.setForeground(CLR_HEADER_BG);
        btn.setBackground(CLR_NAV_BAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(CLR_ACCENT.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(CLR_HEADER_BG);
            }
        });
        return btn;
    }

    // =========================================================
    // 2. TIMETABLE PAGE (Sage Green Design)
    // =========================================================
    private JPanel createTimetablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CLR_BG);
        panel.setBorder(new EmptyBorder(20, 40, 40, 40));

        JLabel lblTitle = new JLabel("class schedule");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_HEADER_BG);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("COMPUTER SCIENCE - SEMESTER 1");
        lblSubtitle.setFont(FONT_SUBTITLE);
        lblSubtitle.setForeground(CLR_ACCENT.darker());
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblSubtitle);
        panel.add(Box.createVerticalStrut(30));
        panel.add(createTable()); 

        return panel;
    }

    // =========================================================
    // 3. TABLE HELPERS (Renderers & Table Config)
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
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(55);
        table.setFont(FONT_CELL);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(CLR_BG);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setBackground(CLR_BG);
        header.setPreferredSize(new Dimension(0, 50));

        SageCellRenderer cellRenderer = new SageCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CLR_BG);
        return scrollPane;
    }

    private class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBackground(CLR_BG);
            label.setForeground(CLR_TEXT_LIGHT);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(FONT_HEADER);
            label.setText(value.toString());
            if (column > 0) return new PillHeaderPanel(value.toString());
            else { label.setForeground(CLR_TEXT_DARK); return label; }
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
            g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 30, 30);
            g2.setColor(CLR_TEXT_LIGHT);
            FontMetrics fm = g2.getFontMetrics(FONT_HEADER);
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.setFont(FONT_HEADER);
            g2.drawString(text, x, y);
        }
    }

    private class SageCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String timeColumnValue = (String) table.getValueAt(row, 0);
            if ("Interval".equalsIgnoreCase(timeColumnValue)) {
                c.setBackground(CLR_ACCENT); c.setForeground(Color.WHITE);
                setFont(new Font("SansSerif", Font.BOLD, 16));
                setHorizontalAlignment(SwingConstants.CENTER);
                if (column == 3) setText("INTERVAL"); else setText("");
            } else {
                c.setBackground(CLR_BG); c.setForeground(CLR_TEXT_DARK);
                setFont(FONT_CELL); setHorizontalAlignment(SwingConstants.CENTER);
                if (column == 0) ((JComponent)c).setBorder(new MatteBorder(0, 0, 0, 1, new Color(200, 195, 180)));
                else ((JComponent)c).setBorder(null);
            }
            return c;
        }
    }

    // Main Method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TimeTableView("Kumar").setVisible(true);
        });
    }
}

// =========================================================
// PLACEHOLDER CLASSES (These ensure the buttons work now)
// =========================================================

class StudentProfileView extends JFrame {
    public StudentProfileView() {
        setTitle("Student Profile");
        setSize(800, 600);
        setLocationRelativeTo(null);
        add(new JLabel("Profile Page Placeholder", SwingConstants.CENTER));
        
        JButton btnBack = new JButton("Back to Timetable");
        btnBack.addActionListener(e -> { new TimeTableView("Student").setVisible(true); dispose(); });
        add(btnBack, BorderLayout.SOUTH);
    }
}

class StudentCoursesView extends JFrame {
    public StudentCoursesView() {
        setTitle("Student Courses");
        setSize(800, 600);
        setLocationRelativeTo(null);
        add(new JLabel("Course Page Placeholder", SwingConstants.CENTER));
        
        JButton btnBack = new JButton("Back to Timetable");
        btnBack.addActionListener(e -> { new TimeTableView("Student").setVisible(true); dispose(); });
        add(btnBack, BorderLayout.SOUTH);
    }
}
