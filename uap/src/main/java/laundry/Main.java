package laundry;

import laundry.model.Laundry;
import laundry.repo.Csv;
import laundry.service.LaundryService;
import laundry.exception.Data;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private final List<Laundry> orders = new ArrayList<>();
    private final Csv repo =
            new Csv("src/main/resources/data/pesanan.csv");
    private final LaundryService service = new LaundryService();

    public Main() {
        try {
            orders.addAll(repo.loadAll());
        } catch (Data ex) {
            JOptionPane.showMessageDialog(null,
                    "Gagal memuat data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Laundry> getOrders() { return orders; }
    public LaundryService getService() { return service; }

    public void persist() {
        try {
            repo.saveAll(orders);
        } catch (Data ex) {
            JOptionPane.showMessageDialog(null,
                    "Gagal menyimpan data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
