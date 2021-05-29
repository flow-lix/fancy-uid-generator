package learn.platform.bootstrap.service.impl;

import learn.platform.bootstrap.service.LeafService;
import org.fancy.common.Result;
import learn.platform.id.Segment;
import learn.platform.id.SegmentBuffer;
import learn.platform.id.entity.IDAllocEntity;
import learn.platform.id.mapper.IDAllocMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fancy.common.Status.FAILURE;
import static org.fancy.common.Status.SUCCESS;
import static org.fancy.common.constant.LeafConstant.EXCEPTION_IDCACHE_INIT_FALSE;
import static org.fancy.common.constant.LeafConstant.EXCEPTION_IDCACHE_KEY_NOT_EXISTED;

@Service
public class SegmentServiceImpl implements LeafService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SegmentServiceImpl.class);

    @Resource
    private IDAllocMapper idAllocMapper;

    private static ConcurrentMap<String, SegmentBuffer> cache = new ConcurrentHashMap<>();

    private volatile boolean initialized;

    private ExecutorService service = new ThreadPoolExecutor(5, 200, 60, TimeUnit.SECONDS,
            new SynchronousQueue<>(), new UpdateThreadFactory());

    @PostConstruct
    public void init() {
        LOGGER.info("Init...");
        updateCacheFromDB();
        // 确保缓存已经被更新了
        initialized = true;
        updateCacheFromDBAtEveryMinute();
    }

    public SegmentServiceImpl() {
        LOGGER.info("Invoke default construct");
    }

    @Override
    public Result generateLeaf(String tag) {
        if (!initialized) {
            return new Result(EXCEPTION_IDCACHE_INIT_FALSE, FAILURE);
        }
        if (!cache.containsKey(tag)) {
            return new Result(EXCEPTION_IDCACHE_KEY_NOT_EXISTED, FAILURE);
        }
        final SegmentBuffer buffer = cache.get(tag);
        if (!buffer.isInitialized()) {
            synchronized (buffer) {
                if (!buffer.isInitialized()) {
                    try {
                        updateSegmentFromDb(tag, buffer.getCurrent());
                        LOGGER.info("初始化buffer, 从数据库中更新业务{}-{}", tag, buffer.getCurrent());
                        buffer.setInitialized(true);
                    } catch (Exception e) {
                        throw new IllegalStateException("Init buffer " + buffer.getCurrent() + " fail!", e);
                    }
                }
            }
        }
        return getIdFromSegmentBuffer(buffer);
    }

    static class UpdateThreadFactory implements ThreadFactory {
        private static AtomicInteger threadIncNum = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-Segment-Update-" + threadIncNum.incrementAndGet());
        }
    }

    private void updateCacheFromDBAtEveryMinute() {
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("check-idCache-thread");
            t.setDaemon(true);
            return t;
        });
        scheduledExecutor.scheduleWithFixedDelay(this::updateCacheFromDB,
                60, 60, TimeUnit.SECONDS);
    }

    /**
     * 更新Segment
     * @param tag 业务标签
     * @param current 当前segment
     */
    private void updateSegmentFromDb(String tag, Segment current) {
        StopWatch sw = new StopWatch();
        sw.start();
        SegmentBuffer buffer = current.getBuffer();

        idAllocMapper.updateMaxId(tag);
        IDAllocEntity idAlloc = idAllocMapper.getIdAlloc(tag);
        buffer.setStep(idAlloc.getStep());

        long value = idAlloc.getMaxId() - idAlloc.getStep();
        current.getValue().set(value);
        current.setMax(idAlloc.getMaxId());
        current.setStep(idAlloc.getStep());
        sw.stop();
    }

    private Result getIdFromSegmentBuffer(SegmentBuffer buffer) {
        while (true) {
            buffer.rLock().lock();
            try {
                final Segment segment = buffer.getCurrent();
                if (!buffer.isNextReady() && (segment.getIdle() < 0.9 * segment.getStep())
                        && buffer.getThreadRunning().compareAndSet(false, true)) {
                    service.execute(() -> {
                        Segment next = buffer.getSegments()[buffer.nextPos()];
                        boolean updated = false;
                        try {
                            updateSegmentFromDb(buffer.getTag(), next);
                            updated = true;
                            LOGGER.info("Update segment {} from db {}", buffer.getTag(), next);
                        } catch (Exception e) {
                            LOGGER.warn("{} updateSegmentFromBuffer fail!", buffer.getTag());
                        } finally {
                            if (updated) {
                                buffer.wLock().lock();
                                buffer.setNextReady(true);
                                buffer.wLock().unlock();
                            }
                            buffer.getThreadRunning().set(false);
                        }
                    });
                }
                long value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    return new Result(value, SUCCESS);
                }
            } finally {
                buffer.rLock().unlock();
            }

            buffer.wLock().lock();
            try {
                if (buffer.isNextReady()) {
                    buffer.switchPos();
                    buffer.setNextReady(false);
                } else {
                    LOGGER.error("Both segment {} are not ready", buffer);
                }
            } finally {
                buffer.wLock().unlock();
            }
        }
    }

    /**
     * 从数据库更新缓存
     */
    private void updateCacheFromDB() {
        LOGGER.info("Update cache from DB.");
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            List<String> dbTags = idAllocMapper.getAllTags();
            // 删除缓存没有的tag
            for (String tag : cache.keySet()) {
                if (!dbTags.contains(tag)) {
                    cache.remove(tag);
                }
            }
            // 从数据库添加新的tag
            for (String tag : dbTags) {
                cache.computeIfAbsent(tag, key -> {
                    LOGGER.info("Add new tag {} from db to IdCache.", key);
                    return new SegmentBuffer(key);
                });
            }
        } catch (Exception e) {
            LOGGER.warn("update cache from db exception", e);
        } finally {
            sw.stop();
        }
    }
}
