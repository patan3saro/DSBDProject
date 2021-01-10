package dsbd.project.ordermanager.controller;

import com.google.gson.Gson;
import dsbd.project.ordermanager.service.ClientException;
import dsbd.project.ordermanager.service.OrderService;
import dsbd.project.ordermanager.service.ServerException;
import order.FinalOrder;
import order.OrderHttpErrorNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Optional;

@Controller
@RequestMapping(path="/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(path="/orders")
    public @ResponseBody String add(@RequestBody OrderRequest orderRequest,
                                    @RequestHeader("X-User-ID") int userId, //Ci serve per ottenere X-User-ID
                                    HttpServletRequest request) throws ServerException, ClientException {

        return orderService.add(orderRequest, userId, request);
    }

    @GetMapping(path ="/orders", params = {"per_page", "page"})
    public @ResponseBody Page<FinalOrder> getAllOrders(@RequestHeader("X-User-ID") int userId,
                                                       @RequestParam("per_page") int per_page,
                                                       @RequestParam("page") int page,
                                                       HttpServletRequest request) throws ServerException, ClientException {
        return orderService.getAllOrders(userId, per_page, page, request);
    }

    @GetMapping(path="/orders/{id}")
    public @ResponseBody Optional<FinalOrder> getId(@PathVariable Integer id,
                                                    @RequestHeader("X-User-ID") int userId,
                                                    HttpServletRequest request) throws ServerException, ClientException {
        return orderService.getId(id, userId, request);
    }

    //Server exception handler
    @ExceptionHandler(ServerException.class)
    public void handleServerException(ServerException e){
        //getting unix timestamp
        long unixTime = Instant.now().getEpochSecond();
        Instant instant = Instant.ofEpochSecond(unixTime);
        //setting kafka response object
        orderService.sendMessage(e.getTopicKey(), e.getTopic(), new Gson().toJson(new OrderHttpErrorNotify()
            .setTimestamp(instant.toString())
            .setSourceIp(e.getRequest().getRemoteAddr())
            .setService("orders")
            .setRequest(e.getRequest().getPathInfo())
            .setError(e.getStackTrace().toString())));
    }
    //Client exception handler
    @ExceptionHandler(ClientException.class)
    public void handleClientException(ClientException e){
        //getting unix timestamp
        long unixTime = Instant.now().getEpochSecond();
        Instant instant = Instant.ofEpochSecond(unixTime);
        //setting kafka response object
        orderService.sendMessage(e.getTopicKey(), e.getTopic(), new Gson().toJson(new OrderHttpErrorNotify()
                .setTimestamp(instant.toString())
                .setSourceIp(e.getRequest().getRemoteAddr())
                .setService("orders")
                .setRequest(e.getRequest().getPathInfo())
                .setError(e.getStatus().toString())));

        throw new ResponseStatusException(e.getStatus());
    }

}
