package rmb;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MainRMB {
    private static final OrderBook orderBook = new OrderBook();
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        goToMainMenu();
    }
    private static void goToMainMenu() {
        String mainMenu =
                """
                          =========================
                             Welcome to RMB
                                 -------------------------
                           1 -> Place Buy Order
                           2 -> Place Sell Order
                           3 -> View All Orders
                           4 -> Exit
                          =========================
                    """;
        String userInput = input(mainMenu);
        if (Objects.equals(userInput, "")) goToMainMenu();
        switch (userInput.charAt(0)) {
            case '1' -> insertBuy();
            case '2' -> insertSell();
            case '3' -> viewAllOrders();
            case '4' -> exitApplication();
            default -> goToMainMenu();
        }
    }
    private static void insertBuy() {
        double price = Double.parseDouble(input("Enter price"));
        int quantity = Integer.parseInt(input("Enter quantity"));
        Order order = new Order(price, quantity, Side.BUY);
        orderBook.insertBuyOrder(order);
        display("Buy order successful");
        goToMainMenu();
    }
    private static void insertSell() {
        double price = Double.parseDouble(input("Enter price"));
        int quantity = Integer.parseInt(input("Enter quantity"));
        Order order = new Order(price, quantity, Side.SELL);
        orderBook.insertSellOrder(order);
        display("Sell order successful");
        goToMainMenu();
    }
    private static void viewAllOrders() {
        List<Order> allOrders = orderBook.viewAllOrders();
        StringBuilder ordersString = new StringBuilder("All Orders:\n");
        if (allOrders.isEmpty()) {
            ordersString.append("No orders available.");
        } else {
            for (Order order : allOrders) {
                ordersString.append(order).append("\n");
            }
        }
        display(ordersString.toString());
        goToMainMenu();
    }

    private static void exitApplication() {
        display("Thank you for using the RMB application");
        System.exit(1);
    }

//    private static String input(String prompt) {
//        display(prompt);
//        return input.nextLine();
//
//    }
//    private static void display(String prompt) {
//        System.out.println(prompt);
//    }
    public static String input(String prompt) {
        return JOptionPane.showInputDialog(prompt);
    }

    public static void display(String prompt) {
        JOptionPane.showMessageDialog(null, prompt);
    }
}
