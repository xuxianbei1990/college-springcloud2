package college.springcloud.common.handler;

import college.springcloud.common.annotation.SearchAuthority;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * @author: xuxianbei
 * Date: 2021/3/2
 * Time: 15:05
 * Version:V1.0
 */
public class SearchAuthorityHandler implements HandlerMethodArgumentResolver {
    private static final Logger log = LoggerFactory.getLogger(SearchAuthorityHandler.class);
    private static final String BRAND_TYPE = "brandIds";
    private static final String MAIN_CUSTOMER_IDS = "mainCustomerIds";
    private static final String VICE_CUSTOMER_IDS = "viceCustomerIds";
    private static final String USER_IDS = "userIds";
    private String applicationName = "";
    private Map<Object, Object> stringRedisTemplate = new HashMap<>();

    public SearchAuthorityHandler(String applicationName) {
        this.applicationName = applicationName;
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SearchAuthority.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        SearchAuthority authority = (SearchAuthority)parameter.getParameterAnnotation(SearchAuthority.class);
        boolean required = authority.required();
        String token = webRequest.getHeader("Authorization");
        UserVO userVO = (UserVO) JSONObject.parseObject((String)this.stringRedisTemplate.get("userToken:" + token), UserVO.class);
        if (!required || token != null && userVO != null) {
            Long userId = userVO.getUserId();
            HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
            if (StringUtils.equals(request.getMethod(), HttpMethod.POST.name())) {
                return request.getContentType().contains("application/json") ? this.doPostJson(parameter, request, userId) : this.doGetAndPostForm(parameter, webRequest, userId);
            } else {
                return StringUtils.equals(request.getMethod(), HttpMethod.GET.name()) ? this.doGetAndPostForm(parameter, webRequest, userId) : parameter.getParameterType().newInstance();
            }
        } else {
            throw new RuntimeException("用户登录失效");
        }
    }

    private Object doGetAndPostForm(MethodParameter parameter, NativeWebRequest webRequest, Long userId) throws IllegalAccessException, InstantiationException, IOException {
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        String privilegeCode = webRequest.getHeader("privilegeCode");
        Class<?> parameterType = parameter.getParameterType();
        Map<Object, Object> map = new HashMap();
        UserPrivilegeDTO privilegeRes = (UserPrivilegeDTO)JSONObject.parseObject((String)this.stringRedisTemplate.get(userId+ ":" + privilegeCode), UserPrivilegeDTO.class);
        Boolean brandStatus = false;
        if (Objects.nonNull(privilegeRes)) {
            ObjectMapper oMapper = new ObjectMapper();
            if (CollectionUtils.isEmpty(privilegeRes.getUserIds()) && CollectionUtils.isEmpty(privilegeRes.getCompanyIds())) {
                privilegeRes.setUserIds(Arrays.asList(userId));
            }

            if (!CollectionUtils.isEmpty(privilegeRes.getBrandIds()) && privilegeRes.getDataPrivilegeCode().contains("BRAND")) {
                brandStatus = true;
            } else {
                privilegeRes.setBrandIds(Arrays.asList(-1));
            }

            map.putAll((Map)oMapper.convertValue(privilegeRes, Map.class));
        }

        Iterator var15 = parameterMap.entrySet().iterator();

        while(true) {
            Map.Entry entry;
            String[] arr;
            do {
                do {
                    if (!var15.hasNext()) {
                        String json;
                        if (this.applicationName.contains("mcn")) {
                            json = webRequest.getHeader("Authorization");
                            //这里是远程获取权限
//                            BaseCustomerIdVO baseCustomerIdVO = (BaseCustomerIdVO)RpcUtil.getObjException(this.baseInfoRemoteServer.getCustomerIdsByPrivilege(privilegeCode, json), "获取mcn客户id异常,请检查数据");
//                            map.put("mainCustomerIds", baseCustomerIdVO.getMainCustomerIds());
//                            map.put("viceCustomerIds", baseCustomerIdVO.getViceCustomerIds());
                        }

                        json = JSON.toJSONString(map);
                        return JSON.parseObject(json, parameterType, new Feature[]{Feature.OrderedField});
                    }

                    entry = (Map.Entry)var15.next();
                    arr = (String[])entry.getValue();
                } while(!ArrayUtils.isNotEmpty(arr));
            } while("".equals(arr[0]));

            String spliterator = ",";
            String join = String.join(spliterator, arr);
            if (!join.contains(spliterator) && !this.getMethodParameterIsList(parameterType, (String)entry.getKey())) {
                map.put(entry.getKey(), arr[0]);
            } else {
                map.put(entry.getKey(), join.split(spliterator));
            }

            if (brandStatus && ((String)entry.getKey()).equals("brandIds") && arr[0].contains("all")) {
                map.put(entry.getKey(), privilegeRes.getBrandIds());
            }

            if (!brandStatus && ((String)entry.getKey()).equals("brandIds")) {
                map.put(entry.getKey(), Arrays.asList(-1));
            }
        }
    }

    private boolean getMethodParameterIsList(Class<?> clazz, String fieldName) {
        try {
            Class<?> type = clazz.getDeclaredField(fieldName).getType();
            return type.equals(List.class);
        } catch (NoSuchFieldException var4) {
            if (clazz.getSuperclass() != null) {
                this.getMethodParameterIsList(clazz.getSuperclass(), fieldName);
            }

            return false;
        }
    }

    private Object doPostJson(MethodParameter parameter, HttpServletRequest request, Long userId) throws IOException {
        BufferedReader reader = request.getReader();
        String privilegeCode = request.getHeader("privilegeCode");
        Class<?> parameterType = parameter.getParameterType();
        StringBuilder sb = new StringBuilder();
        UserPrivilegeDTO privilegeRes = (UserPrivilegeDTO)JSONObject.parseObject((String)this.stringRedisTemplate.get(userId + ":" + privilegeCode), UserPrivilegeDTO.class);
        Boolean brandStatus = false;
        if (Objects.nonNull(privilegeRes)) {
            if (CollectionUtils.isEmpty(privilegeRes.getUserIds()) && CollectionUtils.isEmpty(privilegeRes.getCompanyIds())) {
                privilegeRes.setUserIds(Arrays.asList(userId));
            }

            if (!CollectionUtils.isEmpty(privilegeRes.getBrandIds()) && privilegeRes.getDataPrivilegeCode().contains("BRAND")) {
                brandStatus = true;
            } else {
                privilegeRes.setBrandIds(Arrays.asList(-1));
            }

            sb.append(JSON.toJSONString(privilegeRes));
        }

        char[] buf = new char[1024];

        int len;
        while((len = reader.read(buf)) != -1) {
            sb.append(buf, 0, len);
        }

        String reqBody = sb.toString().replace("}{", ",");
        JSONObject jsonObject = JSONObject.parseObject(reqBody);
        if (jsonObject.containsKey("brandIds")) {
            if (brandStatus && jsonObject.get("brandIds").toString().contains("all")) {
                jsonObject.put("brandIds", privilegeRes.getBrandIds());
            }

            if (!brandStatus) {
                jsonObject.put("brandIds", Arrays.asList(-1));
            }
        }

        if (this.applicationName.contains("mcn")) {
            String token = request.getHeader("Authorization");
//            BaseCustomerIdVO baseCustomerIdVO = (BaseCustomerIdVO)RpcUtil.getObjException(this.baseInfoRemoteServer.getCustomerIdsByPrivilege(privilegeCode, token), "获取mcn客户id异常,请检查数据");
//            jsonObject.put("mainCustomerIds", baseCustomerIdVO.getMainCustomerIds());
//            jsonObject.put("viceCustomerIds", baseCustomerIdVO.getViceCustomerIds());
        }

        return JSON.parseObject(jsonObject.toJSONString(), parameterType);
    }
}
