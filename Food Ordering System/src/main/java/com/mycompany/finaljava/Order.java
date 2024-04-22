package com.mycompany.finaljava;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Order {
    private static int orderIdCounter = 1;

    private int orderId;
    private String customerName;
    private String orderStatus;
    private List<OrderItem> items;
    private LocalDate orderDate;
    private String deliveryStatus;

    public Order(int orderId, String customerName, String orderStatus, List<OrderItem> items, LocalDate orderDate, String deliveryStatus) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderStatus = orderStatus;
        this.items = items;
        this.orderDate = orderDate;
        this.deliveryStatus = deliveryStatus;
    }

    public void removeDeliveryChargeItem() {
    Iterator<OrderItem> iterator = items.iterator();
    while (iterator.hasNext()) {
        OrderItem item = iterator.next();
        if (item.getItemName().equals("Delivery Charge")) {
            iterator.remove();
            break; // Remove only the first occurrence of the delivery charge item
        }
    }
}


    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setStatus(String status) {
        this.orderStatus = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getItemsAsString() {
        StringBuilder itemsAsString = new StringBuilder();
        for (OrderItem item : items) {
            itemsAsString.append(item.getItemName()).append(":").append(item.getQuantity()).append(",");
        }
        if (itemsAsString.length() > 0) {
            itemsAsString.deleteCharAt(itemsAsString.length() - 1);
        }
        return itemsAsString.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId);
        sb.append(", Customer: ").append(customerName);
        sb.append(", Status: ").append(orderStatus);
        sb.append(", Items: ").append(items);
        return sb.toString();
    }
}