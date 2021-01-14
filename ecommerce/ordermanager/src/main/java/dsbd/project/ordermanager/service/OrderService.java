package dsbd.project.ordermanager.service;

import com.google.gson.Gson;
import com.netflix.discovery.EurekaClient;
import dsbd.project.ordermanager.controller.OrderRequest;
import dsbd.project.ordermanager.data.FinalOrderRepository;
import dsbd.project.ordermanager.notificationclasses.OrderCompletedNotify;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import product.ProductUpdateRequest;
import order.FinalOrder;
import order.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import product.Product;
import user.User;

import javax.transaction.Transactional;
import java.net.BindException;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class OrderService {

    @Autowired
    FinalOrderRepository finalOrderRepository;

    @Autowired
    EurekaClient eurekaClient;
    //topics
    @Value("${kafkaTopic}")
    private String topicName;

    @Value("${ordersTopic}")
    private String ordersTopic;

    @Value("${notificationsTopic}")
    private String notificationsTopic;
    //topic keys
    @Value("${ordersAndNotificationsKey}")
    private String ordersAndNotificationsKey;

    @Value("${kafkaTopicKey}")
    private String kafkaTopicKey;

    //this KafkaTemplate simplifies posting to the topic
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String key, String message){
        kafkaTemplate.send(topic, key, message);
    }

    public String add(OrderRequest orderRequest, int userId) throws BindException, NoSuchFieldException { //we need this to obtain X-User-ID
        boolean check = true;
        String USER_MANAGER_URL=eurekaClient.getNextServerFromEureka("usermanager",false).getHomePageUrl();
        User user = new RestTemplate().getForObject(USER_MANAGER_URL + "/user/id/{userId}", User.class, userId);
        if(user!=null) {
            String PRODUCT_MANAGER_URL=eurekaClient.getNextServerFromEureka("productmanager",false).getHomePageUrl();
            List<OrderProduct> list = new ArrayList<>();
            for(Map.Entry<Integer,Integer> item: orderRequest.getProducts().entrySet()){
                Product product = new RestTemplate().getForObject(PRODUCT_MANAGER_URL+"/product/id/{id}" , Product.class, item.getKey());
                if(product.getQuantity() >= item.getValue() && item.getValue()>0) {
                    list.add(new OrderProduct()
                            .setProduct(product)
                            .setQuantity(item.getValue()));
                }
                else
                    check = false;
            }
            if(check) {
                FinalOrder order = new FinalOrder();
                order.setUser(user);
                order.setProducts(list);
                order.setShippingAddress(orderRequest.getShippingAddress());
                order.setBillingAddress(orderRequest.getBillingAddress());
                FinalOrder orderCreated = finalOrderRepository.save(order);

                for (final OrderProduct orderProduct : list) {
                    Product prod = orderProduct.getProduct();
                    sendMessage(topicName, kafkaTopicKey, new Gson().toJson(new ProductUpdateRequest()
                            .setProductId(prod.getId())
                            .setProductQuantity(orderProduct.getQuantity())));
                }
                sendMessage(ordersTopic, ordersAndNotificationsKey, new Gson().toJson(new OrderCompletedNotify()
                        .setOrderId(orderCreated.getId())
                        .setProducts(orderRequest.getProducts())
                        .setTotal(orderCreated.getTotalPrice())
                        .setShippingAddress(orderCreated.getShippingAddress())
                        .setBillingAddress(orderCreated.getBillingAddress())
                        .setUserId(orderCreated.getUser().getId())
                        .setExtraArgs("")));

                sendMessage(notificationsTopic, ordersAndNotificationsKey, new Gson().toJson(new OrderCompletedNotify()
                        .setOrderId(orderCreated.getId())
                        .setProducts(orderRequest.getProducts())
                        .setTotal(orderCreated.getTotalPrice())
                        .setShippingAddress(orderCreated.getShippingAddress())
                        .setBillingAddress(orderCreated.getBillingAddress())
                        .setUserId(orderCreated.getUser().getId())
                        .setExtraArgs("")));

                return "Order created " + order.toString();
            }
            else
                throw new BindException();
        }
        else
            throw new NoSuchFieldException();
    }

    public Page<FinalOrder> getAllOrders(int userId,int per_page, int page) throws NoSuchFieldException {
        String USER_MANAGER_URL=eurekaClient.getNextServerFromEureka("usermanager",false).getHomePageUrl();
        Pageable pageWithElements = PageRequest.of(page, per_page);
        if(userId!=0) {
            User user = new RestTemplate().getForObject(USER_MANAGER_URL + "/user/id/{userId}", User.class, userId);
            Page<FinalOrder> order = finalOrderRepository.findAllByUser(Optional.ofNullable(user), pageWithElements);
            if(StreamSupport.stream(order.spliterator(), false).count()>0)
                return order;
            else
                throw new NoSuchFieldException();
        }
        else {
            Page<FinalOrder> order = finalOrderRepository.findAll(pageWithElements);
            if(StreamSupport.stream(order.spliterator(), false).count()>0)
                return order;
            else
                throw new NoSuchFieldException();
        }
    }

    public Optional<FinalOrder> getId(Integer id, int userId) throws NoSuchFieldException {
        String USER_MANAGER_URL=eurekaClient.getNextServerFromEureka("usermanager",false).getHomePageUrl();
        if(userId!=0) {
            User user = new RestTemplate().getForObject(USER_MANAGER_URL + "/user/id/{userId}", User.class, userId);
            Optional<FinalOrder> order = finalOrderRepository.findFinalOrderByIdAndUser(id, Optional.ofNullable(user));
            if(order.isPresent())
                return order;
            else
                throw new NoSuchFieldException();
        }
        else {
            Optional<FinalOrder> order = finalOrderRepository.findFinalOrderById(id);
            if(order.isPresent())
                return order;
            else
                throw new NoSuchFieldException();
        }
    }

    public FinalOrder updateOrder(FinalOrder finalOrder){
        return finalOrderRepository.save(finalOrder);
    }


    public Optional<FinalOrder> getOrder(Integer orderId){
        return finalOrderRepository.findById(orderId);
    }

}
