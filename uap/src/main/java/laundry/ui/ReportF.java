package laundry.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

import laundry.Main;
import laundry.model.Laundry;
import laundry.model.OrderStatus;
import laundry.model.ServiceType;

public class ReportF extends JFrame {
    private final Main app;

    public ReportF(Main app) {
        this.app = app;
        setTitle("Laporan & Ringkasan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Laporan & Ringkasan", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        root.add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 12));
        cards.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        int total = app.getOrders().size();
        long selesai = app.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.SELESAI).count();
        long pending = app.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        double totalKg = app.getOrders().stream().mapToDouble(Laundry::getWeightKg).sum();
        double revenue = app.getOrders().stream().mapToDouble(o -> app.getService().calculatePrice(o)).sum();

        cards.add(statCard("Total Pesanan", String.valueOf(total), new Color(33,150,243)));
        cards.add(statCard("Selesai", String.valueOf(selesai), new Color(76,175,80)));
        cards.add(statCard("Pending", String.valueOf(pending), new Color(255,193,7)));
        cards.add(statCard("Total Berat (kg)", String.format("%.2f", totalKg), new Color(156,39,176)));

        JTextArea detail = new JTextArea();
        detail.setEditable(false);
        detail.setFont(new Font("Monospaced", Font.PLAIN, 13));
        Map<ServiceType, Long> byService = app.getOrders().stream()
                .collect(Collectors.groupingBy(Laundry::getServiceType, Collectors.counting()));
        StringBuilder sb = new StringBuilder();
        sb.append("Jumlah pesanan per layanan:\n");
        byService.forEach((svc, cnt) -> sb.append(String.format("- %s: %d%n", svc.name(), cnt)));
        sb.append("\nPerkiraan Omzet: Rp ").append((long) revenue);
        detail.setText(sb.toString());

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        JButton backBtn = new JButton("Kembali ke Menu");
        backBtn.setBackground(new Color(33,150,243)); backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> { dispose(); new Menu(app).setVisible(true); });

        bottom.add(new JScrollPane(detail), BorderLayout.CENTER);
        bottom.add(backBtn, BorderLayout.SOUTH);

        root.add(cards, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel statCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel v = new JLabel(value);
        v.setForeground(Color.WHITE);
        v.setFont(new Font("SansSerif", Font.BOLD, 22));
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }
}
