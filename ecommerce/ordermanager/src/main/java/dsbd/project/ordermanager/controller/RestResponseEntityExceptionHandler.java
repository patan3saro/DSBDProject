package dsbd.project.ordermanager.controller;

import com.google.gson.Gson;
import order.OrderHttpErrorNotify;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.BindException;
import java.time.Instant;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @Value("${loggingTopic}")
    private String loggingTopic;

    @Value("${httpErrorsTopicKey}")
    private String httpErrorsTopicKey;

    @Autowired      //quello che facilita la pubblicazione sul topic
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String key, String message){
        kafkaTemplate.send(topic, key, message);
    }

    //400
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            BindException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            TypeMismatchException.class})
    protected void handleConflict0(HttpServletRequest request, Exception e) {
        clientErrorHandler(request, 400);
    }
    //405
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    protected void handleConflict1(HttpServletRequest request) {
        clientErrorHandler(request, 405);
    }
    //404
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoHandlerFoundException.class)
    protected void handleConflict2(HttpServletRequest request) {
        clientErrorHandler(request, 404);
    }
    //406
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    protected void handleConflict3(HttpServletRequest request) {
        clientErrorHandler(request, 406);
    }
    //415
    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    protected void handleConflict4(HttpServletRequest request) {
        clientErrorHandler(request, 415);
    }
    //500
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            MissingPathVariableException.class,
            ConversionNotSupportedException.class,
            HttpMessageNotWritableException.class,
            NullPointerException.class})
    protected void handleConflict5(HttpServletRequest request, Exception e) {
        serverErrorHandler(request,  e);
    }
    //503
    @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    protected void handleConflict6(HttpServletRequest request, AsyncRequestTimeoutException e) {
        serverErrorHandler(request,  e);
    }

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

        sendMessage(loggingTopic, httpErrorsTopicKey , new Gson().toJson(new OrderHttpErrorNotify()
                .setTimestamp(instant.toString())
                .setSourceIp(sourceIp)
                .setService(service)
                .setRequest(request)
                .setError(error)));
    }


    private void clientErrorHandler(HttpServletRequest req, Integer errorCode) {
        long unixTime = Instant.now().getEpochSecond();
        Instant instant = Instant.ofEpochSecond(unixTime);
        String sourceIp = req.getRemoteAddr();
        String service = "orders";
        String request = req.getRequestURI();
        String error = errorCode.toString();

        sendMessage(loggingTopic, httpErrorsTopicKey , new Gson().toJson(new OrderHttpErrorNotify()
                .setTimestamp(instant.toString())
                .setSourceIp(sourceIp)
                .setService(service)
                .setRequest(request)
                .setError(error)));
    }
}
