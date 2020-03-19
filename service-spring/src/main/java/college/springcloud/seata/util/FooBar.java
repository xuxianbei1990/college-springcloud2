package college.springcloud.seata.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: xuxianbei
 * Date: 2020/1/21
 * Time: 17:26
 * Version:V1.0
 */
public class FooBar {
    private int n;
    ReentrantLock reentrantLock = new ReentrantLock();
    Condition condition1 = reentrantLock.newCondition();
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (atomicBoolean.compareAndSet(false, true)) {
                printFoo.run();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        synchronized (this) {
            for (int i = 0; i < n; i++) {
                this.wait();
                // printBar.run() outputs "bar". Do not change or remove this line.
                while (atomicBoolean.get() != true) {
                    Thread.yield();
                }
                atomicBoolean.set(false);
                printBar.run();
                this.notify();
            }
        }

    }
}
