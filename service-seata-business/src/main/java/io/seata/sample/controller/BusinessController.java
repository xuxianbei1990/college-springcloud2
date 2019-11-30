package io.seata.sample.controller;

import io.seata.sample.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
