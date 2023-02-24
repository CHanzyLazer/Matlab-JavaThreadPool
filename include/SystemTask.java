import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * @author CHanzy
 * 用于执行系统命令的 task，提供一个接口来强制关闭正在执行的命令
 */
public class SystemTask implements Runnable {
    private final String mCommand;
    private Process mProcess = null;
    
    public SystemTask(String aCommand) {
        mCommand = aCommand;
    }
    
    public void stop() {
        if (mProcess != null) {
            mProcess.destroyForcibly();
            mProcess = null;
        }
    }
    
    public void run() {
        try {
            // 获取 Runtime 实例
            Runtime runtime = Runtime.getRuntime();
            // 执行指令
            mProcess = runtime.exec(mCommand);
            // 读取执行的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 等待执行完成
            mProcess.waitFor();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 程序结束时停止进程
            stop();
        }
    }
}
