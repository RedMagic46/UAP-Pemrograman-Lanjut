package laundry.repo;

import laundry.exception.Data;
import laundry.model.Laundry;
import laundry.model.OrderStatus;
import laundry.model.ServiceType;
import laundry.util.CsvUtil;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Csv {
    private final File file;

    public Csv(String path) {
        this.file = new File(path);
        ensureParentDir();
    }

    private void ensureParentDir() {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
        try { if (!file.exists()) file.createNewFile(); } catch (IOException ignored) {}
    }

    public List<Laundry> loadAll() throws Data {
        List<Laundry> orders = new ArrayList<>();
        if (!file.exists()) return orders;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = CsvUtil.parseLine(line);
                if (parts.length == 5) {
                    orders.add(new Laundry(
                            parts[0],
                            ServiceType.valueOf(parts[1]),
                            Double.parseDouble(parts[2]),
                            LocalDate.parse(parts[3]),
                            OrderStatus.valueOf(parts[4])
                    ));
                }
            }
        } catch (Exception e) {
            throw new Data("Gagal membaca file CSV", e);
        }
        return orders;
    }

    public void saveAll(List<Laundry> orders) throws Data {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Laundry o : orders) {
                pw.println(String.join(",",
                        CsvUtil.escape(o.getCustomerName()),
                        o.getServiceType().name(),
                        String.valueOf(o.getWeightKg()),
                        o.getOrderDate().toString(),
                        o.getStatus().name()
                ));
            }
        } catch (Exception e) {
            throw new Data("Gagal menulis file CSV", e);
        }
    }
}
