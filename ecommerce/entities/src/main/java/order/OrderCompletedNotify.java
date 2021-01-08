package order;

import java.io.Serializable;
import java.util.Map;


public class OrderCompletedNotify implements Serializable {

    private Integer orderId;

    private Map<Integer,Integer> products;

    private Double total;

    private String shippingAddress;

    private String billingAddress;

    private Integer userId;

    private String extraArgs;

    public Integer getOrderId() {
        return orderId;
    }

    public OrderCompletedNotify setOrderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }

    public OrderCompletedNotify setProducts(Map<Integer, Integer> products) {
        this.products = products;
        return this;
    }

    public Double getTotal() {
        return total;
    }

    public OrderCompletedNotify setTotal(Double total) {
        this.total = total;
        return this;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public OrderCompletedNotify setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public OrderCompletedNotify setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public OrderCompletedNotify setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getExtraArgs() {
        return extraArgs;
    }

    public OrderCompletedNotify setExtraArgs(String extraArgs) {
        this.extraArgs = extraArgs;
        return this;
    }
}
