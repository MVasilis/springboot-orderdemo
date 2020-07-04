package eu.acme.demo.web.request;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {

    String clientReferenceCode;

    String description;

    Integer itemCount;

    BigDecimal itemTotalAmount;

    private List<OrderItemsRequested>  orderItems;

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

    public String getClientReferenceCode() {
        return clientReferenceCode;
    }

    public void setClientReferenceCode(String clientReferenceCode) {
        this.clientReferenceCode = clientReferenceCode;
    }

    public List<OrderItemsRequested> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemsRequested> orderItems) {
        this.orderItems = orderItems;
    }

    //TODO: place required fields in order to create an order submitted by client
}
