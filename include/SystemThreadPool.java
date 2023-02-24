import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author CHanzy
 * 专门用于并行处理 SystemTask 的 pool
 */
public class SystemThreadPool {
    private final ThreadPoolExecutor mPool;
    private final List<SystemTask> mAllTasks = new ArrayList<>();
    
    public SystemThreadPool(int aThreadNumber) {
        mPool = new ThreadPoolExecutor(aThreadNumber, aThreadNumber, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }
    
    public Future<?> submitSystem(String aCommand) {
        SystemTask tSystemTask = new SystemTask(aCommand);
        mAllTasks.add(tSystemTask);
        return mPool.submit(tSystemTask);
    }
    
    public void waitUntilDone() throws InterruptedException {
        while (mPool.getActiveCount() > 0 || mPool.getQueue().size() > 0) Thread.sleep(100);
        mAllTasks.clear();
    }
    
    public int getTaskNumber() {
        return mPool.getActiveCount() + mPool.getQueue().size();
    }
    
    public void shutdown() {mPool.shutdown();}
    
    public List<Runnable> shutdownNow() {
        List<Runnable> tOut = mPool.shutdownNow();
        for (SystemTask tTask : mAllTasks) tTask.stop();
        mAllTasks.clear();
        return tOut;
    }
}
