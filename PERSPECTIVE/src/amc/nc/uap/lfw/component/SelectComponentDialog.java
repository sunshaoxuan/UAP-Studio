package nc.uap.lfw.component;

import java.util.Map;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.dev.LfwComponent;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SelectComponentDialog extends DialogWithTitle{

	
	private Table componentTable;
	
	private String componentId;
	
	private String componentText;
	
	private Map<String,LfwComponent> componentMap;
	
	public SelectComponentDialog(Shell parentShell, String title) {
		super(parentShell, title);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setSize(500, 500);
		parent.setLocation(500, 200);
		Composite container = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);
		container.setLayout(new GridLayout(1, true));
		//Êý¾Ý
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grouId.setLayout(new GridLayout(2,false));
		
		Label label = new Label(grouId, SWT.NONE);
		label.setText(M_application.SelectComponentDialog_0);
		
		Text searchText = new Text(grouId, SWT.NONE);
		searchText.setLayoutData(new GridData(220,15));
		LFWDirtoryTreeItem treeItem = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
		String bcp = treeItem.getBcp().getName();
//		String moduleId = LFWPersTool.getCurrentProjectModuleName();
		String moduleId = LFWPersTool.getProjectModuleName(LFWPersTool.getCurrentProject());
		componentMap = LFWAMCConnector.getCacheComponentMap(moduleId, bcp);
		searchText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				Text text =(Text) e.widget;
				String str = text.getText();
				showselwindow(str);
				componentText = str;
			}
		});
		
		componentTable = new Table(container, SWT.BORDER);
		componentTable.setLayoutData(gd);
		componentTable.setHeaderVisible(true);
		setComponentData(componentTable);
		componentTable.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e){}

			public void widgetSelected(SelectionEvent e){
				componentId = ((TableItem)e.item).getText();
			}
			
		});
		
		return container;
	}
	
	private void showselwindow(String filter){
		
		componentTable.removeAll();
		
		for(LfwComponent comp:componentMap.values()){
			String fullId = null;
			if(comp.getPack()==null||comp.getPack().equals("")) //$NON-NLS-1$
				fullId = comp.getId();
			else fullId = comp.getPack() + "." + comp.getId(); //$NON-NLS-1$
			if(fullId.toLowerCase().indexOf(filter.toLowerCase())>-1){
				TableItem item = new TableItem(componentTable, SWT.NONE);
				item.setText(new String[]{fullId,comp.getName()});			
			}
		}
//		for(LfwComponent winConf : winConfList){
//			if(winConf.getId().toLowerCase().indexOf(filter.toLowerCase())>-1){
//				TableItem item = new TableItem(windowtable, SWT.NONE);
//				String winId = "";
//				if(winConf.getComponentId()==null||winConf.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
//					winId = winConf.getId();
//				else winId = winConf.getComponentId()+"."+winConf.getId();
//				item.setText(new String[]{winId, winConf.getCaption()});
//			}
//			
//		}
//		TableItem item = new TableItem(windowtable, SWT.NONE);
//		item.setText(new String[]{filter, filter});
	}
	
	private void setComponentData(Table t) {
		TableColumn tc1 = new TableColumn(t, SWT.CENTER);
		tc1.setText(M_application.SelectComponentDialog_1);
		tc1.setWidth(223);
		TableColumn tc2 = new TableColumn(t, SWT.CENTER);
		tc2.setText(M_application.SelectComponentDialog_2);
		tc2.setWidth(223);
		componentTable.removeAll();
		if(componentMap==null){
			TableItem item = new TableItem(componentTable, SWT.NONE);
			item.setText(new String[]{LfwUIComponent.ANNOYUICOMPONENT,M_application.SelectComponentDialog_3});			
		}else{
			for(LfwComponent comp:componentMap.values()){
				String fullId = null;
				if(comp.getPack()==null||comp.getPack().equals("")) //$NON-NLS-1$
					fullId = comp.getId();
				else fullId = comp.getPack() + "." + comp.getId(); //$NON-NLS-1$
				TableItem item = new TableItem(componentTable, SWT.NONE);
				item.setText(new String[]{fullId,comp.getName()});			
			}
		}
	}
	@Override
	protected void okPressed() {
		if(componentId==null&&componentText==null){
			MessageDialog.openError(null, M_application.SelectComponentDialog_4, M_application.SelectComponentDialog_5);
			return;
		}
		if(componentId==null){
			if(!MessageDialog.openConfirm(null, M_application.SelectComponentDialog_6, M_application.SelectComponentDialog_7+componentText)){
				return;
			}
		}
		super.okPressed();
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public String getComponentText() {
		return componentText;
	}
	public void setComponentText(String componentText) {
		this.componentText = componentText;
	}
	
	

}
