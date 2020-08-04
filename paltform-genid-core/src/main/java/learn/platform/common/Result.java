package learn.platform.common;

public class Result {

    private final long id;
    private Status status;

    public Result(long id, Status status) {
        this.id = id;
        this.status = status;
    }
}
