package college.springcloud.common.utils.pageinfo;


import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.export.template.ExcelExportOfTemplateUtil;
import college.springcloud.common.rpc.ApiRemoteService;
import college.springcloud.common.utils.PagerDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import feign.Request;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 分页工具
 * 主要处理分页使用的一切信息和统一封装
 *
 * @author: xuxianbei
 * Date: 2020/8/31
 * Time: 14:49
 * Version:V1.0
 */
@Component
@Slf4j
public class PageInfoUtil {

    @Autowired
    @Qualifier("sqlThreadPool")
    private AsyncTaskExecutor sqlThreadPool;

    @Autowired
    @Qualifier("rpcThreadPool")
    private AsyncTaskExecutor rpcThreadPool;

    //    @Autowired
    private ApiRemoteService apiRemoteService;

    private final static Pattern NUMBER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");

    private final static Pattern DAY_DATE_PATTERN = Pattern.compile("^\\d{4}\\/\\d{1,2}\\/\\d{1,2}");

    private final static Pattern MONTH_DATE_PATTERN = Pattern.compile("^\\d{4}\\/\\d{1,2}");


    /**
     * 类型转换
     *
     * @param totalList 从数据库中拿到的list
     * @param volist    返回界面的list
     * @param <T>
     * @return
     */
    public static <T> PageInfo<T> toPageInfo(List<?> totalList, List<T> volist) {
        PageInfo<T> resultPageInfo = new PageInfo(volist);
        PageInfo old = new PageInfo<>(totalList);
        BeanUtils.copyProperties(old, resultPageInfo);
        resultPageInfo.setList(volist);
        resultPageInfo.setTotal(old.getTotal());
        //兼容前端分页问题
        if (resultPageInfo.getPageNum() == 0) {
            resultPageInfo.setPageNum(1);
        }
        return resultPageInfo;
    }

    /**
     * 类型转换
     *
     * @param old    从数据库中拿到的PageInfo
     * @param volist 返回界面的list
     * @param <T>
     * @return
     */
    public static <T> PageInfo<T> toPageInfo(PageInfo<?> old, List<T> volist) {
        PageInfo<T> resultPageInfo = new PageInfo(volist);
        BeanUtils.copyProperties(old, resultPageInfo);
        resultPageInfo.setList(volist);
        if (CollectionUtils.isEmpty(volist)) {
            resultPageInfo.setTotal(0);
        } else {
            resultPageInfo.setTotal(old.getTotal());
        }
        return resultPageInfo;
    }

    public static <T> PageInfo<T> emptyPageInfo() {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setList(new ArrayList<>());
        //兼容前端分页
        pageInfo.setPageNum(1);
        return pageInfo;
    }

    /**
     * 封装分页
     *
     * @param pagerDTO
     */
    public static void startPage(PagerDTO pagerDTO) {
        PageHelper.startPage(pagerDTO.getPageNum(), pagerDTO.getPageSize());
    }

    @Data
    public static class ConcurrentCallable<T> {
        /**
         * 类
         * 用于类型转换和判断
         */
        private Class aClass;
        /**
         * 列表数据
         */
        private List<T> date;
        /**
         * 附加
         */
        private Object dateEx;
    }


    /**
     * 在任务获取之前执行
     * 由主线程执行：
     */
    @FunctionalInterface
    public interface BeforeTaskGet {

        /**
         * 接受
         */
        void accept();
    }

    /**
     * 并发执行sql任务，
     * 不可以中断, 也不可以查看进度
     * 目前用于并发查询数据库
     * beforeTaskGet 由主线程执行
     * 注意：会增加数据库压力
     *
     * @param callables
     * @return
     */
    public final List<ConcurrentCallable> concurrentSql(List<Callable<ConcurrentCallable>> callables, BeforeTaskGet beforeTaskGet) {
        List<Future<ConcurrentCallable>> futures =
                callables.stream().map(task -> sqlThreadPool.submit(task)).collect(Collectors.toList());

        //主线程执行
        if (Objects.nonNull(beforeTaskGet)) {
            beforeTaskGet.accept();
        }

        List<ConcurrentCallable> list = futures.stream().map(task -> {
            try {
                return task.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return list;
    }


    /**
     * 并发执行sql任务，
     * 不可以中断, 也不可以查看进度
     * 目前用于并发查询数据库
     * 注意：会增加数据库压力
     *
     * @param callables
     * @return
     */
    public List<ConcurrentCallable> concurrentSql(List<Callable<ConcurrentCallable>> callables) {
        return concurrentSql(callables, null);
    }


    @FunctionalInterface
    public interface SupplierBytes {

        /**
         * 返回数组对象
         *
         * @param exception
         * @return
         */
        byte[] get(Exception exception);
    }

    /**
     * 获取图片byte[]
     * 指定返回目标信息
     *
     * @param imgName
     * @return
     */
    public byte[] getSingleImage(String imgName, SupplierBytes supplier) {
        try {
            feign.Response download = apiRemoteService.views(imgName);
            InputStream imgStream = download.body().asInputStream();
            return FileCopyUtils.copyToByteArray(imgStream);
        } catch (Exception e) {
            return supplier.get(e);
        }
    }

    /**
     * 获取图片byte[] 没有直接返回空
     *
     * @param imgName
     * @return
     */
    public byte[] getSingleImageDefaultEmpty(String imgName) {
        return getSingleImage(imgName, (exception) -> null);
    }


    /**
     * 获取图片byte[]s 没有直接返回空
     *
     * @param imgNames
     * @return
     * @see #concurrentGetImage
     */
    public Map<String, byte[]> getSingleImageDefaultEmptys(List<String> imgNames) {
        return concurrentGetImage(imgNames, (exception) -> null);
    }

    /**
     * 获取图片byte[]s 没有直接返回空
     * 在原有基础上支持了Lambda
     *
     * @param list
     * @param mapper
     * @param <T>
     * @return
     * @see #getSingleImageDefaultEmptys
     */
    public <T> Map<String, byte[]> getSingleImageDefaultEmptysLambda(List<T> list, Function<? super T, String> mapper) {
        return concurrentGetImage(list.stream().map(mapper).collect(Collectors.toList()), (exception) -> null);
    }


    /**
     * 并发获取图片, 如果没有取指定内容, 默认超时2秒, 如果超过5次获取失败就直接给指定内容
     * 注意：通过rpc获取，因为使用了线程超时机制，所以可以无视全局配置的rpc获取超时问题
     *
     * @param imgNames
     * @return key: imgName value: 图片的byte[]
     */
    public Map<String, byte[]> concurrentGetImage(List<String> imgNames, SupplierBytes supplier) {
        final int maxTimeOutCount = 5;
        final int defaultTimeOver = 5;
        Map<String, CompletableFuture<byte[]>> mapFutures = imgNames.stream().filter(t -> !StringUtils.isEmpty(t)).distinct().collect(Collectors.toMap(key -> key,
                key -> CompletableFuture.supplyAsync(() -> getSingleImage(key, supplier), rpcThreadPool)));
        AtomicInteger timeOuts = new AtomicInteger();
        Map<String, byte[]> result = new HashMap<>(8);
        mapFutures.entrySet().forEach(entry -> {
            byte[] value;
            try {
                if (timeOuts.get() >= maxTimeOutCount) {
                    value = supplier.get(new RuntimeException(String.format("超过%d次获取图片失败", maxTimeOutCount)));
                } else {
                    value = entry.getValue().get(defaultTimeOver, TimeUnit.SECONDS);
                }
                //处理图片服务器脏数据
                if (Objects.nonNull(value) && value.length == 0) {
                    value = null;
                }
            } catch (Exception e) {
                timeOuts.incrementAndGet();
                value = supplier.get(e);
            }
            result.put(entry.getKey(), value);
        });
        return result;
    }


    public <T> T testCustomApi() {
        String imgName = "xxx.jpg";
        return customApi(ApiRemoteService.class, apiRemoteService, "views", imgName);
    }

    /**
     * 判断是否数字
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        return NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 判断是否日期：格式为yyyy/mm/dd
     */
    public static boolean isDayDate(String input) {
        return DAY_DATE_PATTERN.matcher(input).matches();
    }

    public static boolean isMonthDate(String input) {
        return MONTH_DATE_PATTERN.matcher(input).matches();
    }

    /**
     * 动态配置rpc的超时时间
     * 半成品
     *
     * @param interfaceObj
     */
    public <T> T customApi(Class clazz, Object interfaceObj, String methodName, Object... args) {
        Field field = ReflectionUtils.findField(Proxy.class, "h");
        field.setAccessible(true);
        InvocationHandler h = (InvocationHandler) ReflectionUtils.getField(field, interfaceObj);
        Method method = ReflectionUtils.findMethod(clazz, methodName, objectToClass(args));
        Request.Options options = new Request.Options(5000, 5000, true);
        List<Object> objects = new ArrayList();
        objects.addAll(Arrays.asList(args));
        objects.add(options);
        try {
            return (T) h.invoke(interfaceObj, method, objects.toArray());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static Workbook exportExcel(TemplateExportParams params, Map<String, Object> map) {
        return new ExcelExportOfTemplateUtil().createExcelByTemplate(params, null, null, map);
    }

    private Class[] objectToClass(Object[] args) {
        List<Object> list = Arrays.asList(args);
        Class[] result = new Class[list.size()];
        result = list.stream().map(o -> o.getClass()).collect(Collectors.toList()).toArray(result);
        return result;
    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(String.valueOf(key), beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 基础属性拷贝
     *
     * @param oldBase
     * @param newBase
     */
    public static void copyPropertiesBaseInfo(AuthorityInfo oldBase, AuthorityInfo newBase) {
        if (Objects.nonNull(oldBase)) {
            newBase.setCreateBy(oldBase.getCreateBy());
            newBase.setCreateDate(oldBase.getCreateDate());
            newBase.setCreateName(oldBase.getCreateName());
            newBase.setUpdateBy(oldBase.getUpdateBy());
            newBase.setUpdateName(oldBase.getUpdateName());
            newBase.setUpdateDate(oldBase.getUpdateDate());
        }
    }

}
