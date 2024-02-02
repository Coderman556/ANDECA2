package com.example.myapplication;

import java.util.ArrayList;
import java.util.Date;

public class NotesTask {
    public static ArrayList<NotesTask> taskArrayList = new ArrayList<>();

    private int id;
    private String cont;
    private Date date;

    public NotesTask(int id, String cont, Date date) {
        this.id = id;
        this.cont = cont;
        this.date = date;
    }

    public static NotesTask getTaskForID(int passedTaskID) {
        for (NotesTask task : taskArrayList) {
            if (task.getId() == passedTaskID)
                return task;
        }
        return null;
    }

    public static ArrayList<NotesTask> getAllTasks() {
        return taskArrayList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
