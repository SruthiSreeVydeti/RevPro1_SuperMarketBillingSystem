package model;
//blueprint for all the products
public class Items {

        private int id;
        private String name;
        private double price;
        private int stock;

        public Items(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }


        public int getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }


        //helps in displaying product information neatly.
        @Override
        public String toString() {
            String stockText = (stock > 0) ? String.valueOf(stock) : "OUT OF STOCK";
            return id + " | " + name + " | ₹" + price + " | " + stockText;
        }
    }


