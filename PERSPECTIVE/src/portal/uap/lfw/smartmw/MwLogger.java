package uap.lfw.smartmw;

import nc.uap.lfw.core.WEBPersPlugin;

public class MwLogger {
	public static void log(String msg){
		WEBPersPlugin.getDefault().logError(msg);
	}
	
	public static void log(Throwable e){
		WEBPersPlugin.getDefault().logError(e);
	}
	
	public static void logInfo(String msg){
		WEBPersPlugin.getDefault().logInfo(msg);
	}
}
