package dev.morling.onebrc;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CalculateAverage_atulgoel126 {

    private static final Path FILE = Path.of("./measurements.txt");
    private static final Path TEST_FILE = Path.of("./small_measurements.txt");
    private static final byte COLON = ';';
    private static final byte NEW_LINE = '\n';
    private static final byte HYPHEN = '-';
    private static final byte DOT = '.';
    private static final int NO_OF_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {


        try {
            // Instrumentation start
            long startTime = System.currentTimeMillis();
            long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            processLargeFile(FILE.toString());

            // Instrumentation end
            long endTime = System.currentTimeMillis();
            long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            long elapsedTime = endTime - startTime;
            long memoryUsed = endMemory - startMemory;

            System.out.println(String.format("Elapsed time: %d ms (%.2f seconds)", elapsedTime, elapsedTime / 1000.0));
            System.out.println(String.format("Memory used: %d bytes (%.2f MB)", memoryUsed, memoryUsed / (1024.0 * 1024.0)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void processLargeFile(String filePath) throws IOException {
        Map<String, Double> sum = new HashMap<>();
        Map<String, Double> min = new HashMap<>();
        Map<String, Double> max = new HashMap<>();
        int lineCounter = 0;
        int logInterval = 1000*1000*10;
        long startTime = System.currentTimeMillis(); // Start time for logging

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCounter++;
                String[] values = line.split(";");
                try {
                    double temp = Double.parseDouble(values[1]);
                    String city = values[0];
                    sum.put(city, sum.getOrDefault(city, 0.0) + temp);

                    min.put(city, Math.min(min.getOrDefault(city, Double.MAX_VALUE), temp));
                    max.put(city, Math.max(max.getOrDefault(city, Double.MIN_VALUE), temp));
                } catch (NumberFormatException e) {
                    // Handle parsing error if the column value is not a number
                    System.err.println("Invalid number format in row: " + line);
                }

                if (lineCounter % logInterval == 0) {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime;
                    System.out.println(String.format("Processed %d lines in %d ms (%.2f seconds)...", lineCounter, elapsedTime, elapsedTime / 1000.0));
                }
            }
        }

        System.out.println("Sum: " + sum);
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
    }
}

class Helper {
    void createSmallCopy(int count) {
        String inputFilePath = "./measurements.txt"; // Path to the large input file
        String outputFilePath = "./small_measurements.txt"; // Path to the smaller output file
        int linesToCopy = count; // Number of lines to copy

        try {
            createSmallFile(inputFilePath, outputFilePath, linesToCopy);
            System.out.println("Successfully created a smaller file with " + linesToCopy + " lines.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void createSmallFile(String inputFilePath, String outputFilePath, int linesToCopy) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            int linesCopied = 0;

            while ((line = reader.readLine()) != null && linesCopied < linesToCopy) {
                writer.write(line);
                writer.newLine();
                linesCopied++;
            }
        }
    }
}
