package laundry.model;

import java.time.LocalDate;


public class Laundry {
    private String customerName;
    private ServiceType serviceType;
    private double weightKg;
    private LocalDate orderDate;
    private LocalDate pickupDate;
    private OrderStatus status;

    public Laundry(String customerName, ServiceType serviceType, double weightKg,
                        LocalDate orderDate, LocalDate pickupDate, OrderStatus status) {
        this.customerName = customerName;
        this.serviceType = serviceType;
        this.weightKg = weightKg;
        this.orderDate = orderDate;
        this.pickupDate = pickupDate;
        this.status = status;
    }

    public String getCustomerName() { return customerName; }
    public ServiceType getServiceType() { return serviceType; }
    public double getWeightKg() { return weightKg; }
    public LocalDate getOrderDate() { return orderDate; }
    public LocalDate getPickupDate() { return pickupDate; }
    public OrderStatus getStatus() { return status; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
