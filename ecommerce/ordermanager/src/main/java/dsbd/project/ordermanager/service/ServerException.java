package dsbd.project.ordermanager.service;

import javax.servlet.http.HttpServletRequest;

public class ServerException extends Exception {
    private HttpServletRequest request;
    private String topic;
    private String topicKey;

    public ServerException(HttpServletRequest request, String topic, String topicKey) {
        this.request = request;
        this.topic = topic;
        this.topicKey = topicKey;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public ServerException setRequest(HttpServletRequest request) {
        this.request = request;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public ServerException setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getTopicKey() {
        return topicKey;
    }

    public ServerException setTopicKey(String topicKey) {
        this.topicKey = topicKey;
        return this;
    }
}
