package college.seata.rm.datasource;

import college.seata.rm.DefaultResourceManager;
import college.springcloud.io.seata.common.thread.NamedThreadFactory;
import college.springcloud.io.seata.core.exception.ShouldNeverHappenException;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.BranchStatus;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.ResourceManagerInbound;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 16:28
 * Version:V1.0
 */
@Slf4j
public class AsyncWorker implements ResourceManagerInbound {

    private static ScheduledExecutorService timerExecutor;

    private static int ASYNC_COMMIT_BUFFER_LIMIT = 10000;

    private static final int DEFAULT_RESOURCE_SIZE = 16;

    private static final BlockingQueue<Phase2Context> ASYNC_COMMIT_BUFFER = new LinkedBlockingQueue<>(
            ASYNC_COMMIT_BUFFER_LIMIT);

    @Override
    public BranchStatus branchCommit(BranchType branchType, String xid, long branchId, String resourceId, String applicationData) throws TransactionException {
        if (!ASYNC_COMMIT_BUFFER.offer(new Phase2Context(branchType, xid, branchId, resourceId, applicationData))) {
            log.warn("Async commit buffer is FULL. Rejected branch [" + branchId + "/" + xid + "] will be handled by housekeeping later.");
        }
        return BranchStatus.PhaseTwo_Committed;
    }



    /**
     * Init.
     */
    public synchronized void init() {
        log.info("Async Commit Buffer Limit: " + ASYNC_COMMIT_BUFFER_LIMIT);
        timerExecutor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("AsyncWorker", 1, true));
        timerExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    doBranchCommits();
                } catch (Throwable e) {
                    log.info("Failed at async committing ... " + e.getMessage());
                }
            }
        }, 10, 1000 * 1, TimeUnit.MILLISECONDS);
    }

    //为什么需要批量删除undo日志
    private void doBranchCommits() {
        if (ASYNC_COMMIT_BUFFER.size() == 0) {
            return;
        }

        Map<String, List<Phase2Context>> mappedContexts = new HashMap<>(DEFAULT_RESOURCE_SIZE);

        while (!ASYNC_COMMIT_BUFFER.isEmpty()) {
            Phase2Context commitContext = ASYNC_COMMIT_BUFFER.poll();
            List<Phase2Context> contextsGroupedByResourceId = mappedContexts.get(commitContext.resourceId);
            if (contextsGroupedByResourceId == null) {
                contextsGroupedByResourceId = new ArrayList<>();
                mappedContexts.put(commitContext.resourceId, contextsGroupedByResourceId);
            }
            contextsGroupedByResourceId.add(commitContext);
        }
        for (Map.Entry<String, List<Phase2Context>> entry : mappedContexts.entrySet()) {
            Connection conn = null;
            DataSourceProxy dataSourceProxy;
            //这里还没有看明白从哪里来的，不过不是重点
            DataSourceManager resourceManager = (DataSourceManager) DefaultResourceManager.get()
                    .getResourceManager(BranchType.AT);
            dataSourceProxy = resourceManager.get(entry.getKey());
            if (dataSourceProxy == null) {
                throw new ShouldNeverHappenException("Failed to find resource on " + entry.getKey());
            }
        }

    }


    private static class Phase2Context {

        /**
         * Instantiates a new Phase 2 context.
         *
         * @param branchType      the branchType
         * @param xid             the xid
         * @param branchId        the branch id
         * @param resourceId      the resource id
         * @param applicationData the application data
         */
        public Phase2Context(BranchType branchType, String xid, long branchId, String resourceId,
                             String applicationData) {
            this.xid = xid;
            this.branchId = branchId;
            this.resourceId = resourceId;
            this.applicationData = applicationData;
            this.branchType = branchType;
        }

        /**
         * The Xid.
         */
        String xid;
        /**
         * The Branch id.
         */
        long branchId;
        /**
         * The Resource id.
         */
        String resourceId;
        /**
         * The Application data.
         */
        String applicationData;

        /**
         * the branch Type
         */
        BranchType branchType;
    }
}
