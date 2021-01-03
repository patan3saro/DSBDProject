package dsbd.project.monolithic_order.DataModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.util.List;

@Entity
public class FinalOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne  //più ordini ad un utente
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    private List<OrderProduct> products;

    @NotNull(message="The field cannot be empty")
    private String shippingAddress;

    @NotNull(message="The field cannot be empty")
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

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Override
    public String toString() {
        return "FinalOrder{" +
                "id:" + id +
                ", user:" + user +
                ", products:" + products +
                ", shippingAddress:'" + shippingAddress + '\'' +
                ", billingAddress:'" + billingAddress + '\'' +
                '}';
    }
}
