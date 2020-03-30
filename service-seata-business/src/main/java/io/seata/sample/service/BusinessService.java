package io.seata.sample.service;

import com.alibaba.fastjson.JSONObject;
import io.seata.sample.feign.OrderFeignClient;
import io.seata.sample.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class BusinessService {

    @Autowired
    private StorageThreadSafe storageThreadSafe;

    @Resource
    private StorageFeignClient storageFeignClient;

    @Resource
    private OrderFeignClient orderFeignClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    GlobalTransactionalTest globalTransactionalTest;

    /**
     * 减库存，下订单
     *
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int orderCount) {
        storageFeignClient.deduct(commodityCode, orderCount);
        orderFeignClient.create(userId, commodityCode, orderCount);
        if (!validData()) {
            throw new RuntimeException("账户或库存不足,执行回滚");
        }
    }

    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from account_tbl");
        jdbcTemplate.update("delete from order_tbl");
        jdbcTemplate.update("delete from storage_tbl");
        jdbcTemplate.update("insert into account_tbl(user_id,money) values('U100000','10000') ");
        jdbcTemplate.update("insert into storage_tbl(commodity_code,count) values('C100000','200') ");
        jdbcTemplate.update("insert into storage_tbl(commodity_code,count) values('C100001','200') ");
    }

    public boolean validData() {
        Map accountMap = jdbcTemplate.queryForMap("select * from account_tbl where user_id='U100000'");
        if (Integer.parseInt(accountMap.get("money").toString()) < 0) {
            return false;
        }
        Map storageMap = jdbcTemplate.queryForMap("select * from storage_tbl where commodity_code='C100000'");
        if (Integer.parseInt(storageMap.get("count").toString()) < 0) {
            return false;
        }
        return true;
    }

    /**
     * 第一问题：第一个数据库发生更新操作。第二库操作依赖第一个库的更新。这时候seata怎么处理？
     * 第二问题：如果两个程序都去执行多个@GlobalTransactional 那么获取当前tx；会不会异常？ 不会
     */
    @GlobalTransactional
    public void purchaseMulti() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        storageFeignClient.update("C100001", 10);
        String result = globalTransactionalTest.query();
        JSONObject resulMap = JSONObject.parseObject(result);
        Assert.isTrue((Integer) resulMap.get("count") == 10, "xxx");
    }

    @GlobalTransactional
    public void purchaseMulti2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        storageFeignClient.update("C100001", 20);
        String result = globalTransactionalTest.query();
        JSONObject resulMap = JSONObject.parseObject(result);
        Assert.isTrue((Integer) resulMap.get("count") == 20, "xxx");
    }

    @GlobalTransactional
    public void multiUpdate() {
        storageFeignClient.update("C100001", 20);
        storageFeignClient.update("C100001", 30);
        storageFeignClient.update("C100001", 40);
    }

    @GlobalTransactional
    public void multiUpdateA() {
        storageFeignClient.update("C100001", 20);
    }

    @GlobalTransactional
    public void multiUpdateB() {
        storageFeignClient.update("C100001", 10);
    }

    /**
     * 证明：在多线程，子线程环境下，如果子线程报错，seata回滚，则证明seata支持多线程环境
     * 结论：多线下，如果生效seata 需要@Async 下面添加 @GlobalTransactional
     *
     * @return
     */
    @GlobalTransactional
    public String threadSafe() {
        String value = storageFeignClient.query("C100001");
        orderFeignClient.create("xuxianbei", "test", 10);
        Future<Integer> future = storageThreadSafe.deductThread(1);
        try {
            Assert.isTrue(future.get() == 1, "123");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    /**
     * 结论：seata 的作用域是当前方法，且当前包含所有的方法引用，也就是说最外层加一个就可以了
     */
    @GlobalTransactional
    public String scopeSafe() {
        String value = storageFeignClient.query("C100001");
        orderFeignClient.create("xuxianbei", "test", 20);
        storageThreadSafe.deductScope(1);
        return value;
    }

    @GlobalTransactional
    public void multiUpdateRollback(Integer error) {
        storageFeignClient.update("C100001", 20);
        storageFeignClient.update("C100001", 30);
        if (error == 2) {
            throw new RuntimeException();
        }
        storageFeignClient.update("C100001", 40);
    }


    @Transactional
    public String nativeTransaction(int count) {
        storageFeignClient.update("C100001", count);
        return null;
    }

    @GlobalTransactional
    public void seataTransaction(int i, int except) {
        storageFeignClient.update("C100001", i);
        if (except == 1 && Math.floorMod(i, 2) == 1) {
            throw new RuntimeException("ddfd");
        }
    }
}
