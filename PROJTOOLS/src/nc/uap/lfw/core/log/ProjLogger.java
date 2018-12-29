package nc.uap.lfw.core.log;

import org.apache.commons.logging.Log;
import org.eclipse.ui.internal.handlers.WizardHandler.New;


public class ProjLogger implements Log{

	private static Log log = new ProjLogger();;
	
	public static Log getInstance() {
		return log;
	}



	@Override
	public void error(Throwable e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public boolean isDebugEnabled() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isInfoEnabled() {
		// TODO 自动生成的方法存根
		return false;
	}


	@Override
	public boolean isWarnEnabled() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isErrorEnabled() {
		// TODO 自动生成的方法存根
		return false;
	}
//	

	@Override
	public void trace(Object message) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trace(Object message, Throwable t) {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public void debug(Object msg) {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public void error(Object msg, Throwable t) {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public void error(Object msg) {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public void warn(Object string) {
		// TODO 自动生成的方法存根
		
	}
	
	@Override
	public boolean isTraceEnabled() {
		// TODO 自动生成的方法存根
		return false;
	}
}