package nc.uap.cpb.log;

import nc.lfw.lfwtools.perspective.MainPlugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CpLogger {
//	public static final String LFW_LOGGER_NAME = "uapweb";
	private static Log log = LogFactory.getLog("SMART");
	public static void info(String msg){
//		log.info(msg);
	}
	
	public static void console(String msg){
//		log.error(msg);
	}
	
	public static void debug(String msg){
//		log.error(msg);
	}
	
	public static void error(String msg, Throwable t){
		MainPlugin.getDefault().logError(msg,t);
		log.error(msg,t);
	}
	
	public static void error(String msg){
		MainPlugin.getDefault().logError(msg);
		log.error(msg);
	}
	
	public static void error(Throwable e){
		MainPlugin.getDefault().logError(e);
		log.error(e.getMessage(), e);
	}

	public static void warn(String msg) {
//		log.error(msg);
	}
	
	public static boolean isDebugEnabled() {
		return true;
	}

	public static boolean isInfoEnabled() {
		return true;
	}


	public static boolean isWarnEnabled() {
		return true;
	}

	public static boolean isErrorEnabled() {
		return true;
	}
}
