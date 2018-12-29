package nc.uap.lfw.build.exception;



public class SdpBuildRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8050765743805858200L;
	
	private int errorCode;
	

	public SdpBuildRuntimeException() {
		super();
	}

	public SdpBuildRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SdpBuildRuntimeException(String message) {
		super(message);
	}
	
	public SdpBuildRuntimeException(SDPBuildException sdpBuildException){
		super(sdpBuildException.getMessage(), sdpBuildException.getCause());
	}
	
	public SdpBuildRuntimeException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	

}
