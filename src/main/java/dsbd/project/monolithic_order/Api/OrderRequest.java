package dsbd.project.monolithic_order.Api;


import javax.validation.constraints.NotNull;
import java.util.Map;

public class OrderRequest {
    @NotNull
    private Map<Integer,Integer> products;//Insieme di products di cui effettuare l'ordine

    @NotNull
    private String shippingAddress;

    @NotNull
    private String billingAddress;

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
