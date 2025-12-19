package laundry.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import laundry.Main;
import laundry.model.*;
import laundry.exception.Validation;

public class InputF extends JFrame {
    private final Main app;
    private JTextField nameField, weightField, dateField;
    private JComboBox<ServiceType> serviceBox;
    private JComboBox<OrderStatus> statusBox;
    private Laundry editingOrder;

    public InputF(Main app) {
        this.app = app;
        setTitle("Input / Edit Pesanan");
        setSize(560, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Form Pesanan Laundry", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        nameField = new JTextField();
        weightField = new JTextField();
        serviceBox = new JComboBox<>(ServiceType.values());
        statusBox = new JComboBox<>(OrderStatus.values());
        dateField = new JTextField(LocalDate.now().toString());

        form.add(new JLabel("Nama:")); form.add(nameField);
        form.add(new JLabel("Berat (kg):")); form.add(weightField);
        form.add(new JLabel("Layanan:")); form.add(serviceBox);
        form.add(new JLabel("Status:")); form.add(statusBox);
        form.add(new JLabel("Tanggal (YYYY-MM-DD):")); form.add(dateField);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        JButton saveBtn = new JButton("Simpan");
        JButton backBtn = new JButton("Kembali ke Menu");
        JButton calcBtn = new JButton("Hitung Harga");
        saveBtn.setBackground(new Color(76,175,80)); saveBtn.setForeground(Color.WHITE);
        backBtn.setBackground(new Color(33,150,243)); backBtn.setForeground(Color.WHITE);
        calcBtn.setBackground(new Color(255,193,7)); calcBtn.setForeground(Color.BLACK);

        actions.add(calcBtn); actions.add(saveBtn); actions.add(backBtn);

        root.add(form, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);
        setContentPane(root);

        calcBtn.addActionListener(e -> showPricePreview());
        saveBtn.addActionListener(e -> saveOrder());
        backBtn.addActionListener(e -> { dispose(); new Menu(app).setVisible(true); });
    }

    public void setEditingOrder(Laundry order) {
        this.editingOrder = order;
        nameField.setText(order.getCustomerName());
        weightField.setText(String.valueOf(order.getWeightKg()));
        serviceBox.setSelectedItem(order.getServiceType());
        statusBox.setSelectedItem(order.getStatus());
        dateField.setText(order.getOrderDate().toString());
    }

    private void showPricePreview() {
        try {
            var temp = buildOrderFromForm();
            double price = app.getService().calculatePrice(temp);
            JOptionPane.showMessageDialog(this, "Estimasi Harga: Rp " + (long) price,
                    "Estimasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (Validation ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Input tidak valid: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveOrder() {
        try {
            var order = buildOrderFromForm();
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
            dispose();
            new Menu(app).setVisible(true);
        } catch (Validation ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Laundry buildOrderFromForm() throws Validation{
        String name = nameField.getText().trim();
        double weight;
        try { weight = Double.parseDouble(weightField.getText().trim()); }
        catch (Exception e) { throw new Validation("Berat harus berupa angka desimal"); }
        ServiceType service = (ServiceType) serviceBox.getSelectedItem();
        OrderStatus status = (OrderStatus) statusBox.getSelectedItem();
        java.time.LocalDate date;
        try { date = java.time.LocalDate.parse(dateField.getText().trim()); }
        catch (Exception e) { throw new Validation("Format tanggal harus YYYY-MM-DD"); }

        Laundry order = new Laundry(name, service, weight, date, status);
        app.getService().validateOrder(order);
        return order;
    }
}
