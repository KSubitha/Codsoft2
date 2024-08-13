package com.example.todoapp.Model;

public class ToDoModel {
    private int id;
    private int status;
    private String tasks;
    private String date;  // New field for date
    private String time;  // New field for time

    // Getters and setters for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for status
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Getters and setters for tasks
    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    // Getters and setters for date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getters and setters for time
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
