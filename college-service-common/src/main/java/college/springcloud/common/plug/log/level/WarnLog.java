package college.springcloud.common.plug.log.level;


import college.springcloud.common.plug.log.AbstractLog;
import college.springcloud.common.plug.log.Log;
import org.slf4j.event.Level;

@Log
public class WarnLog extends AbstractLog {

    @Override
    public Level level() {
        return Level.WARN;
    }

    @Override
    protected void record(String format, Object... arguments) {
        logger.warn(format, arguments);
    }
}
