package eu.acme.demo.web.dto;

import eu.acme.demo.domain.enums.CustomerGender;

import java.util.List;
import java.util.UUID;

public class CustomerDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private CustomerGender customerGender;
    private String address;
    private List<OrderDto> orders;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public CustomerGender getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(CustomerGender customerGender) {
        this.customerGender = customerGender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }
}
