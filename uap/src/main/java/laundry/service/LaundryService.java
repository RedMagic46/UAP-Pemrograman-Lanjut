package laundry.service;

import laundry.exception.Validation;
import laundry.model.Laundry;
import laundry.model.ServiceType;

import java.util.Comparator;
import java.util.List;

public class LaundryService {
    public void validateOrder(Laundry order) throws Validation{
        if (order.getCustomerName() == null || order.getCustomerName().isBlank())
            throw new Validation ("Nama pelanggan wajib diisi");
        if (order.getWeightKg() <= 0)
            throw new Validation ("Berat cucian harus lebih dari 0");
        if (order.getOrderDate() == null)
            throw new Validation ("Tanggal order tidak boleh kosong");
        if (order.getPickupDate() == null)
            throw new Validation ("Tanggal pickup tidak boleh kosong");
        if (order.getPickupDate().isBefore(order.getOrderDate()))
            throw new Validation ("Tanggal pickup tidak boleh sebelum tanggal order");
    }

    public double calculatePrice(Laundry order) {
        double base = switch (order.getServiceType()) {
            case CUCI_KERING -> 5000;
            case SETRIKA -> 4000;
            case CUCI_SETIRIKA -> 8000;
        };
        return base * order.getWeightKg();
    }

    public void sortByDate(List<Laundry> orders) {
        orders.sort(Comparator.comparing(Laundry::getOrderDate));
    }
}
