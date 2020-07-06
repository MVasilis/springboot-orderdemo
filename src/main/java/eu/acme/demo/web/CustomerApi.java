package eu.acme.demo.web;


import eu.acme.demo.service.CustomerService;
import eu.acme.demo.web.dto.CustomerDto;
import eu.acme.demo.web.request.CustomerRequest;
import eu.acme.demo.web.response.CustomerResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerApi {

    private CustomerService customerService;

    @Autowired
    public CustomerApi(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping
    public ResponseEntity<?> submitCustomer(@RequestBody CustomerRequest customerRequest) {
        //Deep object mapping
        ModelMapper modelMapper = new ModelMapper();
        CustomerDto customerDto = modelMapper.map(customerRequest, CustomerDto.class);
        customerDto = customerService.saveorUpdate(customerDto);
        CustomerResponse customerResponse = modelMapper.map(customerDto, CustomerResponse.class);
        return new ResponseEntity<CustomerResponse>(customerResponse, HttpStatus.OK);
    }

    @GetMapping
    public List<CustomerResponse> fetchCustomers(@RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        List<CustomerDto> returnedList = customerService.findAllCustomers(pageNo, pageSize);
        return convertDtosToResponseObject(returnedList);
    }


    private List<CustomerResponse> convertDtosToResponseObject(List<CustomerDto> customerDtos){
        List<CustomerResponse> customerResponses = new ArrayList<>();
        Type listType = new TypeToken<List<CustomerDto>>() {}.getType();
        customerResponses = new ModelMapper().map(customerDtos, listType);
        return customerResponses;
    }
}
