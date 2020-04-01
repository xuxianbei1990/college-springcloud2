package college.seata.spring.annotation.datasource.undo;


import college.seata.spring.annotation.datasource.undo.parser.JacksonUndoLogParser;

/**
 * @author: xuxianbei
 * Date: 2020/3/31
 * Time: 9:38
 * Version:V1.0
 */
public class UndoLogParserFactory {

    private static final UndoLogParser INSTANCES  = new JacksonUndoLogParser();

    public static UndoLogParser getInstance() {
        return INSTANCES;
    }
}
