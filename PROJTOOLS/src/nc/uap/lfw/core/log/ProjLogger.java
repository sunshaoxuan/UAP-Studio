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
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public boolean isDebugEnabled() {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public boolean isInfoEnabled() {
		// TODO �Զ����ɵķ������
		return false;
	}


	@Override
	public boolean isWarnEnabled() {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public boolean isErrorEnabled() {
		// TODO �Զ����ɵķ������
		return false;
	}
//	

	@Override
	public void trace(Object message) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void trace(Object message, Throwable t) {
		// TODO �Զ����ɵķ������
		
	}



	@Override
	public void debug(Object msg) {
		// TODO �Զ����ɵķ������
		
	}



	@Override
	public void error(Object msg, Throwable t) {
		// TODO �Զ����ɵķ������
		
	}



	@Override
	public void error(Object msg) {
		// TODO �Զ����ɵķ������
		
	}



	@Override
	public void warn(Object string) {
		// TODO �Զ����ɵķ������
		
	}
	
	@Override
	public boolean isTraceEnabled() {
		// TODO �Զ����ɵķ������
		return false;
	}
}