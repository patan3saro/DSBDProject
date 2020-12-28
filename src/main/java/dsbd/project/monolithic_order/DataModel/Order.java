package dsbd.project.monolithic_order.DataModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne  //più ordini ad un utente
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    private List<OrderProduct> products;

    @NotNull
    private String shippingAddress;

    @NotNull
    private String billingAddress;

    @Transient
    public Double getTotalPrice(){
        double total=0.0;
        for (OrderProduct iteratedProduct: products){
            total+=iteratedProduct.getAggregatedPrice();
        }
        return total;

    }

    public Integer getId() {
        return id;
    }

    public Order setId(Integer id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public Order setProducts(List<OrderProduct> products) {
        this.products = products;
        return this;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public Order setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public Order setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public Order(User user, List<OrderProduct> products, @NotNull String shippingAddress, @NotNull String billingAddress) {
        this.user = user;
        this.products = products;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", products=" + products +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                '}';
    }
}