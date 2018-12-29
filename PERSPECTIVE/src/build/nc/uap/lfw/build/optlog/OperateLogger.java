package nc.uap.lfw.build.optlog;

import java.util.ArrayList;
import java.util.List;

/**
 * �����̵߳Ĳ�����־��¼����
 * 
 * @author PH
 */
public abstract class OperateLogger {
	
	/** �߳���־��¼�� */
	private static ThreadLocal<OperateLogger> threadLogs = new ThreadLocal<OperateLogger>();
	
	/**
	 * ��ȡ������־��¼��ʵ����
	 * 
	 * @return ������־��¼��ʵ��
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
	 * ��ȡ���в�����־��Ϣ��
	 * 
	 * @return ���в�����־��Ϣ
	 */
	public abstract List<LogInfo> getLogs();
	
	/**
	 * ���ã����¿�ʼ��¼������־��
	 */
	public abstract void reset();
	
	/**
	 * ������־��
	 * 
	 * @param logLevel ��־����
	 * @param content ��־����
	 */
	public abstract void addLog(LogLevel logLevel, String content);
	
	/**
	 * ������־��Ϣ��
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
	 * ������־����
	 * 
	 * @author PH
	 */
	public enum LogLevel {
		INFO {
			@Override
			public String getLevelDesc() {
				return "��ʾ";
			}
		}, 
		WARN {
			@Override
			public String getLevelDesc() {
				return "����";
			}
		}, 
		ERROR {
			@Override
			public String getLevelDesc() {
				return "����";
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
