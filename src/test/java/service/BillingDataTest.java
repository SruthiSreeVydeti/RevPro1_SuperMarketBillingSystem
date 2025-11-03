package service;

import dao.ItemsDao;
import exception.OutOfStockException;
import exception.ProductNotFoundException;
import model.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BillingDataTest {

    private BillingData billingData;

    @BeforeEach
    void setUp() {
        billingData = new BillingData();
    }

    @Test
    void testOutOfStockThrowsException() throws OutOfStockException,ProductNotFoundException{
        assertThrows(OutOfStockException.class, () -> {
            billingData.addToCartForTest(11, 40); // Maida only 2 in stock
        });
    }


    @Test
    void testGSTCalculation() throws OutOfStockException,ProductNotFoundException {
        billingData.addToCartForTest(1, 2); // Milk ₹50 * 2 = 100
        billingData.addToCartForTest(2, 1); // Bread ₹30 * 1 = 30
        double gst = billingData.getGST();
        assertEquals(6.5, gst, 0.001); // 5% of 130
    }

    @Test
    void testTotalAmountCalculation() throws OutOfStockException,ProductNotFoundException {
        billingData.addToCartForTest(1, 2); // Milk ₹50 * 2 = 100
        billingData.addToCartForTest(2, 1); // Bread ₹30 * 1 = 30
        double total = billingData.getTotalAmount();
        assertEquals(136.5, total, 0.001); // subtotal + GST
    }

}
