package nc.uap.lfw.mlr;

/**
 * 多语资源
 * 
 * @author dingrf
 * 
 */
public class MLRes {

	/**资源文件名*/
	private String resFileName = null;
	
	/**资源ID*/
	private String resID = null;
	
	/**资源值*/
	private String value = null;

	public MLRes(String resFileName, String resID, String value) {
		this.resFileName = resFileName;
		this.resID = resID;
		this.value = value;
	}

	public String getResFileName() {
		return this.resFileName;
	}

	public String getResID() {
		return this.resID;
	}

	public String getValue() {
		return this.value;
	}
}