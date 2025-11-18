package org.example;

import java.util.concurrent.RecursiveTask;

public class ArraySummer extends RecursiveTask<Long> {
    private static final int THRESHOLD = 20;
    private final int[] array;
    private final int start;
    private final int end;

    public ArraySummer(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;

        if (length < THRESHOLD) {
            return computeDirectly();
        }

        int middle = start + length / 2;

        ArraySummer leftTask = new ArraySummer(array, start, middle);
        ArraySummer rightTask = new ArraySummer(array, middle, end);

        leftTask.fork();
        Long rightResult = rightTask.compute();
        Long leftResult = leftTask.join();

        return leftResult + rightResult;
    }

    private long computeDirectly() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum;
    }
}
