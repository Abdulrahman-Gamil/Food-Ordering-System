package com.mycompany.finaljava;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author OMAR
 */
public class AssignmentAdmin {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException{
          
        List<Vendor> vendors = new ArrayList<>();
        loadVendorsAndMenu(vendors);
        loadOrders(vendors);
        List<CustomerDashboard> customers = new ArrayList<>();
        //List<CustomerDashboard> customers = 
        customers = loadCustomersFromFile(vendors);
        
        
        Scanner scanner = new Scanner(System.in); 
        
        Admin A = new Admin();
        int input = 0;
        
        boolean continueProgram = true;
        while (continueProgram) 
        {
            System.out.println("\n+-----------------------------------+");
            System.out.println("|           Main Menu               |");
            System.out.println("+-----------------------------------+");
            System.out.println("| 1. Admin                          |");
            System.out.println("| 2. Vendor                         |");
            System.out.println("| 3. Runner                         |");
            System.out.println("| 4. Customer                       |");
            System.out.println("+-----------------------------------+");
            System.out.print("Enter Number of Choice: ");
            input = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            if (input == 1) {
                System.out.println("\n+---------------------------+");
                System.out.println("|       Admin Login         |");
                System.out.println("+---------------------------+");
                System.out.print("Enter Admin USERNAME: "); 
                String username = scanner.nextLine();   
                
                
                // Compare user input with the username in Admin class
                if (username.equals(A.getUsername())) {
                    System.out.print("Enter Admin PASSWORD: ");
                    String password = scanner.nextLine();

                    // Compare user input with the password in Admin class
                    if (password.equals(A.getPassword())) {
                        System.out.println("Username and Password are correct\n Welcome Admin\n");
                        A.handleAdminOperations(continueProgram);
                        break; // Exit the loop
                    } else {
                        System.out.println("Incorrect Password.\nEnter Username and Password again");
                    }
                } else {
                    System.out.println("Username does not exist.");
                }
            }
            
            else if (input == 2) {
                // Work with vendors
                
                for (Vendor vendor : vendors) 
                {
                    System.out.println(vendor);
                    System.out.println("------------");
                }

                System.out.print("Enter the Vendor ID to work with: ");
                int selectedVendorId = scanner.nextInt();

                String vendorPassword = Vendor.getVendorPassword(selectedVendorId);
                if (vendorPassword != null) {
                    System.out.print("Enter your password: ");
                    String enteredPassword = scanner.next();

                    if (enteredPassword.equals(vendorPassword)) {
                    // Password is correct, proceed with vendor operations
                        for (Vendor vendor : vendors) {
                            if (vendor.getVendorId() == selectedVendorId) {
                                System.out.println("\nSelected Vendor: " + vendor.getVendorName());
                                vendor.handleVendorOperations();
                                break;
                            }
                        }
                } else {
                    
                    System.out.println("Incorrect password.");
                    }
            } else {
                System.out.println("Invalid Vendor ID.");
            } 
        }
            else if (input == 3) {
                
                // Runner operations
                System.out.print("Enter Runner ID: ");
                int runnerId = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.print("Enter Runner password: ");
                String password = scanner.nextLine();

                if (validateRunnerCredentials(runnerId, password)) {
                    // Proceed with runner operations
                    System.out.println("Runner authenticated successfully.");
                    // Runner operations menu code here
                    DeliveryRunner.main_Page(runnerId);
                } else {
                    System.out.println("Invalid Runner ID or Password.");
                }
            }
            else if (input == 4) {
                // Code for working with customers
                
                System.out.print("Enter the Customer ID to work with: ");
                int selectedCustomerId = scanner.nextInt();

                CustomerDashboard selectedCustomer = null;

                for (CustomerDashboard customer : customers) {
                    if (customer.getId() == selectedCustomerId) {
                        selectedCustomer = customer;
                        break;
                    }
                }

                if (selectedCustomer != null) {
                    System.out.print("Enter Customer Password: ");
                    String enteredPassword = scanner.next();

                    if (enteredPassword.equals(selectedCustomer.getPassword())) {
                        System.out.println("\nSelected Customer: " + selectedCustomer.getName());
                        CustomerDashboard dashboard = new CustomerDashboard(
                            selectedCustomer.getId(),
                            selectedCustomer.getName(),
                            selectedCustomer.getPassword(),
                            vendors,
                            selectedCustomer.getBalance()
                        );
                        dashboard.handleCustomerOperations();
                    } else {
                        System.out.println("Invalid Customer Password. Exiting...");
                    }
                } else {
                    System.out.println("Invalid Customer ID. Exiting...");
                }
            } else {
                System.out.println("Invalid choice. Exiting...");
            }

        }
    }
    private static List<Vendor> loadVendorsAndMenu(List<Vendor> vendors) {
    // Reset vendorIdCounter before loading vendors from file
    Vendor.resetVendorIdCounter();

    try (BufferedReader infoReader = new BufferedReader(new FileReader("vendor_info.txt"))) {
        String line;
        while ((line = infoReader.readLine()) != null) {
            try {
                String[] info = line.split(",");
                Vendor vendor = new Vendor(info[1].trim(), info[2].trim());
                vendors.add(vendor);

                // Read menu items from the corresponding menu file
                try (BufferedReader menuReader = new BufferedReader(new FileReader("menu_" + info[0].trim() + ".txt"))) {
                    String menuItemLine;
                    while ((menuItemLine = menuReader.readLine()) != null) {
                        String[] menuItem = menuItemLine.split(";");
                        vendor.addItemToMenu(new FoodItem(menuItem[0].trim(), menuItem[1].trim(), Double.parseDouble(menuItem[2].trim())));
                    }
                }

            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return vendors;
    }



    public static List<Vendor> loadOrders(List<Vendor> vendors) {
        try (BufferedReader infoReader = new BufferedReader(new FileReader("vendor_info.txt"))) {
            String line;
            while ((line = infoReader.readLine()) != null) {
                try {
                    String[] info = line.split(",");
                    try (BufferedReader orderReader = new BufferedReader(new FileReader("order_" + info[0].trim() + ".txt"))) {
                        Vendor currentVendor = vendors.get(Integer.parseInt(info[0].trim()) - 1);

                        String orderLine;
                        while ((orderLine = orderReader.readLine()) != null) {
                            String[] orderParts = orderLine.split(";");
                            int orderId = Integer.parseInt(orderParts[0]);
                            String customerName = orderParts[1];
                            String status = orderParts[2];
                            String deliveryStatus = orderParts[5];

                            String[] itemsString = orderParts[3].split(",");
                            List<OrderItem> items = new ArrayList<>();
                            for (String itemString : itemsString) {
                                String[] parts = itemString.split(":");
                                String itemName = parts[0];
                                int quantity = Integer.parseInt(parts[1]);
                                items.add(new OrderItem(itemName, quantity));
                            }

                            // Extracting the order date assuming it's the last part of the order line
                            String dateString = orderParts[4];
                            LocalDate orderDate = LocalDate.parse(dateString);

                            Order order = new Order(orderId, customerName, status, items, orderDate, deliveryStatus);
                            currentVendor.addOrder(order);
                        }
                    }


                }
                catch (NumberFormatException | IOException e) 
                {
                    e.printStackTrace();
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return vendors;
    }


    private static List<CustomerDashboard> loadCustomersFromFile(List<Vendor> vendors) throws IOException {
        List<CustomerDashboard> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("customer_info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String password = parts[2];
                double balance = Double.parseDouble(parts[3]); // Assuming balance is at index 3
                CustomerDashboard customer = new CustomerDashboard(id, name, password, vendors, balance);
                customers.add(customer);
            }
        }

        // Associate customers with vendors
        for (CustomerDashboard customer : customers) {
            for (Vendor vendor : vendors) {
                vendor.addCustomer(customer);
            }
        }

        return customers;
    }

    private static boolean validateRunnerCredentials(int runnerId, String password) {
        String filename = "runner_info.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].trim().equals(String.valueOf(runnerId)) && parts[2].trim().equals(password)) {
                    return true; // ID and password match
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from " + filename + ": " + e.getMessage());
        }
        return false; // ID and password do not match or file error
        }
    }

