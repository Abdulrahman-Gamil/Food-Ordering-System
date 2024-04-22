package com.mycompany.finaljava;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author i
 */
public class Vendor {

    private static int vendorIdCounter = 1;
    private static boolean vendorsCreated = false;

    private int vendorId;
    private String vendorName;
    private String password;
    private List<FoodItem> menu;
    private List<Order> orders;
    private List<CustomerDashboard> customerss;

    public Vendor(String vendorName , String password) {
        if (!vendorsCreated) {
            resetVendorIdCounter();
            vendorsCreated = true;
        }

        this.vendorId = vendorIdCounter++;
        this.vendorName = vendorName;
        this.menu = new ArrayList<>();
        orders = new ArrayList<>();
        this.customerss = new ArrayList<>();
        this.password = password;
    }

    public static void resetVendorIdCounter() {
        vendorIdCounter = 1;
    }
   

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Vendor() {
        this.customerss = new ArrayList<>(); 
    }

    
    public void addCustomer(CustomerDashboard customer) {
        customerss.add(customer);
    }
    
    

    
    public int getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }
    
    public String getVendorPass() {
        return password;
    }

    public List<FoodItem> getMenu() {
        return menu;
    }

    public void addItemToMenu(FoodItem item) {
        menu.add(item);
    }

     

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    
    public List<Order> getOrders() {
    return orders;
}

    
    


    @Override
    public String toString() {
        return "Vendor ID: " + vendorId + ", Vendor Name: " + vendorName;
    }

    public void handleVendorOperations() throws IOException {
    Scanner scanner = new Scanner(System.in);

    while (true) {
        System.out.println("\n+-------------------------------+");
        System.out.println("| Vendor Operations Menu        |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1. View Menu                  |");
        System.out.println("| 2. Create Item                |");
        System.out.println("| 3. Update Item                |");
        System.out.println("| 4. Delete Item                |");
        System.out.println("| 5. Accept/Cancel Order        |");
        System.out.println("| 6. Update Order Status        |");
        System.out.println("| 7. Check Order History        |");
        System.out.println("| 8. Read Customer Review       |");
        System.out.println("| 9. Revenue Dashboard          |");
        System.out.println("| 0. Exit                       |");
        System.out.println("+-------------------------------+");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("+------------------------------------------------+");
                System.out.println("|                  Viewing Menu                  |");
                System.out.println("+------------------------------------------------+");
                viewMenu();
                break;
            case 2:
                System.out.println("+------------------------------------------------+");
                System.out.println("|             Creating a New Item                |");
                System.out.println("+------------------------------------------------+");
                createItem();
                break;
            case 3:

                updateItem();
                break;
            case 4:
                System.out.println("+-------------------+");
                System.out.println("| Deleting Item     |");
                System.out.println("+-------------------+");
                deleteItem();
                break;
            case 5:
                System.out.println("+--------------------+");
                System.out.println("| Accept/Reject Order|");
                System.out.println("+--------------------+");
                acceptOrRejectOrder();
                break;
            case 6:
                System.out.println("+-------------------+");
                System.out.println("| Update Order Status|");
                System.out.println("+-------------------+");
                updateOrderStatus();
                break;
            case 7:
                System.out.println("+-------------------+");
                System.out.println("| Check Order History|");
                System.out.println("+-------------------+");
                checkOrderHistory();
                break;
            case 8:
                System.out.println("+-------------------+");
                System.out.println("| Read Customer Review|");
                System.out.println("+-------------------+");
                readCustomerReviews(vendorId);
                break;
            case 9:
                System.out.println("+-------------------+");
                System.out.println("| Revenue Dashboard |");
                System.out.println("+-------------------+");
                showRevenueDashboard();
                break;
            case 10:
                System.out.println("+-------------------+");
                System.out.println("| Show Notifications |");
                System.out.println("+-------------------+");
                showVendorNotifications(vendorId);
                break;
            case 0:
                System.out.println("Overwriting Menu and Orders to Files...");
                overwriteMenuToFile(getMenu(), "menu_" + getVendorId() + ".txt");
                overwriteOrdersToFile(getOrders(), "order_" + getVendorId() + ".txt");
                System.out.println("Files updated. Exiting Vendor Operations.");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }
}


    public void viewMenu() {
    
    System.out.println("-------------------------------------------------------------------");
    System.out.printf("%-20s | %-30s | %s\n", "Item Name", "Description", "Price");
    System.out.println("-------------------------------------------------------------------");

    for (FoodItem item : menu) {
        String itemName = item.getItemName();
        String description = item.getDescription();
        double price = item.getPrice();

        System.out.printf("%-20s | %-30s | $%.2f\n", itemName, description, price);
    }

    System.out.println("-------------------------------------------------------------------");
}

    
    
    private void createItem() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the name of the new item: ");
    String itemName = scanner.nextLine();
        System.out.println("-----------------");


    System.out.print("Enter the description of the new item: ");
    String description = scanner.nextLine();
    System.out.println("-----------------");

    double price = 0.0;
    boolean validInput = false;

    while (!validInput) {
        try {
            System.out.print("Enter the price of the new item: ");
            price = Double.parseDouble(scanner.nextLine());
            validInput = true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid price.");
        }
    }
    System.out.println("-----------------");

    // Create a new FoodItem object
    FoodItem newItem = new FoodItem(itemName, description, price);

    // Add the new item to the menu
    addItemToMenu(newItem);

    System.out.println("\nNew item added to the menu:");
    System.out.println("-------------------------------------------------------------------");
    System.out.println("| Item Name          | Description             | Price  |");
    System.out.println("-------------------------------------------------------------------");
    System.out.printf("| %-18s | %-23s | $%.2f |\n", newItem.getItemName(), newItem.getDescription(), newItem.getPrice());
    System.out.println("-------------------------------------------------------------------");
}



    private void updateItem() {
    Scanner scanner = new Scanner(System.in);

    // Display the current menu for reference
    System.out.println("\nCurrent Menu:");
    viewMenu();
    System.out.println("+---------------------------------------+");
    System.out.println("|             Updating Item             |");
    System.out.println("+---------------------------------------+");
    // Prompt the user to enter the name of the item to update
    System.out.print("\nEnter the name of the item to update: ");
    String itemNameToUpdate = scanner.nextLine();
    System.out.println("-----------------");


    // Find the item in the menu
    FoodItem itemToUpdate = findItemByName(itemNameToUpdate);

    if (itemToUpdate != null) {
        // Prompt the user to enter the updated information
        System.out.println("Enter the updated information for the item:");
        

        System.out.print("Enter the new name of the item: ");
        String newItemName = scanner.nextLine();
        System.out.println("-----------------");

        System.out.print("Enter the new description of the item: ");
        String newDescription = scanner.nextLine();
        System.out.println("-----------------");

        double newPrice = 0.0;
        boolean validInput = false;

    while (!validInput) {
        try {
            System.out.print("Enter the new price of the item: ");
            newPrice = Double.parseDouble(scanner.nextLine());
            validInput = true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid price.");
        }
    }
    System.out.println("-----------------");

        // Update the item with the new information
        itemToUpdate.setItemName(newItemName);
        itemToUpdate.setDescription(newDescription);
        itemToUpdate.setPrice(newPrice);

        System.out.println("\nItem updated successfully!");
    System.out.println("-------------------------------------------------------------------");
    System.out.println("| Item Name          | Description             | Price  |");
    System.out.println("-------------------------------------------------------------------");
    System.out.printf("| %-18s | %-23s | $%.2f |\n", newItemName, newDescription, newPrice);
    System.out.println("-------------------------------------------------------------------");
    } else {
        System.out.println("Item not found in the menu.");
    }
}

public FoodItem findItemByName(String itemName) {
    // Find and return the FoodItem with the specified name in the menu
    for (FoodItem item : menu) {
        if (item.getItemName().equalsIgnoreCase(itemName)) {
            return item;
        }
    }
    return null;
}


    public void deleteItem() {
        
        Scanner scanner = new Scanner(System.in);

    // Display the current menu for reference
    System.out.println("\nCurrent Menu:");
    viewMenu();

    // Prompt the user to enter the name of the item to update
    System.out.print("\nEnter the name of the item to Delete: ");
    String itemNameToDelete = scanner.nextLine();
        
        FoodItem itemToRemove = findItemByName(itemNameToDelete);
        if (itemToRemove != null) {
            menu.remove(itemToRemove);
            System.out.println("Item '" + itemNameToDelete + "' deleted from the menu.");
        } else {
            System.out.println("Item not found: " + itemNameToDelete);
        }
    }
    

   

         public void acceptOrRejectOrder() throws IOException {
        // Display orders for the specific vendor
        System.out.println("Orders for Vendor " + getVendorName() + ":");
        displayOrders();

        Scanner scanner = new Scanner(System.in);
        int orderIdToProcess;
        Order orderToProcess;

        while (true) {
            System.out.print("Enter the Order ID to accept or reject (0 to skip): ");
            orderIdToProcess = scanner.nextInt();

            if (orderIdToProcess == 0) {
                System.out.println("Skipping operation.");
                return;
            }

            orderToProcess = findOrderById(orderIdToProcess);

            if (orderToProcess != null && orderToProcess.getOrderStatus().equals("Pending Acceptance")) {
                break;
            } else {
                System.out.println("Order ID not found or not pending acceptance. Please try again.");
            }
        }

        System.out.print("Enter 'A' to accept or 'R' to reject: ");
        scanner.nextLine(); // Consume newline left-over
        String choice = scanner.nextLine().toUpperCase();
        CustomerDashboard customer = getCustomerByName(orderToProcess.getCustomerName());
        switch (choice) {
            case "A":
                orderToProcess.setStatus("Preparing");
                System.out.println("Order " + orderIdToProcess + " accepted.");
                updateTransactionStatus(orderIdToProcess, "Approved");
                overwriteOrdersToFile(getOrders(), "order_" + getVendorId() + ".txt");
                // Customer ID retrieval and notification sending logic here
                sendAcceptanceNotification(customer.getId(),orderIdToProcess,"has been accepted");
                break;

            case "R":
                orderToProcess.setStatus("Rejected");
                System.out.println("Order " + orderIdToProcess + " rejected.");
                updateTransactionStatus(orderIdToProcess, "Denied");

                
                if (customer != null) {
                    double refundAmount = calculateTotalPrice(orderToProcess); // Implement this method to calculate refund
                    String m = customer.getName();
                     double newBalance = (getCustomerBalance(m) + refundAmount);
                    customer.setBalance(newBalance);
                    int customerid = customer.getId();
                    customer.updateBalanceInFile(customerid, newBalance);
                    System.out.println("Refund of $" + refundAmount + " processed for customer " + customer.getName());
                    overwriteOrdersToFile(getOrders(), "order_" + getVendorId() + ".txt");
                    sendAcceptanceNotification(customer.getId(), orderIdToProcess, "has been rejected");
                } else {
                    System.out.println("Customer not found.");
                }
                break;

            default:
                System.out.println("Invalid choice. Please enter 'A' to accept or 'R' to reject.");
                break;
        }
    }

     public CustomerDashboard getCustomerByName(String customerName) {
        for (CustomerDashboard customer : customerss) {
            if (customer.getName().equalsIgnoreCase(customerName)) {
                return customer;
            }
        }
        return null; // Return null if customer is not found
    }
     
     
     public static double getCustomerBalance(String customerName) {
        String filePath = "customer_info.txt"; // Provide the correct file path

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].trim().equalsIgnoreCase(customerName.trim())) {
                    // Assuming the balance is in parts[1]
                    return Double.parseDouble(parts[3].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Handle the exception based on your needs
        }

        return 0.0; // Or you can return any default value for balance
    }
     
private void updateTransactionStatus(int orderId, String newStatus) {
    try {
        String filename = "transactions_" + vendorId + ".txt";
        List<String> transactions = Files.readAllLines(Paths.get(filename));

        for (int i = 0; i < transactions.size(); i++) {
            String transaction = transactions.get(i);
            String[] parts = transaction.split(";");
            int currentOrderId = Integer.parseInt(parts[0]);

            if (currentOrderId == orderId) {
                // Update the status for the identified transaction
                transactions.set(i, parts[0] + ";" + parts[1] + ";" + parts[2] + ";" + newStatus + ";" + parts[4] );
                break;
            }
        }

        // Write the modified transaction data back to the file
        Files.write(Paths.get(filename), transactions);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private Order findOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    private void displayOrders() {
        for (Order order : orders) {
            if (order.getOrderStatus().equals("Pending Acceptance")) {
                System.out.println(order);
            }
        }
        
    }
        


private void updateOrderStatus() throws IOException {
        // Implement logic for updating order status
        //System.out.println("Updating Order Status...");

        // Display orders with "Preparing" status
        System.out.println("Orders with Preparing status for Vendor " + getVendorName() + ":");
        for (Order order : orders) {
            if (order.getOrderStatus().equals("Preparing")) {
                System.out.println("Order ID: " + order.getOrderId() + ", Customer: " + order.getCustomerName());
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Order ID to mark as Ready to collect (0 to skip): ");
        int orderIdToMarkReady = scanner.nextInt();

        if (orderIdToMarkReady != 0 ) {
            for (Order order : orders) {
                if (order.getOrderId() == orderIdToMarkReady && order.getOrderStatus().equals("Preparing")) {
                    
                    
                    order.setStatus("Ready to collect");
                    System.out.println("Order ID " + orderIdToMarkReady + " status updated to Ready to collect.");
                    overwriteOrdersToFile(getOrders(), "order_" + getVendorId() + ".txt");
                    CustomerDashboard customer = getCustomerByName(order.getCustomerName());
                    sendAcceptanceNotification(customer.getId(),order.getOrderId()," is ready to collect");
                    
                    
                    return;
                }
            }
            System.out.println("Invalid Order ID or Order is not in Preparing status.");
        }
    }

  public void checkOrderHistory() {
        String filename = "order_" + vendorId + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<String> orders = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int orderId = Integer.parseInt(parts[0]);
                String customerName = parts[1];
                String status = parts[2];
                String items = parts[3];

                if (!status.equals("Pending Payment")) {
                    String orderDetails = String.format("Order ID: %d, Customer: %s, Status: %s, Items: %s",
                            orderId, customerName, status, items);
                    orders.add(orderDetails);
                    
                }
            }

            if (!orders.isEmpty()) {
                
                System.out.println("Order History for Vendor ID: " + vendorId);
                System.out.println("------------------------------------------");
                for (String order : orders) {
                    System.out.println(order);
                }
                System.out.println("------------------------------------------");
            } else {
                System.out.println("No order history found for the vendor.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    



public void readCustomerReviews(int vendorId) {
        try {
            String filename = "reviews_" + vendorId + ".txt";
            List<String> reviews = Files.readAllLines(Paths.get(filename));

            if (reviews.isEmpty()) {
                System.out.println("No reviews found for this vendor.");
            } else {
                System.out.println("Customer reviews for Vendor ID: " + vendorId);
                System.out.println("------------------------------------------");
                for (String review : reviews) {
                    String[] parts = review.split(";");
                    int reviewId = Integer.parseInt(parts[0]);
                    String customerName = parts[1];
                    String reviewText = parts[2];

                    String formattedReview = String.format("Review ID: %d, Customer: %s, Review: %s",
                            reviewId, customerName, reviewText);
                    System.out.println(formattedReview);
                }
                System.out.println("------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


public void showRevenueDashboard() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Choose the interval for viewing revenue:");
    System.out.println("1. Daily");
    System.out.println("2. Weekly");
    System.out.println("3. Monthly");
    System.out.println("4. Yearly");
    System.out.print("Enter your choice (1-4): ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    String interval = getIntervalFromChoice(choice);
    if (interval == null) {
        System.out.println("Invalid choice.");
        return;
    }

    String filename = "transactions_" + vendorId + ".txt";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate now = LocalDate.now();
    double totalRevenue = 0;

    try {
        List<String> transactions = Files.readAllLines(Paths.get(filename));

        System.out.println("Revenue Dashboard for Vendor ID: " + vendorId + " - " + interval);
        System.out.println("--------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s %-20s %-10s%n", "Order ID", "Total", "Customer Name", "Date", "Status");
        System.out.println("--------------------------------------------------");

        for (String transaction : transactions) {
            String[] parts = transaction.split(";");
            int orderId = Integer.parseInt(parts[0]);
            double total = Double.parseDouble(parts[1]);
            String customerName = parts[2];
            LocalDate transactionDate = LocalDate.parse(parts[4], formatter);
            String status = parts[3];
            long daysBetween = ChronoUnit.DAYS.between(transactionDate, now);

            if (status.equalsIgnoreCase("Approved") && isWithinInterval(daysBetween, interval)) {
                System.out.printf("%-20d $%-19.2f %-20s %-20s %-10s%n", orderId, total, customerName, transactionDate, status);
                totalRevenue += total;
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Total Revenue of Approved Transactions for %s: $%.2f%n", interval, totalRevenue);
    } catch (IOException e) {
        System.out.println("Error reading from " + filename + ": " + e.getMessage());
    }
}

public static String getIntervalFromChoice(int choice) {
    switch (choice) {
        case 1:
            return "daily";
        case 2:
            return "weekly";
        case 3:
            return "monthly";
        case 4:
            return "yearly";
        default:
            return null;
    }
}

private boolean isWithinInterval(long daysBetween, String interval) {
    switch (interval.toLowerCase()) {
        case "daily":
            return daysBetween <= 1;
        case "weekly":
            return daysBetween <= 7;
        case "monthly":
            return daysBetween <= 30;
        case "yearly":
            return daysBetween <= 365;
        default:
            return false;
    }
}




public double calculateTotalPrice(Order order) {
    double totalPrice = 0.0;
    List<OrderItem> items = order.getItems();

    for (OrderItem item : items) {
        FoodItem foodItem = item.getFoodItem(menu); 
        if (foodItem != null) {
            totalPrice += foodItem.getPrice() * item.getQuantity();
        }
    }if (order.getDeliveryStatus().equals("Pending Runner")){
        totalPrice += 3;
    }

    return totalPrice;
}


public void overwriteMenuToFile(List<FoodItem> menuItems, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (FoodItem item : menuItems) {
                String line = item.getItemName() + ";" + item.getDescription() + ";" + item.getPrice();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void overwriteOrdersToFile(List<Order> orders, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Order order : orders) {
                StringBuilder line = new StringBuilder();
                line.append(order.getOrderId()).append(";");
                line.append(order.getCustomerName()).append(";");
                line.append(order.getOrderStatus()).append(";");

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
            }
        }    
    }
    
    
    
    // Methods For ADMIN
    public void writeVendorData( String n, String p) {
        try {
            File vFile = new File("vendor_info.txt");
            FileWriter fileWriter = new FileWriter(vFile, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(vendorId + "," + n + "," + p);
            printWriter.close();
            System.out.println("Vendor data written to the file");
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static void readVendorData() {
        try {
            File vFile = new File("vendor_info.txt");
            BufferedReader reader = new BufferedReader(new FileReader(vFile));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static void searchVendor(int id) {
        try {
            File vFile = new File("vendor_info.txt");
            BufferedReader reader = new BufferedReader(new FileReader(vFile));
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int currentId = Integer.parseInt(data[0]);
                if (currentId == id) {
                    System.out.println("Vendor Found: " + line);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Vendor not found");
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }
    
    public static void deleteVendor(int id) {
        try {
            File inFile = new File("vendor_info.txt");
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
                System.out.println("Vendor deleted successfully");
            } else {
                System.out.println("Vendor not found");
            }

        } catch (IOException ex) {
            System.out.println("Error in deleting the vendor: " + ex.getMessage());
        }
    }
    
    public static void updateVendorInfo(int id, String newUsername, String newPassword) {
        List<String> fileContent = new ArrayList<>();
        boolean found = false;

        try {
            File inFile = new File("vendor_info.txt");
            BufferedReader br = new BufferedReader(new FileReader(inFile));

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int currentId = Integer.parseInt(data[0]);
                if (currentId == id) {
                    fileContent.add(id + "," + newUsername + "," + newPassword);
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
                System.out.println("Vendor's information updated successfully");
            } else {
                System.out.println("Vendor not found");
            }

        } catch (IOException ex) {
            System.out.println("Error in updating the vendor: " + ex.getMessage());
        }
    } 
    
    public static void deleteVendorFiles(int vendorId) throws IOException {
    // Delete review file
    deleteFile("reviews_" + vendorId + ".txt");

    // Delete transactions file
    deleteFile("transactions_" + vendorId + ".txt");

    // Delete menu file
    deleteFile("menu_" + vendorId + ".txt");
    
    deleteFile("order_" + vendorId + ".txt");
}

// Helper method to delete a file
private static void deleteFile(String fileName) throws IOException {
    Files.deleteIfExists(Paths.get(fileName));
}


public static String getVendorPassword(int vendorId) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get("vendor_info.txt"));
    for (String line : lines) {
        String[] parts = line.split(",");
        // Assuming the first part is the ID and the third part is the password
        if (Integer.parseInt(parts[0]) == vendorId) {
            return parts[2]; // Return the password
        }
    }
    return null; // Vendor ID not found
}

public static void showVendorNotifications(int vendorId) {
    try {
        List<String> lines = Files.readAllLines(Paths.get("vendor_notifications.txt"));
        System.out.println("Notifications for Vendor ID " + vendorId + ":");
        boolean hasNotifications = false;

        for (String line : lines) {
            String[] parts = line.split(",");
            if (Integer.parseInt(parts[1]) == vendorId) {
                hasNotifications = true;
                System.out.println(line); // Print the entire line as is
            }
        }

        if (!hasNotifications) {
            System.out.println("No notifications found for this vendor.");
        }
    } catch (IOException e) {
        System.out.println("Error reading from vendor_notifications.txt: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Error in parsing vendor ID: " + e.getMessage());
    }
    }


    public void sendAcceptanceNotification(int customerId, int orderId, String Acceptance) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        String notificationMessage = "Order " + orderId + " " + Acceptance + " by " + vendorName + " on " + formattedDate + ".";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendor_notifications.txt", true))) {
            writer.write("Vendor ID," + customerId + ", " + notificationMessage);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to vendor_notifications.txt: " + e.getMessage());
        }
    }
}