package eu.acme.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.response.ErrorResponse;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final static String SUBMIT_ORDER_URL = "http://localhost:9090/orders";
    private final static String GET_ORDER_BY_ID_URL = "http://localhost:9090/orders/";
    private final static String GET_ALL_ORDERS_URL = "http://localhost:9090/orders";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void testOrderAPI() throws Exception {
        String testReferenceCode = "code12432145423";
        // Submit API request
        MvcResult orderResult = postResquestResult(orderRequestAsString(testReferenceCode), status().isOk());
        // Retreive response
        String response = orderResult.getResponse().getContentAsString();
        OrderDto orderDto = objectMapper.readValue(response, OrderDto.class);
        // Verify Data
        Assert.notNull(orderResult.getResponse().getContentAsString());
        Assert.isTrue(orderDto.getOrderItems().size() == 1, "Wrong OrderItems size");
        Assert.isTrue(orderDto.getClientReferenceCode().equals(testReferenceCode), "Wrong Client reference code");
        Assert.isTrue(orderDto.getItemTotalAmount().compareTo(new BigDecimal(200))== 0 , "Wrong Item Total Amount");
        Assert.isTrue(orderDto.getDescription().equals("Test Description"), "Wrong Description");
    }

    @Test
    @Transactional
    void testOrderDoubleSubmission() throws Exception {
        String testReferenceCode = "code1234567";
        // Submit API request
        postResquestResult(orderRequestAsString(testReferenceCode), status().isOk());
        MvcResult seconsSameOrderResult = postResquestResult(orderRequestAsString(testReferenceCode), status().is4xxClientError());
        // Retreive response
        String response = seconsSameOrderResult.getResponse().getContentAsString();
        // Verify Data
        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
        Assert.isTrue(errorResponse.getErrorCode().equals("400"), "Error Code 400 was expected but got wronge response");
    }

    @Test
    @Transactional
    void testFetchAllOrders() throws Exception{
        String firstTestReferenceCode = "code123";
        String secondTestReferenceCode = "code12345";
        // submit objects to db
        postResquestResult(orderRequestAsString(firstTestReferenceCode), status().isOk());
        postResquestResult(orderRequestAsString(secondTestReferenceCode), status().isOk());
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
        String testReferenceCode = "code123456";
        // submit requets
        MvcResult firstOrderResult = postResquestResult(orderRequestAsString(testReferenceCode), status().isOk());
        String response = firstOrderResult.getResponse().getContentAsString();
        OrderDto savedOrderDto = objectMapper.readValue(response, OrderDto.class);
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

    private MvcResult postResquestResult(String request, ResultMatcher statusResult) throws Exception{
        MvcResult secondOrderResult = this.mockMvc.perform(post(SUBMIT_ORDER_URL).
                content(request)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(statusResult)
                .andReturn();
        return secondOrderResult;
    }

    private MvcResult getOrder(UUID orderId, ResultMatcher statusResult) throws Exception{
        MvcResult getResult = this.mockMvc.perform(MockMvcRequestBuilders.get(GET_ORDER_BY_ID_URL + orderId)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(statusResult)
                .andReturn();
        return getResult;
    }

    private MvcResult getAllOrders() throws Exception{
        MvcResult OrderResult = this.mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_ORDERS_URL)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        return OrderResult;
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
}

