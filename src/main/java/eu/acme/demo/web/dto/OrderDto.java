package eu.acme.demo.web.dto;

import java.util.List;

public class OrderDto extends OrderLiteDto {

    private List<OrderItemDto> orderItems;

    private CustomerDto customerDto;

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }
}
