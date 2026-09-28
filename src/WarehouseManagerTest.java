import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseManagerTest {
    private WarehouseManager manager;
    private Product laptop;
    private Product mouse;
    private Product phone;

    @BeforeEach
    void setUp(){
        manager = new WarehouseManager();
        laptop = new Product("P1", "Ноутбук", "Электроника", 50000.0, 5);
        mouse = new Product("P2", "Мышка", "Электроника", 1500.0, 0);
        phone = new Product("P3", "Телефон", "Электроника", 50000.0, 2);
    }

    @Test
    @DisplayName("Заказ должен быть COMPLETED и списать остаток, если товар есть в наличии")
    void processOrder_Success(){
        //1.Подготовка
        manager.addProduct(laptop);
        Order order = new Order("O-1", List.of(laptop), Order.StatusOrder.NEW);
        //2.Act(действие)
        boolean result = manager.processOrder(order);
        //3.Assert (Проверка)
        assertTrue(result,"Метод должен вернуть True при успешной обработке");
        assertEquals(Order.StatusOrder.COMPLETED,order.getStatus());
        assertEquals(4,laptop.getStockQuantity());
    }

    @Test
    @DisplayName("Заказ должен быть CANCELLED,не уменьшать количество всех товаров")
    void processOrder_Failed(){
        //подготовка
        manager.addProduct(mouse);
        Order order = new Order("O-1",List.of(mouse), Order.StatusOrder.NEW);
        //действие
        boolean result = manager.processOrder(order);
        //проверка
        assertFalse(result,"Метод должен вернуть False при неудачной обработке");
        assertEquals(Order.StatusOrder.CANCELLED,order.getStatus());
        assertEquals(0,mouse.getStockQuantity());
    }

    @Test
    @DisplayName("Метод removeOutOfProducts должен удалять со склада только товары с нулевым остатком")
    void removeOutOfProducts_ShouldRemoveZeroStockProducts(){
        //A1
        manager.addProduct(laptop);
        manager.addProduct(mouse);
        //A2
        manager.removeOutOfProducts();
        //A3
        assertTrue(manager.getProducts().contains(laptop),"Товар в наличии должен остаться");
        assertFalse(manager.getProducts().contains(mouse),"ТОвар должен быть удален которого нет в наличии");
        assertEquals(1,manager.getProducts().size(),"должен остаться только товар который есть в наличии");

    }
    @Test
    @DisplayName("processOrder должен возвращать false при передаче null")
    void processOrder_ShouldReturnFalse_WhenOrderIsNull(){

        boolean result = manager.processOrder(null);

        assertFalse(result,"при передаче null метод должен вернуть false и не падать с NullPointerException");
    }
    @Test
    @DisplayName("Сортировка товаров должна идти на убывание цены , а затем по имени по алфавиту ")
    void getProductsSortedByPrice_ShouldSortCorrectly(){
        //1
        manager.addProduct(laptop);
        manager.addProduct(mouse);
        manager.addProduct(phone);
        //2
        List<Product> sorted = manager.getProductsSortedByPrice();
        //3
        assertEquals(3,sorted.size());
        assertEquals("Ноутбук", sorted.getFirst().getName());
        assertEquals("Телефон",sorted.get(1).getName());
        assertEquals("Мышка",sorted.get(2).getName());
    }
    @Test
    @DisplayName("getRevenueCategory должен считать выручку только по COMPLETED заказам")
    void getRevenueCategory_ShouldCalculateOnlyCompletedOrders(){
        //1
        manager.addProduct(laptop);
        manager.addProduct(mouse);

        Order successOrder = new Order("O-1", List.of(laptop), Order.StatusOrder.NEW);
        manager.processOrder(successOrder); // Перейдет в COMPLETED

        Order failedOrder = new Order("O-2", List.of(mouse), Order.StatusOrder.NEW);
        manager.processOrder(failedOrder); // Перейдет в CANCELLED
        //2
        Map<String,Double> revenue = manager.getRevenueByCategory();
        //3
        assertEquals(50000,revenue.get("Электроника"));
        assertNull(revenue.get("Книги"));
    }

}

