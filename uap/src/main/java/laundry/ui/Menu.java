package laundry.ui;

import javax.swing.*;
import java.awt.*;
import laundry.Main;

public class Menu extends JFrame {
    private final Main app;

    public Menu(Main app) {
        this.app = app;
        setTitle("Menu Navigasi");
        setSize(560, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Dashboard Laundry", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        root.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setBackground(root.getBackground());
        grid.setBorder(BorderFactory.createEmptyBorder(20, 28, 28, 28));

        JButton listBtn = styled("List Data", new Color(33,150,243));
        JButton inputBtn = styled("Input Data", new Color(76,175,80));
        JButton reportBtn = styled("Laporan", new Color(255,193,7));
        JButton logoutBtn = styled("Logout", new Color(244,67,54));

        listBtn.addActionListener(e -> { dispose(); new ListData(app).setVisible(true); });
        inputBtn.addActionListener(e -> { dispose(); new InputF(app).setVisible(true); });
        reportBtn.addActionListener(e -> { dispose(); new ReportF(app).setVisible(true); });
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame(app).setVisible(true); });

        grid.add(listBtn); grid.add(inputBtn); grid.add(reportBtn); grid.add(logoutBtn);
        root.add(grid, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JButton styled(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setFocusPainted(false);
        return b;
    }
}
