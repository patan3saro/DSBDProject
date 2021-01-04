package dsbd.project.ordermanager.controller;

import dsbd.project.ordermanager.data.FinalOrderRepository;
import dsbd.project.ordermanager.service.OrderService;
import dsbd.project.productmanager.data.ProductRepository;
import dsbd.project.usermanager.data.UserRepository;
import order.FinalOrder;
import order.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import product.Product;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping(path="/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(path="/orders")
    public @ResponseBody String add(@RequestBody OrderRequest orderRequest,
                                    @RequestHeader("X-User-ID") int userId)
    { //Ci serve per ottenere X-User-ID
        return orderService.add(orderRequest, userId);
    }

    @GetMapping(path ="/orders", params = {"per_page", "page"})
    public @ResponseBody Page<FinalOrder> getAllOrders(@RequestHeader("X-User-ID") int userId,
                                                       @RequestParam("per_page") int per_page,
                                                       @RequestParam("page") int page)
    {
        return orderService.getAllOrders(userId, per_page, page);
    }

    @GetMapping(path="/orders/{id}")
    public @ResponseBody Optional<FinalOrder> getId(@PathVariable Integer id,
                                                    @RequestHeader("X-User-ID") int userId)
    {
        return orderService.getId(id, userId);
    }


}
