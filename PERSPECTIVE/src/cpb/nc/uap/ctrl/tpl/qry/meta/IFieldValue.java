package nc.uap.ctrl.tpl.qry.meta;

import java.io.Serializable;
import java.util.List;

public interface IFieldValue extends Serializable{

	public List<IFieldValueElement> getFieldValues();
}
