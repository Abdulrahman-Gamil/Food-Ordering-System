package com.mycompany.finaljava;


import java.util.List;


public class OrderItem {

    private static final double DELIVERY_CHARGE = 3.0;
    private String itemName;
    private int quantity;
    private double price;

    public OrderItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = 0.0;
    }

    public OrderItem(String itemName, int quantity, double price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem getDeliveryChargeItem() {
        return new OrderItem("Delivery Charge", 1, DELIVERY_CHARGE);
    }

    public double getPrice() {
        return price;
    }

    public boolean isPriceSet() {
        return price > 0.0;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public FoodItem getFoodItem(List<FoodItem> vendorMenu) {
        for (FoodItem item : vendorMenu) {
            if (item.getItemName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) obj;
        return itemName.equals(orderItem.itemName) &&
                quantity == orderItem.quantity &&
                Double.compare(orderItem.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return itemName.hashCode();
    }

    @Override
    public String toString() {
        return itemName + " x" + quantity;
    }
}