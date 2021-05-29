package org.fancy.common;

public class Result {

    private final long id;
    private Status status;

    public Result(long id, Status status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
