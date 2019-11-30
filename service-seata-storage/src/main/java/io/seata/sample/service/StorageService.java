package io.seata.sample.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class StorageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deduct(String commodityCode, int count) {
        jdbcTemplate.update("update storage_tbl set count = count - ? where commodity_code = ?",
                new Object[]{count, commodityCode});
    }

    public String query(String commodityCode) {
        Map<String, Object> accountMap = jdbcTemplate.queryForMap("select * from storage_tbl where commodity_code='C100001'");
        return JSONObject.toJSONString(accountMap);
    }

    public void update(String commodityCode, Integer count) {
        jdbcTemplate.update("update storage_tbl set count = ? where commodity_code = ?",
                new Object[]{count, commodityCode});
    }
}
