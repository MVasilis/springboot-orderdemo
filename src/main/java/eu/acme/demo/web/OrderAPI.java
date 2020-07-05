package eu.acme.demo.web;

import eu.acme.demo.domain.Order;
import eu.acme.demo.service.OrderService;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.dto.OrderLiteDto;
import eu.acme.demo.web.request.OrderRequest;
import eu.acme.demo.web.response.ErrorResponse;
import eu.acme.demo.web.response.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public Optional<Order> fetchOrder(@PathVariable UUID orderId) {
        System.out.println("FetchOrder : " + orderId);
        //TODO: fetch specific order from DB
        // if order id not exists then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message
        //orderService.findOrderById(orderId);
        return null;
    }

    @PostMapping
    public ResponseEntity<?> submitOrder(@RequestBody OrderRequest orderRequest) {
        // Deep object mapping
        ModelMapper modelMapper = new ModelMapper();
        OrderDto orderDto = modelMapper.map(orderRequest, OrderDto.class);

        if (clientRefCodeAlreadyExists(orderRequest.getClientReferenceCode())){
            return new ResponseEntity<ErrorResponse>(getErrorResponse(), HttpStatus.BAD_REQUEST);
        }

        orderService.saveorUpdate(orderDto);
        OrderResponse orderResponse = modelMapper.map(orderDto, OrderResponse.class);

        //TODO: submit a new order
        // if client reference code already exist then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message
        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
    }

    private ErrorResponse getErrorResponse(){
        return new ErrorResponse("400","reference code already exists");
    }

    private boolean clientRefCodeAlreadyExists(String clientRefCode){
        return orderService.getOrderByClientRefCode(clientRefCode) != null ? true : false;
    }

}
