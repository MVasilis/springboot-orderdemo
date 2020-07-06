package eu.acme.demo.web.response;

import eu.acme.demo.domain.enums.CustomerGender;

import java.util.UUID;

public class CustomerResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private CustomerGender customerGender;
    private String address;



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
}
