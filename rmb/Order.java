package rmb;

public class Order {
    private int id;
    private double price;
    private int quantity;
    private Side side;

    public Order(int id, double price, int quantity, Side side) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }
    public Order(double price, int quantity, Side side) {
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return String.format("""
             ======================
             id: %d
             price: %.2f
             quantity: %d
             side: %s
             ======================
             """, id, price, quantity, side);
    }
}
