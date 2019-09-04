package college.springcloud.student.fallback;

import college.springcloud.common.utils.Result;
import college.springcloud.student.api.StudentApi;
import college.springcloud.student.enums.StudentEnum;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 14:32
 * Version:V1.0
 */
public class StudentApiFallBack implements StudentApi {
    @Override
    public Result get() {
        return Result.failure(StudentEnum.STUDENT_FALLBACK);
    }

    @Override
    public Result insert(String param) {
        return null;
    }
}
