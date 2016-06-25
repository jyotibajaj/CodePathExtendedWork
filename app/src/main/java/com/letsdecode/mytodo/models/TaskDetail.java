package com.letsdecode.mytodo.models;


public class TaskDetail {

    private String itemName;
    private String status;

    public String getTime() {
        return time;
    }

    private String time;
    private String priority;
    private final int id;

    public String getPriority() {
        return priority;
    }

    public TaskDetail(String itemName, String time, String priority, String status, int id) {
        this.itemName = itemName;
        this.status = status;
        this.time = time;
        this.priority = priority;
        this.id = id;


    }

    @Override
    public String toString() {
        return "TaskDetail{" +
                "itemName='" + itemName + '\'' +
                ", status='" + status + '\'' +
                ", time=" + time +
                ", priority='" + priority + '\'' +
                ", id=" + id +
                '}';
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }


}
