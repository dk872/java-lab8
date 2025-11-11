package org.example;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForkJoinApp {
    private static final int ARRAY_SIZE = 1_000_000;
    private static final int MAX_VALUE = 100;
    private static final Logger LOGGER = Logger.getLogger(ForkJoinApp.class.getName());

    public static void main(String[] args) {
        int[] array = new int[ARRAY_SIZE];
        long expectedSum = initializeArray(array);

        System.out.printf("Array of size %,d elements generated.%n", ARRAY_SIZE);
        System.out.printf("Expected sum (sequential calculation): %,d%n", expectedSum);

        long forkJoinResult = runForkJoinTask(array);

        System.out.println("----------------------------------------------");
        verifyResults(expectedSum, forkJoinResult);
        System.out.println("----------------------------------------------");
    }

    private static long initializeArray(int[] array) {
        Random random = new Random();
        long expectedSum = 0;

        System.out.println("Creating and initializing an array...");

        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(MAX_VALUE + 1);
            expectedSum += array[i];
        }

        return expectedSum;
    }

    private static long runForkJoinTask(int[] array) {
        ArraySummer mainTask = new ArraySummer(array, 0, ARRAY_SIZE);
        Long result = 0L;

        try (ForkJoinPool pool = new ForkJoinPool()) {

            System.out.println("\nRunning parallel ForkJoin computation...");
            long startTime = System.nanoTime();

            result = pool.invoke(mainTask);

            long endTime = System.nanoTime();
            long executionTimeNanos = endTime - startTime;

            System.out.printf("Calculated sum (ForkJoin): %,d%n", result);
            System.out.printf("Execution time: %,d ns (or %.2f ms)%n",
                    executionTimeNanos, (double) executionTimeNanos / 1_000_000.0);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while executing ForkJoinPool.", e);
        }

        return result;
    }

    private static void verifyResults(long expected, long actual) {
        if (actual == expected) {
            System.out.println("The results match.");
        } else {
            System.err.println("Error: Results do not match!");
        }
    }
}