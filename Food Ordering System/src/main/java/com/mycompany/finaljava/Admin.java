package com.mycompany.finaljava;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author OMAR
 */
public class Admin {
        
    Scanner scanner = new Scanner(System.in);
    private static final String ADMIN_INFO_FILE = "admin_info.txt";

    // Default Constructor
    public Admin() {}

    public String getUsername() {
        return readAdminInfo(1); // Assuming the username is at the second position
    }

    public String getPassword() {
        return readAdminInfo(2); // Assuming the password is at the third position
    }

    private String readAdminInfo(int position) {
        String result = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_INFO_FILE))) {
            String line = reader.readLine(); // Assuming there is only one line for admin info
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length > position) {
                    result = parts[position].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from " + ADMIN_INFO_FILE + ": " + e.getMessage());
        }
        return result;
    }
    
    
    public void handleAdminOperations(boolean continueProgram) throws IOException{
        
        boolean continueAdminOperations = true;
        while(continueAdminOperations){
            
            System.out.println("\n+-----------------------------------+");
            System.out.println("|       Admin Operations Menu       |");
            System.out.println("+-----------------------------------+");
            System.out.println("| 1. Customer_options               |");
            System.out.println("| 2. Vendor_options                 |");
            System.out.println("| 3. Runner_options                 |");
            System.out.println("| 0. Exit                           |");
            System.out.println("+-----------------------------------+");
            System.out.print("Enter your choice: ");
            int input2 = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
            
            if (input2 == 1) { // Customer operations
            boolean continueCustomerOperations = true;
            while (continueCustomerOperations) {
                System.out.println("\n+-----------------------------------+");
                System.out.println("|       Customer Options Menu       |");
                System.out.println("+-----------------------------------+");
                System.out.println("| 1. Add Customer                   |");
                System.out.println("| 2. Read Customer Data             |");
                System.out.println("| 3. Search Customer                |");
                System.out.println("| 4. Update Customer                |");
                System.out.println("| 5. Add Credit                     |");
                System.out.println("| 6. Delete Customer                |");
                System.out.println("| 7. Exit                           |");
                System.out.println("+-----------------------------------+");
                System.out.print("Enter your choice: ");
                int customerChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                if (customerChoice == 1) {
                    // Add Customer
                    addCustomer();
                    
                } else if (customerChoice == 2) {
                    
                    System.out.println("\n+--------------------------+");
                    System.out.println("|   Reading Customer Data  |");
                    System.out.println("+--------------------------+");
                    // Read Customer Data
                    CustomerDashboard.readCustomerData();
                } else if (customerChoice == 3) {
                    
                    System.out.println("\n+--------------------------+");
                    System.out.println("|   Searching Customer     |");
                    System.out.println("+--------------------------+");
                    // Search Customer
                    System.out.print("Enter Customer ID to search: ");
                    int searchId = scanner.nextInt();
                    CustomerDashboard.searchCustomer(searchId);
                    
                } else if (customerChoice == 4) {
                    // Update Customer
                    System.out.println("\n+--------------------------+");
                    System.out.println("|   Updating Customer      |");
                    System.out.println("+--------------------------+");
                    System.out.println("Enter Customer ID to update:");
                    int updateId = scanner.nextInt();
                    if (CustomerDashboard.doesCustomerIdExist(updateId))
                    {
                      scanner.nextLine(); // Consume newline
                    System.out.println("Enter new Username:");
                    String newUsername = scanner.nextLine();
                    System.out.println("Enter new Password:");
                    String newPassword = scanner.nextLine();
                    
                    if(vendorExists(newUsername,"customer_info.txt"))
                    {
                        System.out.println("Username Already Exists.");
                    }
                    else{
                      CustomerDashboard.updateCustomerInfo(updateId, newUsername, newPassword); // Method to update customer info  
                    }
                    }
                    else{
                        System.out.println("ID NOT FOUND");
                    }
                    
                } 
                else if (customerChoice == 5){
                    
                    System.out.println("\n+--------------------------+");
                    System.out.println("|   Adding Credit          |");
                    System.out.println("+--------------------------+");
                    
                    System.out.println("Enter Customer ID to update:");
                    int updateId = scanner.nextInt();
                    if (CustomerDashboard.doesCustomerIdExist(updateId)) {
                    System.out.println("Amount to Add:");
                    double creditToAdd = scanner.nextDouble();
                    CustomerDashboard.updateBalance(updateId, creditToAdd); // Method to update customer credit
                    CustomerDashboard.sendBalanceUpdateNotification(updateId, creditToAdd); // Send notification
                    }
                    else
                    {
                        System.out.println("ID NOT FOUND");
                    }
                }
                else if (customerChoice == 6) {
                    
                    System.out.println("\n+--------------------------+");
                    System.out.println("|   Deleting Customer      |");
                    System.out.println("+--------------------------+");
                    // Delete Customer
                    System.out.println("Enter Customer ID to delete:");
                    int deleteId = scanner.nextInt();
                    CustomerDashboard.deleteCustomer(deleteId);
                    
                    
                } else if (customerChoice == 7) {
                    
                    System.out.println("Exiting vendor operations.");
                    continueCustomerOperations = false;
                    input2 = 0;
                    // Exit Customer Operations
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        }
            
            else if (input2 == 2) { // Vendor operations
                boolean continueVendorOperations = true;
                while (continueVendorOperations) {
                    System.out.println("\n+--------------------------------------+");
                    System.out.println("|          Vendor Operations           |");
                    System.out.println("+--------------------------------------+");
                    System.out.println("| 1. Add Vendor                        |");
                    System.out.println("| 2. Read Vendor Data                  |");
                    System.out.println("| 3. Search Vendor                     |");
                    System.out.println("| 4. Update Vendor                     |");
                    System.out.println("| 5. Delete Vendor                     |");
                    System.out.println("| 6. Exit                              |");
                    System.out.println("+--------------------------------------+");
                    System.out.print("Enter your choice: ");
                    int vendorChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    if (vendorChoice == 1) {
                        
                        System.out.println("\n+---------------------------+");
                        System.out.println("|    Adding New Vendor      |");
                        System.out.println("+---------------------------+");
            
                        System.out.print("Enter the Vendor Name: ");
                        String vendorName = scanner.nextLine();
                        System.out.print("Enter the Vendor Password: ");
                        String vendorPassword = scanner.nextLine();
                        
                        if (!vendorExists(vendorName,"vendor_info.txt")){
                        Vendor newVendor = new Vendor(vendorName, vendorPassword);

                        
                        // Write vendor info to the file
                        writeVendorToFile(newVendor);
                        System.out.println("New vendor added successfully!");
                    }
                    else{
                        System.out.println("Username already Exists.");
                    }
                        // Create a new Vendor object
                        
            
                    
                    } else if (vendorChoice == 2) {
                        
                        System.out.println("\n+---------------------------+");
                        System.out.println("|    Reading Vendor Data    |");
                        System.out.println("+---------------------------+");
                        Vendor.readVendorData();
                        
                    } else if (vendorChoice == 3) {
                        
                        System.out.println("\n+---------------------------+");
                        System.out.println("|    Searching Vendor       |");
                        System.out.println("+---------------------------+");
                        System.out.println("Enter Vendor ID to search:");
                        int searchId = scanner.nextInt();
                        Vendor.searchVendor(searchId);
                        
                    } else if (vendorChoice == 4) {
                        
                        System.out.println("\n+---------------------------+");
                        System.out.println("|    Updating Vendor Info   |");
                        System.out.println("+---------------------------+");
                        System.out.println("Enter Vendor ID to update:");
                        int updateId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter new Username:");
                        String newUsername = scanner.nextLine();
                        System.out.println("Enter new Password:");
                        String newPassword = scanner.nextLine();
                        if(vendorExists(newUsername,"vendor_info.txt"))
                        {
                            System.out.println("Username Already Exists.");
                        }
                        else{
                            Vendor.updateVendorInfo(updateId, newUsername, newPassword);
                        }
                        
                        
                    } else if (vendorChoice == 5) {
                        
                        System.out.println("\n+---------------------------+");
                        System.out.println("|    Deleting Vendor        |");
                        System.out.println("+---------------------------+");
                        System.out.println("Enter Vendor ID to delete:");
                        int deleteId = scanner.nextInt();
                        Vendor.deleteVendor(deleteId);
                        Vendor.deleteVendorFiles(deleteId);
                        
                    } else if (vendorChoice == 6) {
                        
                        System.out.println("Exiting vendor operations.");
                        continueVendorOperations = false;
                        input2 = 0;
                        
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                }
            }
            
            else if (input2 == 3) { // Runner operations
                List<Runner> runners = new ArrayList<>();
                boolean continueRunnerOperations = true;
                while (continueRunnerOperations) {
                    System.out.println("\n+-----------------------------------+");
                    System.out.println("|       Runner Operations Menu      |");
                    System.out.println("+-----------------------------------+");
                    System.out.println("| 1. Add Runner                     |");
                    System.out.println("| 2. Read Runner Data               |");
                    System.out.println("| 3. Search Runner                  |");
                    System.out.println("| 4. Update Runner                  |");
                    System.out.println("| 5. Delete Runner                  |");
                    System.out.println("| 6. Exit                           |");
                    System.out.println("+-----------------------------------+");
                    System.out.print("Enter your choice: ");
                    int runnerChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline


                    if (runnerChoice == 1) {
                        // Add Runner logic
                        System.out.println("\n+----------------------------+");
                        System.out.println("|      Adding New Runner     |");
                        System.out.println("+----------------------------+");
                        System.out.print("Enter Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String password = scanner.nextLine();

                        if (!usernameExists(username, "runner_info.txt")) {
                            Runner newRunner = new Runner(username, password);
                            newRunner.writeRunnerData(); // Method to write runner data and automatically assign ID
                            runners.add(newRunner); // Add to list

                            System.out.println("Runner added successfully with ID: " + newRunner.getRunnerID());
                        } else {
                            System.out.println("Username already Exists.");
                        }
                
                    } else if (runnerChoice == 2) {
                        System.out.println("\n+----------------------------+");
                        System.out.println("|    Reading Runner Data     |");
                        System.out.println("+----------------------------+");
                        Runner.readRunnerData(); // Method to read runner data
                        
                    
                    } else if (runnerChoice == 3) {

                        System.out.println("\n+----------------------------+");
                        System.out.println("|     Searching Runner       |");
                        System.out.println("+----------------------------+");
                        System.out.print("Enter Runner ID to search: ");
                        int searchId = scanner.nextInt();
                        Runner.searchRunner(searchId); // Method to search runner
                        

                    } else if (runnerChoice == 4) {
                        
                        System.out.println("\n+----------------------------+");
                        System.out.println("|    Updating Runner Info    |");
                        System.out.println("+----------------------------+");
                        System.out.print("Enter Runner ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new Username: ");
                        String newUsername = scanner.nextLine();
                        System.out.print("Enter new Password: ");
                        String newPassword = scanner.nextLine();
                        
                        if (!vendorExists(newUsername,"runner_info.txt")){
                            
                            Runner.updateRunnerInfo(updateId, newUsername, newPassword); // Method to update runner info
                            
                            
                        }
                        else{
                            System.out.println("Username already Exists.");
                        }
                        

                    } else if (runnerChoice == 5) {
                        
                        System.out.println("\n+----------------------------+");
                        System.out.println("|     Deleting Runner        |");
                        System.out.println("+----------------------------+");
                        System.out.print("Enter Runner ID to delete: ");
                        int deleteId = scanner.nextInt();
                        Runner.deleteRunner(deleteId); // Method to delete runner
                        

                    } else if (runnerChoice == 6) {
                        
                        System.out.println("Exiting Runner operations.");
                        continueRunnerOperations = false;
                        input2 = 0;

                    } else {
                        System.out.println("Invalid option. Please try again.");
                        
                    }
                }
            }
            else if (input2 == 0){
                
            System.out.println("Exiting Admin operations.");
            continueAdminOperations = false;
            continueProgram = true; // Set the flag to return to the main menu
            }
        }
    }
    

    // Method to write vendor info to the file
    private static void writeVendorToFile(Vendor vendor) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendor_info.txt", true))) {
            // Append the new vendor's information to the file
            writer.write(vendor.getVendorId() + "," + vendor.getVendorName() + "," + vendor.getVendorPass());
            writer.newLine(); // Add a new line for the next entry
        } catch (IOException e) {
            e.printStackTrace();
        }
        createVendorFiles(vendor.getVendorId());

    }


    // Method to create files for a new vendor
    private static void createVendorFiles(int vendorId) throws IOException {
        // Create review file
        createFile("reviews_" + vendorId + ".txt");

        // Create transactions file
        createFile("transactions_" + vendorId + ".txt");

        // Create menu file
        createFile("menu_" + vendorId + ".txt");

        createFile("order_" + vendorId + ".txt");
    }

    // Helper method to create a file
    private static void createFile(String fileName) throws IOException {
        Files.createFile(Paths.get(fileName));
    }
    
    private static boolean userExists(int id, String username, String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int existingId = Integer.parseInt(parts[0]);
                String existingUsername = parts[1];

                if (existingId == id || existingUsername.equals(username)) {
                    return true; // Customer with the same ID or username already exists
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false; // Customer doesn't exist with the same ID or username
    }
    private static boolean vendorExists(String username, String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String existingUsername = parts[1];

                if ( existingUsername.equals(username)) {
                    return true; // Customer with the same ID or username already exists
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false; // Customer doesn't exist with the same ID or username
    }
    
    private void addCustomer() throws IOException {
        System.out.println("\n+--------------------------+");
        System.out.println("|   Adding New Customer    |");
        System.out.println("+--------------------------+");
        System.out.println("Enter Customer Information");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Credit: ");
        double credit = scanner.nextDouble();
        scanner.nextLine(); // Consume the leftover newline

        if (!userExists(username, "customer_info.txt")) {
            CustomerDashboard newCustomer = new CustomerDashboard();
            newCustomer.writeCustomerData(username, password, credit);
        } else {
            System.out.println("Username already Exists.");
        }
    }

    private boolean userExists(String username, String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String existingUsername = parts[1];
                if (existingUsername.equals(username)) {
                    return true; // User with the same username already exists
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User does not exist with the same username
    }
    
    private static boolean usernameExists(String username, String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[1].equalsIgnoreCase(username)) {
                    return true; // Username already exists
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false; // Username does not exist
    }   
}


