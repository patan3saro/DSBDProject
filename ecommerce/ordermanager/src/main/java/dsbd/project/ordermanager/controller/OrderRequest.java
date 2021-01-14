package dsbd.project.ordermanager.controller;


import javax.validation.constraints.NotNull;
import java.util.Map;
//this class allows us to map the client request in order to manage its infos
public class OrderRequest {
    //Binding fields
    //Set of products to order
    @NotNull
    private Map<Integer,Integer> products;

    @NotNull
    private String shippingAddress;

    @NotNull
    private String billingAddress;

    //getters and setters

    public Map<Integer, Integer> getProducts() {
        return products;
    }

    public OrderRequest setProducts(Map<Integer, Integer> products) {
        this.products = products;
        return this;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public OrderRequest setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public OrderRequest setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }
}
