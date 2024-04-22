package com.mycompany.finaljava;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DeliveryRunner extends Runner {
    
    private boolean st = false;
    private final Scanner sc = new Scanner(System.in);
    private static String username;
    private static int RunnerID;
    private String password;

    
    
    
    public static void viewTasks(int runnerId) throws IOException {
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        List<String[]> pendingOrders = new ArrayList<>();
        Map<String, File> orderIdToFileMap = new HashMap<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().startsWith("order_")) {
                    try (BufferedReader orderReader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = orderReader.readLine()) != null) {
                            String[] parts = line.split(";");
                            if ("Pending Runner".equals(parts[5].trim())&& "Ready to collect".equals(parts[2].trim())) {
                                pendingOrders.add(parts);
                                orderIdToFileMap.put(parts[0], file); // Map order ID to its file
                                System.out.println("Order ID: " + parts[0]);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (pendingOrders.isEmpty()) {
            System.out.println("          No pending tasks available❎.           ");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Order ID you want to accept/reject      : ");
        String chosenOrderId = scanner.nextLine();

        for (String[] order : pendingOrders) {
            if (order[0].equals(chosenOrderId)) {
                System.out.println("Accept or Reject Order ID " + chosenOrderId + "? (A✅/R❎):");
                String choice = scanner.nextLine().toUpperCase();

                switch (choice) {
                    case "A":
                        // Handle order acceptance
                        handleAcceptOrder(order, runnerId, orderIdToFileMap.get(order[0]));
                        
                        break;
                    case "R":
                        // Handle order rejection
                        System.out.println("\n     Order " + chosenOrderId + " declined by runner    ");
                        break;
                    default:
                        System.out.println("\n     *Invalid option. Please try again*    ");
                        break;
                }
                return; // Break out of the loop once an order has been processed
            }
        }
        System.out.println("\n|+------------------+|");
        System.out.println("\nOrder ID not found.");
        System.out.println("\n|+------------------+|");
    }

    private static void handleAcceptOrder(String[] order, int runnerId, File file) throws IOException {
    order[5] = "In Progress";
    order = addRunnerIdToOrder(order, runnerId); // Add runner ID to the order
    // Write the accepted order to runner's history
    writeOrderToRunnerHistory(order, runnerId);
    // Update the status in the original order file
    updateStatus(file, String.join(";", order));
    System.out.println("Order " + order[0] + " accepted.");

    // Retrieve customer name from the order
    String customerName = order[1];

    // Find customer ID by customer name
    int customerId = findCustomerIdByName(customerName);

    // Check if customer ID is found
    if (customerId != -1) {
        sendAcceptanceNotification(customerId, Integer.parseInt(order[0]), "accepted");
    } else {
        System.out.println("Customer ID for " + customerName + " not found.");
    }
}



    private static String[] addRunnerIdToOrder(String[] orderParts, int runnerId) {
        // Assuming the runner ID should be added as the last part of the order
        String[] newParts = new String[orderParts.length + 1];
        System.arraycopy(orderParts, 0, newParts, 0, orderParts.length);
        newParts[newParts.length - 1] = String.valueOf(runnerId);
        return newParts;
    }

    private static void writeOrderToRunnerHistory(String[] orderParts, int runnerId) throws IOException {
        String orderString = String.join(";", orderParts);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("runner_history.txt", true))) {
            writer.write(orderString);
            writer.newLine();
        }
    }
    private static void updateStatus(File file, String newLine) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (String line : lines) {
                if (line.contains("Pending Runner")) {
                    writer.write(newLine + System.lineSeparator());
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }
        }
    }
    public static void updateOrderStatus(int runnerId) {
        String historyFilename = "runner_history.txt";
        List<String> updatedLines = new ArrayList<>();
        boolean orderFound = false;
        String CustomerName = null;
 

        try (BufferedReader reader = new BufferedReader(new FileReader(historyFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if ("In Progress".equals(parts[5].trim())) {
                    System.out.println("Order ID: " + parts[0] + ", Status: " + parts[5]);
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading from " + historyFilename + ": " + e.getMessage());
            return;
        }

        System.out.print("Enter the Order ID to mark as Delivered: ");
        Scanner scanner = new Scanner(System.in);
        String orderIdToDeliver = scanner.nextLine();

        for (int i = 0; i < updatedLines.size(); i++) {
            String[] parts = updatedLines.get(i).split(";");
            if (parts[0].equals(orderIdToDeliver) && "In Progress".equals(parts[5].trim())) {
                parts[5] = "Delivered"; // Update status to Delivered
                updatedLines.set(i, String.join(";", parts));
                orderFound = true;
                CustomerName = parts[1];
                
            }
        }

        if (!orderFound) {
            System.out.println("Order ID not found or not in progress.");
            return;
        }

        // Write the updated lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFilename))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            System.out.println("Order status updated to Delivered.");
            int customerId = findCustomerIdByName(CustomerName);
            sendAcceptanceNotification(customerId,Integer.parseInt(orderIdToDeliver),"Order has been Delivered");
            
        } catch (IOException e) {
            System.out.println("Error writing to " + historyFilename + ": " + e.getMessage());
        }
    }

    public static void viewFeedback(int runnerId) {
        String filename = "runner_reviews.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean hasFeedback = false;

            System.out.println("Feedback for Runner ID " + runnerId + ":"
                    +"\n+--------------------------------------------------+");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[3].trim().equals(String.valueOf(runnerId))) {
                    // Display the feedback


                    System.out.println("Order ID: " + parts[0] + ", Customer: " + parts[1] + ", Review: " + parts[2]);
                    hasFeedback = true;
                }
            }

            if (!hasFeedback) {
                System.out.println("No feedback found for this runner.");
            }

        } catch (IOException e) {
            System.out.println("Error reading from " + filename + ": " + e.getMessage());
        }
    }

    public static void main_Page(int runnerid) throws IOException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n+--------------------------------------------------+");
            System.out.println("|                Runner Operations Menu            |");
            System.out.println("+--------------------------------------------------+");
            System.out.println("| 1. View Available Tasks                          |");
            System.out.println("| 2. Update Delivery Status                        |");
            System.out.println("| 3. View Delivery History                         |");
            System.out.println("| 4. View Runner Feedback                          |");
            System.out.println("| 5. View Notifications                            |");
            System.out.println("| 0. Exit                                          |");
            System.out.println("+--------------------------------------------------+");

            System.out.print("Enter your choice: ");
            int runnerChoice = sc.nextInt();

            switch (runnerChoice) {
                case 1:
                    System.out.println("+--------------------------------------------------+");
                    System.out.println("|                   Viewing Task                   |");
                    System.out.println("+--------------------------------------------------+");
                    viewTasks(runnerid);
                    break;
                case 2:
                    System.out.println("+--------------------------------------------------+");
                    System.out.println("|                Updating Order Status             |");
                    System.out.println("+--------------------------------------------------+");
                    updateOrderStatus(runnerid);
                    break;
                case 3:
                    // Method to view delivery history
                    System.out.println("+--------------------------------------------------+");
                    System.out.println("|               Viewing Task History               |");
                    System.out.println("+--------------------------------------------------+");
                    viewOrderHistory(runnerid);
                    break;
                case 4:

                    System.out.println("+--------------------------------------------------+");
                    System.out.println("|                 Viewing FeedBack                 |");
                    System.out.println("+--------------------------------------------------+");
                    // Method to view feedback for the runner
                    viewFeedback(runnerid);
                    break;
                case 5:
                    System.out.println("+--------------------------------------------------+");
                    System.out.println("|                 Viewing Notifications            |");
                    System.out.println("+--------------------------------------------------+");
                    viewNotifications(runnerid);
                case 0:
                    System.out.println("Exiting runner operations");
                    return;
                default:
                    System.out.println("\n     *Invalid option. Please try again*    ");
                    break;
            }
        }
    }
    public static void viewOrderHistory(int runnerId) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose the interval for viewing order history:");
        System.out.println("\n+-------------------------------------------------+");
        System.out.println("| 1. Daily                                         |");
        System.out.println("| 2. Weekly                                        |");
        System.out.println("| 3. Monthly                                       |");
        System.out.println("| 4. Yearly                                        |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("Enter your choice (1-4): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String interval = getIntervalFromChoice(choice);
        if (interval == null) {
            System.out.println("\n*Invalid option. Please try again*\n");
            return;
        }

        String filename = "runner_history.txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        double totalRevenue = 0.0;
        final double DELIVERY_FEE = 3.0;
        List<String> orderDetails = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 6 && parts[6].equals(String.valueOf(runnerId))) {
                    LocalDate orderDate = LocalDate.parse(parts[4], formatter);
                    long daysBetween = ChronoUnit.DAYS.between(orderDate, now);

                    if ((parts[5].equals("Delivered")) && isWithinInterval(daysBetween, interval)) {
                        totalRevenue += DELIVERY_FEE;
                        orderDetails.add("Order ID: " + parts[0] + ", Date: " + parts[4] + ", Status: " + parts[5] + ", Revenue: $" + DELIVERY_FEE);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from " + filename + ": " + e.getMessage());
            return;
        }

        if (orderDetails.isEmpty()) {
            System.out.println("No orders found for the specified interval.");
        } else {
            System.out.println("Orders for " + interval + ":");
            for (String detail : orderDetails) {
                System.out.println(detail);
            }
            System.out.println("Total Revenue for " + interval + ": $" + totalRevenue);
        }
    }

    private static boolean isWithinInterval(long daysBetween, String interval) {
        switch (interval.toLowerCase()) {
            case "daily":
                return daysBetween == 0;
            case "weekly":
                return daysBetween < 7;
            case "monthly":
                return daysBetween < 30;
            case "yearly":
                return daysBetween < 365;
            default:
                return false;
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
                return null;  // Invalid choice
        }
    }
    
    
    
    private static void sendAcceptanceNotification(int customerId, int orderId, String acceptance) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDate = now.format(formatter);

    String notificationMessage = "Order " + orderId + " " + acceptance + " by runner on " + formattedDate + ".";

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("customer_notifications.txt", true))) {
        writer.write(orderId + "," + customerId + "," + notificationMessage);
        writer.newLine();
    } catch (IOException e) {
        System.out.println("Error writing to customer_notifications.txt: " + e.getMessage());
    }
}
    
    private static int findCustomerIdByName(String customerName) {
    // Assuming customer_info.txt has customer ID, name, password, and balance
    try (BufferedReader reader = new BufferedReader(new FileReader("customer_info.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2 && parts[1].trim().equals(customerName)) {
                return Integer.parseInt(parts[0]); // Return customer ID
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading from customer_info.txt: " + e.getMessage());
    }
    return -1; // Return -1 if not found
}
    
    
    public static void viewNotifications(int runnerId) {
        String filename = "runner_notifications.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean hasNotifications = false;

            System.out.println("Notifications for Runner ID: " + runnerId);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int notificationRunnerId = Integer.parseInt(parts[1].trim());

                if (notificationRunnerId == runnerId) {
                    hasNotifications = true;
                    // Assuming the notification message is the second part of the line
                    System.out.println(line);
                }
            }

            if (!hasNotifications) {
                System.out.println("No notifications found for Runner ID: " + runnerId);
            }

        } catch (IOException e) {
            System.out.println("Error reading from " + filename + ": " + e.getMessage());
        }
    }
}