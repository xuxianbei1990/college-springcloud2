package io.seata.sample.controller;

import io.seata.sample.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    /**
     * 购买下单，模拟全局事务提交
     *
     * @return
     */
    @RequestMapping(value = "/purchase/commit", produces = "application/json")
    public String purchaseCommit() {
//        throw new RuntimeException("test");
        try {
            businessService.purchase("U100000", "C100000", 30);
        } catch (Exception exx) {
            return exx.getMessage();
        }
        return "全局事务提交";
    }

    /**
     * 购买下单，模拟全局多个事务提交
     *
     * @return
     */
    @RequestMapping(value = "/purchase/Multi", produces = "application/json")
    public String purchaseCommitMulti() {
//        throw new RuntimeException("test");
        try {
            for (int i = 0; i < 30; i++)
                businessService.purchaseMulti();
        } catch (Exception exx) {
            return exx.getMessage();
        }
        return "全局事务提交1";
    }

    @RequestMapping(value = "/purchase/Multi2", produces = "application/json")
    public String purchaseCommitMulti2() {
//        throw new RuntimeException("test");
        try {
            for (int i = 0; i < 30; i++)
                businessService.purchaseMulti2();
        } catch (Exception exx) {
            return exx.getMessage();
        }
        return "全局事务提交2";
    }

    /**
     * 购买下单，模拟全局事务回滚
     * 账户或库存不足
     *
     * @return
     */
    @RequestMapping("/purchase/rollback")
    public String purchaseRollback() {
        try {
            businessService.purchase("U100000", "C100000", 99999);
        } catch (Exception exx) {
            return exx.getMessage();
        }
        return "全局事务提交";
    }

    /**
     * seata多次更新
     * 账户或库存不足
     *
     * @return
     */
    @RequestMapping("/multi/update")
    public String multiUpdate() {
        try {
            businessService.multiUpdate();
        } catch (Exception exx) {
            return exx.getMessage();
        }
        return "全局事务提交";
    }


    /**
     * 场景：A事务发起。暂停，B事务启动，执行完毕。接着执行A事务。
     * 结论： 这种情况A事务执行成功
     *
     * @return
     */
    @RequestMapping("/multi/update/a")
    public String multiUpdateA() {
        for (int i = 0; i < 30; i++)
            try {
                businessService.multiUpdateA();
            } catch (Exception exx) {
                return exx.getMessage();
            }
        return "全局事务提交";
    }

    /**
     * 场景：A事务发起。暂停，B事务启动，执行完毕。接着执行A事务。
     * 结论：这种情况A事务执行成功
     *
     * @return
     */
    @RequestMapping("/multi/update/b")
    public String multiUpdateB() {
        for (int i = 0; i < 30; i++)
            try {
                businessService.multiUpdateB();
            } catch (Exception exx) {
                return exx.getMessage();
            }
        return "全局事务提交";
    }


    /**
     * seata多次更新 失败滚回测试
     * 账户或库存不足
     *
     * @return
     */
    @GetMapping("/multi/update/rollback")
    public String multiUpdateRollback(Integer error) {
        try {
            businessService.multiUpdateRollback(error);
        } catch (Exception exx) {
            return exx.getMessage();
        }
        return "全局事务提交";
    }


    /**
     * 线程安全
     *
     * @return
     */
    @GetMapping("/thread/safe")
    public String threadSafe() {
        return businessService.threadSafe();
    }

    /**
     * 作用域安全
     *
     * @return
     */
    @GetMapping("/scope/safe")
    public String scopeSafe() {
        return businessService.scopeSafe();
    }


    /**
     * seata 和其他事务一起使用
     *
     * 结论： 第一个结论如果都是成功的话，那么大家都没有问题
     *        第二个结论如果seata发生回滚，那么将一直无法回滚。然后seata无限重试，导致失败。
     */
    @GetMapping("/native")
    public String nativeTransaction(Integer except) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        new Thread(()->{
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 10; i++) {
                businessService.nativeTransaction(i);
            }
        }).start();

        new Thread(()->{
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            for (int i = 10; i < 20; i++) {
                businessService.seataTransaction(i, except);
            }
        }).start();


        return "success";
    }
}