package com.galaxyinternet.framework.core.distributedLock;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * 
 * redis分布式锁
 *
 */
public class RedisDistributedLock extends DistributedLock {

    /**
     * redis操作数据源
     */
    private JedisManager jedisManager = JedisManagerFactory.getJedisManager();

    /**
     * lock的key
     */
    private String key;

    /**
     * lock的心跳时间(毫秒)
     * <p>
     * 必须满足(heartbeatTime <= timeout*1000)
     */
    private long heartbeatTime;

    /**
     * lock的自然超时时间(秒)
     */
    private int timeout;

    /**
     * 版本号时间，作为获取锁的客户端操作的依据
     */
    private long versionTime;

    /**
     * 是否快速失败
     */
    private boolean fastfail;

    /**
     * 
     * @param key
     * @param timeout
     */
    public RedisDistributedLock(String key, int timeout) {
        this(key, timeout * 1000, timeout, true);
    }

    /**
     * 
     * @param key
     * @param heartbeatTime 必须满足(heartbeatTime <= timeout*1000)
     * @param timeout
     */
    public RedisDistributedLock(String key, long heartbeatTime, int timeout) {
        this(key, heartbeatTime, timeout, (heartbeatTime == timeout * 1000 ? true : false));
    }

    /**
     * 
     * @param key
     * @param heartbeatTime 必须满足(heartbeatTime <= timeout*1000)
     * @param timeout
     * @param fastfail 快速失败只在heartbeatTime == timeout * 1000时才有意义
     */
    public RedisDistributedLock(String key, long heartbeatTime, int timeout, boolean fastfail) {
        Preconditions.checkArgument(heartbeatTime <= timeout * 1000,
                "info:heartbeatTime 必须满足(heartbeatTime <= timeout*1000) ");
        this.key = key;
        this.heartbeatTime = heartbeatTime;
        this.timeout = timeout;
        this.fastfail = fastfail;
    }

    /**
     * 获取锁
     * <p>
     * 1.通过timeout控制持有锁对象失去响应导致的最终超时
     * <p>
     * 2.通过heartbeatTime中存放的时间戳内容控制锁的释放，只能释放自己的锁,同时也可以作为锁的心跳检测
     * 
     * @return
     */
    protected boolean lock() {
        // 1. 通过setnx试图获取一个lock,setnx成功，则成功获取一个锁
        boolean setnx = jedisManager.setnx(key, buildVal(), timeout);
        // 2.快速失败，锁获取成功返回true,锁获取失败并且快速失败的为true则直接返回false
        if (setnx || fastfail) {
            return setnx ? true : false;
        }
        // 3.setnx失败，说明锁仍然被其他对象保持，检查其是否已经超时，未超时，则直接返回失败
        long oldValue = getLong(jedisManager.get(key));
        if (oldValue > System.currentTimeMillis()) {
            return false;
        }
        // 4.已经超时,则获取锁
        long getSetValue = getLong(jedisManager.getSet(key, buildVal()));
        // 5.key在当前方法执行过程中失效(即oldValue返回了0或者getSetValue返回了0)，故可再进行一次竞争
        if (getSetValue == 0) {
            // 6.true代表竞争成功，false则已被其他进程获取
            return jedisManager.setnx(key, buildVal(), timeout);
        }
        // 7.已被其他进程获取(已被其他进程通过getset设置新值)
        if (getSetValue != oldValue) {
            return false;
        }
        // 8.获取成功，续租过期时间
        if (jedisManager.expire(key, timeout)) {
            return true;
        }
        // 9.续租失败,key可能失效了，再获取一次
        return jedisManager.setnx(key, buildVal(), timeout);
    }

    /**
     * 检查所是否有效,锁是否超时or锁是否已被其他进程重新获取
     * 
     * @return
     */
    public boolean check() {
        long getVal = getLong(jedisManager.get(key));
        return System.currentTimeMillis() < getVal && versionTime == getVal;
    }

    /**
     * 维持心跳，仅在heartbeatTime < timeout时需要
     * <p>
     * 如果heartbeatTime == timeout，此操作是没有意义的
     * 
     * @return
     */
    @Override
    public boolean heartbeat() {
        // 1. 避免操作非自己获取得到的锁
        return check() && getLong(jedisManager.getSet(key, buildVal())) != 0;
    }

    /**
     * 释放锁
     */
    public boolean unLock() {
        // 1. 避免删除非自己获取得到的锁
        return check() && jedisManager.del(key);
    }

    /**
     * if value==null || value=="" <br>
     * &nbsp return 0 <br>
     * else <br>
     * &nbsp return Long.valueOf(value)
     * 
     * @param value
     * @return
     */
    private long getLong(String value) {
        return StringUtils.isBlank(value) ? 0 : Long.valueOf(value);
    }

    /**
     * 生成val,当前系统时间+心跳时间
     * 
     * @return System.currentTimeMillis() + heartbeatTime + 1
     */
    private String buildVal() {
        versionTime = System.currentTimeMillis() + heartbeatTime + 1;
        return String.valueOf(versionTime);
    }
}
