package nc.uap.lfw.template.mastersecondlyflow;

import java.util.List;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.application.CreateWidgetByTemp;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.datamodel.MdClassVO;
import nc.uap.lfw.core.datamodel.MdComponnetVO;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.md.LfwWfmBizItf;
import nc.uap.lfw.template.NewTempleteWindowWizard;
import nc.uap.lfw.template.mastersecondly.MasterSecondlyWindowAction;
import nc.uap.lfw.template.mastersecondly.TempWinConfigPage;
import nc.uap.lfw.template.tools.LfwTemplateTool;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * 
 * @author guomq1 2012-8-6
 */
public class MasterSecondlyFlowWindowAction extends MasterSecondlyWindowAction{

	private static final String DATA_FLOW_TYPE_PK = "flowTypePk"; //$NON-NLS-1$
	private static final String MASTER_SECONDLYFLOW_FACTORY = "nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory"; //$NON-NLS-1$
	private static final String TEMPLATES_CTRL_BASE = "templates/multiTableFlowQuery/controller"; //$NON-NLS-1$
	private static final String TEMPLATE_BASE = "templates/multiTableFlowQuery/web/html/nodes"; //$NON-NLS-1$

	public MasterSecondlyFlowWindowAction(NewTempleteWindowWizard wizard) {
		super(wizard);
	}

	@Override
	protected void config(){
		//设置模板类型
		setTemplateType(MASTER_SECONDLYFLOW_FACTORY);
		setDoubleDsPage((FlowDoubleDsSelectPage)getWizard().getPage(MainPlugin.getResourceString(WEBPersConstants.KEY_TEMP_FLOW_THDDPAGE_DESC)));
		setTempWinConfigPage ((FlowTempWinConfigPage)getWizard().getPage(WEBPersConstants.KEY_TEMP_FLOWFORTHPAGE_DESC));
		setCreateAction(new CreateWidgetByTemp("MasterSecondlyFlowQuery")); //$NON-NLS-1$
	}

	public boolean run() {
		return super.run();
	}
	
	@Override
	protected boolean doGen() throws Exception {
		String projectPath = LFWAMCPersTool.getProjectPath();
		int index = projectPath.lastIndexOf("/"); //$NON-NLS-1$
		String iprojectPath = projectPath.substring(0, index);
		String prePath = iprojectPath + "/" + data.get(DATA_LOCATION); //$NON-NLS-1$
		String controlPathDot = getTempWinConfigPage().getController()+".wfm"; //$NON-NLS-1$
		String controlPath = controlPathDot.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		List[] listArray = new List[2];
		listArray[0] = getCreateAction().createFromTemp(getTemplateCtrlBase() + "/" + TempWinConfigPage.POPWIN + "/", prePath + "/" + controlPath + "/", "WfmFlwFormOper.java.template", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		listArray[1] = getCreateAction().createFromTemp(getTemplateCtrlBase() + "/" + TempWinConfigPage.POPWIN + "/", prePath + "/" + controlPath + "/", "WfmFlwFormVO.java.template", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		getCreateAction().saveFile(listArray);
		
		TempFlowConfigPage tempFlowConfigPage = (TempFlowConfigPage) getWizard().getPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_FLOWPAGE_DESC));
		// 流程注册
		String flowCatPk = tempFlowConfigPage.getFlowCatPk();
		String typeName = tempFlowConfigPage.getTypeName();
		String typeCode = tempFlowConfigPage.getTypeCode();
		String serverClass = getTempWinConfigPage().getController() + ".wfm.WfmFlwFormOper"; //$NON-NLS-1$
		String flowTypePk = LfwTemplateTool.registerFlowType(flowCatPk, typeCode, typeName, serverClass);
		data.put(DATA_FLOW_TYPE_PK, flowTypePk);
		return super.doGen();
	}

	@Override
	protected boolean genOtherParamData(Map<String, String> dataParam) {
		MdComponnetVO componnetVO = getDoubleDsPage().getComponentVo();
		if(componnetVO == null){
			Shell shell = new Shell(Display.getCurrent());
			MessageDialog.openError(shell, M_template.MasterSecondlyFlowWindowAction_0, M_template.MasterSecondlyFlowWindowAction_1);
			return false;
		}
//		String aggVoName =  NCConnector.getAggVOByComponent(componnetVO.getName());
//		if(aggVoName==null){
//			Shell shell = new Shell(Display.getCurrent());
//			MessageDialog.openError(shell, "错误", "所选元数据不是主子表,请重新选择");
//			return false;
//		}
		
		
		String businessEntityFullName = null;
		List<MdClassVO> classVos = NCConnector.getAllClassByComId(componnetVO.getId());

		for (MdClassVO classVo : classVos){
			if (classVo.getFullclassname().equals(getMasterDs().getVoMeta())){
				businessEntityFullName = componnetVO.getNamespace() + "." + classVo.getName(); //$NON-NLS-1$
				break;
			}
		}
		Map<String,String> entity = NCConnector.getBusinessEntity(businessEntityFullName);
		if(entity != null){
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_APPROVEDATE, entity.get(LfwWfmBizItf.ATTRIBUTE_APPROVEDATE));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLMAKEDATE, entity.get(LfwWfmBizItf.ATTRIBUTE_BILLMAKEDATE));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLMAKER, entity.get(LfwWfmBizItf.ATTRIBUTE_BILLMAKER));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLNO, entity.get(LfwWfmBizItf.ATTRIBUTE_BILLNO));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_ORG, entity.get(LfwWfmBizItf.ATTRIBUTE_ORG));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_APPROVER, entity.get(LfwWfmBizItf.ATTRIBUTE_APPROVER));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMSTATE, entity.get(LfwWfmBizItf.ATTRIBUTE_FORMSTATE));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMTITLE, entity.get(LfwWfmBizItf.ATTRIBUTE_FORMTITLE));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMINSPK, entity.get(LfwWfmBizItf.ATTRIBUTE_FORMINSPK));
		}
		else{
			Shell shell = new Shell(Display.getCurrent());
			MessageDialog.openError(shell, M_template.MasterSecondlyFlowWindowAction_0, M_template.MasterSecondlyFlowWindowAction_2);
			return false;
		}
		return true;
	}

	@Override
	protected String getTemplateBase() {
		return TEMPLATE_BASE;
	}

	@Override
	protected String getTemplateCtrlBase() {
		return TEMPLATES_CTRL_BASE;
	}

	
}
