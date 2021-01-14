package dsbd.project.ordermanager.controller;

import dsbd.project.ordermanager.service.OrderService;
import order.FinalOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.BindException;
import java.util.Optional;
//we split the controller and the body of this one
//in controlle and service
@Controller
@RequestMapping(path="/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(path="/orders")
    public @ResponseBody String add(@RequestBody OrderRequest orderRequest,
                                    //we need this annotation in order to obtain X-User-ID
                                    @RequestHeader("X-User-ID") int userId) throws BindException, NoSuchFieldException
    {
        return orderService.add(orderRequest, userId);
    }

    @GetMapping(path ="/orders", params = {"per_page", "page"})
    public @ResponseBody Page<FinalOrder> getAllOrders(@RequestHeader("X-User-ID") int userId,
                                                       @RequestParam("per_page") int per_page,
                                                       @RequestParam("page") int page) throws NoSuchFieldException {
        return orderService.getAllOrders(userId, per_page, page);
    }

    @GetMapping(path="/orders/{id}")
    public @ResponseBody Optional<FinalOrder> getId(@PathVariable Integer id,
                                                    @RequestHeader("X-User-ID") int userId) throws NoSuchFieldException {
        return orderService.getId(id, userId);
    }




}
