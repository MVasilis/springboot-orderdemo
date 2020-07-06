package eu.acme.demo.web;

import eu.acme.demo.service.OrderService;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.dto.OrderLiteDto;
import eu.acme.demo.web.request.OrderRequest;
import eu.acme.demo.web.response.ErrorResponse;
import eu.acme.demo.web.response.OrderLiteResponse;
import eu.acme.demo.web.response.OrderResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderAPI {

    private static final String REF_CODE_ALREADY_EXISTS = "reference code already exists";
    private static final String ORDER_DOES_NOT_EXISTS = "Order does not exists, Please chect you order id";

    private OrderService orderService;


    @Autowired
    public OrderAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderLiteResponse> fetchOrders(@RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        List<OrderLiteDto> returnedList = orderService.findAllOrders(pageNo, pageSize);
        return getOrders(returnedList);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> fetchOrder(@PathVariable UUID orderId) {
        OrderDto orderDto = orderService.findOrderById(orderId);
        if(ObjectUtils.isEmpty(orderDto))
            return new ResponseEntity<ErrorResponse>(getErrorResponse(ORDER_DOES_NOT_EXISTS), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<OrderResponse>(getOrderResponse(orderDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> submitOrder(@RequestBody OrderRequest orderRequest) {
        //Deep object mapping
        ModelMapper modelMapper = new ModelMapper();
        OrderDto orderDto = modelMapper.map(orderRequest, OrderDto.class);
        if (clientRefCodeAlreadyExists(orderRequest.getClientReferenceCode())){
            return new ResponseEntity<ErrorResponse>(getErrorResponse(REF_CODE_ALREADY_EXISTS), HttpStatus.BAD_REQUEST);
        }
        orderDto = orderService.saveorUpdate(orderDto);
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
        // Convert OrderDto to OrderResponse object
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

    private List<OrderLiteResponse> getOrders(List<OrderLiteDto> orderLiteDtos){
        // Convert OrderDtos to OrderResponses List Object
        List<OrderLiteResponse> orderLiteResponses = new ArrayList<>();
        Type listType = new TypeToken<List<OrderLiteDto>>() {}.getType();
        orderLiteResponses = new ModelMapper().map(orderLiteDtos, listType);
        return orderLiteResponses;
    }

}
