import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class StudentView extends JFrame {

    private final Color CLR_BG_START   = new Color(20, 24, 42);
    private final Color CLR_BG_END     = new Color(40, 45, 70);
    private final Color CLR_GLASS_BG   = new Color(255, 255, 255, 15);
    private final Color CLR_ACCENT     = new Color(212, 175, 55);
    private final Color CLR_WHITE      = new Color(245, 245, 245);
    private final Color CLR_LOGOUT     = new Color(255, 80, 80);
    private final Color CLR_NAV_BAR    = new Color(15, 18, 32);

    private final Font FONT_TITLE  = new Font("Inter", Font.ITALIC | Font.BOLD, 42);
    private final Font FONT_SUB    = new Font("SansSerif", Font.PLAIN, 18);
    private final Font FONT_CARD   = new Font("SansSerif", Font.BOLD, 20);

    public StudentView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Faculty Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
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

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createGridMenu(), BorderLayout.CENTER);
        mainPanel.add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(60, 50, 20, 50));

        JLabel lblTitle = new JLabel("Student Dashboard", SwingConstants.LEFT);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(CLR_WHITE);

        JLabel lblSub = new JLabel("Welcome back!");
        lblSub.setFont(FONT_SUB);
        lblSub.setForeground(CLR_ACCENT);

        header.add(lblTitle);
        header.add(lblSub);
        return header;
    }

    private JPanel createGridMenu() {
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(20, 50, 50, 50));

        gridPanel.add(createNavCard("Profile", "Manage student profiles", e -> openView(new StudentProfileView())));
        gridPanel.add(createNavCard("Time Table", "Get to know your schedule", e -> openView(new TimeTableView("Student"))));
        gridPanel.add(createNavCard("Courses", "Courses & Grades", e -> openView(new CourseEnrolled("Student"))));

        return gridPanel;
    }

    private JPanel createNavCard(String title, String desc, java.awt.event.ActionListener action) {
        JPanel card = new JPanel(new BorderLayout()) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                    public void mousePressed(MouseEvent e) { if(action != null) action.actionPerformed(null); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (hovered) {
                    g2.setColor(new Color(212, 175, 55, 40));
                    g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 25, 25);
                }

                g2.setColor(hovered ? new Color(255, 255, 255, 30) : CLR_GLASS_BG);
                g2.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 25, 25);

                g2.setColor(hovered ? CLR_ACCENT : new Color(255,255,255,40));
                g2.setStroke(new BasicStroke(hovered ? 2 : 1));
                g2.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 25, 25);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_CARD);
        lblTitle.setForeground(CLR_ACCENT);

        JTextArea lblDesc = new JTextArea(desc);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDesc.setForeground(CLR_WHITE);
        lblDesc.setOpaque(false);
        lblDesc.setEditable(false);
        lblDesc.setFocusable(false);
        lblDesc.setLineWrap(true);
        lblDesc.setWrapStyleWord(true);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblDesc, BorderLayout.CENTER);

        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(0, 0, 30, 50));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLogout.setForeground(CLR_LOGOUT);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorder(BorderFactory.createLineBorder(CLR_LOGOUT, 1));
        btnLogout.setPreferredSize(new Dimension(160, 35));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogout.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION) == 0) {
                new SignInView().setVisible(true);
                this.dispose();
            }
        });

        footer.add(btnLogout);
        return footer;
    }

    private void openView(JFrame frame) {
        if (frame != null) {
            frame.setVisible(true);
            this.dispose();
        }
    }
}
