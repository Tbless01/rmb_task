package rmb;

import java.util.List;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;

public class OrderBook {
    private TreeMap<Double, List<Order>> sellOrdersByPrice = new TreeMap<>();
    private TreeMap<Double, List<Order>> buyOrdersByPrice = new TreeMap<>();
    private int nextOrderId;

    public OrderBook() {
        this.sellOrdersByPrice = new TreeMap<>();
        this.buyOrdersByPrice = new TreeMap<>();
        this.nextOrderId = 1;
    }

    public void insertSellOrder(Order sellOrder) {
        sellOrder.setId(nextOrderId++);
        List<Order> matchingBids = getOrdersAtPriceLevel(sellOrder.getPrice(), Side.BUY);
        int sellAmountRemaining =
                sellOrder.getQuantity();
        Iterator<Order> bidIterator = matchingBids.iterator();
        while (sellAmountRemaining > 0 && bidIterator.hasNext()) {
            Order buy = bidIterator.next();
            int fillAmount = Math.min(sellAmountRemaining, buy.getQuantity());
            buy.setQuantity(buy.getQuantity() - fillAmount);
            sellAmountRemaining -= fillAmount;
            if (buy.getQuantity() == 0) {
                bidIterator.remove();
            }
        }
        if (sellAmountRemaining > 0) {
            sellOrder.setQuantity(sellAmountRemaining);
            addOrder(sellOrder);
        }
    }

    public void insertBuyOrder(Order buyOrder) {
        buyOrder.setId(nextOrderId++);
        List<Order> matchingSells = getOrdersAtPriceLevel(buyOrder.getPrice(), Side.SELL);
        int buyAmountRemaining
                = buyOrder.getQuantity();
        Iterator<Order> sellIterator = matchingSells.iterator();
        while (buyAmountRemaining > 0 && sellIterator.hasNext()) {
            Order sell = sellIterator.next();
            int fillAmount = Math.min(buyAmountRemaining, sell.getQuantity());
            sell.setQuantity(sell.getQuantity() - fillAmount);
            buyAmountRemaining -= fillAmount;
            if (sell.getQuantity() == 0) {
                sellIterator.remove();
            }
        }
        if (buyAmountRemaining > 0) {
            buyOrder.setQuantity(buyAmountRemaining);
            addOrder(buyOrder);
        }
    }

    public List<Order> viewAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        if (buyOrdersByPrice != null) {
            for (Map.Entry<Double, List<Order>> entry : buyOrdersByPrice.entrySet()) {
                allOrders.addAll(entry.getValue());
            }
        }
        if (sellOrdersByPrice != null) {
            for (Map.Entry<Double, List<Order>> entry : sellOrdersByPrice.entrySet()) {
                allOrders.addAll(entry.getValue());
            }
        }
        return allOrders;
    }

    public List<Order> getOrdersAtPriceLevel(double price, Side side) {
        TreeMap<Double, List<Order>> ordersByPrice = (side == Side.BUY) ? buyOrdersByPrice : sellOrdersByPrice;
        return ordersByPrice.getOrDefault(price, Collections.emptyList());
    }

    public int getBuyCount() {
        return buyOrdersByPrice.values().stream().mapToInt(List::size).sum();
    }

    public int getSellCount() {
        return sellOrdersByPrice.values().stream().mapToInt(List::size).sum();
    }

    public int getTotalCount() {
        return getBuyCount() + getSellCount();
    }

    private void addOrder(Order order) {
        TreeMap<Double, List<Order>> ordersByPrice = (order.getSide() == Side.BUY) ? buyOrdersByPrice : sellOrdersByPrice;
        ordersByPrice.computeIfAbsent(order.getPrice(), k -> new ArrayList<>()).add(order);
    }

    private void deleteOrder(int orderId, Side side) {
        TreeMap<Double, List<Order>> ordersByPrice = (side == Side.BUY) ? buyOrdersByPrice : sellOrdersByPrice;
        for (Map.Entry<Double, List<Order>> entry : ordersByPrice.entrySet()) {
            List<Order> orders = entry.getValue();
            orders.removeIf(order -> order.getId() == orderId);
            if (orders.isEmpty()) {
                ordersByPrice.remove(entry.getKey());
            } else {
                ordersByPrice.put(entry.getKey(), orders);
            }
            break;
        }
    }

    public void updateOrder(Order order) {
        TreeMap<Double, List<Order>> ordersByPrice = order.getSide() == Side.BUY ? buyOrdersByPrice : sellOrdersByPrice;
        for (Map.Entry<Double, List<Order>> entry : ordersByPrice.entrySet()) {
            List<Order> orders = entry.getValue();
            Iterator<Order> iterator = orders.iterator();
            while (iterator.hasNext()) {
                Order orderFound = iterator.next();
                if (orderFound.getId() == order.getId()) {
                    iterator.remove();
                    if (orders.isEmpty()) {
                        ordersByPrice.remove(entry.getKey());
                    }
                    orderFound.setPrice(order.getPrice());
                    orderFound.setQuantity(order.getQuantity());
                    addOrder(orderFound);
                    return;
                }
            }
        }
    }
}
