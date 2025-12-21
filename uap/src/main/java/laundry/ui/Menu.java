package laundry.ui;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.DefaultListCellRenderer;
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
import laundry.model.User;
import laundry.exception.Validation;
import laundry.repo.UserCsv;
import laundry.exception.Data;

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
        } else if (menuId.equals("USERS")) {
            refreshUsersTable();
        } else if (menuId.equals("DASHBOARD") || menuId.equals("Overview")) {
            refreshDashboardPanel();
        } else if (menuId.equals("REPORT")) {
            refreshReportPanel();
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

        dashboardPanel = createDashboardPanel();
        contentArea.add(dashboardPanel, "DASHBOARD");
        contentArea.add(createListDataPanel(), "LIST");
        contentArea.add(createInputDataPanel(), "INPUT");
        contentArea.add(createUsersPanel(), "USERS");
        reportPanel = createReportPanel();
        contentArea.add(reportPanel, "REPORT");
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

        List<Laundry> orders = new java.util.ArrayList<>(app.getOrders());
        orders.sort(Comparator.comparing(Laundry::getOrderDate).reversed());
        List<Laundry> recentOrders = orders.subList(0, Math.min(10, orders.size()));

        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setMaximumFractionDigits(0);

        List<Laundry> allOrders = app.getOrders();
        for (Laundry order : recentOrders) {
            int actualIndex = allOrders.indexOf(order);
            String orderId = "ORD-" + String.format("%03d", actualIndex + 1);
            String serviceType = getServiceTypeDisplayName(order.getServiceType());
            String status = order.getStatus().name();
            String pickupDate = order.getPickupDate().toString();
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
        JTable table = new JTable(listDataModel);
        table.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(listDataModel);
        table.setRowSorter(sorter);
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
        for (int i = 1; i < table.getColumnCount(); i++) {
            if (i != 4) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 4) {
                    String status = value.toString();
                    if (status.equals("PENDING")) {
                        c.setBackground(new Color(255, 152, 0));
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
        editBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        editBtn.setForeground(Color.WHITE);
        editBtn.setBorderPainted(false);
        editBtn.setFocusPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setPreferredSize(new Dimension(100, 40));
        
        JButton completeBtn = new JButton("Tandai Selesai") {
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
        completeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setBorderPainted(false);
        completeBtn.setFocusPainted(false);
        completeBtn.setContentAreaFilled(false);
        completeBtn.setPreferredSize(new Dimension(140, 40));
        
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
            if (selected.getStatus() == OrderStatus.SELESAI) {
                showInfoDialog(this, "Pesanan yang sudah selesai tidak dapat diedit");
                return;
            }
            showEditOrderDialog(selected, modelIndex);
        });

        completeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data untuk tandai selesai");
                return;
            }
            int modelIndex = table.convertRowIndexToModel(row);
            Laundry selected = app.getOrders().get(modelIndex);
            if (selected.getStatus() == OrderStatus.SELESAI) {
                showInfoDialog(this, "Pesanan ini sudah selesai");
                return;
            }
            boolean confirmed = showCompleteConfirmDialog();
            if (!confirmed) return;
            selected.setStatus(OrderStatus.SELESAI);
            app.persist();
            refreshListTable(listDataModel);
            refreshOverviewAndReport();
            showCompleteSuccessDialog();
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data untuk hapus");
                return;
            }
            int modelIndex = table.convertRowIndexToModel(row);
            Laundry selected = app.getOrders().get(modelIndex);
            if (selected.getStatus() == OrderStatus.SELESAI) {
                showInfoDialog(this, "Pesanan yang sudah selesai tidak dapat dihapus");
                return;
            }
            boolean confirmed = showDeleteConfirmDialog();
            if (!confirmed) return;
            app.getOrders().remove(modelIndex);
            app.persist();
            refreshListTable(listDataModel);
            refreshOverviewAndReport();
            showDeleteSuccessDialog();
        });

        actions.add(editBtn);
        actions.add(completeBtn);
        actions.add(delBtn);
        root.add(actions, BorderLayout.SOUTH);

        return root;
    }

    private String getServiceTypeDisplayName(ServiceType serviceType) {
        switch (serviceType) {
            case CUCI_KERING:
                return "Cuci Kering";
            case SETRIKA:
                return "Setrika Saja";
            case CUCI_SETIRIKA:
                return "Cuci Setrika";
            default:
                return serviceType.name();
        }
    }

    private void refreshListTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Laundry o : app.getOrders()) {
            model.addRow(new Object[]{
                o.getCustomerName(),
                getServiceTypeDisplayName(o.getServiceType()),
                o.getWeightKg(),
                o.getOrderDate().toString(),
                o.getStatus().name()
            });
        }
    }

    private JTextField inputNameField;
    private JTextField inputWeightField;
    private JTextField inputDateField;
    private JTextField inputPickupDateField;
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

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        inputNameField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        inputNameField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        inputNameField.setOpaque(false);
        inputNameField.setBackground(Color.WHITE);
        inputNameField.setPreferredSize(new Dimension(300, 40));

        inputWeightField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        inputWeightField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        inputWeightField.setOpaque(false);
        inputWeightField.setBackground(Color.WHITE);
        inputWeightField.setPreferredSize(new Dimension(300, 40));

        inputServiceBox = new JComboBox<ServiceType>(ServiceType.values()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        inputServiceBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ServiceType) {
                    ((JLabel) c).setText(getServiceTypeDisplayName((ServiceType) value));
                }
                return c;
            }
        });
        inputServiceBox.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        inputServiceBox.setOpaque(false);
        inputServiceBox.setBackground(Color.WHITE);
        inputServiceBox.setPreferredSize(new Dimension(300, 40));
        inputServiceBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                        g2d.setColor(new Color(100, 100, 100));
                        int[] xPoints = {getWidth() / 2 - 5, getWidth() / 2, getWidth() / 2 + 5};
                        int[] yPoints = {getHeight() / 2 - 2, getHeight() / 2 + 3, getHeight() / 2 - 2};
                        g2d.fillPolygon(xPoints, yPoints, 3);
                        g2d.dispose();
                    }
                };
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                return button;
            }
        });

        inputStatusBox = new JComboBox<OrderStatus>(OrderStatus.values()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        inputStatusBox.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        inputStatusBox.setOpaque(false);
        inputStatusBox.setBackground(Color.WHITE);
        inputStatusBox.setPreferredSize(new Dimension(300, 40));
        inputStatusBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                        g2d.setColor(new Color(100, 100, 100));
                        int[] xPoints = {getWidth() / 2 - 5, getWidth() / 2, getWidth() / 2 + 5};
                        int[] yPoints = {getHeight() / 2 - 2, getHeight() / 2 + 3, getHeight() / 2 - 2};
                        g2d.fillPolygon(xPoints, yPoints, 3);
                        g2d.dispose();
                    }
                };
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                return button;
            }
        });

        LocalDate today = LocalDate.now();
        inputDateField = new JTextField(today.toString()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        inputDateField.setEditable(false);
        inputDateField.setEnabled(false);
        inputDateField.setBackground(new Color(240, 240, 240));
        inputDateField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        inputDateField.setOpaque(false);
        inputDateField.setPreferredSize(new Dimension(300, 40));

        inputPickupDateField = new JTextField(today.plusDays(1).toString()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        inputPickupDateField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        inputPickupDateField.setOpaque(false);
        inputPickupDateField.setBackground(Color.WHITE);
        inputPickupDateField.setPreferredSize(new Dimension(300, 40));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Nama:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(inputNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel weightLabel = new JLabel("Berat (kg):");
        weightLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(weightLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(inputWeightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel serviceLabel = new JLabel("Layanan:");
        serviceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(serviceLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(inputServiceBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(inputStatusBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel orderDateLabel = new JLabel("Tanggal Order:");
        orderDateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(orderDateLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(inputDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel pickupDateLabel = new JLabel("Tanggal Pickup:");
        pickupDateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(pickupDateLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(inputPickupDateField, gbc);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JButton calcBtn = new JButton("Hitung Harga") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(255, 193, 7).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(255, 193, 7).brighter();
                } else {
                    bgColor = new Color(255, 193, 7);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        calcBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        calcBtn.setForeground(Color.BLACK);
        calcBtn.setBorderPainted(false);
        calcBtn.setFocusPainted(false);
        calcBtn.setContentAreaFilled(false);
        calcBtn.setPreferredSize(new Dimension(130, 40));
        
        JButton saveBtn = new JButton("Simpan") {
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
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setContentAreaFilled(false);
        saveBtn.setPreferredSize(new Dimension(100, 40));

        calcBtn.addActionListener(e -> {
            try {
                Laundry temp = buildOrderFromInputForm();
                double price = app.getService().calculatePrice(temp);
                showInfoDialog(this, "Estimasi Harga: Rp " + (long) price);
            } catch (Validation ex) {
                showInfoDialog(this, ex.getMessage());
            } catch (Exception ex) {
                showInfoDialog(this, "Input tidak valid: " + ex.getMessage());
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                if (editingOrder == null) {
                    inputDateField.setText(LocalDate.now().toString());
                    Laundry order = buildOrderFromInputForm();
                    app.getService().validateOrder(order);
                    app.getOrders().add(order);
                } else {
                    Laundry order = buildOrderFromInputForm();
                    editingOrder.setCustomerName(order.getCustomerName());
                    editingOrder.setWeightKg(order.getWeightKg());
                    editingOrder.setServiceType(order.getServiceType());
                    editingOrder.setStatus(order.getStatus());
                    editingOrder.setPickupDate(order.getPickupDate());
                }
                app.persist();
                refreshOverviewAndReport();
                JOptionPane.showMessageDialog(this, "Pesanan tersimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                editingOrder = null;
                clearInputForm();
                navigateToMenu("LIST");
            } catch (Validation ex) {
                showInfoDialog(this, ex.getMessage());
            } catch (Exception ex) {
                showInfoDialog(this, "Gagal menyimpan: " + ex.getMessage());
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
        inputDateField.setEditable(false);
        inputDateField.setEnabled(false);
        inputPickupDateField.setText(order.getPickupDate().toString());
    }

    private void clearInputForm() {
        inputNameField.setText("");
        inputWeightField.setText("");
        inputServiceBox.setSelectedIndex(0);
        inputStatusBox.setSelectedIndex(0);
        LocalDate today = LocalDate.now();
        inputDateField.setText(today.toString());
        inputPickupDateField.setText(today.plusDays(1).toString());
        inputDateField.setEditable(false);
        inputDateField.setEnabled(false);
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
        LocalDate orderDate;
        try {
            orderDate = LocalDate.parse(inputDateField.getText().trim());
        } catch (Exception e) {
            throw new Validation("Format tanggal order harus YYYY-MM-DD");
        }
        LocalDate pickupDate;
        try {
            pickupDate = LocalDate.parse(inputPickupDateField.getText().trim());
        } catch (Exception e) {
            throw new Validation("Format tanggal pickup harus YYYY-MM-DD");
        }

        Laundry order = new Laundry(name, service, weight, orderDate, pickupDate, status);
        app.getService().validateOrder(order);
        return order;
    }

    private DefaultTableModel usersModel;
    private UserCsv usersRepo;
    private LocalDate reportStartDate = null;
    private LocalDate reportEndDate = null;
    private JPanel reportPanel;
    private JPanel dashboardPanel;

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

        String[] cols = {"Username", "Password"};
        usersModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(usersModel);
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

        usersRepo = new UserCsv("src/main/resources/data/users.csv");
        refreshUsersTable();

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
            try {
                List<User> users = usersRepo.loadAll();
                User selected = users.get(modelIndex);
                showEditUserDialog(selected, modelIndex);
            } catch (Data ex) {
                JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data untuk hapus");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            try {
                int modelIndex = table.convertRowIndexToModel(row);
                List<User> users = usersRepo.loadAll();
                users.remove(modelIndex);
                usersRepo.saveAll(users);
                refreshUsersTable();
                JOptionPane.showMessageDialog(this, "User berhasil dihapus", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Data ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        actions.add(editBtn);
        actions.add(delBtn);
        root.add(actions, BorderLayout.SOUTH);

        return root;
    }

    private void refreshUsersTable() {
        if (usersModel == null || usersRepo == null) return;
        usersModel.setRowCount(0);
        try {
            List<User> users = usersRepo.loadAll();
            for (User user : users) {
                usersModel.addRow(new Object[]{
                    user.getUsername(),
                    user.getPassword()
                });
            }
        } catch (Data ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEditUserDialog(User user, int index) {
        JDialog editDialog = new JDialog(this, "Edit User", true);
        editDialog.setSize(400, 250);
        editDialog.setLocationRelativeTo(this);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.WHITE);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField usernameField = new JTextField(user.getUsername()) {
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
        usernameField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        usernameField.setOpaque(false);
        usernameField.setBackground(Color.WHITE);
        usernameField.setPreferredSize(new Dimension(200, 35));
        form.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        form.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(user.getPassword()) {
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
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        passwordField.setOpaque(false);
        passwordField.setBackground(Color.WHITE);
        passwordField.setPreferredSize(new Dimension(200, 35));
        form.add(passwordField, gbc);

        rootPanel.add(form, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton cancelBtn = new JButton("Batal");
        cancelBtn.setPreferredSize(new Dimension(80, 35));
        cancelBtn.addActionListener(e -> editDialog.dispose());
        
        JButton saveBtn = new JButton("Simpan") {
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
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setContentAreaFilled(false);
        saveBtn.setPreferredSize(new Dimension(80, 35));
        
        saveBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                List<User> users = usersRepo.loadAll();
                for (int i = 0; i < users.size(); i++) {
                    if (i != index && users.get(i).getUsername().equals(username)) {
                        JOptionPane.showMessageDialog(editDialog, "Username sudah digunakan!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                users.get(index).setUsername(username);
                users.get(index).setPassword(password);
                usersRepo.saveAll(users);
                refreshUsersTable();
                editDialog.dispose();
                JOptionPane.showMessageDialog(this, "User berhasil diupdate", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Data ex) {
                JOptionPane.showMessageDialog(editDialog, "Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        editDialog.add(rootPanel);
        editDialog.setVisible(true);
    }

    private void showEditOrderDialog(Laundry order, int index) {
        JDialog editDialog = new JDialog(this, "Edit Pesanan", true);
        editDialog.setSize(500, 450);
        editDialog.setLocationRelativeTo(this);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(227, 242, 253)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        rootPanel.setOpaque(true);

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        dialogPanel.setOpaque(false);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Edit Pesanan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 150, 243));
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialogPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Nama:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField nameField = new JTextField(order.getCustomerName()) {
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
        nameField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        nameField.setOpaque(false);
        nameField.setBackground(Color.WHITE);
        nameField.setPreferredSize(new Dimension(250, 35));
        dialogPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Berat (kg):"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField weightField = new JTextField(String.valueOf(order.getWeightKg())) {
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
        weightField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        weightField.setOpaque(false);
        weightField.setBackground(Color.WHITE);
        weightField.setPreferredSize(new Dimension(250, 35));
        dialogPanel.add(weightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Layanan:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JComboBox<ServiceType> serviceBox = new JComboBox<>(ServiceType.values());
        serviceBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ServiceType) {
                    ((JLabel) c).setText(getServiceTypeDisplayName((ServiceType) value));
                }
                return c;
            }
        });
        serviceBox.setSelectedItem(order.getServiceType());
        serviceBox.setPreferredSize(new Dimension(250, 35));
        dialogPanel.add(serviceBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JComboBox<OrderStatus> statusBox = new JComboBox<>(OrderStatus.values());
        statusBox.setSelectedItem(order.getStatus());
        statusBox.setPreferredSize(new Dimension(250, 35));
        dialogPanel.add(statusBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Tanggal Order (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField dateField = new JTextField(order.getOrderDate().toString()) {
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
        dateField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        dateField.setOpaque(false);
        dateField.setBackground(Color.WHITE);
        dateField.setPreferredSize(new Dimension(250, 35));
        dialogPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Tanggal Pickup (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField pickupDateField = new JTextField(order.getPickupDate().toString()) {
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
        pickupDateField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        pickupDateField.setOpaque(false);
        pickupDateField.setBackground(Color.WHITE);
        pickupDateField.setPreferredSize(new Dimension(250, 35));
        dialogPanel.add(pickupDateField, gbc);

        rootPanel.add(dialogPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JButton cancelBtn = new JButton("Batal") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(158, 158, 158).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(158, 158, 158).brighter();
                } else {
                    bgColor = new Color(158, 158, 158);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelBtn.addActionListener(e -> editDialog.dispose());
        
        JButton saveBtn = new JButton("Simpan") {
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
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setContentAreaFilled(false);
        saveBtn.setPreferredSize(new Dimension(100, 40));
        
        saveBtn.addActionListener(e -> {
            if (order.getStatus() == OrderStatus.SELESAI) {
                showInfoDialog(editDialog, "Pesanan yang sudah selesai tidak dapat diedit");
                return;
            }
            String name = nameField.getText().trim();
            double weight;
            try {
                weight = Double.parseDouble(weightField.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Berat harus berupa angka desimal", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ServiceType service = (ServiceType) serviceBox.getSelectedItem();
            OrderStatus status = (OrderStatus) statusBox.getSelectedItem();
            LocalDate orderDate;
            try {
                orderDate = LocalDate.parse(dateField.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Format tanggal order harus YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate pickupDate;
            try {
                pickupDate = LocalDate.parse(pickupDateField.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Format tanggal pickup harus YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Laundry updatedOrder = new Laundry(name, service, weight, orderDate, pickupDate, status);
                app.getService().validateOrder(updatedOrder);
                order.setCustomerName(name);
                order.setWeightKg(weight);
                order.setServiceType(service);
                order.setStatus(status);
                order.setOrderDate(orderDate);
                order.setPickupDate(pickupDate);
                app.persist();
                refreshListTable(listDataModel);
                refreshOverviewAndReport();
                editDialog.dispose();
                JOptionPane.showMessageDialog(this, "Pesanan berhasil diupdate", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Validation ex) {
                JOptionPane.showMessageDialog(editDialog, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        editDialog.add(rootPanel);
        editDialog.setVisible(true);
    }

    private JPanel createReportPanel() {
        reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel("Reports Overview");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 58, 95));
        header.add(titleLabel, BorderLayout.WEST);

        JPanel headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerButtons.setOpaque(false);

        JButton resetBtn = new JButton("Reset") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(158, 158, 158).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(158, 158, 158).brighter();
                } else {
                    bgColor = new Color(158, 158, 158);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setBorderPainted(false);
        resetBtn.setFocusPainted(false);
        resetBtn.setContentAreaFilled(false);
        resetBtn.setPreferredSize(new Dimension(100, 35));
        resetBtn.addActionListener(e -> {
            reportStartDate = null;
            reportEndDate = null;
            refreshReportPanel();
        });

        JButton dateRangeBtn = new JButton("Date Range") {
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
        dateRangeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        dateRangeBtn.setForeground(Color.WHITE);
        dateRangeBtn.setBorderPainted(false);
        dateRangeBtn.setFocusPainted(false);
        dateRangeBtn.setContentAreaFilled(false);
        dateRangeBtn.setPreferredSize(new Dimension(120, 35));
        dateRangeBtn.addActionListener(e -> showDateRangeDialog());

        headerButtons.add(resetBtn);
        headerButtons.add(dateRangeBtn);
        header.add(headerButtons, BorderLayout.EAST);

        reportPanel.add(header, BorderLayout.NORTH);

        LocalDate endDate = reportEndDate != null ? reportEndDate : LocalDate.now();
        LocalDate startDate = reportStartDate != null ? reportStartDate : endDate.minusDays(6);

        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(new Color(245, 247, 250));
        chartContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        
        JLabel chartTitle = new JLabel("Daily Revenue");
        chartTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        chartTitle.setForeground(new Color(30, 58, 95));
        chartTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        chartContainer.add(chartTitle, BorderLayout.NORTH);
        chartContainer.add(createDailyRevenueChart(startDate, endDate), BorderLayout.CENTER);

        JPanel transactionsContainer = new JPanel(new BorderLayout());
        transactionsContainer.setBackground(new Color(245, 247, 250));
        transactionsContainer.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        transactionsContainer.add(createTransactionsTable(startDate, endDate), BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(chartContainer, BorderLayout.NORTH);
        centerPanel.add(transactionsContainer, BorderLayout.CENTER);

        reportPanel.add(centerPanel, BorderLayout.CENTER);

        return reportPanel;
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

    private Map<LocalDate, Double> calculateDailyRevenue(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Double> dailyRevenue = new java.util.HashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dailyRevenue.put(date, 0.0);
        }
        List<Laundry> filteredOrders = filterOrdersByDateRange(startDate, endDate);
        for (Laundry order : filteredOrders) {
            LocalDate orderDate = order.getOrderDate();
            double price = app.getService().calculatePrice(order);
            dailyRevenue.put(orderDate, dailyRevenue.get(orderDate) + price);
        }
        return dailyRevenue;
    }

    private List<Laundry> filterOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return app.getOrders();
        }
        return app.getOrders().stream()
            .filter(order -> {
                LocalDate orderDate = order.getOrderDate();
                return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
            })
            .collect(Collectors.toList());
    }

    private JPanel createDailyRevenueChart(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Double> dailyRevenue = calculateDailyRevenue(startDate, endDate);
        List<LocalDate> dates = new java.util.ArrayList<>(dailyRevenue.keySet());
        dates.sort(LocalDate::compareTo);
        
        double maxRevenueValue = dailyRevenue.values().stream().mapToDouble(Double::doubleValue).max().orElse(100.0);
        if (maxRevenueValue == 0) maxRevenueValue = 100.0;
        final double maxRevenue = maxRevenueValue;
        double scale = maxRevenue / 100.0;
        
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int padding = 60;
                int chartWidth = width - padding * 2;
                int chartHeight = height - padding * 2;
                int barWidth = chartWidth / dates.size();
                int barSpacing = 5;
                int actualBarWidth = barWidth - barSpacing * 2;
                
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, width, height);
                
                g2d.setColor(new Color(240, 240, 240));
                for (int i = 0; i <= 5; i++) {
                    int y = padding + (chartHeight * i / 5);
                    g2d.drawLine(padding, y, width - padding, y);
                }
                
                g2d.setColor(new Color(100, 100, 100));
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
                FontMetrics fm = g2d.getFontMetrics();
                
                for (int i = 0; i <= 5; i++) {
                    double value = (maxRevenue * (5 - i) / 5.0);
                    String label = String.format("%.0f", value);
                    int y = padding + (chartHeight * i / 5);
                    int labelWidth = fm.stringWidth(label);
                    g2d.drawString(label, padding - labelWidth - 10, y + fm.getAscent() / 2);
                }
                
                g2d.setColor(new Color(33, 150, 243));
                for (int i = 0; i < dates.size(); i++) {
                    LocalDate date = dates.get(i);
                    double revenue = dailyRevenue.get(date);
                    double normalizedRevenue = revenue / scale;
                    int barHeight = (int) (chartHeight * normalizedRevenue / 100.0);
                    int x = padding + i * barWidth + barSpacing;
                    int y = padding + chartHeight - barHeight;
                    
                    g2d.fillRect(x, y, actualBarWidth, barHeight);
                    
                    String dateLabel = String.format("%02d/%02d", date.getDayOfMonth(), date.getMonthValue());
                    int labelX = x + actualBarWidth / 2 - fm.stringWidth(dateLabel) / 2;
                    int labelY = height - padding / 2;
                    g2d.setColor(new Color(100, 100, 100));
                    g2d.drawString(dateLabel, labelX, labelY);
                    g2d.setColor(new Color(33, 150, 243));
                }
                
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRect(padding, padding, chartWidth, chartHeight);
                
                g2d.dispose();
            }
        };
        chartPanel.setPreferredSize(new Dimension(800, 300));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        return chartPanel;
    }

    private JPanel createTransactionsTable(LocalDate startDate, LocalDate endDate) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));
        
        JLabel titleLabel = new JLabel("Detailed Transactions Report");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 58, 95));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        root.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Order ID", "Service Type", "Status", "Pickup Date", "Total Amount"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        List<Laundry> filteredOrders = filterOrdersByDateRange(startDate, endDate);
        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setMaximumFractionDigits(0);
        
        List<Laundry> allOrders = app.getOrders();
        for (Laundry order : filteredOrders) {
            int actualIndex = allOrders.indexOf(order);
            String orderId = "ORD-" + String.format("%03d", actualIndex + 1);
            String serviceType = getServiceTypeDisplayName(order.getServiceType());
            String status = order.getStatus().name();
            String pickupDate = order.getPickupDate().toString();
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
                        c.setBackground(new Color(255, 152, 0));
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
        
        root.add(scrollPane, BorderLayout.CENTER);
        
        return root;
    }

    private void showDateRangeDialog() {
        JDialog rangeDialog = new JDialog(this, "Pilih Date Range", true);
        rangeDialog.setSize(450, 300);
        rangeDialog.setLocationRelativeTo(this);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(227, 242, 253)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        rootPanel.setOpaque(true);

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        dialogPanel.setOpaque(false);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Pilih Range Tanggal");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 150, 243));
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialogPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Tanggal Mulai:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField startDateField = new JTextField(reportStartDate != null ? reportStartDate.toString() : LocalDate.now().minusDays(6).toString()) {
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
        startDateField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        startDateField.setOpaque(false);
        startDateField.setBackground(Color.WHITE);
        startDateField.setPreferredSize(new Dimension(250, 35));
        dialogPanel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Tanggal Akhir:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField endDateField = new JTextField(reportEndDate != null ? reportEndDate.toString() : LocalDate.now().toString()) {
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
        endDateField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        endDateField.setOpaque(false);
        endDateField.setBackground(Color.WHITE);
        endDateField.setPreferredSize(new Dimension(250, 35));
        dialogPanel.add(endDateField, gbc);

        rootPanel.add(dialogPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JButton cancelBtn = new JButton("Batal") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(158, 158, 158).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(158, 158, 158).brighter();
                } else {
                    bgColor = new Color(158, 158, 158);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelBtn.addActionListener(e -> rangeDialog.dispose());
        
        JButton applyBtn = new JButton("Apply") {
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
        applyBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        applyBtn.setForeground(Color.WHITE);
        applyBtn.setBorderPainted(false);
        applyBtn.setFocusPainted(false);
        applyBtn.setContentAreaFilled(false);
        applyBtn.setPreferredSize(new Dimension(100, 40));
        
        applyBtn.addActionListener(e -> {
            try {
                LocalDate start = LocalDate.parse(startDateField.getText().trim());
                LocalDate end = LocalDate.parse(endDateField.getText().trim());
                
                if (start.isAfter(end)) {
                    JOptionPane.showMessageDialog(rangeDialog, "Tanggal mulai tidak boleh lebih besar dari tanggal akhir", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                reportStartDate = start;
                reportEndDate = end;
                rangeDialog.dispose();
                refreshReportPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(rangeDialog, "Format tanggal harus YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(applyBtn);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        rangeDialog.add(rootPanel);
        rangeDialog.setVisible(true);
    }

    private void refreshReportPanel() {
        if (reportPanel != null) {
            contentArea.remove(reportPanel);
            reportPanel = createReportPanel();
            contentArea.add(reportPanel, "REPORT");
            if (activeMenu.equals("REPORT")) {
                cardLayout.show(contentArea, "REPORT");
            }
        }
    }

    private void refreshDashboardPanel() {
        if (dashboardPanel != null) {
            contentArea.remove(dashboardPanel);
            dashboardPanel = createDashboardPanel();
            contentArea.add(dashboardPanel, "DASHBOARD");
            if (activeMenu.equals("Overview")) {
                cardLayout.show(contentArea, "DASHBOARD");
            }
        }
    }

    private void refreshOverviewAndReport() {
        refreshDashboardPanel();
        refreshReportPanel();
    }

    private boolean showDeleteConfirmDialog() {
        JDialog confirmDialog = new JDialog(this, "Konfirmasi", true);
        confirmDialog.setSize(400, 180);
        confirmDialog.setLocationRelativeTo(this);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(227, 242, 253)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        rootPanel.setOpaque(true);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel messageLabel = new JLabel("Yakin hapus data?");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rootPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        final boolean[] result = {false};

        JButton noBtn = new JButton("Tidak") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(158, 158, 158).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(158, 158, 158).brighter();
                } else {
                    bgColor = new Color(158, 158, 158);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        noBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        noBtn.setForeground(Color.WHITE);
        noBtn.setBorderPainted(false);
        noBtn.setFocusPainted(false);
        noBtn.setContentAreaFilled(false);
        noBtn.setPreferredSize(new Dimension(100, 40));
        noBtn.addActionListener(e -> {
            result[0] = false;
            confirmDialog.dispose();
        });

        JButton yesBtn = new JButton("Ya") {
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
        yesBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.setBorderPainted(false);
        yesBtn.setFocusPainted(false);
        yesBtn.setContentAreaFilled(false);
        yesBtn.setPreferredSize(new Dimension(100, 40));
        yesBtn.addActionListener(e -> {
            result[0] = true;
            confirmDialog.dispose();
        });

        buttonPanel.add(noBtn);
        buttonPanel.add(yesBtn);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        confirmDialog.add(rootPanel);
        confirmDialog.setVisible(true);

        return result[0];
    }

    private void showDeleteSuccessDialog() {
        JDialog successDialog = new JDialog(this, "Sukses", true);
        successDialog.setSize(400, 180);
        successDialog.setLocationRelativeTo(this);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(227, 242, 253)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        rootPanel.setOpaque(true);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel messageLabel = new JLabel("Pesanan berhasil dihapus");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rootPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton okBtn = new JButton("OK") {
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
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        okBtn.setForeground(Color.WHITE);
        okBtn.setBorderPainted(false);
        okBtn.setFocusPainted(false);
        okBtn.setContentAreaFilled(false);
        okBtn.setPreferredSize(new Dimension(100, 40));
        okBtn.addActionListener(e -> successDialog.dispose());

        buttonPanel.add(okBtn);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        successDialog.add(rootPanel);
        successDialog.setVisible(true);
    }

    private boolean showCompleteConfirmDialog() {
        JDialog confirmDialog = new JDialog(this, "Konfirmasi", true);
        confirmDialog.setSize(450, 180);
        confirmDialog.setLocationRelativeTo(this);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(227, 242, 253)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        rootPanel.setOpaque(true);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel messageLabel = new JLabel("Tandai pesanan ini sebagai selesai?");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rootPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        final boolean[] result = {false};

        JButton noBtn = new JButton("Tidak") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(158, 158, 158).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(158, 158, 158).brighter();
                } else {
                    bgColor = new Color(158, 158, 158);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        noBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        noBtn.setForeground(Color.WHITE);
        noBtn.setBorderPainted(false);
        noBtn.setFocusPainted(false);
        noBtn.setContentAreaFilled(false);
        noBtn.setPreferredSize(new Dimension(100, 40));
        noBtn.addActionListener(e -> {
            result[0] = false;
            confirmDialog.dispose();
        });

        JButton yesBtn = new JButton("Ya") {
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
        yesBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.setBorderPainted(false);
        yesBtn.setFocusPainted(false);
        yesBtn.setContentAreaFilled(false);
        yesBtn.setPreferredSize(new Dimension(100, 40));
        yesBtn.addActionListener(e -> {
            result[0] = true;
            confirmDialog.dispose();
        });

        buttonPanel.add(noBtn);
        buttonPanel.add(yesBtn);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        confirmDialog.add(rootPanel);
        confirmDialog.setVisible(true);

        return result[0];
    }

    private void showCompleteSuccessDialog() {
        JDialog successDialog = new JDialog(this, "Sukses", true);
        successDialog.setSize(450, 180);
        successDialog.setLocationRelativeTo(this);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(227, 242, 253)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        rootPanel.setOpaque(true);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel messageLabel = new JLabel("Pesanan berhasil ditandai selesai");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rootPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton okBtn = new JButton("OK") {
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
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        okBtn.setForeground(Color.WHITE);
        okBtn.setBorderPainted(false);
        okBtn.setFocusPainted(false);
        okBtn.setContentAreaFilled(false);
        okBtn.setPreferredSize(new Dimension(100, 40));
        okBtn.addActionListener(e -> successDialog.dispose());

        buttonPanel.add(okBtn);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        successDialog.add(rootPanel);
        successDialog.setVisible(true);
    }

    private void showInfoDialog(Component parent, String message) {
        JDialog infoDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Info", true);
        infoDialog.setSize(400, 180);
        infoDialog.setLocationRelativeTo(parent);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(227, 242, 253)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        rootPanel.setOpaque(true);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rootPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton okBtn = new JButton("OK") {
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
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        okBtn.setForeground(Color.WHITE);
        okBtn.setBorderPainted(false);
        okBtn.setFocusPainted(false);
        okBtn.setContentAreaFilled(false);
        okBtn.setPreferredSize(new Dimension(100, 40));
        okBtn.addActionListener(e -> infoDialog.dispose());

        buttonPanel.add(okBtn);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        infoDialog.add(rootPanel);
        infoDialog.setVisible(true);
    }
}
