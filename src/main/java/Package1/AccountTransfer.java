package Package1;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class AccountTransfer {

    private static final String INPUT_DIR = "src/main/resources/input";
    private static final String ARCHIVE_DIR = "src/main/resources/archive";
    private static final String REPORT_FILE = "src/main/resources/report/report.txt";

    public static void main(String[] args) {

        Scanner in=new Scanner(System.in);
        System.out.println("""
                Choose option:
                1 - parse files from input
                2 - display report file
                """);
        int option =in.nextInt();
        switch (option) {
            case 1:
                parseFiles();
                break;
            case 2:
                displayReport();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void parseFiles() {
        List<Path> paths = getAllFilesInDirectory(INPUT_DIR);
        List<String> report = new ArrayList<>();

        for (Path path : paths) {
            if (isValidFile(path)) {
                try {
                    processFile(path);
                    report.add(getReportEntry(path, "successfully processed"));
                    moveFileToArchive(path);
                } catch (Exception e) {
                    report.add(getReportEntry(path, "error in processing time: " + e.getMessage()));
                }
            } else {
                report.add(getReportEntry(path, "invalid format, skipped"));
            }
        }

        writeReportToFile(report);
    }

    private static void displayReport() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(REPORT_FILE));
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error reading report file: " + e.getMessage());
        }
    }

    private static List<Path> getAllFilesInDirectory(String directory) {
        List<Path> paths = new ArrayList<>();
        try {
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(directory));
            for (Path path : dirStream) {
                paths.add(path);
            }
        } catch (IOException e) {
            System.out.println("Error reading files in directory: " + e.getMessage());
        }
        return paths;
    }

    private static boolean isValidFile(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.endsWith(".txt");
    }

    private static void processFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            String fromAccount = parts[0];
            String toAccount = parts[1];
            int amount = Integer.parseInt(parts[2]);

            // Check account numbers and amounts
            if (amount < 0 || !isValidAccountNumber(fromAccount) || !isValidAccountNumber(toAccount)) {
                throw new IllegalArgumentException("Invalid transfer");
            }

            // Update account amounts
            updateAccountAmount(fromAccount, -amount);
            updateAccountAmount(toAccount, amount);
        }
    }

    private static boolean isValidAccountNumber(String accountNumber) {
        // Add your validation logic here
        return true;
    }

    private static void updateAccountAmount(String accountNumber, int amount) {
        // Update account amount in your data model
    }

    private static void moveFileToArchive(Path path) {
        try {
            Path archivePath = Paths.get(ARCHIVE_DIR, path.getFileName().toString());
            Files.move(path, archivePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error moving file to archive: " + e.getMessage());
        }
    }

    private static String getReportEntry(Path path, String status) {
        return LocalDateTime.now() + " | " + path.getFileName() + " | " + status + "\n";
    }

    private static void writeReportToFile(List<String> report) {
        try {
            Files.write(Paths.get(REPORT_FILE), report);
        }
        catch (IOException e) {
            System.out.println("Error writing report file: " + e.getMessage());
        }
    }
}


