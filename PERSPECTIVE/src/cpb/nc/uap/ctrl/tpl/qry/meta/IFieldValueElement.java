package nc.uap.ctrl.tpl.qry.meta;

import java.io.Serializable;

public interface IFieldValueElement extends Serializable {

	public String getShowString();
	
	public String getSqlString();
	
	public Object getValueObject();
}
