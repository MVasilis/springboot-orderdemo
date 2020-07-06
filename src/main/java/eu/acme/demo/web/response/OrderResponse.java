package eu.acme.demo.web.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderResponse {

    private UUID id;

    private String clientReferenceCode;

    private String description;

    private Integer itemCount;

    private BigDecimal itemTotalAmount;

    private List<OrderItemResponse> orderItems;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getClientReferenceCode() {
        return clientReferenceCode;
    }

    public void setClientReferenceCode(String clientReferenceCode) {
        this.clientReferenceCode = clientReferenceCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getItemTotalAmount() {
        return itemTotalAmount;
    }

    public void setItemTotalAmount(BigDecimal itemTotalAmount) {
        this.itemTotalAmount = itemTotalAmount;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }
}
