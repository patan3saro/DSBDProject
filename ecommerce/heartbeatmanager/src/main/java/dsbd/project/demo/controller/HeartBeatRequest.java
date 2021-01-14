package dsbd.project.demo.controller;

//this is the body which the HeartBeatRequest sent
public class HeartBeatRequest {

    private String service;
    private String serviceStatus;
    private String dbStatus;

    @Override
    public String toString() {
        return "{" +
                "service:'" + service + '\'' +
                ", serviceStatus:'" + serviceStatus + '\'' +
                ", dbStatus:'" + dbStatus + '\'' +
                '}';
    }

    //getters and setters
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }
}
