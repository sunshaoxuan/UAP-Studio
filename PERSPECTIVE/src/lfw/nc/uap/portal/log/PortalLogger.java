package nc.uap.portal.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PortalLogger {
//	public static final String LFW_LOGGER_NAME = "uapweb";
	private static Log log = LogFactory.getLog("SMART");
//	private static boolean develop = LfwMultiSysFactory.getMultiSysFactory().isDevMode();
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
		log.error(msg,t);
	}
	
	public static void error(String msg){
		log.error(msg);
	}
	
	public static void error(Throwable e){
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