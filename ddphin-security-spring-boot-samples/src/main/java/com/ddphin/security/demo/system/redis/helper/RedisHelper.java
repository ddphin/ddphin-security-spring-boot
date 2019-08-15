package com.ddphin.security.demo.system.redis.helper;

import org.springframework.data.redis.core.*;

/**
 * RedisHelper
 *
 * @Date 2019/7/18 下午3:13
 * @Author ddphin
 */
public interface RedisHelper {
    RedisTemplate redis();

    ValueOperations forValue();

    SetOperations forSet();

    ZSetOperations forZSet();

    ClusterOperations forCluster();

    HashOperations forHash();

    ListOperations forList();

    GeoOperations forGeo();

    HyperLogLogOperations forHyper();
}

