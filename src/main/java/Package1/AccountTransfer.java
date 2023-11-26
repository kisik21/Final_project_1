package Package1;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountTransfer {
    private static final String INPUT_DIR = "src/main/resources/input";
    private static final String ARCHIVE_DIR = "src/main/resources/archive";
    private static final String REPORT_FILE = "src/main/resources/report/report.txt";
    private static final String INFO_FILE = "src/main/resources/accounts&sums/accounts&sums.txt";
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
                    processFile(path, report);
                    report.add(getReportEntry(path, "file successfully processed"));
                    moveFileToArchive(path);
                } catch (Exception e) {
                    report.add(getReportEntry(path, "error in processing time: " + e.getMessage()));
                }
            } else {
                report.add(getReportEntry(path, "invalid format, file skipped"));
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
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(directory))) {
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

    private static void processFile(Path path, List<String> report) throws IOException {
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
            String str=updateAccountAmount(fromAccount, -amount);
            report.add(getReportEntry(path, "transfer from " + fromAccount + " to " + toAccount +" " + amount + " " + str ));
            if (str.equals("successfully processed")) {
                updateAccountAmount(toAccount, amount);
            }
        }
    }

    private static boolean isValidAccountNumber(String accountNumber) {
        Pattern pattern = Pattern.compile("[0-9]{5}+-[0-9]{5}");
        Matcher matcher = pattern.matcher(accountNumber);
        return matcher.matches();
    }

    private static String updateAccountAmount(String accountNumber, int amount) {
        try {
            File file = new File(INFO_FILE);
            Scanner fileScanner = new Scanner(file);
            StringBuilder sb = new StringBuilder();
            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();
                String[] parts = line.split(" ");
                if (parts[0].equals(accountNumber)) {
                    int newSum=Integer.parseInt(parts[1])+amount;
                    if (newSum<0) {
                        return "not enough money";
                    }
                    parts[1] = Integer.toString(newSum);
                    line = parts[0] + " " + parts[1];
                }
                sb.append(line).append("\n");
            }
            fileScanner.close();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(String.valueOf(sb));
            fileWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        } catch (IOException e) {
            System.out.println("Error: IOException.");
        }
        return "successfully processed";
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