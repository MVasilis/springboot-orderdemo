package eu.acme.demo.web;

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
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderAPI {

/*    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;*/

    private static final String REF_CODE_ALREADY_EXISTS = "reference code already exists";
    private static final String ORDER_DOES_NOT_EXISTS = "Order does not exists, Please chect you order id";

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
    public ResponseEntity<?> fetchOrder(@PathVariable UUID orderId) {
        //TODO: fetch specific order from DB
        // if order id not exists then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message
        System.out.println("FetchOrder : " + orderId);
        OrderDto orderDto = orderService.findOrderById(orderId);
        if(ObjectUtils.isEmpty(orderDto))
            return new ResponseEntity<ErrorResponse>(getErrorResponse(ORDER_DOES_NOT_EXISTS), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<OrderResponse>(getOrderResponse(orderDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> submitOrder(@RequestBody OrderRequest orderRequest) {
        //TODO: submit a new order
        // if client reference code already exist then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message
        // Deep object mapping
        ModelMapper modelMapper = new ModelMapper();
        OrderDto orderDto = modelMapper.map(orderRequest, OrderDto.class);
        if (clientRefCodeAlreadyExists(orderRequest.getClientReferenceCode())){
            return new ResponseEntity<ErrorResponse>(getErrorResponse(REF_CODE_ALREADY_EXISTS), HttpStatus.BAD_REQUEST);
        }
        orderService.saveorUpdate(orderDto);
        OrderResponse orderResponse = modelMapper.map(orderDto, OrderResponse.class);
        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
    }

    private ErrorResponse getErrorResponse(String errorMessage){
        return new ErrorResponse("400",errorMessage);
    }

    private boolean clientRefCodeAlreadyExists(String clientRefCode){
        return orderService.getOrderByClientRefCode(clientRefCode) != null ? true : false;
    }

    private OrderResponse getOrderResponse(OrderDto orderDto){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

}
