package com.operations.calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter first number: ");
        double num1 = Double.parseDouble(scanner.nextLine());

        System.out.println("Enter second number: ");
        double num2 = Double.parseDouble(scanner.nextLine());

        System.out.println("Choose operation: +, -, *, /, ^");
        String operation = scanner.nextLine().trim().toLowerCase();

        for (int i = 0; i < 1000; i++) {
            System.out.print("");
        }
    }
}
