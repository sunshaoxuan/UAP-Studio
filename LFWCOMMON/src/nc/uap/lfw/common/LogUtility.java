package nc.uap.lfw.common;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class LogUtility {
	private ILog log;
	private String pluginId;
	public LogUtility(ILog log, String pluginId){
		this.log = log;
		this.pluginId = pluginId;
	}
	
	private void log(IStatus status)
	{
		log.log(status);
	}

	public void logErrorMessage(String message)
	{
		log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, message, null));
	}


	public void logInfoMessage(String message)
	{
		log(new Status(IStatus.INFO, getPluginId(), IStatus.INFO, message, null));
	}

	public void log(String msg, Throwable e){
		if (e instanceof InvocationTargetException)
			e = ((InvocationTargetException) e).getTargetException();
		IStatus status = null;
		if (e instanceof CoreException)
			status = ((CoreException) e).getStatus();
		else
			status = new Status(IStatus.ERROR, getPluginId(), IStatus.OK, msg, e);
		log(status);
	}
	public void log(Throwable e)
	{
		log(e.getMessage(), e);
	}
	
	private String getPluginId() {
		return pluginId;
	}
}
