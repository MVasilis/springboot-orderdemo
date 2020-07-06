package eu.acme.demo.web.response;

import eu.acme.demo.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderLiteResponse {

    private UUID id;
    private OrderStatus status;
    private String description;
    /**
     * reference code used by client system to track order
     */
    private String clientReferenceCode;
    private BigDecimal itemTotalAmount;
    private int itemCount;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientReferenceCode() {
        return clientReferenceCode;
    }

    public void setClientReferenceCode(String clientReferenceCode) {
        this.clientReferenceCode = clientReferenceCode;
    }

    public BigDecimal getItemTotalAmount() {
        return itemTotalAmount;
    }

    public void setItemTotalAmount(BigDecimal itemTotalAmount) {
        this.itemTotalAmount = itemTotalAmount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
