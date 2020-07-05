package eu.acme.demo.service;

import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.dto.OrderItemDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    public OrderDto saveorUpdate(OrderDto orderDto){
        for(int i = 0; i < orderDto.getOrderItems().size(); i++){
            OrderItemDto orderItemDto = orderDto.getOrderItems().get(i);
            orderItemDto.setOrder(orderDto);
            orderDto.getOrderItems().set(i, orderItemDto);
        }
        ModelMapper modelMapper = new ModelMapper();
        Order order = modelMapper.map(orderDto, Order.class);
        order.setStatus(OrderStatus.SUBMITTED);

        orderRepository.save(order);
        OrderDto returnValue = modelMapper.map(order, OrderDto.class);
        return returnValue;
    }

    @Transactional
    public OrderDto findOrderById(UUID orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent())
            return getOrderDto(order);
        return null;
    }

    public Iterable<Order> findAllOrders(){
        return orderRepository.findAll();
    };

    public Order getOrderByClientRefCode(String clientReferenceCode){
        return orderRepository.findByClientReferenceCode(clientReferenceCode);
    }

    private OrderDto getOrderDto(Optional<Order> order){
        ModelMapper modelMapper = new ModelMapper();
        OrderDto returnValue = modelMapper.map(order.get(), OrderDto.class);
        return returnValue;
    }

}
