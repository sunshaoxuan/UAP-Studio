package nc.uap.ctrl.tpl.qry.meta;

import java.util.ArrayList;
import java.util.List;

import nc.uap.ctrl.tpl.qry.operator.IOperator;

import org.apache.commons.lang.StringUtils;

public class Operator {

	private boolean isSelected;// �Ƿ�ѡ��
	
	private String code;// ����������
	private String name;// ����������
	
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
	 * ���ز�����������ѡ��Ĳ���������
	 * 
	 * @param operators
	 *            ����ѡ����Ϣ�Ĳ���������
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
	 * ���ز��������в������γɵĲ��������봮������"=@<@<=@"
	 * 
	 * @param operators
	 *            ����������
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