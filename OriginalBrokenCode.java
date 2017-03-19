package com.org.code;

import java.util.concurrent.Executors;
import java.lang.*;
import java.util.concurrent.ExecutorService;

public class OriginalBrokenCode {

    static Integer THREADS= 10;
    private static Object sum_lock = new Object();
    static private long totalSum = 0;

    static private float averageCalculator(long sum,int items){
        return(float)sum/items;
    }

    static class Number_count implements Runnable {
        private int min;
        private int max;

        Number_count(int min, int max){
            this.min = min;
            this.max = max;
        }
        public void run() {
            int sum = 0;
            for(int i = min; i <= max; i++) {
                synchronized  (sum_lock) {
                    // Add the numbers
                   sum = sum + i;
                }
            }
            totalSum = totalSum + sum; /* Update total */
        }

    }

    static public void main(String args[]) {
        int min = 0;
        int max = 0;

        try{
            min = Integer.parseInt(args[0]);
            max = Integer.parseInt(args[1]);

            if( max<min )
            {
                throw new IllegalArgumentException("Maximum value should be greater than or equal minimum value!");
            }
            ExecutorService exec = Executors.newFixedThreadPool(THREADS);
            Number_count c = new Number_count(min, max);

            exec.submit(c);
            exec.shutdown();

            System.out.println("The sum of the numbers between " + min + " to " + max + " is " + totalSum);
            System.out.println("The average value is: " + averageCalculator(totalSum, max - min + 1));
        }
        catch(IllegalArgumentException exception){
            exception.printStackTrace();
        }

    }
}
