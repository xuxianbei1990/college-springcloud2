package college.springcloud.student.service;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import college.springcloud.student.dto.ExportVo;
import college.springcloud.student.dto.StudentDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * User: xuxianbei
 * Date: 2019/11/7
 * Time: 16:42
 * Version:V1.0
 */
@Service
public class StudentServiceImpl implements IExcelExportServer {
    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        //这里只是简单的是示例
        StudentDto studentDto = (StudentDto) queryParams;
        //本来是这里设置页码数量的
        studentDto.setCurrentPage(page);
        List<Object> list = new ArrayList<>();
        if (page >= 2) return list;
        Supplier<ExportVo> supplier = ExportVo::new;
        ExportVo exportVo = supplier.get();
        exportVo.setFsupplierOrderId(studentDto.getName());
        exportVo.setFaftersaleStatus(1);
        exportVo.setFbatchId(studentDto.getName());
        exportVo.setFcreateTime(new Date());
        list.add(exportVo);
        return list;
    }
}
