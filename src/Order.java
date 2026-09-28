import java.util.List;

public class Order {
    public  enum StatusOrder  {NEW,COMPLETED,CANCELLED}

    private String orderId;
    private List<Product> items;
    private StatusOrder status;

    public Order(String orderId, List<Product> items, StatusOrder status) {
        this.orderId = orderId;
        this.items = items;
        this.status = StatusOrder.NEW;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<Product> getItems() {
        return items;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder status) {
        this.status = status;
    }
}
