package college.seata.spring.annotation.datasource.undo.parser;

import college.seata.spring.annotation.datasource.undo.BranchUndoLog;
import college.seata.spring.annotation.datasource.undo.UndoLogParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author: xuxianbei
 * Date: 2020/3/31
 * Time: 9:39
 * Version:V1.0
 */
@Slf4j
public class JacksonUndoLogParser implements UndoLogParser {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public String getName() {
        return null;
    }

    @Override
    public byte[] getDefaultContent() {
        return new byte[0];
    }

    @Override
    public byte[] encode(BranchUndoLog branchUndoLog) {
        return new byte[0];
    }

    @Override
    public BranchUndoLog decode(byte[] bytes) {
        try {
            BranchUndoLog branchUndoLog;
            if (Arrays.equals(bytes, getDefaultContent())) {
                branchUndoLog = new BranchUndoLog();
            } else {
                branchUndoLog = MAPPER.readValue(bytes, BranchUndoLog.class);
            }
            return branchUndoLog;
        } catch (IOException e) {
            log.error("json decode exception, {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
