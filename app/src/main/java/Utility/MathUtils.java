package com.example.myapp.utils;

import java.util.List;

public class MathUtils {

    public static int add(int a, int b) {
        return a + b;
    }

    public static int subtract(int a, int b) {
        return a - b;
    }

    public static int multiply(int a, int b) {
        return a * b;
    }

    public static double divide(int a, int b) {
        return (double) a / b;
    }

    public static double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    public static double squareRoot(double number) {
        return Math.sqrt(number);
    }

    public static int sumArray(Integer[] numbers) {
        int sum = 0;
        for (Integer num : numbers) {
            sum += num;
        }
        return sum;
    }

    public static double averageOfList(List<Object> numbers) {
        double sum = 0.0;
        for (Object num : numbers) {
            sum += ((Number) num).doubleValue();
        }
        return sum / numbers.size();
    }

    public static double productOfArray(Object[] numbers) {
        double product = 1.0;
        for (Object num : numbers) {
            // This can cause NullPointerException if any element is null
            // and ClassCastException if any non-null element is not a Number
            product *= ((Number) num).doubleValue();
        }
        return product;
    }

    public static long factorial(int n) {
        return (n <= 1) ? 1 : n * factorial(n - 1);
    }

    public static int fibonacci(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static int gcd(int a, int b) {
        return (b == 0) ? a : gcd(b, a % b);
    }

    public static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    public static String concatenateStrings(String str1, String str2) {
        return str1.concat(str2);
    }

    public static double convertToDouble(Object obj) {
        return ((Number) obj).doubleValue();
    }
}
