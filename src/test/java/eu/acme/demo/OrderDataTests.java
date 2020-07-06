package eu.acme.demo;


import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.OrderItem;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class OrderDataTests {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void testCreateOrder() {
        Order o = new Order();
        o.setStatus(OrderStatus.SUBMITTED);
        o.setClientReferenceCode("ORDER-1");
        o.setDescription("first order");
        o.setItemCount(10);
        o.setItemTotalAmount(BigDecimal.valueOf(100.23));
        orderRepository.save(o);

        Assert.isTrue(orderRepository.findById(o.getId()).isPresent(), "order not found");
        Assert.isTrue(!orderRepository.findById(UUID.randomUUID()).isPresent(), "non existing order found");
    }

    @Test
    public void testCreateOrderWithOrderItems(){
        // Create test Data Order with OrderItems
        Order order = new Order();
        order.setStatus(OrderStatus.SUBMITTED);
        order.setClientReferenceCode("ORDER-2");
        order.setDescription("secondirst order");
        order.setItemCount(2);
        order.setItemTotalAmount(BigDecimal.valueOf(85.03));
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem firstOrderItem = newOrderItem(order);
        orderItems.add(firstOrderItem);
        order.setOrderItems(orderItems);
        // Save Objects in DB
        orderRepository.save(order);
        // Retreive OrderItem via Order ID
        Assert.isTrue(!orderItemRepository.findByOrderId(order.getId()).isEmpty(), "orderItem not found");
        Assert.isTrue(orderItemRepository.findByOrderId(order.getId()).size() == 1, "Different size of OrderItems List");
    }

    @Test
    public void testCreateOrderWithMultipleOrderItems(){
        // Create test Data Order with OrderItems
        Order order = new Order();
        order.setStatus(OrderStatus.SUBMITTED);
        order.setClientReferenceCode("ORDER-2");
        order.setDescription("secondirst order");
        order.setItemCount(2);
        order.setItemTotalAmount(BigDecimal.valueOf(85.03));
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem firstOrderItem = newOrderItem(order);
        orderItems.add(firstOrderItem);
        OrderItem secondOrderItem = newOrderItem(order);
        orderItems.add(secondOrderItem);
        OrderItem thirdOrderItem = newOrderItem(order);
        orderItems.add(thirdOrderItem);
        order.setOrderItems(orderItems);
        // Save Objects in DB
        orderRepository.save(order);
        // Retreive OrderItem via Order ID
        Assert.isTrue(!orderItemRepository.findByOrderId(order.getId()).isEmpty(), "orderItem not found");
        Assert.isTrue(orderItemRepository.findByOrderId(order.getId()).size() == 3, "Different size of OrderItems List");
    }

    private OrderItem newOrderItem(Order order){
        OrderItem orderItem = new OrderItem();
        orderItem.setTotalPrice(BigDecimal.valueOf(11.12));
        orderItem.setUnitPrice(BigDecimal.valueOf(11.12));
        orderItem.setUnits(1);
        orderItem.setOrder(order);
        return orderItem;
    }

}
