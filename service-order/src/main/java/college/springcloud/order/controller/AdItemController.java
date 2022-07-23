package college.springcloud.order.controller;

import college.springcloud.common.utils.Result;
import college.springcloud.order.model.AdItems;
import college.springcloud.order.service.impl.AdItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: xuxianbei
 * Date: 2022/4/8
 * Time: 16:39
 * Version:V1.0
 */
@RestController
@RequestMapping("order/ad")
public class AdItemController {

    @Autowired
    private AdItemServiceImpl adItemService;


    @PostMapping("insert")
    public Result<Integer> insert(){
       return Result.success(adItemService.insert());
    }

    @GetMapping("get")
    public Result<AdItems> get(@RequestParam Integer id){
        return Result.success(adItemService.get(id));
    }
}
