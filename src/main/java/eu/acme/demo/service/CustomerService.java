package eu.acme.demo.service;


import eu.acme.demo.domain.Customer;
import eu.acme.demo.repository.CustomerRepository;
import eu.acme.demo.web.dto.CustomerDto;
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
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerDto saveorUpdate(CustomerDto customerDto){
        Customer customer = convertDtoToCustomer(customerDto);
        customer = customerRepository.save(customer);
        return convertCustToDto(customer);
    }

    public CustomerDto findCustomerById(UUID customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent())
            return null;
        return convertCustToDto(customer.get());
    }

    public List<CustomerDto> findAllCustomers(Integer pageNo, Integer pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return customerDtos(paging);
    };

    private List<CustomerDto> customerDtos(Pageable paging){
        Page<Customer> customers = customerRepository.findAll(paging);
        return convertListOfCustToDtos(customers.getContent());
    }

    private  List<CustomerDto> convertListOfCustToDtos(List<Customer> customers){
        List<CustomerDto> customerDtos = new ArrayList<>();
        Type listType = new TypeToken<List<CustomerDto>>() {}.getType();
        customerDtos = new ModelMapper().map(customers, listType);
        return customerDtos;
    }

    private CustomerDto convertCustToDto(Customer customer){
        ModelMapper modelMapper = new ModelMapper();
        CustomerDto returenedValeu =  modelMapper.map(customer, CustomerDto.class);
        return returenedValeu;
    }

    private Customer convertDtoToCustomer(CustomerDto customerDto){
        ModelMapper modelMapper = new ModelMapper();
        Customer customer =  modelMapper.map(customerDto, Customer.class);
        return customer;
    }



}
