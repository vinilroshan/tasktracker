package com.codeskittles.cc.tasktracker;

public enum Status {
    TODO, INPROGRESS, DONE;

    public static Status fromString(final String str) {
        return switch (str.toLowerCase()) {
            case "todo" -> TODO;
            case "in-progress" -> INPROGRESS;
            case "done" -> DONE;
            default -> throw new IllegalArgumentException("Invalid status: " + str);
        };
    }
}
