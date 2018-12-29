package nc.uap.ctrl.tpl.qry.meta;

import java.util.ArrayList;
import java.util.List;

import nc.uap.ctrl.tpl.qry.operator.IOperator;

import org.apache.commons.lang.StringUtils;

public class Operator {

	private boolean isSelected;// 是否被选择
	
	private String code;// 操作符编码
	private String name;// 操作符名称
	
	public Operator(){
	}
	
	public Operator(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public int hashCode() {
		if(StringUtils.isBlank(this.getCode()))
			return -1;
		int value = 0;
		value+=Integer.valueOf(this.getCode());
		if(!StringUtils.isBlank(this.getName()))
			value += Integer.valueOf(this.getName())* Math.pow(10, Double.valueOf(this.getName())+1);
		
		return value;
	  }
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Operator other = (Operator) obj;
		return getCode().equals(other.getCode());
	}
	
	public String toString(){
		return code + " " + name;
	}
	
	/**
	 * 返回参数数组中已选择的操作符数组
	 * 
	 * @param operators
	 *            带有选择信息的操作符数组
	 */
	public static Operator[] getSelectedOperators(Operator[] operators){
		if (operators == null) return null;
		List<Operator> list = new ArrayList<Operator>();
		for (Operator operator : operators) {
			if (operator.isSelected()) {
				list.add(operator);
			}
		}
		return list.toArray(new Operator[0]);
	}
	
	/**
	 * 返回参数数组中操作符形成的操作符编码串，形如"=@<@<=@"
	 * 
	 * @param operators
	 *            操作符数组
	 */
	public static String getOperatorCodes(Operator[] operators) {
		if (operators == null) return null;
		StringBuilder sb = new StringBuilder();
		for (Operator operator : operators) {
			sb.append(operator.getCode());
			sb.append(IOperator.TOKEN);
		}
		return sb.toString();
	}
}