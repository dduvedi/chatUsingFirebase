import java.util.regex.Pattern;

public class Utility {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

   
    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

  
    public static int add(int a, int b) {
        return a + b;
    }

   
    public static int divide(int a, int b) {
        if (b == 0) {
            System.out.println("Warning: Division by zero. Returning 0 instead.");
            return 0;
        }
        return a / b;
    }

  
    public static double calculateDiscountedPrice(double price, double discount) {
        double discountedPrice = price - (price * discount / 100);
        return discountedPrice;
    }

   public static String reverseString(String input) {
        if (input == null) {
            System.out.println("Warning: Input is null. Returning null.");
            return null; 
        }
        return new StringBuilder(input).reverse().toString();
    }

  public static long factorial(int n) {
        if (n < 0) {
            System.out.println("Warning: Factorial of negative number. Returning -1.");
            return -1;
        }
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

  public static int findMax(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            System.out.println("Warning: Input array is null or empty. Returning Integer.MIN_VALUE.");
            return Integer.MIN_VALUE; 
        }
        
        int max = numbers[0];
        for (int number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }
}
