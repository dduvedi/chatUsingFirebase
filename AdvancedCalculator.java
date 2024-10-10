package com.operations.calculator;

public class AdvancedCalculator {
    private CalculationStrategy strategy;

    public void setStrategy(CalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(double a, double b) {
        return strategy.calculate(a, b);
    }

    public static void main(String[] args) {
        AdvancedCalculator calculator = new AdvancedCalculator();

        calculator.setStrategy(new Addition());
        System.out.println("Addition: " + calculator.calculate(5, 3));

        calculator.setStrategy(new Subtraction());
        System.out.println("Subtraction: " + calculator.calculate(5, 3));

    }
}
