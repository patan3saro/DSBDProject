package order;

public class OrderPaymentNotify {

    private Integer orderId;

    private Integer userId;

    private double amountPaid;

    private String extraArgs;

    public Integer getOrderId() {
        return orderId;
    }

    public OrderPaymentNotify setOrderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public OrderPaymentNotify setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public OrderPaymentNotify setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
        return this;
    }

    public String getExtraArgs() {
        return extraArgs;
    }

    public OrderPaymentNotify setExtraArgs(String extraArgs) {
        this.extraArgs = extraArgs;
        return this;
    }
}
