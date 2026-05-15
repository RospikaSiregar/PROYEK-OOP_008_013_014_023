package utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ValidationUtil {

    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // clear invalid input
                System.out.println("Input tidak valid! Masukkan angka yang benar.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // clear invalid input
                System.out.println("Input tidak valid! Masukkan angka yang benar.");
            }
        }
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     SISTEM MANAJEMEN RESTORAN");
        System.out.println("=".repeat(50));
        System.out.println("1. Menu Management");
        System.out.println("2. Customer Management");
        System.out.println("3. Table Management");
        System.out.println("4. Order Management");
        System.out.println("5. Laporan");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

    public static String formatRupiah(double amount) {
        return String.format("Rp. %.2f", amount);
    }

    public static void printSeparator() {
        System.out.println("-".repeat(80));
    }
}
