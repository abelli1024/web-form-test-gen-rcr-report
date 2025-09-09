package mo.must.chat.common.framework;

import mo.must.chat.common.config.CustomConf;
import mo.must.chat.common.util.SpringContextUtil;
import org.apache.commons.lang3.RandomUtils;

public class SnowflakeIdWorker {

    private final long twepoch = 1489111610226L;

    private final long workerIdBits = 5L;

    private final long dataCenterIdBits = 5L;

    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    private final long sequenceBits = 12L;

    private final long workerIdShift = sequenceBits;

    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long workerId;

    private long dataCenterId;

    private long sequence = 0L;

    private long lastTimestamp = -1L;

    private static SnowflakeIdWorker idWorker;

    public static CustomConf customConf = SpringContextUtil.getBean(CustomConf.class);

    static {
        idWorker = new SnowflakeIdWorker(getWorkId(), getDataCenterId());
    }


    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can''t be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can''t be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }


    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }


    private static Long getWorkId() {
        try {
            return customConf.getWorkId();
        } catch (Exception e) {
            e.printStackTrace();
            return RandomUtils.nextLong(0, 31);
        }
    }

    private static Long getDataCenterId() {
        try {
            return customConf.getDataCenterId();
        } catch (Exception e) {
            e.printStackTrace();
            return RandomUtils.nextLong(0, 31);
        }
    }


    public static Long generateId() {
        long id = idWorker.nextId();
        return id;
    }

}
