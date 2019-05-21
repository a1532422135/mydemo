package com.example.test.threadPool;

import javassist.bytecode.stackmap.BasicBlock;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2), new MyThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i <= 200; i++) {
            Future<String> future = executorService.submit(new Callable<String>() {
                @Override
                public String call() {
                    throw new RuntimeException("aaa");
                }
            });
            try {
                System.out.println(future.get());
            } catch (Exception e) {
                log.error("exception:", e);
            } finally {
                executorService.shutdown();
            }
        }
    }

    static class MyThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public MyThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-XXXThread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
