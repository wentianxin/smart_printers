package com.qg.smpt.printer.util.threads;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by tisong on 4/1/17.
 */
public class LimitLatch {

    public LimitLatch(long limit) {
        this.limit = limit;
        this.count = new AtomicLong(0);
        this.sync  = new Sync();
    }

    private class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1L;

        public Sync() {

        }

        @Override
        protected int tryAcquireShared(int ignored) {
            long newCount = count.incrementAndGet();
            if (!released && newCount > limit) {
                count.decrementAndGet();
                return -1;
            } else {
                return 1;
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            count.decrementAndGet();
            return true;
        }
    }

    private final Sync sync;

    private final AtomicLong count;

    private volatile long limit;

    private volatile boolean released = false;


    public void countUpOrAwait() throws InterruptedException {
        // 以共享模式获取对象
        sync.acquireSharedInterruptibly(1);
    }

    public long countDown() {
        sync.releaseShared(0);
        long result = getCount();
        return result;
    }

    public boolean releaseAll() {
        released = true;
        return sync.releaseShared(0);
    }

    /**
     * Resets the latch and initializes the shared acquisition counter to zero.
     * @see #releaseAll()
     */
    public void reset() {
        this.count.set(0);
        released = false;
    }

    public long getCount() {
        return count.get();
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }


}
