package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFilterMeta;

public class LikeOperatorDecarator extends AbstractOperator{
	
	private static final long serialVersionUID = 1L;
	private LikeOperator likeop= LikeOperator.getInstance();
	private String opcode = likeop.getOperatorCode();
		
	private LikeOperatorDecarator(String opercode){
		this.opcode = opercode;
	}
	
	public static LikeOperatorDecarator getInstance(String opercode) {
		return new LikeOperatorDecarator(opercode);
	}

	public String getDescription(IFilterMeta meta, IFieldValue value) {
		return likeop.getDescription(meta, toString(),value);
	}

	public String getOperatorCode() {
		return this.opcode;
	}

	public int getParameterNumber() {
		return likeop.getParameterNumber();
	}

	public String getSQLString(FilterMeta meta, IFieldValue value) {
		if(value==null||value.getFieldValues()==null||value.getFieldValues().size()<1||value.getFieldValues().get(0)==null)
			return null;
		String code = likeop.getOperatorCode();
		String valueString = getValueStr(meta, value);
		return getSqlFieldCode(meta.getSQLFieldCode())+" "+code+" "+valueString;
	}

	private String getValueStr(FilterMeta meta, IFieldValue value) {
		String valueString = value.getFieldValues().get(0).getSqlString();
		if((!isNumberType(meta))&&(!isAttribute(value.getFieldValues().get(0))))
		{
			if(opcode.equals(IOperatorConstants.LIKE)
					||opcode.equals(IOperatorConstants.LIKEIC))
				valueString ="'%"+valueString+"%'";
			else if(opcode.equals(IOperatorConstants.LLIKE)
					||opcode.equals(IOperatorConstants.LLIKEIC))
				valueString ="'"+valueString+"%'";
			else if(opcode.equals(IOperatorConstants.RLIKE)
					||opcode.equals(IOperatorConstants.RLIKEIC))
				valueString ="'%"+valueString+"'";
		}
		if(isIgnoreCase())
			valueString = valueString.toUpperCase();
		return valueString;
	}
	
	private String getSqlFieldCode(String metacode){
		if(isIgnoreCase())
			return "upper("+metacode+")";
		else
			return metacode;
		
	}
	
	private boolean isIgnoreCase(){
		if(opcode.equals(IOperatorConstants.LIKEIC)
				||opcode.equals(IOperatorConstants.LLIKEIC)
				||opcode.equals(IOperatorConstants.RLIKEIC))
			return true;
		return false;
	}

	public boolean isFuzzy() {
		return likeop.isFuzzy();
	}
	
	public String toString(){
		if(opcode.equals(IOperatorConstants.LIKE)
				||opcode.equals(IOperatorConstants.LIKEIC))
			return likeop.toString();
		else if(opcode.equals(IOperatorConstants.LLIKE)
				||opcode.equals(IOperatorConstants.LLIKEIC))
			return IOperatori18n.getLLIKE_Desc();
		else 
			return IOperatori18n.getRLIKE_Desc();
//		//暂时都统一返回“包含”，待取到合适名字再改
//		return likeop.toString();
	}
}
