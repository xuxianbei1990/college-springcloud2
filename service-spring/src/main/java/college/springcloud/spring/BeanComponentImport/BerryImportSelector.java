package college.springcloud.spring.BeanComponentImport;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * User: xuxianbei
 * Date: 2019/11/5
 * Time: 16:03
 * Version:V1.0
 */
public class BerryImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"college.springcloud.spring.BeanComponentImport.Berry"};
    }
}
