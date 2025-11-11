package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ForkJoinPool;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class ArraySummerTest {

    private ForkJoinPool pool;

    @BeforeEach
    void setUp() {
        pool = new ForkJoinPool();
    }

    @Test
    void testSmallArrayDirectComputation() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArraySummer task = new ArraySummer(array, 0, array.length);

        long expectedSum = 55L;
        long actualSum = pool.invoke(task);

        assertEquals(expectedSum, actualSum);
    }

    @Test
    void testArraySizeEqualToThreshold() {
        int[] array = new int[20];
        long expectedSum = 0;
        for (int i = 0; i < 20; i++) {
            array[i] = i + 1;
            expectedSum += array[i];
        }

        ArraySummer task = new ArraySummer(array, 0, array.length);
        long actualSum = pool.invoke(task);

        assertEquals(expectedSum, actualSum);
    }

    @Test
    void testArraySizeTriggersForkJoin() {
        int[] array = new int[21];
        long expectedSum = 0;
        for (int i = 0; i < 21; i++) {
            array[i] = 10;
            expectedSum += 10;
        }

        ArraySummer task = new ArraySummer(array, 0, array.length);
        long actualSum = pool.invoke(task);

        assertEquals(expectedSum, actualSum);
    }

    @Test
    void testLargeArrayRandomData() {
        int largeSize = 50000;
        int[] array = new int[largeSize];
        long expectedSum = 0;

        Random random = new Random();
        for (int i = 0; i < largeSize; i++) {
            array[i] = random.nextInt(100);
            expectedSum += array[i];
        }

        ArraySummer task = new ArraySummer(array, 0, array.length);
        long actualSum = pool.invoke(task);

        assertEquals(expectedSum, actualSum);
    }

    @Test
    void testEmptyArray() {
        int[] array = {};
        ArraySummer task = new ArraySummer(array, 0, array.length);
        long actualSum = pool.invoke(task);

        assertEquals(0L, actualSum);
    }

    @Test
    void testSingleElementArray() {
        int[] array = {123};
        ArraySummer task = new ArraySummer(array, 0, array.length);
        long actualSum = pool.invoke(task);

        assertEquals(123L, actualSum);
    }

    @Test
    void testArrayWithNegativeNumbers() {
        int[] array = {10, -5, 20, -15, 1};
        ArraySummer task = new ArraySummer(array, 0, array.length);
        long actualSum = pool.invoke(task);

        assertEquals(11L, actualSum);
    }

    @Test
    void testSubArrayCalculation() {
        int[] array = {10, 20, 30, 40, 50, 60};
        ArraySummer task = new ArraySummer(array, 1, 4);
        long actualSum = pool.invoke(task);

        assertEquals(90L, actualSum);
    }

    @Test
    void testAllZerosArray() {
        int largeSize = 10000;
        int[] array = new int[largeSize];

        ArraySummer task = new ArraySummer(array, 0, array.length);
        long actualSum = pool.invoke(task);

        assertEquals(0L, actualSum);
    }

    @Test
    void testTaskCreation() {
        int[] array = {1, 1, 1};
        ArraySummer task = new ArraySummer(array, 0, array.length);
        assertNotNull(task);
    }
}