package com.mycompany.finaljava;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;



public class User {
    private static final String CREDENTIALS_FILE_PATH = "User.txt";
    private static final String ROLE_PREFIX = " role:";

    private String username;
    private String password;
    private String userRole;
    private String userName;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void login() {
        try {
            authenticateUser();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private void authenticateUser() throws IOException {
        String cred = "username:" + username + " password:" + password + " name:";

        try (BufferedReader br = new BufferedReader(new FileReader(CREDENTIALS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(cred)) {
                    userName = line.substring(cred.length(), line.indexOf(ROLE_PREFIX));
                    userRole = line.substring(line.indexOf(ROLE_PREFIX) + ROLE_PREFIX.length());
                    System.out.println("Welcome, " + userName + "!");
                    grantAccess();
                    return;
                }
            }
        }
        handleAuthenticationFailure();
    }

    private void grantAccess() {
        System.out.println(userRole + " access granted. You are inside the system.");
    }

    private void handleAuthenticationFailure() {
        System.out.println("Failed to access. Check your credentials.");
        promptForCredentials();
    }

    private void handleIOException(IOException e) {
        e.printStackTrace();
        System.out.println("An error occurred while processing credentials.");
    }

    private void promptForCredentials() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your USERNAME: ");
        String username = sc.nextLine();
        System.out.println("Enter your PASSWORD: ");
        String password = sc.nextLine();
        User user = new User(username, password);
        user.login();
    }

    public String GETuserRole() {
        return userRole;
    }
}


    


