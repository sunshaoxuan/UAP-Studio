package nc.uap.lfw.template;

import java.util.List;

import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory;


public class ResourceShowUtil {
	

	private List<ResultVO> allResVOList = null;
	
	private String tempType = null;
	
	private String projectPath = LFWAMCPersTool.getProjectPath();
	
	
	public ResourceShowUtil(List<ResultVO> allResVOList,String tempType){
		this.allResVOList = allResVOList;
		this.tempType = tempType;
	}
	/**
	 * ����������������Դ֮ǰ��tableview����ʾ��Ҫ���ɵ���Դ
	 */
	public void setTableViewNeedShowAllRes() {
		allResVOList.clear();
		ResultVO vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0000/*׼������*/, uap.lfw.lang.M_template.ResourceShowUtil_0001/*��ȡģ��ҳ�漰����*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0000/*׼������*/, uap.lfw.lang.M_template.ResourceShowUtil_0002/*׼���������ݼ�*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0000/*׼������*/, uap.lfw.lang.M_template.ResourceShowUtil_0003/*׼��ģ���滻��Ҫ�Ĳ���*/, "running");
		if(tempType.equals(M_template.MasterSecondlyFlowFactory_0)){
			vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0007/*����ע��*/, uap.lfw.lang.M_template.ResourceShowUtil_0008/*���ݿ�д���������ͺ�Ĭ�����̶���(wfm_flwtype��wfm_prodef��)*/, "running");
			allResVOList.add(vo);
		}
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0004/*ģ�忽��*/, uap.lfw.lang.M_template.ResourceShowUtil_0005/*����ģ��ǰ̨�ļ�*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0004/*ģ�忽��*/, uap.lfw.lang.M_template.ResourceShowUtil_0006/*����ģ������ļ�*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0009/*����ҳ��*/, uap.lfw.lang.M_template.ResourceShowUtil_0010/*��������ģʽ��ҳ�棬�ȴ�����ҳ�滺��*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0009/*����ҳ��*/, uap.lfw.lang.M_template.ResourceShowUtil_0011/*ʹ�ò������ģ�壬�����б�ҳ��*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0009/*����ҳ��*/, uap.lfw.lang.M_template.ResourceShowUtil_0012/*ʹ�ò������ģ�壬���ɿ�Ƭҳ��*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0009/*����ҳ��*/, uap.lfw.lang.M_template.ResourceShowUtil_0013/*ҳ��佨�����Ӻͽ�������*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0014/*����ģ��*/, uap.lfw.lang.M_template.ResourceShowUtil_0015/*���ɲ�ѯģ��*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0014/*����ģ��*/, uap.lfw.lang.M_template.ResourceShowUtil_0016/*���ɴ�ӡģ��*/, "running");
		allResVOList.add(vo);
	}
}
