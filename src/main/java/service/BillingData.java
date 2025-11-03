package service;

import dao.ItemsDao;
import model.Items;
import exception.OutOfStockException;
import exception.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BillingData {

    private final ItemsDao productDAO = new ItemsDao();
    private final List<Items> cart = new ArrayList<>();
    private final Scanner sc = new Scanner(System.in);

    //to print all products
    public void showProducts() {
        List<Items> products = productDAO.getAllProducts();
        System.out.println("\n-------Available Products ------");
        System.out.printf("%-5s %-25s %-10s %-10s%n", "ID", "Product", "Price (₹)", "Stock");
        System.out.println("--------------------------------------------------------");

        for (Items p : products) {
            System.out.printf("%-5d %-25s %-10.2f %-10d%n", p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
        System.out.println("---------------------------------------------------------");
    }


    public void addToCart() {
        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();

        Items product = productDAO.getProductById(id);
        try {
            if (product == null) {
                throw new ProductNotFoundException("Product with ID " + id + " not found");
            }

            if (product.getStock() <= 0) {
                System.out.println(" Product is OUT OF STOCK!");
                return;
            }

            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt();

            if (quantity > product.getStock()) {
                System.out.println(" Only " + product.getStock() + " units available.");
                return;
            }

            productDAO.updateStock(id, product.getStock() - quantity);


            Items selected = new Items(product.getId(), product.getName(), product.getPrice(), quantity);
            cart.add(selected);

            System.out.println(" " + quantity + " " + product.getName() + " added to cart!");
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (OutOfStockException e) {
            System.out.println(e.getMessage());
        }
    }

    //for test case
    public boolean addToCartForTest(int id, int quantity) throws OutOfStockException, ProductNotFoundException {
        Items product = productDAO.getProductById(id);
        if (product == null) throw new ProductNotFoundException("Product with ID " + id + " not found");
        if (quantity > product.getStock()) throw new OutOfStockException("Not enough stock");
        productDAO.updateStock(id, product.getStock() - quantity);
        cart.add(new Items(product.getId(), product.getName(), product.getPrice(), quantity));
        return true;
    }


    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println(" Your cart is empty!");
            return;
        }

        System.out.println("\n------Your Cart ------");
        for (Items p : cart) {
            System.out.println(p.getName() + " | Quantity: " + p.getStock() + " | Price: " + p.getPrice());
        }
    }

    public void generateBill() {
        if (cart.isEmpty()) {
            System.out.println(" Your cart is empty. Add items first!");
            return;
        }

        double total = getSubtotal();
        double gst = getGST();
        double finalAmount = getTotalAmount();

        System.out.println("\n************ SREE SUPERMARKET BILL ************");
        System.out.printf("%-25s %-10s %-15s%n", "Product", "Quantity", "Price (₹)");
        System.out.println("--------------------------------------------------------");

        for (Items p : cart) {
            double price = p.getPrice() * p.getStock();
            System.out.printf("%-25s %-10d %-15.2f%n", p.getName(), p.getStock(), price);
        }

        System.out.println("------------------------------------------");
        System.out.printf("%-25s %-10s %-15.2f%n", "Subtotal", "", total);
        System.out.printf("%-25s %-10s %-15.2f%n", "GST (5%)", "", gst);
        System.out.printf("%-25s %-10s %-15.2f%n", "Total Amount", "", finalAmount);
        System.out.println("******************************************");

        cart.clear();
    }

    public double getSubtotal() {
        double total = 0;
        for (Items p : cart) {
            total += p.getPrice() * p.getStock();
        }
        return total;
    }
//for test cases
    public double getGST() {
        return getSubtotal() * 0.05; // 5% GST
    }

    public double getTotalAmount() {
        return getSubtotal() + getGST();
    }

    public List<Items> getCart() {
        return cart;
    }

    public ItemsDao getProductDAO() {
        return productDAO;
    }
}