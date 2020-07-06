package eu.acme.demo.service;

import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.dto.OrderItemDto;
import eu.acme.demo.web.dto.OrderLiteDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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

    public List<OrderLiteDto> findAllOrders(Integer pageNo, Integer pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return orderLiteDtos(paging);

    };

    public Order getOrderByClientRefCode(String clientReferenceCode){
        return orderRepository.findByClientReferenceCode(clientReferenceCode);
    }

    private OrderDto getOrderDto(Optional<Order> order){
        ModelMapper modelMapper = new ModelMapper();
        OrderDto returnValue = modelMapper.map(order.get(), OrderDto.class);
        return returnValue;
    }

    private List<OrderLiteDto> orderLiteDtos(Pageable paging){
        Page<Order> orders = orderRepository.findAll(paging);
        return convertOrdersToOrdersDtos(orders);
    }

    private List<OrderLiteDto> convertOrdersToOrdersDtos(Page<Order> orders){
        List<OrderLiteDto> orderLiteDtos = new ArrayList<>();
        Type listType = new TypeToken<List<OrderLiteDto>>() {}.getType();
        orderLiteDtos = new ModelMapper().map(orders.getContent(), listType);
        return orderLiteDtos;
    }

}
