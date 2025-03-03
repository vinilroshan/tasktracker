package com.codeskittles.cc.tasktracker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskTracker {

    private static final String ADD_USAGE = "task-cli add <task-description>";
    private static final String UPDATE_USAGE = "task-cli update <task-id> <task-description>";
    private static final String DELETE_USAGE = "task-cli delete <task-id>";
    private static final String MARK_IN_PROGRESS_USAGE = "task-cli mark-in-progress <task-id>";
    private static final String MARK_DONE_USAGE = "task-cli mark-done <task-id>";
    private static final String LIST_USAGE = "task-cli list [done|todo|in-progress]";
    private static final String USAGE_STRING = "Usage: task-cli <command> [options]\n" +
            "ADD Task: " + ADD_USAGE + "\n" +
            "UPDATE Task: " + UPDATE_USAGE + "\n" +
            "DELETE Task: " + DELETE_USAGE + "\n" +
            "MARK Task In Progress: " + MARK_IN_PROGRESS_USAGE + "\n" +
            "MARK Task Done: " + MARK_DONE_USAGE + "\n" +
            "LIST Tasks: " + LIST_USAGE + "\n";

    private static final String TASK_FILE_PATH = System.getProperty("user.home") + "/tasks.json";
    private static final File TASK_FILE = new File(TASK_FILE_PATH);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        try {
            TASK_FILE.createNewFile();
            if (TASK_FILE.length() == 0) {
                final var tasks = new TasksRoot();
                tasks.setNextId(1);
                tasks.setTasks(new ArrayList<>());

                objectMapper.writeValue(TASK_FILE, tasks);
            }
        } catch (IOException ex) {
            System.err.println("Error creating/writing task file: " + ex.getMessage());
        }

        objectMapper.findAndRegisterModules();
    }

    private static void addTask(final String[] args) {
        if (args.length < 2) {
            System.err.println("Task description is required");
            System.out.println(ADD_USAGE);
            System.exit(1);
        }

        try {
            final var tasks = objectMapper.readValue(TASK_FILE, TasksRoot.class);
            
            final var task = new Task();
            task.setId(tasks.getNextId());
            task.setDescription(args[1]);
            task.setStatus(Status.TODO);
            task.setCreatedAt(java.time.LocalDateTime.now());
            task.setUpdatedAt(java.time.LocalDateTime.now());

            tasks.getTasks().add(task);
            tasks.setNextId(tasks.getNextId() + 1);

            objectMapper.writeValue(TASK_FILE, tasks);
        } catch (IOException ex) {
            System.err.println("Error reading/writing task file");
            System.exit(1);
        }
    }

    private static void updateTask(final String[] args) {
        if (args.length < 3) {
            System.err.println("Task id and description are required");
            System.out.println(UPDATE_USAGE);
            System.exit(1);
        }

        try {
            final var tasks = objectMapper.readValue(TASK_FILE, TasksRoot.class);
            final var taskId = Integer.parseInt(args[1]);

            final var task = tasks.getTasks().stream()
                    .filter(t -> t.getId() == taskId)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Task not found"));

            task.setDescription(args[2]);
            task.setUpdatedAt(java.time.LocalDateTime.now());

            objectMapper.writeValue(TASK_FILE, tasks);
        } catch (IOException ex) {
            System.err.println("Error reading/writing task file");
            System.exit(1);
        }
    }

    private static void deleteTask(final String[] args) {
        if (args.length < 2) {
            System.err.println("Task id is required");
            System.out.println(DELETE_USAGE);
            System.exit(1);
        }

        try {
            final var tasks = objectMapper.readValue(TASK_FILE, TasksRoot.class);
            final var taskId = Integer.parseInt(args[1]);

            tasks.getTasks().removeIf(t -> t.getId() == taskId);

            objectMapper.writeValue(TASK_FILE, tasks);
        } catch (IOException ex) {
            System.err.println("Error reading/writing task file");
            System.exit(1);
        }
    }

    private static void markTask(final String[] args) {
        if (args.length < 2) {
            System.err.println("Task id is required");
            System.out.println(MARK_IN_PROGRESS_USAGE + " | " + MARK_DONE_USAGE);
            System.exit(1);
        }

        try {
            final var tasks = objectMapper.readValue(TASK_FILE, TasksRoot.class);
            final var taskId = Integer.parseInt(args[1]);

            final var task = tasks.getTasks().stream()
                    .filter(t -> t.getId() == taskId)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Task not found"));

            if (args[0].equals("mark-in-progress")) {
                if (task.getStatus() == Status.DONE) {
                    System.err.println("Cannot mark a done task as in progress");
                    System.exit(1);
                }
                task.setStatus(Status.INPROGRESS);
            } else if (args[0].equals("mark-done")) {
                if (task.getStatus() != Status.INPROGRESS) {
                    System.err.println("Cannot mark a task as done if it is not in progress");
                    System.exit(1);
                }
                task.setStatus(Status.DONE);
            }
            task.setUpdatedAt(java.time.LocalDateTime.now());

            objectMapper.writeValue(TASK_FILE, tasks);
        } catch (IOException ex) {
            System.err.println("Error reading/writing task file");
            System.exit(1);
        }
    }

    private static void listTasks(final String[] args) {
        try {
            final var tasks = objectMapper.readValue(TASK_FILE, TasksRoot.class);

            if (args.length < 2) {
                tasks.getTasks().forEach(t -> System.out.println(t.getId() + " - " + t.getDescription() + " - " + t.getStatus()));
            } else {
                final var status = Status.fromString(args[1]);
                tasks.getTasks().stream()
                        .filter(t -> t.getStatus() == status)
                        .forEach(t -> System.out.println(t.getId() + " - " + t.getDescription() + " - " + t.getStatus()));
            }
        } catch (IOException ex) {
            System.err.println("Error reading/writing task file " + ex.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Command is required");
            System.out.println(USAGE_STRING);
            System.exit(1);
        }

        final var command = args[0];

        switch (command) {
            case "add" -> addTask(args);
            case "update" -> updateTask(args);
            case "delete" -> deleteTask(args);
            case "mark-in-progress", "mark-done" -> markTask(args);
            case "list" -> listTasks(args);
            default -> {
                System.err.println("Invalid command");
                System.out.println(USAGE_STRING);
                System.exit(1);
            }
        }
    }
}
