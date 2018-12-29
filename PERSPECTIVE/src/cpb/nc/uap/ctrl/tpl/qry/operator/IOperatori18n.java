package nc.uap.ctrl.tpl.qry.operator;

import uap.lfw.core.ml.LfwResBundle;

public class IOperatori18n {
	
	//����
	public static String getEQ_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-eq");
	}
	
	//	"������"
	public static String getNEQ_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-ne");
	}
	
	//	���� greater than
	public static String getGT_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-gt");	
	}
	
	//���ڵ���
	public static String getGE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-ge");
	}
	
	//	С�� less than
	public static String getLT_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-lt");
	}
	//	С�ڵ���
	public static String getLE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-le");
	}
	
	//like ����
	public static String getLIKE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-like")/* @res "����" */;
	}
	
	//left like �����
	public static String getLLIKE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-llike")/* @res "�����" */;
	}
	
	//right like �Ұ���
	public static String getRLIKE_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-rlike")/* @res "�Ұ���" */;
	}
	//not like ������
	public static String getNotLike_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-notlike")/* @res "������" */;
	}
	public static String getIn_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-in")/* @res "������" */;
	}
	public static String getNotIn_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-notin")/* @res "��������" */;
	}
	
	//Ϊ��
	public static String getISNull_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-null")/* @res "Ϊ��" */;
	}
	//��Ϊ��
	public static String getISNotNull_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPP_Template-000512")/* @res "��Ϊ��" */;
	}
	
	
	//����
	public static String getBetween_Desc(){
		return LfwResBundle.getInstance().getStrByID("_template", "UPT_Template-between")/* @res "����" */;
	}
	
	public static String getOperatorDescByOperCode(String opercode){
		//�Ժ���˵��
		return null;
	}

}
