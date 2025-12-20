package laundry.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import laundry.Main;
import laundry.model.Laundry;
import laundry.model.OrderStatus;

public class Menu extends JFrame {
    private final Main app;
    private JPanel sidebar;
    private JPanel contentArea;
    private String activeMenu = "DASHBOARD";

    public Menu(Main app) {
        this.app = app;
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        createSidebar();
        createContentArea();

        root.add(sidebar, BorderLayout.WEST);
        root.add(contentArea, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void createSidebar() {
        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(30, 58, 95));
        sidebar.setPreferredSize(new Dimension(250, 0));

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));

        JLabel adminLabel = new JLabel("ADMIN DASHBOARD");
        adminLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        adminLabel.setForeground(new Color(200, 200, 200));
        adminLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topSection.add(adminLabel, BorderLayout.CENTER);

        sidebar.add(topSection, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        menuPanel.add(createMenuItem("📋", "LIST DATA", "LIST"));
        menuPanel.add(createMenuItem("📁", "INPUT DATA", "INPUT"));
        menuPanel.add(createMenuItem("👤", "Users", "USERS"));
        menuPanel.add(createMenuItem("📊", "LAPORAN", "REPORT"));

        sidebar.add(menuPanel, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("🚪 LOGOUT");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(0, 50));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(app).setVisible(true);
        });
        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(200, 35, 51));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(220, 53, 69));
            }
        });

        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        logoutPanel.add(logoutBtn, BorderLayout.CENTER);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);
    }

    private JPanel createMenuItem(String icon, String text, String menuId) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boolean isActive = activeMenu.equals(menuId);
        Color bgColor = isActive ? new Color(52, 73, 94) : new Color(30, 58, 95);
        Color textColor = Color.WHITE;

        item.setBackground(bgColor);
        item.setOpaque(true);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        iconLabel.setForeground(textColor);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        textLabel.setForeground(textColor);

        item.add(iconLabel, BorderLayout.WEST);
        item.add(textLabel, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                navigateToMenu(menuId);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive) {
                    item.setBackground(new Color(40, 68, 105));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) {
                    item.setBackground(bgColor);
                }
            }
        });

        return item;
    }

    private void navigateToMenu(String menuId) {
        dispose();
        switch (menuId) {
            case "LIST":
                new ListData(app).setVisible(true);
                break;
            case "INPUT":
                new InputF(app).setVisible(true);
                break;
            case "USERS":
                JOptionPane.showMessageDialog(this, "Fitur Users belum tersedia", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "REPORT":
                new ReportF(app).setVisible(true);
                break;
            default:
                new Menu(app).setVisible(true);
                break;
        }
    }

    private void createContentArea() {
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(new Color(245, 247, 250));

        JPanel header = createHeader();
        JPanel metricsPanel = createMetricsPanel();
        JPanel ordersPanel = createRecentOrdersPanel();

        contentArea.add(header, BorderLayout.NORTH);
        contentArea.add(metricsPanel, BorderLayout.CENTER);
        contentArea.add(ordersPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel("Orders Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 58, 95));
        header.add(titleLabel, BorderLayout.WEST);

        return header;
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        List<Laundry> orders = app.getOrders();
        
        long totalOrders = orders.size();
        long pending = 0;
        long completed = 0;
        double totalRevenue = 0.0;
        double totalWeight = 0.0;
        
        for (Laundry order : orders) {
            if (order.getStatus() == OrderStatus.PENDING) {
                pending++;
            } else if (order.getStatus() == OrderStatus.SELESAI) {
                completed++;
            }
            
            double price = app.getService().calculatePrice(order);
            totalRevenue += price;
            totalWeight += order.getWeightKg();
        }

        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setMaximumFractionDigits(0);

        panel.add(createStatCard("Total Omset", "Rp " + currencyFormat.format(totalRevenue), new Color(33, 150, 243)));
        panel.add(createStatCard("Pesanan Pending", String.valueOf(pending), new Color(255, 193, 7)));
        panel.add(createStatCard("Pesanan Selesai", String.valueOf(completed), new Color(76, 175, 80)));
        panel.add(createStatCard("Total Pesanan", String.valueOf(totalOrders), new Color(255, 152, 0)));
        panel.add(createStatCard("Total Berat (kg)", String.format("%.1f", totalWeight), new Color(244, 67, 54)));

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                int arc = 20;
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);

        JLabel valueLabel = new JLabel(value != null ? value : "0");
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setOpaque(false);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        valueLabel.setVerticalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.WEST);
        card.add(valueLabel, BorderLayout.EAST);

        return card;
    }

    private JPanel createRecentOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JLabel titleLabel = new JLabel("Recent Orders");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 58, 95));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Service Type", "Status", "Pickup Date", "Total Amount"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Laundry> orders = app.getOrders();
        orders.sort(Comparator.comparing(Laundry::getOrderDate).reversed());
        List<Laundry> recentOrders = orders.subList(0, Math.min(10, orders.size()));

        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setMaximumFractionDigits(0);

        for (int i = 0; i < recentOrders.size(); i++) {
            Laundry order = recentOrders.get(i);
            String orderId = "ORD-" + String.format("%03d", i + 1);
            String serviceType = order.getServiceType().name();
            String status = order.getStatus().name();
            String pickupDate = order.getOrderDate().toString();
            String totalAmount = "Rp " + currencyFormat.format(app.getService().calculatePrice(order));

            model.addRow(new Object[]{orderId, serviceType, status, pickupDate, totalAmount});
        }

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(30, 58, 95));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2) {
                    String status = value.toString();
                    if (status.equals("PENDING")) {
                        c.setBackground(new Color(255, 193, 7));
                        c.setForeground(Color.WHITE);
                    } else if (status.equals("SELESAI")) {
                        c.setBackground(new Color(76, 175, 80));
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(33, 150, 243));
                        c.setForeground(Color.WHITE);
                    }
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    ((JLabel) c).setOpaque(true);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                }
                return c;
            }
        };
        table.getColumnModel().getColumn(2).setCellRenderer(statusRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
