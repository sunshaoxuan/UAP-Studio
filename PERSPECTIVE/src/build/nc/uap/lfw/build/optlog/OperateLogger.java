package nc.uap.lfw.build.optlog;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于线程的操作日志记录器。
 * 
 * @author PH
 */
public abstract class OperateLogger {
	
	/** 线程日志记录器 */
	private static ThreadLocal<OperateLogger> threadLogs = new ThreadLocal<OperateLogger>();
	
	/**
	 * 获取操作日志记录器实例。
	 * 
	 * @return 操作日志记录器实例
	 */
	public static OperateLogger getInstance(){
		OperateLogger logger = threadLogs.get();
		if(logger == null){
			logger = new OperateLoggerDefaultImpl();
			setInstance(logger);
		}
		return logger;
	}
	
	protected static void setInstance(OperateLogger operateLogInfo){
		threadLogs.set(operateLogInfo);
	}
	
	/**
	 * 获取所有操作日志信息。
	 * 
	 * @return 所有操作日志信息
	 */
	public abstract List<LogInfo> getLogs();
	
	/**
	 * 重置，重新开始记录操作日志。
	 */
	public abstract void reset();
	
	/**
	 * 增加日志。
	 * 
	 * @param logLevel 日志级别
	 * @param content 日志内容
	 */
	public abstract void addLog(LogLevel logLevel, String content);
	
	/**
	 * 操作日志信息。
	 * 
	 * @author PH
	 */
	public static class LogInfo{
		LogLevel logLevel;
		
		String content;
		
		public LogInfo(LogLevel logLevel, String content) {
			this.logLevel = logLevel;
			this.content = content;
		}

		public LogLevel getLogLevel() {
			return logLevel;
		}

		public String getContent() {
			return content;
		}
	}
	
	/**
	 * 操作日志级别。
	 * 
	 * @author PH
	 */
	public enum LogLevel {
		INFO {
			@Override
			public String getLevelDesc() {
				return "提示";
			}
		}, 
		WARN {
			@Override
			public String getLevelDesc() {
				return "警告";
			}
		}, 
		ERROR {
			@Override
			public String getLevelDesc() {
				return "错误";
			}
		};
		
		public abstract String getLevelDesc();
	}
	
	/**
	 * Default implement.
	 * 
	 * @author PH
	 */
	static class OperateLoggerDefaultImpl extends OperateLogger {
		
		private List<LogInfo> logs;
		
		OperateLoggerDefaultImpl() {
			this.logs = new ArrayList<LogInfo>();
		}

		@Override
		public List<LogInfo> getLogs(){
			return new ArrayList<LogInfo>(logs);
		}
		
		@Override
		public void addLog(LogLevel logLevel, String content) {
			logs.add(new LogInfo(logLevel, content));
		}

		@Override
		public void reset() {
			logs.clear();
		}
	}
	
}
