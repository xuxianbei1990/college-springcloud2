package college.springcloud.common.utils;

import college.springcloud.common.exception.BizException;
import college.springcloud.common.exception.CollegeExceptionCode;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结果工具类
 * User: xuxianbei
 * Date: 2019/8/31
 * Time: 12:14
 * Version:V1.0
 */
public class ResultUtils {

    private ResultUtils() {

    }

    /**
     * 从result中获取data
     *
     * @param result
     * @return
     */
    public static <T> T getData(Result<T> result) {
        return getData(result, CollegeExceptionCode.DATA_EXCEPTION, null);
    }

    /**
     * 从result中获取data 失败就用外面创建的
     *
     * @param result
     * @return
     */
    public static <T> T getData(Result<T> result, T t) {
        return getData(result, CollegeExceptionCode.DATA_EXCEPTION, t);
    }

    /**
     * 从result中获取data
     * 数据不存在报指定异常
     *
     * @param result
     * @return
     */
    public static <T> T getData(Result<T> result, CollegeExceptionCode optExceptionCode, T t) {
        if (!result.isSuccess()) {
            throw new BizException(CollegeExceptionCode.INTERNAL_SERVER_ERROR);
        }
        T data = result.getData();
        if (data == null) {
            if (t != null) {
                return t;
            }
            throw new BizException(optExceptionCode);
        }
        return data;
    }

    private static Map<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    //属性拷贝
    public static void copyProperties(Object source, Object dest) {
        String key = source.getClass() + ":" + dest.getClass();
        BeanCopier copier = null;
        if (beanCopierMap.containsKey(key)) {
            copier = beanCopierMap.get(key);
        } else {
            synchronized (ResultUtils.class) {
                if (!beanCopierMap.containsKey(key)) {
                    copier = BeanCopier.create(source.getClass(), dest.getClass(), true);
                    beanCopierMap.putIfAbsent(key, copier);
                }
            }
        }
        Converter converter = (o, aClass, o1) -> {
            if (aClass.equals(String.class)) {
                return String.valueOf(o);
            } else if (aClass.equals(BigDecimal.class)) {
                if (o instanceof Long) {
                    return BigDecimal.valueOf((Long) o);
                } else if (o instanceof Integer) {
                    return BigDecimal.valueOf((Integer) o);
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                return o;
            }
        };
        copier.copy(source, dest, converter);
    }
}
