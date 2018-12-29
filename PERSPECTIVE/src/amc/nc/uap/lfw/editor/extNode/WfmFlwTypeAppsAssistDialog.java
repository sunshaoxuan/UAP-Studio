package nc.uap.lfw.editor.extNode;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.wfm.vo.WfmFlwTypeVO;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uap.lfw.lang.M_extNode;

public class WfmFlwTypeAppsAssistDialog extends DialogWithTitle{

	private Table appListTable;
	private String appId;
	private String id;
	public WfmFlwTypeAppsAssistDialog(String title) {
		super(null, title);
	}
	public WfmFlwTypeAppsAssistDialog(String title, WfmFlwTypeVO flwType) {
		super(null, title);
	}

	public WfmFlwTypeAppsAssistDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	
	protected Control createDialogArea(Composite parent) {
		parent.setSize(500, 500);
		Composite container = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);
		container.setLayout(new GridLayout(1, true));
		
		appListTable = new Table(container, SWT.BORDER);
		appListTable.setLayoutData(gd);
		appListTable.setHeaderVisible(true);
		setApplicationData(appListTable);
		return container;
	}
	
	private void setApplicationData(Table t) {
		//列WindowID
		TableColumn tc1 = new TableColumn(t, SWT.CENTER);
		tc1.setText("Application ID");
		tc1.setWidth(223);
		//列WindowName
		TableColumn tc2 = new TableColumn(t, SWT.CENTER);
		tc2.setText("APPSNode ID");
		tc2.setWidth(223);
		
		CpAppsNodeVO[] vos = LFWWfmConnector.getAppsNodeVOsByCondition(" 1 = 1 order by ts desc");
		int len = vos != null ? vos.length : 0;
		for(int i = 0; i < len; i++){
			if(vos[i] == null){
				continue;
			}
			TableItem item = new TableItem(t, SWT.NONE);
			item.setText(new String[]{vos[i].getAppid(), vos[i].getId()});
		}
	}
	
//	private Collection<Application> getApplicationList() {
//		TreeItem item = LFWPersTool.getCurrentTreeItem();
//		String bcpId = LFWPersTool.getBcpId(item);
//		String moduleId = LFWPersTool.getProjectModuleName(LFWPersTool.getCurrentProject());
//		Map<String, Application> appsMap = LFWAMCConnector.getApplications(moduleId, bcpId);
//		if(appsMap == null)
//			return new ArrayList<Application>();
//		return appsMap.values();
//	}
	
	protected void okPressed() {
		TableItem[] items = appListTable.getSelection();
		if(items == null || items.length == 0){
			MessageDialog.openError(this.getShell(), M_extNode.WfmFlwTypeAppsAssistDialog_0000/*错误*/, M_extNode.WfmFlwTypeAppsAssistDialog_0001/*请先选中功能节点*/);
		}
		TableItem item = items[0];
		this.appId = item.getText(0);
		this.id = item.getText(1);
		super.okPressed();
	}

	public String getAppId() {
		return this.appId;
	}
	
	public String getId(){
		return this.id;
	}
	
}
