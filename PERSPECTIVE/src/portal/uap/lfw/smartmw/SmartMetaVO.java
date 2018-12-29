package uap.lfw.smartmw;

public class SmartMetaVO {
	private String key;
	private String value;
	private boolean single = true;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isSingle() {
		return single;
	}
	public void setSingle(boolean single) {
		this.single = single;
	}
}
