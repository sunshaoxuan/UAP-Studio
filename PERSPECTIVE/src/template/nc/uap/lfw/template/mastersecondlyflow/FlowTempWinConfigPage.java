package nc.uap.lfw.template.mastersecondlyflow;

import java.util.List;

import nc.uap.lfw.template.mastersecondly.TempWinConfigPage;

/**
 * @author guomq1 2012-7-30
 */
public class FlowTempWinConfigPage extends TempWinConfigPage {

	public FlowTempWinConfigPage(String pageName) {
		super(pageName);
	}

	protected void addMessages(List<String> filemessages){
//		String windowId = LISTWIN;
		String location = this.getLocation();
//		String projectPath = getProjectPath();
		String iprojectPath = getIprojectPath();
		String controllerPrefix = this.getController().replace(".", "/");
		
		super.addMessages(filemessages);
		String defaultFormOperPath = iprojectPath + "/" + location + controllerPrefix+"/wfm/WfmFlwFormOper.java"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String defalutWfmFormInfoCtxPath = iprojectPath + "/" + location + controllerPrefix+"/wfm/WfmFlwFormVO.java"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		filemessages.add(defaultFormOperPath);
		filemessages.add(defalutWfmFormInfoCtxPath);
	}
	
}
