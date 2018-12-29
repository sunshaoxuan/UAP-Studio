package nc.vo.pub;



/**
 *     业务异常类。对违反业务逻辑的操作抛出这个异常。这属于EJB规范
 * 中的应用级异常。<br>
 *     这类异常应由操作员判断如何处理，因此BO类不应捕获这类异常，而
 * 应直接将它们转发给客户端。<br>
 *     注意：对BO类中抛出业务异常的方法，应在该BO对象的remote接口中
 * 对应方法的声明上加上对该异常的声明。
 * 
 * 创建日期：(2001-2-15 9:18:57)
 * @author：Zhao Jijiang
 */
public class BusinessException extends Exception {
	
	static final long serialVersionUID = -35466L;

    private String hint;
    
	/*
	 *错误码，需统一编写，包括业务插件抛出的错误
	 **/
	//TODO
	private String errorCodeString = ""; 
 
    /**
     * BusinessException 构造子注解。
     */
    public BusinessException() {
        super();
    }

    /**
     * BusinessException 构造子注解。
     * @param s java.lang.String
     */
    public BusinessException(String s) {
        super(s);
        setErrorCodeString(IErrorDict.ERR_BUSI_BILL_EXCEPTION);
    }
    
    /**
     * BusinessException 构造子注解。
     * @param s java.lang.String
     */
    public BusinessException(String s,String errorCode) {
        super(s);
        setErrorCodeString(errorCode);
    }

    /**
     * 获得引发异常的原因的提示或说明。
     * 
     * 创建日期：(2001-3-1 9:55:28)
     * @return java.lang.String 引发异常的原因说明。
     */
    public java.lang.String getHint() {
        return hint;
    }

    /**
     * 设置对引发异常的原因的提示或说明。
     * 
     * 创建日期：(2001-3-1 9:55:28)
     * @param newHint java.lang.String 引发异常的原因。
     */
    public void setHint(java.lang.String newHint) {
        hint = newHint;
    }

    /**
     * @since  nc3.1
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @since  nc3.1
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }

	public String getErrorCodeString() {
		return errorCodeString;
	}

	public void setErrorCodeString(String errorCode) {
		this.errorCodeString = errorCode;
	}

}
