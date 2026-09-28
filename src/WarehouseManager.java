import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseManager {
    private final  Set<Product> products = new HashSet<>();
    private final  List<Order> orders = new ArrayList<>();


    public Set<Product> getProducts(){
        return products;
    }
    public void addProduct(Product product){
        if (product != null) {
            products.add(product);
        }
    }
    public void removeOutOfProducts(){
        Iterator<Product> iterator = products.iterator();
        while ( iterator.hasNext()){
            Product product = iterator.next();

            if (product.getStockQuantity() == 0){
                iterator.remove();
            }

        }
    }
    boolean processOrder(Order order){
        if (order == null || order.getItems() == null || order.getItems().isEmpty()){
            return false;
        }
        for (Product product : order.getItems()){
            if (!products.contains(product) || product.getStockQuantity() < 1){
                order.setStatus(Order.StatusOrder.CANCELLED);
                return false;
            }
        }
        for (Product product : order.getItems()){
            product.setStockQuantity(product.getStockQuantity()-1);

        }
        order.setStatus(Order.StatusOrder.COMPLETED);
        orders.add(order);
        return true;
    }
    List<Product> getProductsSortedByPrice(){
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice)
                        .reversed().thenComparing(Product::getName))
                .toList();
    }
    List<String> getAllOrderedProductNames(){
        return orders.stream()
                .filter(order -> order.getStatus() == Order.StatusOrder.COMPLETED)
                .flatMap(order -> order.getItems().stream())
                .map(Product::getName).distinct().toList();
    }
    Map<String, Double> getRevenueByCategory(){
        return orders.stream()
                .filter(order -> order.getStatus() == Order.StatusOrder.COMPLETED)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(Product::getCategory,
                        Collectors.summingDouble(Product::getPrice)));
    }
}
