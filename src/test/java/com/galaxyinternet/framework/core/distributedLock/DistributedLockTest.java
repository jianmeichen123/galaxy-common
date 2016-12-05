package com.galaxyinternet.framework.core.distributedLock;

/**
 * Created by zhaoying on 2016/12/5.
 */
public class DistributedLockTest {

    public static void main(String[] args) throws Exception {

        /**
         * 构建锁对象
         *
         * key：lock-key-1
         * 心跳时间：10 * 1000
         * timeout：60
         */
        DistributedLock lock = new RedisDistributedLock("lock-key-1", 10 * 1000, 60);
        // 获取锁成功，执行业务
        if (lock.tryLock()) {
            // 业务逻辑
            for (int i = 0; i < 10; i++) {
                // 业务代码,耗时五秒
                Thread.sleep(5 * 1000);
                // 心跳一次
                if (lock.heartbeat()) {
                    // 心跳成功
                } else {
                    // 当前业务超时，心跳已失效，锁可能会被其他进程获取
                }
            }
            // 业务执行完毕 解锁
            lock.unLock();
        }

    }
}
