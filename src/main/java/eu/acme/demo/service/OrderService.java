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

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    public OrderDto saveorUpdate(OrderDto orderDto){
        // TODO Add try catch
        //Order order = new Order();
        //BeanUtils.copyProperties(orderDto, order);

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

    public Optional<Order> findOrderById(UUID orderId){
        return orderRepository.findById(orderId);
    }

    public Iterable<Order> findAllOrders(){
        return orderRepository.findAll();
    };

}