package com.ddphin.security.demo.system.redis.helper.impl;

import com.ddphin.security.demo.system.redis.helper.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

/**
 * RedisHelperImpl
 *
 * @Date 2019/7/18 下午3:15
 * @Author ddphin
 */
@Service
public class RedisHelperImpl implements RedisHelper {

    private ValueOperations valueOperations;
    private SetOperations setOperations;
    private ZSetOperations zSetOperations;
    private ClusterOperations clusterOperations;
    private HashOperations hashOperations;
    private ListOperations listOperations;
    private GeoOperations geoOperations;
    private HyperLogLogOperations hyperLogLogOperations;
    private RedisTemplate redisTemplate;

    @Autowired
    public RedisHelperImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.zSetOperations = redisTemplate.opsForZSet();
        this.clusterOperations = redisTemplate.opsForCluster();
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
        this.setOperations = redisTemplate.opsForSet();
        this.geoOperations = redisTemplate.opsForGeo();
        this.hyperLogLogOperations = redisTemplate.opsForHyperLogLog();
    }

    @Override
    public RedisTemplate redis() {
        return this.redisTemplate;
    }

    @Override
    public ValueOperations forValue() {
        return this.valueOperations;
    }
    @Override
    public SetOperations forSet() {
        return this.setOperations;
    }
    @Override
    public ZSetOperations forZSet() {
        return this.zSetOperations;
    }
    @Override
    public ClusterOperations forCluster() {
        return this.clusterOperations;
    }
    @Override
    public HashOperations forHash() {
        return this.hashOperations;
    }
    @Override
    public ListOperations forList() {
        return this.listOperations;
    }
    @Override
    public GeoOperations forGeo() {
        return this.geoOperations;
    }
    @Override
    public HyperLogLogOperations forHyper() {
        return this.hyperLogLogOperations;
    }
}
