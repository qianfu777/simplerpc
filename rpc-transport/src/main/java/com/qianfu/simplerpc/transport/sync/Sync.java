package com.qianfu.simplerpc.transport.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author Fu
 * @date 2018/6/12
 */
@Slf4j
public class Sync {
    private CountDownLatch countDownLatch;
    
    public Sync() {
        countDownLatch = new CountDownLatch(1);
    }
    
    public void lock() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public void unlock() {
        countDownLatch.countDown();
    }
    
    
}
