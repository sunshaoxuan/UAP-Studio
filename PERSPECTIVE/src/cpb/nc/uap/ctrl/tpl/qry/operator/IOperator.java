package nc.uap.ctrl.tpl.qry.operator;

import java.io.Serializable;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFilterMeta;

public interface IOperator extends Serializable{

	/** 操作符编码(名称)串的分隔标记 &quot@&quot */
	public static final String TOKEN = "@";
	
	public String getSQLString(FilterMeta meta,IFieldValue value);
	public String getDescription(IFilterMeta meta,IFieldValue value);
	public int getParameterNumber();
	public boolean isFuzzy();
	public String getOperatorCode();
}