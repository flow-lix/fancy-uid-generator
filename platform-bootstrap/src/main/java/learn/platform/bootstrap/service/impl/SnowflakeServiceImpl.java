package learn.platform.bootstrap.service.impl;

import learn.platform.bootstrap.service.LeafService;
import learn.platform.common.Result;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * /----/-------------/---------/---------/
 * /(1)/ timestamp(41)/workerID(10)/sequence(12)
 */
@Service
public class SnowflakeServiceImpl implements LeafService {

    private int sequence = 0;
    private final int SEQUENCE_BITS = 12;
    private final int SEQUENCE_MASK = ~(-1 << SEQUENCE_BITS);

    private int workerId;
    private int workerShift = 12;
    private final int MAX_WORKER_ID = ~(-1 << 10);

    private int timestamoShift = workerShift + SEQUENCE_BITS;
    private long lastTimestamp = -1;

    @PostConstruct
    public void init() {

    }

    @Override
    public Result generateLeaf(String tag) {
        return null;
    }
}
