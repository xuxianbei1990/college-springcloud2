package college.springcloud.order.controller;

import college.springcloud.common.rpc.BaseApi;
import college.springcloud.order.po.UndoLog;
import college.springcloud.order.service.UndoLogService;
import io.seata.common.Constants;
import io.seata.rm.datasource.undo.BranchUndoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/undoLog")
public class UndoLogController extends BaseApi<UndoLog> {

    @Autowired
    UndoLogService undoLogService;

    @GetMapping("/decodeUndoLogRollbackInfo")
    public String decodeUndoLogRollbackInfo() {
        List<UndoLog> logs = undoLogService.queryAll();
        logs.forEach(t -> {
            BranchUndoLog branchUndoLog;
//            ObjectMapper MAPPER = new ObjectMapper();
//            try {
//                branchUndoLog = MAPPER.readValue(t.getRollbackInfo(), BranchUndoLog.class);
//                if (branchUndoLog != null)
//                System.out.println(branchUndoLog.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            System.out.println(new String(t.getRollbackInfo(), Constants.DEFAULT_CHARSET));
            System.out.println("===========");
        });

        return "success";
    }

}