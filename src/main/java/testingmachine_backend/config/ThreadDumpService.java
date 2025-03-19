package testingmachine_backend.config;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.util.HashMap;
import java.util.Map;

public class ThreadDumpService {

    public static Map<String, Object> getActiveAndWaitingThreads() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.getAllThreadIds();

        int runnableCount = 0;
        int waitingCount = 0;
        int blockedCount = 0;
        int timedCount = 0;

        for (long threadId : threadIds) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId, 10); // StackTrace-тай авах
            if (threadInfo != null) {
                switch (threadInfo.getThreadState()) {
                    case RUNNABLE:
                        runnableCount++;
                        break;
                    case WAITING:
                        waitingCount++;
                        break;
                    case TIMED_WAITING:
                        timedCount++;
                        break;
                    case BLOCKED:
                        blockedCount++;
                        break;
                    default:
                        break;
                }
            }
        }

        // JSON-д зориулсан өгөгдлийн бүтэц
        Map<String, Object> threadStatus = new HashMap<>();
        threadStatus.put("Active Threads (RUNNABLE)", runnableCount);
        threadStatus.put("Threads (WAITING)", waitingCount);
        threadStatus.put("Threads (BLOCKED)", blockedCount);
        threadStatus.put("Threads (TIMED_WAITING)", timedCount);

        return threadStatus;
    }

}
