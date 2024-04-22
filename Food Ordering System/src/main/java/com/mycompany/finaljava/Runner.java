package com.mycompany.finaljava;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Runner {
    private int RunnerID;
    private String RunnerUserName;
    private String RunnerPassword;

    public Runner(){}
    
    
    
    public Runner(int ID, String UN, String PW) {
        RunnerID = ID;
        RunnerUserName = UN;
        RunnerPassword = PW;
    }
    public Runner(String UN, String PW) {
        
        RunnerUserName = UN;
        RunnerPassword = PW;
    }

    
    
    
    
    public String getRunner(){
        return RunnerUserName;
    }
    public int getRunnerID(){
        return RunnerID;
    }
    
    
    
    
    
    public void writeRunnerData() {
        try {
            File rFile = new File("runner_info.txt");
            List<String> lines = Files.readAllLines(rFile.toPath());
            int newRunnerId = 1;
            for (String line : lines) {
                String[] data = line.split(",");
                int currentId = Integer.parseInt(data[0]);
                if (currentId >= newRunnerId) {
                    newRunnerId = currentId + 1;
                }
            }
            this.RunnerID = newRunnerId; // Set the new ID for this runner

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rFile, true))) {
                writer.write(this.RunnerID + "," + this.RunnerUserName + "," + this.RunnerPassword);
                writer.newLine();
                System.out.println("Runner data written to the file with ID: " + this.RunnerID);
            }
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static void readRunnerData() {
        try {
            File rFile = new File("runner_info.txt");
            BufferedReader reader = new BufferedReader(new FileReader(rFile));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static void searchRunner(int id) {
        try {
            File rFile = new File("runner_info.txt");
            BufferedReader reader = new BufferedReader(new FileReader(rFile));
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int currentId = Integer.parseInt(data[0]);
                if (currentId == id) {
                    System.out.println("Runner Found: " + line);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Runner not found");
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }
    
    public static void deleteRunner(int id) {
        File inFile = new File("runner_info.txt");
        File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

        try {
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

            if (!found) {
                System.out.println("Runner not found");
                return;
            }

            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            if (!tempFile.renameTo(inFile)) {
                System.out.println("Could not rename file");
            } else {
                System.out.println("Runner deleted successfully");
            }

        } catch (IOException ex) {
            System.out.println("Error in deleting the runner: " + ex.getMessage());
        }
    }


    public static void updateRunnerInfo(int id, String newUsername, String newPassword) {
        // Implementation to update username and password for a runner using their ID
        List<String> fileContent = new ArrayList<>();
        boolean found = false;

        try {
            File inFile = new File("runner_info.txt");
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
                System.out.println("Runner's information updated successfully");
            } else {
                System.out.println("Runner not found");
            }

        } catch (IOException ex) {
            System.out.println("Error in updating the runner: " + ex.getMessage());
        }
    }

}