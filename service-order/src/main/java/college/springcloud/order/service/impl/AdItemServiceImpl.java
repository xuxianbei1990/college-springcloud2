package college.springcloud.order.service.impl;

import college.springcloud.order.mapper.AdItemsMapper;
import college.springcloud.order.model.AdItems;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: xuxianbei
 * Date: 2022/4/8
 * Time: 16:46
 * Version:V1.0
 */
@Service
public class AdItemServiceImpl {

    @Resource
    private AdItemsMapper adItemsMapper;

    public Integer insert() {
        AdItems adItems = new AdItems();
        adItems.setName("xxb");
        adItems.setSkuId("001");
        adItems.setSort(1);
        adItems.setType(1);
        return adItemsMapper.insert(adItems);
    }

    /**
     * 因为jetcache存储的是对象，
     * @param id
     * @return
     */
    @Cached(name = "ad-items-skus", key = "#id", expire = 24000, cacheType = CacheType.REMOTE)
    @CacheRefresh(refresh = 3)
    public AdItems get(Integer id) {
        return adItemsMapper.selectByPrimaryKey(id);
    }
}
