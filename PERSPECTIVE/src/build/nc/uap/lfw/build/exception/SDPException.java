package nc.uap.lfw.build.exception;

/**
 * 构造过程的统一异常类
 * @author syang
 *
 * 2008-7-3
 */
public class SDPException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3316971880008424033L;

	/**
	 * 
	 * @param message
	 */
	public SDPException(String message) {
        super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public SDPException(String message, Throwable cause) {
        super(message, cause);
	}
}
