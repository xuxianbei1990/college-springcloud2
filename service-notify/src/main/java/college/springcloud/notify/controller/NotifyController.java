package college.springcloud.notify.controller;

import college.springcloud.notify.mapper.OrderAftersaleMapper;
import college.springcloud.notify.mapper.UserAccountMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * User: xuxianbei
 * Date: 2019/11/30
 * Time: 11:57
 * Version:V1.0
 */
@RestController
@RequestMapping("/notify")
public class NotifyController {

    @Resource
    UserAccountMapper userAccountMapper;

    @Resource
    OrderAftersaleMapper orderAftersaleMapper;

    @GetMapping("/order")
    public Object selectOrderAfterSale() {
        return orderAftersaleMapper.selectByPrimaryKey("S5719910742575908");
    }

    @GetMapping("/account")
    public Object selectUserAccount() {
        return userAccountMapper.selectByPrimaryKey(1);
    }
}
