package dsbd.project.monolithic_order.Api;

import dsbd.project.monolithic_order.DataModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(path="/order")
public class OrderController {

    @Autowired
    FinalOrderRepository finalOrderRepository;

    //Provvisorio: la modificheremo con i microservizi
    @Autowired
    UserRepository userRepository;

    //Provvisorio: la modificheremo con i microservizi
    @Autowired
    ProductRepository productRepository;

    @PostMapping(path="/orders")
    public @ResponseBody String add(@RequestBody OrderRequest orderRequest, @RequestHeader("X-User-ID") int userId){ //Ci serve per ottenere X-User-ID
        Optional<User> user= userRepository.findById(userId);
        //Ciò verrà fatto atomicamente nel service
        if(user.isPresent()) {
            List<OrderProduct> list = new ArrayList<>();
            for(Map.Entry<Integer,Integer> item: orderRequest.getProducts().entrySet()){
                Product product=productRepository.findByIdAndQuantityGreaterThanEqual(item.getKey(),item.getValue());
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



    @GetMapping(path="/orders/")
    public @ResponseBody Iterable<FinalOrder> getAllOrders(@RequestHeader("X-User-ID") int userId){
        return finalOrderRepository.findAllByUser(userRepository.findById(userId));
    }

    /* @GetMapping(path="/orders/{id}")
    public @ResponseBody Optional<FinalOrder> getId(@PathVariable Integer id, @RequestHeader("X-User-ID") int userId){

        return finalOrderRepository.findFinalOrderByIdAndUserIsIn(id, userRepository.findById(userId));
        /*Integer ownerUserId=*///.getUser().getId();

        /*if(userId==0 || userId==ownerUserId){
            return finalOrder;
        }*/




}
