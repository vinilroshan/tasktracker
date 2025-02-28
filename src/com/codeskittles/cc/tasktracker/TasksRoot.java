package com.codeskittles.cc.tasktracker;

import java.util.List;

public class TasksRoot {
    private int nextId;
    private List<Task> tasks;

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
