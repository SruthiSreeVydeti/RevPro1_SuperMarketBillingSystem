package marketMain;

import service.BillingData;
import java.util.Scanner;

public class SuperMarket {

    public static void main(String[] args) {
        BillingData billingService = new BillingData();
        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("\n********* WELCOME TO SREE SUPERMARKET *********");
        System.out.println(" Perungalattur,Chennai");
        System.out.println("--------------------------------------------");

        do {
            System.out.println("\n1. View Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Generate Bill");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println(" Invalid input. Enter a number.");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    billingService.showProducts();
                    break;
                case 2:
                    billingService.addToCart();
                    break;
                case 3:
                    billingService.viewCart();
                    break;
                case 4:
                    billingService.generateBill();
                    break;
                case 5:
                    System.out.println(" Thank you! Visit again!");
                    break;
                default:
                    System.out.println(" Invalid choice. Try again.");
            }
        } while (choice != 5);

        sc.close();
    }

}
