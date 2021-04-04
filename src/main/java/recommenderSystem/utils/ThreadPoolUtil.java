package recommenderSystem.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 */
public class ThreadPoolUtil {

	private static ThreadPoolExecutor poolExecutor;//线程池执行者 ，java内部通过该api实现对线程池管理
	/**
	 * 线程池的核心线程数
	 */
	private static int corePoolSize = 30;

	/**
	 * 线程池的最大线程数
	 */
	private static int maximumPoolSize = 60;
	/**
	 * 线程池空闲时线程的存活时长
	 */
	private static long keepAliveTime = 600000L;

	private synchronized static void createThreadPoolExecutor() {
		if (poolExecutor == null) {
			poolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize,
					keepAliveTime,
					TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<>(500),
					new ThreadPoolExecutor.CallerRunsPolicy());
		}
	}

	public static void execute(Runnable run) {
		if (poolExecutor == null) {
			createThreadPoolExecutor();
		}
		poolExecutor.execute(run);
	}

	static {
		// 加入进程关闭的钩子 将本线程池也一并关闭再关闭进程
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				if (poolExecutor != null) {
					poolExecutor.shutdown();
				}
			}
		}));
	}

    /*corePoolSize：线程池的核心线程数；

    maximumPoolSize：线程池的最大线程数；

    keepAliveTime：线程池空闲时线程的存活时长；

    unit：线程存活时长大单位，结合上个参数使用；

    workQueue：存放任务的队列，使用的是阻塞队列；*/
}