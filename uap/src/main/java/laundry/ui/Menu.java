package laundry.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import laundry.Main;
import laundry.model.Laundry;
import laundry.model.OrderStatus;
import laundry.model.ServiceType;
import laundry.exception.Validation;

public class Menu extends JFrame {
    private final Main app;
    private JPanel sidebar;
    private CardLayout cardLayout;
    private JPanel contentArea;
    private String activeMenu = "Overview";
    private JButton dashboardBtn;
    private JButton listBtn;
    private JButton inputBtn;
    private JButton usersBtn;
    private JButton reportBtn;

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
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        menuPanel.add(Box.createVerticalStrut(5));
        dashboardBtn = createMenuItem("🏠", "Overview", "DASHBOARD");
        menuPanel.add(dashboardBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        listBtn = createMenuItem("📋", "List Data", "LIST");
        menuPanel.add(listBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        inputBtn = createMenuItem("📁", "Input Data", "INPUT");
        menuPanel.add(inputBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        usersBtn = createMenuItem("👤", "Users", "USERS");
        menuPanel.add(usersBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        reportBtn = createMenuItem("📊", "Laporan", "REPORT");
        menuPanel.add(reportBtn);

        sidebar.add(menuPanel, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("🚪 LOGOUT") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(200, 35, 51);
                } else if (getModel().isRollover()) {
                    bgColor = new Color(200, 35, 51);
                } else {
                    bgColor = new Color(220, 53, 69);
                }
                int arc = 20;
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                
                Color borderColor = new Color(180, 30, 45);
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(0, 50));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(app).setVisible(true);
        });

        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        logoutPanel.add(logoutBtn, BorderLayout.CENTER);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuItem(String icon, String text, String menuId) {
        Color defaultBgColor = new Color(30, 58, 95);
        Color activeBgColor = new Color(52, 73, 94);
        Color hoverBgColor = new Color(40, 68, 105);
        Color borderColor = new Color(100, 120, 150);
        
        JButton button = new JButton(icon + "  " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isActive = activeMenu.equals(menuId);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = activeBgColor;
                } else if (getModel().isRollover()) {
                    bgColor = isActive ? activeBgColor : hoverBgColor;
                } else {
                    bgColor = isActive ? activeBgColor : defaultBgColor;
                }
                
                int arc = 20;
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 50));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        
        button.addActionListener(e -> navigateToMenu(menuId));
        
        return button;
    }

    private void navigateToMenu(String menuId) {
        activeMenu = menuId;
        updateButtonStates();
        if (menuId.equals("LIST") && listDataModel != null) {
            refreshListTable(listDataModel);
        }
        cardLayout.show(contentArea, menuId);
    }

    private void updateButtonStates() {
        dashboardBtn.repaint();
        listBtn.repaint();
        inputBtn.repaint();
        usersBtn.repaint();
        reportBtn.repaint();
    }

    private void createContentArea() {
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(new Color(245, 247, 250));

        contentArea.add(createDashboardPanel(), "DASHBOARD");
        contentArea.add(createListDataPanel(), "LIST");
        contentArea.add(createInputDataPanel(), "INPUT");
        contentArea.add(createUsersPanel(), "USERS");
        contentArea.add(createReportPanel(), "REPORT");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JPanel header = createHeader();
        JPanel metricsPanel = createMetricsPanel();
        JPanel ordersPanel = createRecentOrdersPanel();

        panel.add(header, BorderLayout.NORTH);
        panel.add(metricsPanel, BorderLayout.CENTER);
        panel.add(ordersPanel, BorderLayout.SOUTH);

        return panel;
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

    private JPanel createListDataPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel("List Pesanan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 58, 95));
        header.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        JTextField searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2d.dispose();
            }
        };
        searchField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        searchField.setOpaque(false);
        searchField.setBackground(Color.WHITE);
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JButton searchBtn = new JButton("Cari") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(33, 150, 243).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(33, 150, 243).brighter();
                } else {
                    bgColor = new Color(33, 150, 243);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        searchBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setContentAreaFilled(false);
        searchBtn.setPreferredSize(new Dimension(100, 40));
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        header.add(searchPanel, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        String[] cols = {"Nama", "Layanan", "Berat (kg)", "Tanggal", "Status"};
        listDataModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(listDataModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
                }
                return c;
            }
        };
        table.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(listDataModel);
        table.setRowSorter(sorter);
        table.setRowHeight(50);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(33, 150, 243, 30));
        table.setSelectionForeground(new Color(30, 58, 95));
        table.setGridColor(new Color(240, 240, 240));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
        tableHeader.setBackground(new Color(30, 58, 95));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 50));
        tableHeader.setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("SansSerif", Font.PLAIN, 14));
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
                }
                return c;
            }
        };

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("SansSerif", Font.PLAIN, 14));
                ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
                }
                return c;
            }
        };

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = value.toString();
                c.setFont(new Font("SansSerif", Font.BOLD, 13));
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(8, 15, 8, 15),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
                ((JLabel) c).setOpaque(true);
                
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
                
                if (isSelected) {
                    c.setBackground(((Color) c.getBackground()).darker());
                }
                
                return c;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(statusRenderer);

        refreshListTable(listDataModel);

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            sorter.setRowFilter(keyword.isEmpty() ? null : RowFilter.regexFilter("(?i)" + keyword));
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setOpaque(true);

        root.add(scrollPane, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JButton editBtn = new JButton("Edit") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(76, 175, 80).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(76, 175, 80).brighter();
                } else {
                    bgColor = new Color(76, 175, 80);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        editBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        editBtn.setForeground(Color.WHITE);
        editBtn.setBorderPainted(false);
        editBtn.setFocusPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setPreferredSize(new Dimension(100, 40));
        
        JButton delBtn = new JButton("Hapus") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(244, 67, 54).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(244, 67, 54).brighter();
                } else {
                    bgColor = new Color(244, 67, 54);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        delBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        delBtn.setForeground(Color.WHITE);
        delBtn.setBorderPainted(false);
        delBtn.setFocusPainted(false);
        delBtn.setContentAreaFilled(false);
        delBtn.setPreferredSize(new Dimension(100, 40));

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data untuk edit");
                return;
            }
            int modelIndex = table.convertRowIndexToModel(row);
            Laundry selected = app.getOrders().get(modelIndex);
            navigateToMenu("INPUT");
            setEditingOrderInInputPanel(selected);
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data untuk hapus");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            int modelIndex = table.convertRowIndexToModel(row);
            app.getOrders().remove(modelIndex);
            app.persist();
            refreshListTable(listDataModel);
        });

        actions.add(editBtn);
        actions.add(delBtn);
        root.add(actions, BorderLayout.SOUTH);

        return root;
    }

    private void refreshListTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Laundry o : app.getOrders()) {
            model.addRow(new Object[]{
                o.getCustomerName(),
                o.getServiceType().name(),
                o.getWeightKg(),
                o.getOrderDate().toString(),
                o.getStatus().name()
            });
        }
    }

    private JTextField inputNameField;
    private JTextField inputWeightField;
    private JTextField inputDateField;
    private JComboBox<ServiceType> inputServiceBox;
    private JComboBox<OrderStatus> inputStatusBox;
    private Laundry editingOrder;
    private DefaultTableModel listDataModel;

    private JPanel createInputDataPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel("Form Pesanan Laundry");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 58, 95));
        header.add(titleLabel, BorderLayout.WEST);

        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inputNameField = new JTextField();
        inputWeightField = new JTextField();
        inputServiceBox = new JComboBox<>(ServiceType.values());
        inputStatusBox = new JComboBox<>(OrderStatus.values());
        inputDateField = new JTextField(LocalDate.now().toString());

        form.add(new JLabel("Nama:"));
        form.add(inputNameField);
        form.add(new JLabel("Berat (kg):"));
        form.add(inputWeightField);
        form.add(new JLabel("Layanan:"));
        form.add(inputServiceBox);
        form.add(new JLabel("Status:"));
        form.add(inputStatusBox);
        form.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        form.add(inputDateField);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JButton calcBtn = new JButton("Hitung Harga");
        JButton saveBtn = new JButton("Simpan");
        calcBtn.setBackground(new Color(255, 193, 7));
        calcBtn.setForeground(Color.BLACK);
        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.WHITE);

        calcBtn.addActionListener(e -> {
            try {
                Laundry temp = buildOrderFromInputForm();
                double price = app.getService().calculatePrice(temp);
                JOptionPane.showMessageDialog(this, "Estimasi Harga: Rp " + (long) price,
                    "Estimasi", JOptionPane.INFORMATION_MESSAGE);
            } catch (Validation ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                Laundry order = buildOrderFromInputForm();
                if (editingOrder == null) {
                    app.getService().validateOrder(order);
                    app.getOrders().add(order);
                } else {
                    editingOrder.setCustomerName(order.getCustomerName());
                    editingOrder.setWeightKg(order.getWeightKg());
                    editingOrder.setServiceType(order.getServiceType());
                    editingOrder.setStatus(order.getStatus());
                    editingOrder.setOrderDate(order.getOrderDate());
                }
                app.persist();
                JOptionPane.showMessageDialog(this, "Pesanan tersimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                editingOrder = null;
                clearInputForm();
                navigateToMenu("LIST");
            } catch (Validation ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        actions.add(calcBtn);
        actions.add(saveBtn);
        root.add(actions, BorderLayout.SOUTH);

        return root;
    }

    private void setEditingOrderInInputPanel(Laundry order) {
        editingOrder = order;
        inputNameField.setText(order.getCustomerName());
        inputWeightField.setText(String.valueOf(order.getWeightKg()));
        inputServiceBox.setSelectedItem(order.getServiceType());
        inputStatusBox.setSelectedItem(order.getStatus());
        inputDateField.setText(order.getOrderDate().toString());
    }

    private void clearInputForm() {
        inputNameField.setText("");
        inputWeightField.setText("");
        inputServiceBox.setSelectedIndex(0);
        inputStatusBox.setSelectedIndex(0);
        inputDateField.setText(LocalDate.now().toString());
    }

    private Laundry buildOrderFromInputForm() throws Validation {
        String name = inputNameField.getText().trim();
        double weight;
        try {
            weight = Double.parseDouble(inputWeightField.getText().trim());
        } catch (Exception e) {
            throw new Validation("Berat harus berupa angka desimal");
        }
        ServiceType service = (ServiceType) inputServiceBox.getSelectedItem();
        OrderStatus status = (OrderStatus) inputStatusBox.getSelectedItem();
        LocalDate date;
        try {
            date = LocalDate.parse(inputDateField.getText().trim());
        } catch (Exception e) {
            throw new Validation("Format tanggal harus YYYY-MM-DD");
        }

        Laundry order = new Laundry(name, service, weight, date, status);
        app.getService().validateOrder(order);
        return order;
    }

    private JPanel createUsersPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel("Users Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 58, 95));
        header.add(titleLabel, BorderLayout.WEST);

        root.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel infoLabel = new JLabel("Fitur Users belum tersedia");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoLabel.setForeground(new Color(100, 100, 100));
        center.add(infoLabel, gbc);

        root.add(center, BorderLayout.CENTER);

        return root;
    }

    private JPanel createReportPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel("Laporan & Ringkasan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 58, 95));
        header.add(titleLabel, BorderLayout.WEST);

        root.add(header, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 12));
        cards.setBackground(new Color(245, 247, 250));
        cards.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        int total = app.getOrders().size();
        long selesai = app.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.SELESAI).count();
        long pending = app.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        double totalKg = app.getOrders().stream().mapToDouble(Laundry::getWeightKg).sum();
        double revenue = app.getOrders().stream().mapToDouble(o -> app.getService().calculatePrice(o)).sum();

        cards.add(createReportStatCard("Total Pesanan", String.valueOf(total), new Color(33, 150, 243)));
        cards.add(createReportStatCard("Selesai", String.valueOf(selesai), new Color(76, 175, 80)));
        cards.add(createReportStatCard("Pending", String.valueOf(pending), new Color(255, 193, 7)));
        cards.add(createReportStatCard("Total Berat (kg)", String.format("%.2f", totalKg), new Color(156, 39, 176)));

        JTextArea detail = new JTextArea();
        detail.setEditable(false);
        detail.setFont(new Font("Monospaced", Font.PLAIN, 13));
        detail.setBackground(Color.WHITE);
        detail.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Map<ServiceType, Long> byService = app.getOrders().stream()
            .collect(Collectors.groupingBy(Laundry::getServiceType, Collectors.counting()));
        StringBuilder sb = new StringBuilder();
        sb.append("Jumlah pesanan per layanan:\n");
        byService.forEach((svc, cnt) -> sb.append(String.format("- %s: %d%n", svc.name(), cnt)));
        sb.append("\nPerkiraan Omzet: Rp ").append((long) revenue);
        detail.setText(sb.toString());

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBackground(new Color(245, 247, 250));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        bottom.add(new JScrollPane(detail), BorderLayout.CENTER);

        root.add(cards, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        return root;
    }

    private JPanel createReportStatCard(String title, String value, Color color) {
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
