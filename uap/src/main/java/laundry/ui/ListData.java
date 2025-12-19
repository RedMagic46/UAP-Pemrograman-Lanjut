package laundry.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import laundry.Main;
import laundry.model.Laundry;

public class ListData extends JFrame {
    private final Main app;
    private DefaultTableModel model;
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;

    public ListData(Main app) {
        this.app = app;
        setTitle("List Data Pesanan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel h = new JLabel("List Pesanan");
        h.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(h, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchField = new JTextField();
        JButton searchBtn = new JButton("Cari");
        searchBtn.addActionListener(e -> applySearch());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        header.add(searchPanel, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        String[] cols = {"Nama", "Layanan", "Berat (kg)", "Tanggal", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        table.setRowHeight(24);
        refreshTable();
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Hapus");
        JButton backBtn = new JButton("Kembali ke Menu");
        editBtn.setBackground(new Color(76,175,80)); editBtn.setForeground(Color.WHITE);
        delBtn.setBackground(new Color(244,67,54)); delBtn.setForeground(Color.WHITE);
        backBtn.setBackground(new Color(33,150,243)); backBtn.setForeground(Color.WHITE);

        editBtn.addActionListener(e -> editSelected());
        delBtn.addActionListener(e -> deleteSelected());
        backBtn.addActionListener(e -> { dispose(); new Menu(app).setVisible(true); });

        actions.add(editBtn); actions.add(delBtn); actions.add(backBtn);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void refreshTable() {
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

    private void applySearch() {
        String keyword = searchField.getText().trim();
        sorter.setRowFilter(keyword.isEmpty() ? null : RowFilter.regexFilter("(?i)" + keyword));
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data untuk edit");
            return;
        }
        int modelIndex = table.convertRowIndexToModel(row);
        var selected = app.getOrders().get(modelIndex);
        dispose();
        InputF input = new InputF(app);
        input.setEditingOrder(selected);
        input.setVisible(true);
    }

    private void deleteSelected() {
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
        refreshTable();
    }
}
