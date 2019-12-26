package college.springcloud.common.interceptor;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/** LRU 内存淘汰
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 10:55
 * Version:V1.0
 */
public class LRUMemory<K, V> extends LinkedHashMap<K, V> {

    private int cacheSize;

    public LRUMemory() {
        this(1024);
    }

    public LRUMemory(int cacheSize) {
        super(cacheSize, 0.75f, true);
        this.cacheSize = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > this.cacheSize;
    }

    public static void main(String[] args) {
        LRUMemory lruMemory = new LRUMemory(4);
        lruMemory.put("1", "2");
        lruMemory.put("2", "3");
        lruMemory.put("3", "3");
        lruMemory.put("4", "5");
        lruMemory.get("1");
        lruMemory.put("5", "3");
        System.out.println(Arrays.toString(lruMemory.entrySet().toArray()));
    }
}
