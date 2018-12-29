package uap.lfw.smartmw.mw;

/**
 * @author linmin
 * 
 */
public class JdbcProperty implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String propertyName;

	private String propertyValue;

	public JdbcProperty() {
		super();
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public boolean equals(JdbcProperty p) {
		if (p == null || propertyName == null || propertyValue == null)
			return false;
		if (!propertyName.equals(p.getPropertyName()))
			return false;
		if (!propertyValue.equals(p.getPropertyValue()))
			return false;

		return true;
	}

	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + (propertyName == null ? 0 : propertyName.hashCode());
		hash = hash * 31
				+ (propertyValue == null ? 0 : propertyValue.hashCode());
		return hash;
	}
}
