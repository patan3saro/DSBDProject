package dsbd.project.ordermanager.controller;

import com.google.gson.Gson;
import dsbd.project.ordermanager.service.OrderService;
import dsbd.project.ordermanager.notificationclasses.OrderHttpErrorNotify;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.BindException;
import java.time.Instant;

@Component
// this annotation allows our class to recognize global exceptions in our application
@ControllerAdvice
// this annotation allows us to obtain different levels of precedence for a class
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandlerResolver {

    @Autowired
    OrderService orderService;
    //topics
    @Value("${loggingTopic}")
    private String loggingTopic;

    @Value("${httpErrorsTopicKey}")
    private String httpErrorsTopicKey;

    //the exceptions have been divided according to the code they generate

    //client exceptions
    //400   BAD REQUEST
    @ExceptionHandler(value = {
            BindException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            TypeMismatchException.class,
            MissingRequestHeaderException.class})
    protected void handleBadRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //we need to set our servlet response with desired code in order to show the error body to the client
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        clientErrorHandler(request, response.getStatus());
    }

    //404   NOT FOUND
    @ExceptionHandler(value = {
            NoHandlerFoundException.class,
            NoSuchFieldException.class,
            NoSuchMethodException.class})
    protected void handleNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        clientErrorHandler(request, response.getStatus());
    }

    //405   METHOD NOT ALLOWED
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    protected void handleMethodNotAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        clientErrorHandler(request, response.getStatus());
    }

    //406   NOT ACCEPTABLE
    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    protected void handleNotAcceptable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        clientErrorHandler(request, response.getStatus());
    }
    //415   UNSUPPORTED MEDIA TYPE
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    protected void handleUnsupportedMediaType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        clientErrorHandler(request, response.getStatus());
    }
    //server exceptions
    //500   INTERNAL SERVER ERROR
    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            MissingPathVariableException.class,
            ConversionNotSupportedException.class,
            HttpMessageNotWritableException.class,
            NullPointerException.class})
    protected void handleInternalServerError(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        serverErrorHandler(request, e);
    }

    //503   SERVICE UNAVAILABLE
    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    protected void handleServiceUnavailable(HttpServletRequest request, HttpServletResponse response, AsyncRequestTimeoutException e) throws IOException {
        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        serverErrorHandler(request, e);
    }
    // generic client function in wich we construct the body of the message
    // to send in kafka logging topic
    private void clientErrorHandler(HttpServletRequest req, Integer errorCode) {
        //getting unix time
        long unixTime = Instant.now().getEpochSecond();
        Instant instant = Instant.ofEpochSecond(unixTime);
        String sourceIp = req.getRemoteAddr();
        String service = "orders";
        String request = req.getRequestURI();
        String error = errorCode.toString();

        orderService.sendMessage(loggingTopic, httpErrorsTopicKey , new Gson().toJson(new OrderHttpErrorNotify()
                .setTimestamp(instant.toString())
                .setSourceIp(sourceIp)
                .setService(service)
                .setRequest(request)
                .setError(error)));
    }
    //this function is like the previous one function, but with a difference:
    // the error field has the stack trace
    private void serverErrorHandler(HttpServletRequest req, Exception e){
        long unixTime = Instant.now().getEpochSecond();
        Instant instant = Instant.ofEpochSecond(unixTime);
        String sourceIp = req.getRemoteAddr();
        String service = "orders";
        String request = req.getRequestURI();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String error = sw.toString();

        orderService.sendMessage(loggingTopic, httpErrorsTopicKey , new Gson().toJson(new OrderHttpErrorNotify()
                .setTimestamp(instant.toString())
                .setSourceIp(sourceIp)
                .setService(service)
                .setRequest(request)
                .setError(error)));
    }
}