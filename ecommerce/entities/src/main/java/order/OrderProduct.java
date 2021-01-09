package order;

import product.Product;

import javax.persistence.*;
import java.beans.Transient;

@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Product product;

    private Integer quantity;

    @Transient
    public double getAggregatedPrice(){
        return quantity*product.getPrice();
    }

    public Integer getId() {
        return id;
    }

    public OrderProduct setId(Integer id) {
        this.id = id;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public OrderProduct setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderProduct setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

}

