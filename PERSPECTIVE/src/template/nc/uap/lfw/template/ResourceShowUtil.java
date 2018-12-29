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
	 * 设置在生成所有资源之前在tableview中显示需要生成的资源
	 */
	public void setTableViewNeedShowAllRes() {
		allResVOList.clear();
		ResultVO vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0000/*准备工作*/, uap.lfw.lang.M_template.ResourceShowUtil_0001/*读取模板页面及配置*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0000/*准备工作*/, uap.lfw.lang.M_template.ResourceShowUtil_0002/*准备所有数据集*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0000/*准备工作*/, uap.lfw.lang.M_template.ResourceShowUtil_0003/*准备模板替换需要的参数*/, "running");
		if(tempType.equals(M_template.MasterSecondlyFlowFactory_0)){
			vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0007/*流程注册*/, uap.lfw.lang.M_template.ResourceShowUtil_0008/*数据库写入流程类型和默认流程定义(wfm_flwtype和wfm_prodef表)*/, "running");
			allResVOList.add(vo);
		}
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0004/*模板拷贝*/, uap.lfw.lang.M_template.ResourceShowUtil_0005/*拷贝模板前台文件*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0004/*模板拷贝*/, uap.lfw.lang.M_template.ResourceShowUtil_0006/*拷贝模板控制文件*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0009/*生成页面*/, uap.lfw.lang.M_template.ResourceShowUtil_0010/*初步生成模式化页面，等待加入页面缓存*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0009/*生成页面*/, uap.lfw.lang.M_template.ResourceShowUtil_0011/*使用参数填充模板，生成列表页面*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0009/*生成页面*/, uap.lfw.lang.M_template.ResourceShowUtil_0012/*使用参数填充模板，生成卡片页面*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0009/*生成页面*/, uap.lfw.lang.M_template.ResourceShowUtil_0013/*页面间建立连接和交互方法*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0014/*生成模板*/, uap.lfw.lang.M_template.ResourceShowUtil_0015/*生成查询模板*/, "running");
		allResVOList.add(vo);
		vo = new ResultVO(uap.lfw.lang.M_template.ResourceShowUtil_0014/*生成模板*/, uap.lfw.lang.M_template.ResourceShowUtil_0016/*生成打印模板*/, "running");
		allResVOList.add(vo);
	}
}
