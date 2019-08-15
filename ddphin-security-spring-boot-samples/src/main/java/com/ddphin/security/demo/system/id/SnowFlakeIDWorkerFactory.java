package com.ddphin.security.demo.system.id;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnowFlakeIDWorkerFactory {
    // 机器id所占的位数
    private final static long workerIdBits = 5L;
    // 数据标识id所占的位数
    private final static long dataCenterIdBits = 5L;
    // 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
    private final static long maxWorkerId = ~(-1L << workerIdBits);
    // 支持的最大数据标识id，结果是31
    private final static long maxDataCenterId = ~(-1L << dataCenterIdBits);

    // 工作机器ID(0~31)
    private long workerId;
    // 数据中心ID(0~31)
    private long dataCenterId;

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public SnowFlakeIDWorkerFactory(long workerId, long dataCenterId){
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }

        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public SnowFlakeIDWorker newWorker() {
        return new SnowFlakeIDWorker(this.workerId, this.dataCenterId, 0);
    }
}
