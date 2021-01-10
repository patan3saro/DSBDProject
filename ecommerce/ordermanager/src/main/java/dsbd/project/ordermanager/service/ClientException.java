package dsbd.project.ordermanager.service;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

public class ClientException extends Exception{
    private HttpServletRequest request;
    private String topic;
    private String topicKey;
    private HttpStatus status;

    public ClientException(HttpServletRequest request, String topic, String topicKey) {
        this.request = request;
        this.topic = topic;
        this.topicKey = topicKey;
    }

    public ClientException(HttpServletRequest request, String topic, String topicKey, HttpStatus status) {
        this.request = request;
        this.topic = topic;
        this.topicKey = topicKey;
        this.status = status;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public ClientException setRequest(HttpServletRequest request) {
        this.request = request;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public ClientException setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ClientException setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public String getTopicKey() {
        return topicKey;
    }

    public ClientException setTopicKey(String topicKey) {
        this.topicKey = topicKey;
        return this;
    }


}
