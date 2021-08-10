package com.npav.npavhelpdeskticket.pojo;

public class Assign {
    private int toAgentID;
    private int fromAgentID;
    private String status;
    private String info;
    private String toDepartment;

    public int getToAgentID() {
        return toAgentID;
    }

    public void setToAgentID(int toAgentID) {
        this.toAgentID = toAgentID;
    }

    public int getFromAgentID() {
        return fromAgentID;
    }

    public void setFromAgentID(int fromAgentID) {
        this.fromAgentID = fromAgentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(String toDepartment) {
        this.toDepartment = toDepartment;
    }
}
