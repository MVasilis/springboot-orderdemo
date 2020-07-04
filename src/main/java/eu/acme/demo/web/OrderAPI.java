package eu.acme.demo.web;

import eu.acme.demo.service.OrderService;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.dto.OrderLiteDto;
import eu.acme.demo.web.request.OrderRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderAPI {

/*    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;*/

    private OrderService orderService;

/*    public OrderAPI(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }*/

    @Autowired
    public OrderAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderLiteDto> fetchOrders() {
        //TODO: fetch all orders in DB
        return null;
    }

    @GetMapping("/{orderId}")
    public OrderDto fetchOrder(@PathVariable UUID orderId) {
        //TODO: fetch specific order from DB
        // if order id not exists then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message
        return null;
    }

    @PostMapping
    public OrderDto submitOrder(@RequestBody OrderRequest orderRequest) {

        //OrderDto orderDto = new OrderDto();
        //BeanUtils.copyProperties(orderRequest,orderDto);
        // Deep object mapping
        ModelMapper modelMapper = new ModelMapper();
        OrderDto orderDto = modelMapper.map(orderRequest, OrderDto.class);




        //TODO: submit a new order
        // if client reference code already exist then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message
        return orderService.saveorUpdate(orderDto);
    }

}
