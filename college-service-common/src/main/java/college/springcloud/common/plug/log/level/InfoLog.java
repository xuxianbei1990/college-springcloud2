package college.springcloud.common.plug.log.level;


import college.springcloud.common.plug.log.AbstractLog;
import college.springcloud.common.plug.log.Log;
import org.slf4j.event.Level;

@Log
public class InfoLog extends AbstractLog {

    @Override
    public Level level() {
        return Level.INFO;
    }

    @Override
    protected void record(String format, Object... arguments) {
        logger.info(format, arguments);
    }
}
