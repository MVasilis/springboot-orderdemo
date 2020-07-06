package eu.acme.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.acme.demo.web.dto.CustomerDto;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.response.ErrorResponse;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderAPITests {

    private final static String SERVER_URL = "http://localhost:";
    private final static String ORDER_METHOD = "/orders";
    private final static String CUSTOMER_METHOD = "/customers";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${server.port}")
    private String port;


    @Test
    @Transactional
    void testOrderAPI() throws Exception {
        // Create Customer
        CustomerDto customerDto = createCustomer();
        String testReferenceCode = "code12432145423";
        // Create Order
        OrderDto orderDto = createOrder(testReferenceCode, customerDto.getId());
        // Verify Data
        Assert.isTrue(orderDto.getOrderItems().size() == 1, "Wrong OrderItems size");
        Assert.isTrue(orderDto.getClientReferenceCode().equals(testReferenceCode), "Wrong Client reference code");
        Assert.isTrue(orderDto.getItemTotalAmount().compareTo(new BigDecimal(200))== 0 , "Wrong Item Total Amount");
        Assert.isTrue(orderDto.getDescription().equals("Test Description"), "Wrong Description");
    }

    @Test
    @Transactional
    void testOrderDoubleSubmission() throws Exception {
        CustomerDto customerDto = createCustomer();
        String testReferenceCode = "code1234567";
        // Submit API request
        postResquestResult(orderRequestAsString(testReferenceCode), status().isOk(), customerDto.getId());
        MvcResult seconsSameOrderResult = postResquestResult(orderRequestAsString(testReferenceCode), status().is4xxClientError(), customerDto.getId());
        // Retreive response
        String response = seconsSameOrderResult.getResponse().getContentAsString();
        // Verify Data
        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
        Assert.isTrue(errorResponse.getErrorCode().equals("400"), "Error Code 400 was expected but got wronge response");
    }

    @Test
    @Transactional
    void testFetchAllOrders() throws Exception{
        // Create Customer
        CustomerDto customerDto = createCustomer();
        String firstTestReferenceCode = "code123";
        String secondTestReferenceCode = "code12345";
        // submit objects to db
        createOrder(firstTestReferenceCode, customerDto.getId());
        createOrder(secondTestReferenceCode, customerDto.getId());
        // get object
        MvcResult orderResult = getAllOrders();
        String response = orderResult.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONArray jsnobject = (JSONArray) parser. parse(response);
        // Verify list size
        Assert.isTrue(jsnobject.size() == 2, "Wrong size of saved Objects");

    }

    @Test
    @Transactional
    void testFetchCertainOrder() throws Exception{
        // Create Customer
        CustomerDto customerDto = createCustomer();
        String testReferenceCode = "code123456";
        // submit requets
        OrderDto savedOrderDto = createOrder(testReferenceCode, customerDto.getId());
        MvcResult getResult = getOrder(savedOrderDto.getId(), status().isOk());
        String getResponse = getResult.getResponse().getContentAsString();
        OrderDto getOrder = objectMapper.readValue(getResponse, OrderDto.class);
        // Verify saved order
        Assert.isTrue(savedOrderDto.getId().equals(getOrder.getId()), "Saved order does not match with retreived one");
        // Verify that response is  http 400 if order does not exists.
        MvcResult getErrorResult = getOrder(UUID.randomUUID(), status().is4xxClientError());
        String getErrorResponse = getErrorResult.getResponse().getContentAsString();
        ErrorResponse errorResponse = new ObjectMapper().readValue(getErrorResponse, ErrorResponse.class);
        Assert.isTrue(errorResponse.getErrorCode().equals("400"), "Wrong error response");
    }

    private MvcResult postResquestResult(String request, ResultMatcher statusResult, UUID customerId) throws Exception{
        MvcResult secondOrderResult = this.mockMvc.perform(post(SERVER_URL + port + ORDER_METHOD + "/" + customerId).
                content(request)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(statusResult)
                .andReturn();
        return secondOrderResult;
    }

    private MvcResult getOrder(UUID orderId, ResultMatcher statusResult) throws Exception{
        MvcResult getResult = this.mockMvc.perform(MockMvcRequestBuilders.get(SERVER_URL + port + ORDER_METHOD + "/" + orderId)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(statusResult)
                .andReturn();
        return getResult;
    }

    private MvcResult getAllOrders() throws Exception{
        MvcResult OrderResult = this.mockMvc.perform(MockMvcRequestBuilders.get(SERVER_URL + port + ORDER_METHOD)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        return OrderResult;
    }

    private MvcResult postCustomerResult(String request, ResultMatcher statusResult) throws Exception{
        MvcResult secondOrderResult = this.mockMvc.perform(post(SERVER_URL + port + CUSTOMER_METHOD).
                content(request)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(statusResult)
                .andReturn();
        return secondOrderResult;
    }

    private CustomerDto createCustomer() throws Exception{
        // Create Customer
        MvcResult customerResult = postCustomerResult(customerRequestAsString(), status().isOk());
        String customerResponse = customerResult.getResponse().getContentAsString();
        return objectMapper.readValue(customerResponse, CustomerDto.class);
    }

    private OrderDto createOrder(String referenceCode, UUID customerId) throws Exception{
        MvcResult orderResult = postResquestResult(orderRequestAsString(referenceCode), status().isOk(), customerId);
        // Retreive response
        String response = orderResult.getResponse().getContentAsString();
        OrderDto orderDto = objectMapper.readValue(response, OrderDto.class);
        Assert.notNull(orderResult.getResponse().getContentAsString());
        return orderDto;
    }

    private String orderRequestAsString(String referenceCode){
        JSONObject orderObject = new JSONObject();
        orderObject.put("clientReferenceCode", referenceCode);
        orderObject.put("description", "Test Description");
        orderObject.put("itemCount", 1);
        orderObject.put("itemTotalAmount",200);
        List<JSONObject> orderItemsObjects = new ArrayList<>();
        JSONObject orderItemsObject = new JSONObject();
        orderItemsObject.put("units", 1);
        orderItemsObject.put("unitPrice",15);
        orderItemsObject.put("totalPrice",15);
        orderItemsObjects.add(orderItemsObject);
        orderObject.put("orderItems", orderItemsObjects);
        return orderObject.toString();
    }


    private String customerRequestAsString(){
        JSONObject customerObject = new JSONObject();
        customerObject.put("firstName", "Nick");
        customerObject.put("lastName", "Papadopoulos");
        customerObject.put("customerGender", "MALE");
        customerObject.put("address", "Tertipi 24");
        return customerObject.toString();
    }
}

