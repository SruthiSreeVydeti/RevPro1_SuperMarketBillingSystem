package dao;

import dataBase.dbConnection;
import model.Items;
import exception.OutOfStockException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//database interactions

public class ItemsDao {

    //fetches all products and adds to the new array list products.
    public List<Items> getAllProducts() {
        List<Items> products = new ArrayList<>();

        String query = "SELECT * FROM items";
        try (Connection con = dbConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");

                products.add(new Items(id, name, price, stock));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }

        return products;
    }

    //to update the stock in the database when a product is purchased.
    public void updateStock(int productId, int quantityPurchased) throws OutOfStockException {

        Items product = getProductById(productId);
        if (product == null) {
            throw new OutOfStockException("Product ID " + productId + " not found");
        }

        int currentStock = product.getStock();
        if (currentStock < quantityPurchased) {
            throw new OutOfStockException("Not enough stock for product ID: " + productId);
        }

        String query = "UPDATE items SET stock = ? WHERE id = ?";
        try (Connection con = dbConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, currentStock - quantityPurchased);
            stmt.setInt(2, productId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
    }

    //fetches single product by Id
    public Items getProductById(int id) {
        Items product = null;
        String query = "SELECT * FROM items WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                product = new Items(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching product by ID: " + e.getMessage());
        }

        return product;
    }


}

