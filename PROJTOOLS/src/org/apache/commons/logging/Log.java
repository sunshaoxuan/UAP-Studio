package org.apache.commons.logging;

public abstract interface Log {
	
	public void debug(Object msg);
	
	public void error(Object msg, Throwable t);
	
	public void error(Object msg);
	
	public void error(Throwable e);

	public boolean isDebugEnabled();

	public boolean isInfoEnabled();
	
	public boolean isTraceEnabled();

	public void warn(Object string);

	public boolean isWarnEnabled();

	public boolean isErrorEnabled();
	
	public void trace(Object message);
	 
	public void trace(Object message, Throwable t);
	 
//	private static Log log = new Log();
//	
//	public static Log getInstance(){
//		return log;
//	}
//	public static void info(String msg){
////		log.info(msg);
//	}
//	
//	public static void console(String msg){
////		log.error(msg);
//	}
//	
//	public static void debug(String msg){
////		log.error(msg);
//	}
//	
//	public static void error(String msg, Throwable t){
//		log.error(msg,t);
//	}
//	
//	public static void error(String msg){
//		log.error(msg);
//	}
//	
//	public static void error(Throwable e){
//		log.error(e);
//	}
//
//	public static void warn(String msg) {
////		log.error(msg);
//	}
//	
//	public static boolean isDebugEnabled() {
//		return true;
//	}
//
//	public static boolean isInfoEnabled() {
//		return true;
//	}
//
//
//	public static boolean isWarnEnabled() {
//		return true;
//	}
//
//	public static boolean isErrorEnabled() {
//		return true;
//	}
}
