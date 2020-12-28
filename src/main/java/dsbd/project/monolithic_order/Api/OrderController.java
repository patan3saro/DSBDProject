package dsbd.project.monolithic_order.Api;

import dsbd.project.monolithic_order.DataModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(path="/order")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    //Provvisorio: la modificheremo con i microservizi
    @Autowired
    UserRepository userRepository;

    //Provvisorio: la modificheremo con i microservizi
    @Autowired
    ProductRepository productRepository;

    @PostMapping(path="/orders")
    public @ResponseBody String add(@RequestBody OrderRequest orderRequest, Principal principal){ //Ci serve per ottenere X-User-ID
        Optional<User> user= userRepository.findById(Integer.parseInt(principal.getName()));
        //Ciò verrà fatto atomicamente nel service
        if(user.isPresent()) {
            List<OrderProduct> list = new ArrayList<>();
            for(Map.Entry<Integer,Integer> item: orderRequest.getProducts().entrySet()){
                Product product=productRepository.findByIdAndQuantityGreaterThanEqual(item.getKey(),item.getValue());
                list.add(new OrderProduct(product, item.getValue()));
                product.setQuantity(product.getQuantity()- item.getValue());
            }
            Order order= new Order(user.get(),list, orderRequest.getShippingAddress(), orderRequest.getBillingAddress());
            orderRepository.save(order);
            return "Order created" + order.toString();
        }
        else
            return "The user" + principal.getName() + "is not present";

    }


}
