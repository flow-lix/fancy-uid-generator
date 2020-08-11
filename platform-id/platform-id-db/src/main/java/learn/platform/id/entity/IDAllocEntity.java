package learn.platform.id.entity;

/**
 * 分配ID的实体类
 */
public class IDAllocEntity {

    /**
     * 业务标签
     */
    private String tag;
    /**
     * 待分配的最大ID
     */
    private long maxId;
    /**
     * 扩展步长
     */
    private int step;
    /**
     * 更新时间
     */
    private String updateTime;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
