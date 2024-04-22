package com.mycompany.finaljava;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class CustomerDashboard {
    private List<Vendor> vendors;
    private static Scanner scanner = new Scanner(System.in);
    private Random random = new Random();
    private Set<Integer> usedReviewIds = new HashSet<>(); 
    private Set<Integer> usedOrderIds = new HashSet<>(); 

    private int id;
    private String name;
    private String password;
    private double balance;

    
    public CustomerDashboard(){}
    
public CustomerDashboard(int id, String name, String password, List<Vendor> vendors , double balance) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.vendors = vendors;
    this.balance=balance;
}

    

    public static void main(String[] args) {
        
    }
    @Override
    public String toString() {
        return "Customer ID: " + id + ", Name: " + name;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }
   

    
   

    public void handleCustomerOperations() throws IOException {
        while (true) {
            displayDashboard();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    readVendorReviews();
                    break;
                case 3:
                    placeOrder();
                    break;
                case 4:
                    cancelOrder(name , vendors);
                    break;
                case 5:
                    String orderStatus = checkOrderStatus(name, vendors);
                    System.out.println(orderStatus);
                    break;
                case 6:
                    checkOrderHistory(name , vendors);
                    break;
                case 7:
                   checkTransactionHistory(name , vendors);
                    break;
                case 8:
                   provideReview();
                    break;
                case 9:
                    provideReviewRunner(name);
                    break;
                case 10:
                   reorderUsingOrderHistory(name);
                    break;
                case 11:
                    payPendingOrder(vendors);
                    break;
                case 12:
                    printBalance();
                    break;
                case 13:
                    showNotifications(id);
                    break;
                case 0:
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayDashboard() {
        System.out.println("|---------------------------------|");
        System.out.println("|--------Customer Dashboard-------|");
        System.out.println("|---------------------------------|");
        System.out.println("|1. View Menu                     |");
        System.out.println("|2. Read Customer Review          |");
        System.out.println("|3. Place Order                   |");
        System.out.println("|4. Cancel Order                  |");
        System.out.println("|5. Check Order Status            |");
        System.out.println("|6. Check Order History           |");
        System.out.println("|7. Check Transaction History     |");
        System.out.println("|8. Provide Review for Vendor     |");
        System.out.println("|9. Provide Review for Runner     |");
        System.out.println("|10. Reorder Using Order History  |");
        System.out.println("|11.Pay Pending orders            |");
        System.out.println("|12.Check Balance                 |");
        System.out.println("|13.Check Notifications           |");
        System.out.println("|0. Exit                          |");
        System.out.println("|---------------------------------|");
        System.out.print("Enter your choice: ");
    }

    private void viewMenu() {
        Vendor selectedVendor = selectVendor();

        if (selectedVendor != null) {
            System.out.println("Menu for " + selectedVendor.getVendorName() + ":");
            for (FoodItem item : selectedVendor.getMenu()) {
                System.out.println("Item Name: " + item.getItemName() + " | Description: " + item.getDescription() + " | Price: $" + item.getPrice());
            }
        } else {
            System.out.println("No vendor selected.");
        }
    }

    private void displayVendors() {
        System.out.println("Available Vendors:");
        for (int i = 0; i < vendors.size(); i++) {
            System.out.println((i + 1) + ". " + vendors.get(i).getVendorName());
        }
    }

    private Vendor selectVendor() {
        displayVendors();
        System.out.print("Select a vendor: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice > 0 && choice <= vendors.size()) {
            return vendors.get(choice - 1);
        } else {
            System.out.println("Invalid choice. Please try again.");
            return null;
        }
    }


    private void placeOrder() {
    displayVendors();
    System.out.print("Select a vendor: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    
    String deliveryStatus = "";
    double DELIVERY_CHARGE = 3.0;

    if (choice > 0 && choice <= vendors.size()) {
        Vendor selectedVendor = vendors.get(choice - 1);
        
        
        if (selectedVendor != null) {
            System.out.println("Menu for " + selectedVendor.getVendorName() + ":");
            selectedVendor.viewMenu();

            
            int itemCount = getIntInput("How many items will you order? ");
            
            
            List<OrderItem> orderItems = new ArrayList<>(); // List to store order items

            for (int i = 0; i < itemCount; i++) {
                System.out.print("Enter the item name: ");
                String itemName = scanner.nextLine();

                System.out.print("Enter the quantity: ");
                try {
                    int quantity = Integer.parseInt(scanner.nextLine());

                    FoodItem foundItem = selectedVendor.findItemByName(itemName.trim());
                    if (foundItem != null) {
                        orderItems.add(new OrderItem(itemName, quantity)); // Add item to orderItems list
                    } else {
                        System.out.println("Item '" + itemName + "' not found in the menu.");
                        i--; // Re-prompt for the same item count
                    }

                    
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity format. Please enter a number.");
                    i--; // Re-prompt for the same item count
                }
                
            }
            System.out.println("Please choose your food type:");
                    System.out.println("1. Delivery");
                    System.out.println("2. Dine-in");
                    System.out.println("3. Takeaway");

                    int foodTypeChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the leftover newline character

                    switch (foodTypeChoice) {
                        case 1:
                            deliveryStatus = "Pending Runner";
                            System.out.println("You've chosen delivery. An additional delivery charge will be applied.");

                            // Add delivery charge as an item
                            orderItems.add(OrderItem.getDeliveryChargeItem());

                            break;
                        case 2:
                            deliveryStatus = "No";
                            // Handle dine-in
                            System.out.println("You've chosen dine-in.");
                            // Implement dine-in-specific functionality
                            break;
                        case 3:
                            // Handle takeaway
                            deliveryStatus = "No";
                            System.out.println("You've chosen takeaway.");
                            // Implement takeaway-specific functionality
                            break;
                        default:
                            System.out.println("Invalid choice. Please choose a valid option.");
                    }

            if (!orderItems.isEmpty()) {
                int orderId = generateOrderId();
                
                String orderString = orderId + ";" + name + ";" + "Pending Payment" + ";" + orderItemsToString(orderItems) + ";" + LocalDate.now() + ";" + deliveryStatus;

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("order_" + selectedVendor.getVendorId() + ".txt", true))) {
                    writer.write(orderString);
                    writer.newLine();
                    System.out.println("Order placed successfully. Order ID: " + orderId);
                    sendVendorNotification(selectedVendor.getVendorId(), orderId, name,"placed"); // Assuming 'name' is the customer's name

                    Order newOrder = new Order(orderId, name, "Pending Payment", orderItems,LocalDate.now(), deliveryStatus);
                    selectedVendor.addOrder(newOrder); // Add the new order to the selected vendor
                } catch (IOException e) {
                    System.out.println("Error placing order: " + e.getMessage());
                }
            } else {
                System.out.println("No valid items selected. Order not placed.");
            }
        } else {
            System.out.println("No vendor selected.");
        }
    } else {
        System.out.println("Invalid choice.");
    }
}

// Helper method to convert order items list to string
private String orderItemsToString(List<OrderItem> orderItems) {
    StringBuilder orderDetails = new StringBuilder();
    for (int i = 0; i < orderItems.size(); i++) {
        OrderItem item = orderItems.get(i);
        orderDetails.append(item.getItemName()).append(":").append(item.getQuantity());
        if (i < orderItems.size() - 1) {
            orderDetails.append(",");
        }
    }
    return orderDetails.toString();
}

 

private String checkOrderStatus(String customerName, List<Vendor> vendors) {
    StringBuilder orderDetails = new StringBuilder();

    for (Vendor vendor : vendors) {
        List<Order> orders = vendor.getOrders();
        for (Order order : orders) {
            if (order.getCustomerName().equalsIgnoreCase(customerName) && !order.getOrderStatus().equalsIgnoreCase("Canceled by Customer")) {
                orderDetails.append("Order ID: ").append(order.getOrderId()).append(", Status: ").append(order.getOrderStatus()).append("\n");
            }
        }
    }

    return orderDetails.toString();
}


private int getIntInput(String prompt) {
    int input = 0;
    boolean validInput = false;
    while (!validInput) {
        try {
            System.out.print(prompt);
            input = Integer.parseInt(scanner.nextLine());
            validInput = true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
        }
    }
    return input;
}


private void cancelOrder(String customerName , List<Vendor> vendors) {
    

    StringBuilder ordersToCancel = new StringBuilder();
for (Vendor vendor : vendors) {
    List<Order> orders = vendor.getOrders();
    for (Order order : orders) {
        if (order.getCustomerName().equalsIgnoreCase(customerName) && order.getOrderStatus().equalsIgnoreCase("Pending Payment")) {
            ordersToCancel.append("Order ID: ").append(order.getOrderId())
                    .append(", Status: ").append(order.getOrderStatus())
                    .append(", Items: ").append(order.getItemsAsString())
                    .append("\n");
        }
    }
}


    if (ordersToCancel.length() == 0) {
        System.out.println("No orders found for the customer.");
        return;
    }

    // Display orders associated with the customer
    System.out.println("Orders for " + customerName + ":");
    System.out.println(ordersToCancel.toString());

    // Select an order to cancel
    System.out.print("Enter the Order ID to cancel: ");
    int orderIdToCancel = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    // Find the order and cancel if status is "Pending"
     for (Vendor vendor : vendors) {
        List<Order> orders = vendor.getOrders();
        for (Order order : orders) {
            if (order.getOrderId() == orderIdToCancel && order.getCustomerName().equalsIgnoreCase(customerName)) {
                if (order.getOrderStatus().equalsIgnoreCase("Pending Payment")) {
                    order.setStatus("Canceled by customer");
                    System.out.println("Order " + orderIdToCancel + " canceled successfully.");
                    sendVendorNotification(vendor.getVendorId(),orderIdToCancel, name,"canceled");

                    // Call the overwriteOrdersToFile method to update the file with the new order status
                    try {
                        vendor.overwriteOrdersToFile(orders, "order_" + vendor.getVendorId() + ".txt");
                    } catch (IOException e) {
                        System.out.println("Error updating order status in file: " + e.getMessage());
                    }
                } else {
                    System.out.println("This order cannot be canceled.");
                }
                break;
            }
        }
    }

    
}

public void checkOrderHistory(String customerName, List<Vendor> vendors) {
    StringBuilder orderHistory = new StringBuilder();

    for (Vendor vendor : vendors) {
        List<Order> orders = vendor.getOrders();
        for (Order order : orders) {
            if (order.getCustomerName().equalsIgnoreCase(customerName) && (order.getOrderStatus().equals("Canceled by customer") || order.getOrderStatus().equals("ready to collect"))) {
                orderHistory.append("Order ID: ").append(order.getOrderId()).append(", Status: ").append(order.getOrderStatus())
                        .append(", Items: ").append(order.getItemsAsString()).append("\n");
            }
        }
    }

    if (orderHistory.length() == 0) {
        System.out.println("No order history found for the customer.");
    } else {
        System.out.println("Order history for " + customerName + ":");
        System.out.println(orderHistory.toString());
    }
}

public void reorderUsingOrderHistory(String customerName) {
    // Search for orders associated with the customer's name
    StringBuilder orderHistory = new StringBuilder();
    
    for (Vendor vendor : vendors) {
        for (Order order : vendor.getOrders()) {
            if (order.getCustomerName().equalsIgnoreCase(customerName) && 
                (!order.getOrderStatus().equalsIgnoreCase("Preparing") || !order.getOrderStatus().equalsIgnoreCase("Pending Payment"))) {
                orderHistory.append("Vendor ID: ").append(vendor.getVendorId()).append("\n");
                orderHistory.append("Order ID: ").append(order.getOrderId()).append(", Status: ").append(order.getOrderStatus());
                orderHistory.append(", Items: ").append(order.getItemsAsString()).append("\n");
            }
        }
    }

    if (orderHistory.length() == 0) {
        System.out.println("No orders found in the order history for the customer.");
        return;
    }

    // Display order history associated with the customer
    System.out.println("Order history for " + customerName + ":");
    System.out.println(orderHistory.toString());

    // Select an order to reorder
    System.out.print("Enter the Order ID to reorder: ");
    int orderIdToReorder = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    System.out.println("Please choose your food type:");
    System.out.println("1. Delivery");
    System.out.println("2. Dine-in");
    System.out.println("3. Takeaway");
    int foodTypeChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    final double DELIVERY_CHARGE = 3.0;
    boolean isDelivery = (foodTypeChoice == 1);

    // Find the order and reorder it
    for (Vendor vendor : vendors) {
        for (Order order : vendor.getOrders()) {
            if (order.getOrderId() == orderIdToReorder && order.getCustomerName().equalsIgnoreCase(customerName)) {
                if (!order.getOrderStatus().equalsIgnoreCase("Pending")) {
                    // Reorder the selected order
                    int newOrderId = generateOrderId(); // Implement this method to generate a new unique order ID
                    List<OrderItem> newOrderItems = new ArrayList<>(order.getItems());

                    // Add delivery charge if delivery is selected
                    if (isDelivery && order.getDeliveryStatus().equals("No")) {
                        newOrderItems.add(new OrderItem("Delivery Charge", 1, DELIVERY_CHARGE));
                    }

                    // Create a new order with the updated items
                    Order newOrder = new Order(newOrderId, order.getCustomerName(), "Pending Payment", newOrderItems,
                            LocalDate.now(), isDelivery ? "Pending Runner" : "No");

                    // Remove the delivery charge item if it exists
                    newOrder.removeDeliveryChargeItem();

                    // Display the order before and after removing the delivery charge item
                    System.out.println("Order before removing delivery charge item:");
                    System.out.println(order);

                    System.out.println("\nOrder after removing delivery charge item:");
                    System.out.println(newOrder);

                    // Add the new order to the vendor's order list
                    vendor.addOrder(newOrder);

                    // Save the new order to the order file
                    saveOrderToFile(newOrder, "order_" + vendor.getVendorId() + ".txt");
                    System.out.println("New order placed successfully with ID: " + newOrderId);
                    sendVendorNotification(vendor.getVendorId(), newOrderId, customerName, "reordered");
                    return;
                } else {
                    System.out.println("This order cannot be reordered.");
                    return;
                }
            }
        }
    }
    System.out.println("No order found with the provided Order ID for the customer.");
}



// Inside the Vendor class

public void saveOrderToFile(Order order, String filename) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
        StringBuilder line = new StringBuilder();
        line.append(order.getOrderId()).append(";");
        line.append(order.getCustomerName()).append(";");
        line.append(order.getOrderStatus()).append(";");

        // Concatenating items
        List<OrderItem> items = order.getItems();
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            line.append(item.getItemName()).append(":").append(item.getQuantity());
            if (i != items.size() - 1) {
                line.append(",");
            }
        }

        line.append(";").append(LocalDate.now()).append(";"); // Adding the order date
        line.append(order.getDeliveryStatus()); // Adding the delivery status

        writer.write(line.toString());
        writer.newLine();
    } catch (IOException e) {
        System.out.println("Error writing order to file: " + e.getMessage());
    }
}


public void readVendorReviews() {
        scanner = new Scanner(System.in);

        // Display available vendors for reviews
        displayVendors();

        // Ask for the vendor ID
        System.out.print("Enter the Vendor ID to read reviews: ");
        int vendorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Read and display reviews for the chosen vendor
        readReviewsForVendor(vendorId);
    }

    private void readReviewsForVendor(int vendorId) {
        String fileName = "reviews_" + vendorId + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            System.out.println("Reviews for Vendor ID: " + vendorId);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading reviews: " + e.getMessage());
        }
    }


    
    public void provideReview() {
    
    displayVendors();

    
    System.out.print("Enter the Vendor ID to provide a review: ");
    int vendorId = scanner.nextInt();
    scanner.nextLine(); // Consume newline


    boolean hasOrder = checkOrders(vendorId, name);

    if (hasOrder) {
        // Ask for the review details
        System.out.print("Enter your review: ");
        String review = scanner.nextLine();

        // Write the review to the file
        writeReviewToFile(vendorId, name, review);
    } else {
        System.out.println("You haven't made any orders with this vendor. Unable to provide a review.");
    }
}

    private void writeReviewToFile(int vendorId, String customerName, String review) {
        String fileName = "reviews_" + vendorId + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Generate a unique review ID
            int reviewId = generateReviewId(vendorId);

            // Write the review to the file
            writer.write(reviewId + ";" + customerName + ";" + review);
            writer.newLine();

            System.out.println("Review submitted successfully for Vendor ID: " + vendorId);
        } catch (IOException e) {
            System.out.println("Error writing review: " + e.getMessage());
        }
    }

    private int generateReviewId(int vendorId) {
    int reviewId;
    do {
        reviewId = random.nextInt(10001); 
    } while (usedReviewIds.contains(reviewId)); 

    usedReviewIds.add(reviewId); 
    return reviewId;
}
    
    public int generateOrderId() {
    Random random = new Random();
    int orderId;
    do {
        orderId = random.nextInt(10000) + 1; 
    } while (usedOrderIds.contains(orderId)); 

    usedOrderIds.add(orderId); 
    return orderId;
}
    
    

private boolean checkOrders(int vendorId, String customerName) {
    for (Vendor vendor : vendors) {
        if (vendor.getVendorId() == vendorId) {
            List<Order> orders = vendor.getOrders();
            for (Order order : orders) {
                if (order.getCustomerName().equalsIgnoreCase(customerName)) {
                    return true; // Customer has made an order with this vendor
                }
            }
        }
    }
    return false; 
}


public void payPendingOrder(List<Vendor> vendors) throws IOException {
    double totalPendingPayment = 0;
    List<Order> pendingOrders = new ArrayList<>();

    // Accumulate the total cost of pending orders and collect them for display
    for (Vendor vendor : vendors) {
        List<Order> orders = vendor.getOrders();
        for (Order order : orders) {
            if (order.getCustomerName().equalsIgnoreCase(name) && order.getOrderStatus().equalsIgnoreCase("Pending Payment")) {
                totalPendingPayment += vendor.calculateTotalPrice(order);
                pendingOrders.add(order);
            }
        }
    }

    if (pendingOrders.isEmpty()) {
        System.out.println("No pending orders to pay.");
        return;
    }

    System.out.println("Pending Orders:");
    for (int i = 0; i < pendingOrders.size(); i++) {
        Order order = pendingOrders.get(i);
        Vendor vendor = findVendorForOrder(order, vendors);
        if (vendor != null) {
           
            System.out.println((i + 1) + ". Order ID: " + order.getOrderId() + ", Total Cost: $" + vendor.calculateTotalPrice(order));
            
        }
    }

    System.out.print("Enter the number of the order you want to pay: ");
    int orderNumber = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (orderNumber > 0 && orderNumber <= pendingOrders.size()) {
        Order selectedOrder = pendingOrders.get(orderNumber - 1);
        Vendor selectedVendor = findVendorForOrder(selectedOrder, vendors);

        if (selectedVendor != null) {
            double orderCost = selectedVendor.calculateTotalPrice(selectedOrder);
            double customerBalance = getBalance();

            if (orderCost > customerBalance) {
                System.out.println("Insufficient balance. Please top up your account.");
            } else {
                BigDecimal bdBalance = new BigDecimal(Double.toString(balance));
                BigDecimal bdOrderCost = new BigDecimal(Double.toString(orderCost));

                // Subtract orderCost from balance and round to 2 decimal places
                BigDecimal newBalance = bdBalance.subtract(bdOrderCost).setScale(2, RoundingMode.HALF_UP);

                // Update the balance variable with the new value
                balance = newBalance.doubleValue();
                setBalance(balance);
                updateBalanceInFile(id,balance);
                selectedOrder.setStatus("Pending Acceptance");
                // Ensure all other properties of selectedOrder are correctly set
                saveOrderToFile(selectedOrder, "order_" + selectedVendor.getVendorId() + ".txt");
                
                System.out.println("Order " + selectedOrder.getOrderId() + " has been successfully paid.");
                writeTransactionToFile(selectedOrder.getOrderId() ,orderCost, name, selectedVendor.getVendorId() , selectedOrder.getOrderDate());
                removeOldOrder(selectedOrder, selectedVendor);
                sendVendorNotification(selectedVendor.getVendorId(),selectedOrder.getOrderId(),name,"paid");
            }
        } else {
            System.out.println("Vendor not found for the selected order.");
        }
    } else {
        System.out.println("Invalid order number.");
    }
}
private Vendor findVendorForOrder(Order order, List<Vendor> vendors) {
    for (Vendor vendor : vendors) {
        if (vendor.getOrders().contains(order)) {
            return vendor;
        }
    }
    return null;
}

private void removeOldOrder(Order order, Vendor vendor) throws IOException {
    String filename = "order_" + vendor.getVendorId() + ".txt";
    List<String> fileContent = Files.readAllLines(Paths.get(filename));

    // Remove the order with the old status from the file
    fileContent.removeIf(line -> {
        String[] parts = line.split(";");
        return parts[0].equals(String.valueOf(order.getOrderId())) && parts[2].equalsIgnoreCase("Pending Payment");
    });

    // Write the updated content back to the file
    Files.write(Paths.get(filename), fileContent);

    // Remove the order with the old status from the list in memory
    List<Order> orders = vendor.getOrders();
    orders.removeIf(o -> o.getOrderId() == order.getOrderId() && o.getOrderStatus().equalsIgnoreCase("Pending Payment"));
}


public void printBalance() {
    double balance = getBalance();
    System.out.println("Your current balance is: $" + balance);
}

public static void updateBalanceInFile(int id,double newBalance ) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get("customer_info.txt"));

    // Find and update the line for this customer
    for (int i = 0; i < lines.size(); i++) {
        String[] parts = lines.get(i).split(",");
        if (Integer.parseInt(parts[0]) == id) { 
            lines.set(i, parts[0] + "," + parts[1] + "," + parts[2] + "," + newBalance); // Update the balance
            break;
        }
    }

    // Write the updated content back to the file
    Files.write(Paths.get("customer_info.txt"), lines);
}

private void writeTransactionToFile(int orderId, double total, String customerName, int vendorId, LocalDate date) throws IOException {
    String filename = "transactions_" + vendorId + ".txt";
    String transaction = orderId + ";" + total + ";" + customerName + ";Approving" + ";" + date; // Adding the 'Approving' status

    // Write the transaction to the file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
        writer.write(transaction);
        writer.newLine();
    }
}



public void checkTransactionHistory(String customerName, List<Vendor> vendors) {
    StringBuilder transactionHistory = new StringBuilder();

    for (Vendor vendor : vendors) {
        try {
            String filename = "transactions_" + vendor.getVendorId() + ".txt";
            List<String> transactions = Files.readAllLines(Paths.get(filename));

            for (String transaction : transactions) {
                String[] parts = transaction.split(";");
                if (parts.length >= 3 && parts[2].equalsIgnoreCase(customerName)) {
                    transactionHistory.append("Order ID: ").append(parts[0]).append(", Total: ").append(parts[1]).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    if (transactionHistory.length() == 0) {
        System.out.println("No transaction history found for the customer.");
    } else {
        System.out.println("Transaction history for " + customerName + ":");
        System.out.println(transactionHistory.toString());
    }
}

// Methods For ADMIN

    public void writeCustomerData(String n, String p, double c) { // Function for writing customer data
        try {
            File cFile = new File("customer_info.txt");
            FileWriter fileWriter = new FileWriter(cFile, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Generate new customer ID
            int newCustomerId = generateNewCustomerId();

            printWriter.println(newCustomerId + "," + n + "," + p + "," + c);
            printWriter.close();
            System.out.println("Customer data written to the file with ID: " + newCustomerId);
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    private int generateNewCustomerId() throws IOException {
        int highestId = 0;
        File cFile = new File("customer_info.txt");
        BufferedReader reader = new BufferedReader(new FileReader(cFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            int currentId = Integer.parseInt(data[0]);
            if (currentId > highestId) {
                highestId = currentId;
            }
        }
        reader.close();
        return highestId + 1; // Increment the highest ID by 1 to generate a new ID
    }

    public static void readCustomerData() { //Fucntion for writing customer data
        try {
            File cFile = new File("customer_info.txt");
            BufferedReader reader = new BufferedReader(new FileReader(cFile));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static void searchCustomer(int id) { //Fucntion for searching specific customer data
        try {
            File cFile = new File("customer_info.txt");
            BufferedReader reader = new BufferedReader(new FileReader(cFile));
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int currentId = Integer.parseInt(data[0]);
                if (currentId == id) {
                    System.out.println("Customer Found: " + line);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Customer not found");
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static void deleteCustomer(int id) { //Fucntion for deleting customer data
        try {
            File inFile = new File("customer_info.txt");
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                int currentId = Integer.parseInt(line.split(",")[0]);
                if (currentId != id) {
                    pw.println(line);
                    pw.flush();
                } else {
                    found = true;
                }
            }
            pw.close();
            br.close();

            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");

            if (found) {
                System.out.println("Customer deleted successfully");
            } else {
                System.out.println("Customer not found");
            }
        } catch (IOException ex) {
            System.out.println("Error in deleting the customer: " + ex.getMessage());
        }
    }
    
    public static void updateCustomerInfo(int id, String newUsername, String newPassword) { //Fucntion for updating all customer data
        List<String> fileContent = new ArrayList<>();
        boolean found = false;

        try {
            File inFile = new File("customer_info.txt");
            BufferedReader br = new BufferedReader(new FileReader(inFile));

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int currentId = Integer.parseInt(data[0]);
                if (currentId == id) {
                    // Keep the credit same as before
                    double credit = Double.parseDouble(data[3]);
                    fileContent.add(id + "," + newUsername + "," + newPassword + "," + credit);
                    found = true;
                } else {
                    fileContent.add(line);
                }
            }
            br.close();

            if (found) {
                PrintWriter pw = new PrintWriter(new FileWriter(inFile));
                for (String str : fileContent) {
                    pw.println(str);
                }
                pw.close();
                System.out.println("Customer's general information updated successfully");
            } else {
                System.out.println("Customer not found");
            }

        } catch (IOException ex) {
            System.out.println("Error in updating the customer: " + ex.getMessage());
        }
    }
    
    public static void updateBalance(int id, double balanceToAdd) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get("customer_info.txt"));

    // Find and update the line for this customer
    for (int i = 0; i < lines.size(); i++) {
        String[] parts = lines.get(i).split(",");
        if (Integer.parseInt(parts[0]) == id) {
            double currentBalance = Double.parseDouble(parts[3]); // Parse the current balance as a double
            double newBalance = currentBalance + balanceToAdd; // Add the balanceToAdd
            // Update the line with the new balance
            lines.set(i, parts[0] + "," + parts[1] + "," + parts[2] + "," + newBalance);
            break;
        }
    }

    // Write the updated lines back to the file
    Files.write(Paths.get("customer_info.txt"), lines);
    }
    
    public static boolean doesCustomerIdExist(int id) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("customer_info.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) == id) {
                    return true; // ID found
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from customer_info.txt: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error in parsing customer ID: " + e.getMessage());
        }
        return false; // ID not found
    }
    
    public static void sendBalanceUpdateNotification(int customerId, double amountAdded) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String formattedDate = now.format(formatter);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("customer_notifications.txt", true))) {
        writer.write("Notification: Dear Customer," + customerId +
                                 ", your balance has been updated by " + amountAdded +
                                 " on " + formattedDate + ".");
        writer.newLine();
    } catch (IOException e) {
        System.out.println("Error writing to customer_notifications.txt: " + e.getMessage());
    }
    }
    
    public static void showNotifications(int customerId) {
    try {
        List<String> lines = Files.readAllLines(Paths.get("customer_notifications.txt"));
        System.out.println("Notifications for Customer ID " + customerId + ":");
        boolean hasNotifications = false;

        for (String line : lines) {
            String[] parts = line.split(",");
            if (Integer.parseInt(parts[1]) == customerId) {
                hasNotifications = true;
                System.out.println(line); // Print the entire line as is
            }
        }

        if (!hasNotifications) {
            System.out.println("No notifications found for this customer.");
        }
    } catch (IOException e) {
        System.out.println("Error reading from customer_notifications.txt: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Error in parsing customer ID: " + e.getMessage());
    }
    }
    
    private void sendVendorNotification(int vendorId, int orderId, String customerName,String Accept) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        String notificationMessage = "Order " + orderId + " has been " + Accept + " by " + customerName + " on " + formattedDate + ".";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendor_notifications.txt", true))) {
            writer.write("Vendor ID," + vendorId + ", " + notificationMessage);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to vendor_notifications.txt: " + e.getMessage());
        }
    }
    
    private void sendRunnerFeedbackNotification(String runnerId, String orderId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        
        String notificationMessage = "Feedback for Order " + String.valueOf(orderId) + " from " + name + " on " + formattedDate + ".";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("runner_notifications.txt", true))) {
            writer.write("Runner ID," + runnerId + ", " + notificationMessage);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to runner_notifications.txt: " + e.getMessage());
        }
    }
    
    public void provideReviewRunner(String customerName) {
    Scanner scanner = new Scanner(System.in);
    boolean foundDeliveredOrders = false;
    String historyFilename = "runner_history.txt";

    try (BufferedReader reader = new BufferedReader(new FileReader(historyFilename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if ( "Delivered".equals(parts[5].trim()) && customerName.equals(parts[1].trim())) {
                System.out.println("Order ID: " + parts[0] + ", Runner ID: " + parts[6]);
                foundDeliveredOrders = true;
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }

    if (!foundDeliveredOrders) {
        System.out.println("No delivered orders found for the customer.");
        return;
    }

    System.out.print("Enter the Order ID to review: ");
    String orderId = scanner.nextLine();

    System.out.print("Enter your review: ");
    String review = scanner.nextLine();

    // Assuming the Runner ID is the last part
    String runnerId = findRunnerIdForOrder(orderId);

    if (runnerId == null) {
        System.out.println("No order found with the given ID for the customer.");
        return;
    }

    String reviewEntry = orderId + "," + customerName + "," + review + "," + runnerId;

    // Write the review into runner_reviews.txt
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("runner_reviews.txt", true))) {
        writer.write(reviewEntry);
        writer.newLine();
        System.out.println("Review submitted successfully.");
        sendRunnerFeedbackNotification(runnerId, orderId);
    } catch (IOException e) {
        System.out.println("Error writing review: " + e.getMessage());
    }
}

private static String findRunnerIdForOrder(String orderId) {
    String historyFilename = "runner_history.txt";
    try (BufferedReader reader = new BufferedReader(new FileReader(historyFilename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts[0].equals(orderId)) {
                return parts[6]; // Return the runner ID
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }
    return null;
}
    
}
