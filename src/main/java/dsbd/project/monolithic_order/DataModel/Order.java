package dsbd.project.monolithic_order.DataModel;

import javax.persistence.*;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne  //più ordini ad un utente
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    private List<OrderProduct> products;

    private String shippingAddress;

    private String billingAddress;

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
}
