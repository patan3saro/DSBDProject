package dsbd.project.ordermanager.service;

import dsbd.project.ordermanager.controller.OrderRequest;
import dsbd.project.ordermanager.data.FinalOrderRepository;
import order.FinalOrder;
import order.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import product.Product;
import user.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class OrderService {

    @Autowired
    FinalOrderRepository finalOrderRepository;

    /*@Autowired
    UserService userService;*/

    /*@Autowired
    ProductService productService;*/

    private static String USER_MANAGER_URL="http://usermanager:2222";
    private static String PRODUCT_MANAGER_URL="http://productmanager:3333";


    public String add(OrderRequest orderRequest, int userId){ //Ci serve per ottenere X-User-ID
        User user = new RestTemplate().getForObject(USER_MANAGER_URL + "/user/id/{userId}", User.class, userId);
        if(user!=null) {
            List<OrderProduct> list = new ArrayList<>();
            for(Map.Entry<Integer,Integer> item: orderRequest.getProducts().entrySet()){
                Product product = new RestTemplate().getForObject(PRODUCT_MANAGER_URL+"/product/id/{id}" , Product.class, item.getKey());
                if(product.getQuantity()> item.getValue()) {
                    list.add(new OrderProduct()
                            .setProduct(product)
                            .setQuantity(item.getValue()));
                    product.setQuantity(product.getQuantity() - item.getValue());
                }
            }
            FinalOrder order = new FinalOrder();
            order.setUser(user);
            order.setProducts(list);
            order.setShippingAddress(orderRequest.getShippingAddress());
            order.setBillingAddress(orderRequest.getBillingAddress());
            finalOrderRepository.save(order);
            return "Order created " + order.toString();
        }
        else
            return "The user " + userId + " is not present";
    }

    public Page<FinalOrder> getAllOrders(int userId,int per_page, int page){
        Pageable pageWithElements = PageRequest.of(page, per_page);
        if(userId!=0) {
            User user = new RestTemplate().getForObject(USER_MANAGER_URL + "/user/id/{userId}", User.class, userId);
            Page<FinalOrder> order = finalOrderRepository.findAllByUser(Optional.ofNullable(user), pageWithElements);
            if(StreamSupport.stream(order.spliterator(), false).count()>0)
                return order;
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else {
            Page<FinalOrder> order = finalOrderRepository.findAll(pageWithElements); //findAll(pageWithElements);
            if(StreamSupport.stream(order.spliterator(), false).count()>0)
                return order;
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<FinalOrder> getId(Integer id, int userId) {
        if(userId!=0) {
            User user = new RestTemplate().getForObject(USER_MANAGER_URL + "/user/id/{userId}", User.class, userId);
            Optional<FinalOrder> order = finalOrderRepository.findFinalOrderByIdAndUser(id, Optional.ofNullable(user));
            if(order.isPresent())
                return order;
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else {
            Optional<FinalOrder> order = finalOrderRepository.findFinalOrderById(id);
            if(order.isPresent())
                return order;
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
