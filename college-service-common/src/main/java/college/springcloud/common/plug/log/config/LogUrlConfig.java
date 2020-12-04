package college.springcloud.common.plug.log.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 把controller的类拿了过来
 * @author: xuxianbei
 * Date: 2020/11/30
 * Time: 11:46
 * Version:V1.0
 */
@Lazy
public class LogUrlConfig implements ApplicationContextAware {

    //兼容多数据源延迟加载问题
    private RequestMappingHandlerMapping handlerMapping;
    private ApplicationContext context;

    private RequestMappingHandlerMapping getHandlerMapping() {
        if (handlerMapping == null) {
            handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        }

        return handlerMapping;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     * 获取所有的请求对应的文件
     * 实际是拿到所有Controller
     * @return
     */
    public Map<String, String> getAllUrlFile() throws IOException {
        RequestMappingHandlerMapping handlerMapping = getHandlerMapping();
        if (handlerMapping == null) {
            return new HashMap<>();
        }
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        Map<String, String> urlNumberMap = new HashMap<>();

        Collection<HandlerMethod> values = handlerMethods.values();

        Set<String> existMappings = new HashSet<>();
        for (HandlerMethod handlerMethod : values) {
            existMappings.add(handlerMethod.getBeanType().getCanonicalName().replaceAll("\\.", "/") + ".java");
        }

        Map<String, Integer> allFileLine = getAllFileLine(existMappings);

        // 替换查询
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            if (entry.getKey().getPatternsCondition().isEmpty()) {
                continue;
            }

            String method = entry.getValue().getMethod().toString().replaceAll("\\(.*", "");
            Integer num = allFileLine.get(method);
            if (num == null) {
                continue;
            }

            for (String url : entry.getKey().getPatternsCondition().getPatterns()) {
                urlNumberMap.put(url, entry.getValue().getBeanType().toString() + "("
                        + entry.getValue().getBeanType().getSimpleName() + ".java:" + num + ")");
            }
        }
        return urlNumberMap;
    }


    private Map<String, Integer> getAllFileLine(Set<String> existMappings) throws IOException {

        Set<Path> currentAllPath = getCurrentAllPath(Paths.get("."), new HashSet());
        if (currentAllPath.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Integer> allFileLine = new HashMap<>();
        for (Path path : currentAllPath) {
            boolean isFileExistFlag = false;
            if (existMappings.isEmpty()) {
                isFileExistFlag = true;
            } else {

                for (String filePath : existMappings) {
                    if (path.toFile().getAbsolutePath().endsWith(filePath)) {
                        isFileExistFlag = true;
                        break;
                    }
                }

            }

            if (isFileExistFlag) {
                allFileLine.putAll(getMethodNumByFile(path));
            }

        }
        return allFileLine;

    }

    /**
     * 获取当前路径的所有的java文件
     *
     * @param path    当前路径
     * @param allPath 所有的路径
     * @return
     */
    private Set<Path> getCurrentAllPath(Path path, Set<Path> allPath) {

        File file = path.toFile();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    getCurrentAllPath(f.toPath(), allPath);
                } else if (f.getName().endsWith(".java")) {
                    allPath.add(f.toPath());
                }
            }

        } else if (file.getName().endsWith(".java")) {
            allPath.add(file.toPath());
        }

        return allPath;

    }

    /**
     * 根据文件获取方法的行数
     * 最终找到controller的行数
     *
     * @param path 当前文件路径
     */
    private Map<String, Integer> getMethodNumByFile(Path path) throws IOException {
        List<String> strings = Files.readAllLines(path);
        // 加载类的导入配置
        Map<String, String> importMap = new HashMap<>();
        Map<String, Integer> methodLineRecordMap = new HashMap<>();

        Pattern classPattern = Pattern.compile("(\\s+)?public\\s+class\\s+([\\w.]+)(\\s+)?([\\s\\w]+)?\\{");
        Pattern importPattern = Pattern.compile("(\\s+)?import\\s+([\\w.]+(\\s+)?);");
        Pattern methodPattern = Pattern.compile("(\\s+)?public\\s+([\\w.]+)\\s+([\\w.]+)\\(");
        Pattern packagePattern = Pattern.compile("(\\s+)?package\\s+([\\w.\\s]+)(\\s+)?;");
        Pattern controllerPattern = Pattern.compile("(\\s+)?(@RestController|@Controller|@Service)(\\s+)?");

        boolean isClassEndFlag = false;

        String lastLine = "";
        String packageName = "";
        String className = "";

        int lineNum = 0;
        boolean isControllerFileFlag = false;
        for (String line : strings) {
            lineNum++;
            //判断是否controller
            if (controllerPattern.matcher(line).find()) {
                isControllerFileFlag = true;
            }

            //找类名
            if (!isClassEndFlag) {
                Matcher importMatcher = importPattern.matcher(line);
                Matcher packageNameMatcher = packagePattern.matcher(line);
                Matcher classMatcher = classPattern.matcher(line);
                if (importMatcher.find()) {
                    importMap.put(importMatcher.group(2).substring(importMatcher.group(2).lastIndexOf(".") + 1).trim(),
                            importMatcher.group(2));

                } else if (classMatcher.find()) {
                    isClassEndFlag = true;
                    className = packageName + "." + classMatcher.group(2);

                } else if (packageNameMatcher.find()) {
                    packageName = packageNameMatcher.group(2);
                }
            } else {
                Matcher methodMatcher = methodPattern.matcher(line);

                if (methodMatcher.find()) {
                    String methodName = methodMatcher.group(3);
                    String returnType = getClassTypeByName(methodMatcher.group(2), importMap);
                    methodLineRecordMap.put("public " + returnType.replaceAll("\\s+", "") + " "
                            + className.replaceAll("\\s+", "") + "." + methodName.replaceAll("\\s+", ""), lineNum);

                } else {
                    // 增加梯度判断
                    if (line.trim().startsWith("public") || !lastLine.isEmpty()) {
                        lastLine += line;
                        methodMatcher = methodPattern.matcher(lastLine);
                        if (methodMatcher.find()) {
                            String methodName = methodMatcher.group(3);
                            String returnType = getClassTypeByName(methodMatcher.group(2), importMap);
                            methodLineRecordMap.put("public " + returnType.replaceAll("\\s+", "") + " "
                                    + className.replaceAll("\\s+", "") + "." + methodName.replaceAll("\\s+", ""), lineNum);

                            lastLine = "";

                        }
                    }

                }
            }

        }
        // 增加是否是controller类排除
        if (!isControllerFileFlag) {
            methodLineRecordMap.clear();
        }
        return methodLineRecordMap;
    }


    /**
     * 根据名称获取类的完整路径
     *
     * @param name      类的名字
     * @param importMap 导包路径
     * @return
     */
    private String getClassTypeByName(String name, Map<String, String> importMap) {
        String returnType;
        if ("void".equals(name)) {
            returnType = "void";
        } else if (name.contains(".")) {
            returnType = name;
        } else {
            returnType = importMap.get(name);
            if (returnType == null) {
                returnType = "java.lang." + name;
                try {
                    Class.forName(returnType);

                } catch (ClassNotFoundException e) {
                    returnType = name;
                }

            }
        }
        return returnType;
    }
}
