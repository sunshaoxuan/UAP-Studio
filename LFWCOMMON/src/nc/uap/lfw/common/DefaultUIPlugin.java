package nc.uap.lfw.common;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public abstract class DefaultUIPlugin extends AbstractUIPlugin implements ILogSupport{
	private LogUtility logUtility;
	public void logError(String msg) {
		getLogProxy().logErrorMessage(msg);
	}

	public void logError(Throwable e) {
		getLogProxy().log(e);
	}

	public void logError(String msg, Throwable e) {
		getLogProxy().log(msg, e);
	}

	public void logInfo(String msg) {
		getLogProxy().logInfoMessage(msg);
	}
	
	public LogUtility getLogProxy() {
		if(logUtility == null){
			logUtility = new LogUtility(getLog(), getBundle().getSymbolicName());
		}
		return logUtility;
	}
	
}
