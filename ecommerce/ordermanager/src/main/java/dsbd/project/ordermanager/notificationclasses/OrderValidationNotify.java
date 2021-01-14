package dsbd.project.ordermanager.notificationclasses;

public class OrderValidationNotify {

    private String timestamp;

    private Integer status;

    private Integer orderId;

    private String extraArgs;

    //getters and setters

    public String getTimestamp() {
        return timestamp;
    }

    public OrderValidationNotify setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public OrderValidationNotify setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public OrderValidationNotify setOrderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getExtraArgs() {
        return extraArgs;
    }

    public OrderValidationNotify setExtraArgs(String extraArgs) {
        this.extraArgs = extraArgs;
        return this;
    }
}
