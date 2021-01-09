package order;

public class OrderHttpErrorNotify {

    private String timestamp;

    private String sourceIp;

    private String service;

    private String request;

    private String error;

    public String getTimestamp() {
        return timestamp;
    }

    public OrderHttpErrorNotify setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public OrderHttpErrorNotify setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
        return this;
    }

    public String getService() {
        return service;
    }

    public OrderHttpErrorNotify setService(String service) {
        this.service = service;
        return this;
    }

    public String getRequest() {
        return request;
    }

    public OrderHttpErrorNotify setRequest(String request) {
        this.request = request;
        return this;
    }

    public String getError() {
        return error;
    }

    public OrderHttpErrorNotify setError(String error) {
        this.error = error;
        return this;
    }
}
