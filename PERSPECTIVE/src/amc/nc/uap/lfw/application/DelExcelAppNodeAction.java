package nc.uap.lfw.application;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

public class DelExcelAppNodeAction extends NodeAction{

	public DelExcelAppNodeAction(){
		super(WEBPersConstants.DELEXCEL_APPLICATION, WEBPersConstants.DELEXCEL_APPLICATION);
	}
	public void run() {
		if(MessageDialog.openConfirm(null, M_application.DelExcelAppNodeAction_0, M_application.DelExcelAppNodeAction_1)){
			TreeItem item = LFWPersTool.getCurrentTreeItem();
			if(item instanceof LFWDirtoryTreeItem){
				String id = ((LFWDirtoryTreeItem)item).getId();
				CpAppsNodeVO[] appsNodeVOs = LFWWfmConnector.getAppsNodeVOsByCondition("appid = '"+id+"'"); //$NON-NLS-1$ //$NON-NLS-2$
				if(appsNodeVOs!=null&&appsNodeVOs.length>0){
					String pk_funnode = appsNodeVOs[0].getPk_appsnode();
					LFWWfmConnector.delAppsNode(pk_funnode);
					CpMenuItemVO[] menuItemVOs = LFWWfmConnector.getMenuItemsByCondition("pk_funnode = '"+pk_funnode+"'"); //$NON-NLS-1$ //$NON-NLS-2$
					if(menuItemVOs!=null && menuItemVOs.length>0){
						String pk_menuitem = menuItemVOs[0].getPk_menuitem();
						LFWWfmConnector.delMenuItem(pk_menuitem);
					}
					String funcCode = appsNodeVOs[0].getId();
					String queryPk = LFWWfmConnector.getQueryTemplateByNodeCode(funcCode);
					String printPk = LFWWfmConnector.getPrintTemplateByNodeCode(funcCode);
					if(queryPk!=null) LFWWfmConnector.delQryTemplate(queryPk);
					if(printPk!=null) LFWWfmConnector.delPrintTemplate(printPk);
					MessageDialog.openInformation(null, M_application.DelExcelAppNodeAction_2, M_application.DelExcelAppNodeAction_3);
				}
				else{
					MessageDialog.openWarning(null, M_application.DelExcelAppNodeAction_4, M_application.DelExcelAppNodeAction_5);
				}
			}
		}
	}
}
