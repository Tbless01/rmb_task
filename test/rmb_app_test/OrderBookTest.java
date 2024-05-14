package test.rmb_app_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rmb.Order;
import rmb.OrderBook;
import rmb.Side;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderBookTest {

    private OrderBook orderBook;

    @BeforeEach
    public void alwaysTestThisFirst() {
        orderBook = new OrderBook();
        Order order = new Order(9, 40, Side.BUY);
        orderBook.insertBuyOrder(order);
    }

    @Test
    public void testThatBuyOrderCanBeInserted() {
        assertEquals(1, orderBook.getBuyCount());
    }

    @Test
    public void testThatMoreThanOneBuyOrdersCanBeInserted() {
        Order secondOrder = new Order(9, 20, Side.BUY);
        orderBook.insertBuyOrder(secondOrder);
        assertEquals(2, orderBook.getBuyCount());
    }

    @Test
    public void testThatSellOrderCanBeInserted() {
        Order sellOrder = new Order(9, 55, Side.SELL);
        orderBook.insertSellOrder(sellOrder);
        assertEquals(1, orderBook.getSellCount());
    }

    @Test
    public void testThatMoreThanOneSellOrdersCanBeInserted() {
        Order order = new Order(8, 20, Side.SELL);
        Order secondOrder = new Order(8, 20, Side.SELL);
        orderBook.insertSellOrder(order);
        orderBook.insertSellOrder(secondOrder);
        assertEquals(2, orderBook.getSellCount());
        assertEquals(3, orderBook.getTotalCount());
    }

    @Test
    public void testForFullAndPartialFill() {
        Order buyOrder = new Order(9, 20, Side.BUY);
        Order sellOrder = new Order(9, 55, Side.SELL);
        orderBook.insertBuyOrder(buyOrder);
        orderBook.insertSellOrder(sellOrder);

        List<Order> buyOrdersAtPrice = orderBook.getOrdersAtPriceLevel(9, Side.BUY);

        assertEquals(1, orderBook.getBuyCount());
        Order remainingBuyOrder = buyOrdersAtPrice.get(0);
        assertEquals(5, remainingBuyOrder.getQuantity());

    }
}
