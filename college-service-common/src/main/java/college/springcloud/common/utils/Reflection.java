package college.springcloud.common.utils;

import tk.mybatis.mapper.weekend.Fn;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * User: xuxianbei
 * Date: 2019/8/28
 * Time: 19:19
 * Version:V1.0
 */
public class Reflection {
    private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
    private static final Pattern IS_PATTERN = Pattern.compile("^is[A-Z].*");

    public static <A, B> String[] fnToFieldName(Fn... fns) {
        String[] arr = new String[fns.length];

        for(int i = 0; i < fns.length; ++i) {
            arr[i] = fnToFieldName(fns[i]);
        }

        return arr;
    }

    //下面的方法就是为了拿到传入方法的去掉get，is的名称
    public static <A, B> String fnToFieldName(Fn<A, B> fn) {
        try {
            SerializedLambda serializedLambda = getSerializedLambda(fn);
            String getter = serializedLambda.getImplMethodName();
            if (GET_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(3);
            } else if (IS_PATTERN.matcher(getter).matches()) {
                getter = getter.substring(2);
            }
            return Introspector.decapitalize(getter);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static SerializedLambda getSerializedLambda(Fn fn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = fn.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        return (SerializedLambda)method.invoke(fn);
    }
}
