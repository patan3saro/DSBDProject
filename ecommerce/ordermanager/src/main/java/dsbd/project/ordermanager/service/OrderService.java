package dsbd.project.ordermanager.service;

import dsbd.project.ordermanager.controller.OrderRequest;
import dsbd.project.ordermanager.data.FinalOrderRepository;
import dsbd.project.productmanager.service.ProductService;
import dsbd.project.usermanager.service.UserService;
import order.FinalOrder;
import order.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;


    public String add(OrderRequest orderRequest, int userId){ //Ci serve per ottenere X-User-ID
        Optional<User> user= userService.findById(userId);
        //Ciò verrà fatto atomicamente nel service
        if(user.isPresent()) {
            List<OrderProduct> list = new ArrayList<>();
            for(Map.Entry<Integer,Integer> item: orderRequest.getProducts().entrySet()){
                Product product= productService.findByIdAndQuantityGreaterThanEqual(item.getKey(),item.getValue());
                list.add(new OrderProduct()
                        .setProduct(product)
                        .setQuantity(item.getValue()));
                product.setQuantity(product.getQuantity() - item.getValue());
            }
            FinalOrder order = new FinalOrder();
            order.setUser(user.get());
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
            Page<FinalOrder> order = finalOrderRepository.findAllByUser(userService.findById(userId), pageWithElements);
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
            Optional<FinalOrder> order = finalOrderRepository.findFinalOrderByIdAndUser(id, userService.findById(userId));
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
