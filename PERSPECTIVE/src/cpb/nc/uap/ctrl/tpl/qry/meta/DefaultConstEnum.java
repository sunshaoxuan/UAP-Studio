package nc.uap.ctrl.tpl.qry.meta;

import java.io.Serializable;

public class DefaultConstEnum implements IConstEnum, Serializable {
	private static final long serialVersionUID = 7634115571828705954L;
	private Object value;
	private String name;

	public DefaultConstEnum(Object value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * getShowName 方法注解。
	 */
	public String getName() {
		return name;
	}

	/**
	 * getValue 方法注解。
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-11-8 14:58:53)
	 * 
	 * @return java.lang.String
	 */
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IConstEnum) {
			if (value != null) {
				return value.equals(((IConstEnum) obj).getValue());
			}

		}
		if (value == null && obj == null) {
			return true;
		}
		return false;
	}
}
