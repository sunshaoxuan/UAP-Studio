package uap.lfw.smartmw.mw;

/**
 * 环境变量描述
 */
public class EMVParameter implements java.io.Serializable {

	private static final long serialVersionUID = 8085712020336822081L;

	public java.lang.String ParameterName;

	public java.lang.String ParameterValue;

	public EMVParameter() {
		super();
	}

	public java.lang.String getParameterName() {
		return ParameterName;
	}

	public java.lang.String getParameterValue() {
		return ParameterValue;
	}

	public void setParameterName(java.lang.String newParameterName) {
		ParameterName = newParameterName;
	}

	public void setParameterValue(java.lang.String newParameterValue) {
		ParameterValue = newParameterValue;
	}

	public boolean equals(EMVParameter ep) {
		if (ep == null || ParameterName == null || ParameterValue == null)
			return false;
		if (!ParameterName.equals(ep.ParameterName))
			return false;
		if (!ParameterValue.equals(ep.ParameterValue))
			return false;
		return true;
	}

	public int hashCode() {
		int hash = 1;
		hash = hash * 31
				+ (ParameterName == null ? 0 : ParameterName.hashCode());
		hash = hash * 31
				+ (ParameterValue == null ? 0 : ParameterValue.hashCode());
		return hash;
	}

}