package college.springcloud.common.plug.log.level;


import college.springcloud.common.plug.log.AbstractLog;
import college.springcloud.common.plug.log.Log;
import org.slf4j.event.Level;

@Log
public class ErrorLog extends AbstractLog {

    @Override
    public Level level() {
        return Level.ERROR;
    }

    @Override
    protected void record(String format, Object... arguments) {
        logger.error(format, arguments);
    }

}
