/**
 * 
 */
package nc.uap.lfw.editor.window;

import java.util.Collections;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;

import org.eclipse.core.resources.IProject;
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
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * Window列表显示对话框类
 * @author chouhl
 *
 */
public class WindowListDialog extends DialogWithTitle {
	
	private String windowId = null;
	
	private String windowName = null;
	
	private String applicationId = null;
	
	private String applicationName = null;
	
	private IProject project = null;
	
	private TreeItem currentTreeItem = null;
	
	private Table windowtable;
	
	private List<LfwWindow> winConfList;

	public WindowListDialog(String title, IProject project, TreeItem currentTreeItem) {
		super(null, title);
		this.project = project;
		this.currentTreeItem = currentTreeItem;
	}
	
	public WindowListDialog(Shell parentShell, String title, IProject project, TreeItem currentTreeItem) {
		super(parentShell, title);
		this.project = project;
		this.currentTreeItem = currentTreeItem;
	}
	
	protected Control createDialogArea(Composite parent) {
		parent.setSize(500, 500);
		Composite container = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);
		container.setLayout(new GridLayout(1, true));
		//数据
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grouId.setLayout(new GridLayout(2,false));
		
		Label label = new Label(grouId, SWT.NONE);
		label.setText(M_editor.WindowListDialog_0);
		
		Text searchText = new Text(grouId, SWT.NONE);
		searchText.setLayoutData(new GridData(220,15));
		searchText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				Text text =(Text) e.widget;
				String str = text.getText();
				showselwindow(str);
			}
		});
		
		windowtable = new Table(container, SWT.BORDER);
		windowtable.setLayoutData(gd);
		windowtable.setHeaderVisible(true);
		setWindowData(windowtable);
		windowtable.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e){}

			public void widgetSelected(SelectionEvent e){
				windowId = ((TableItem)e.item).getText(0);
				windowName = ((TableItem)e.item).getText(1);
				applicationId = ((LFWBasicTreeItem)LFWAMCPersTool.getCurrentTreeItem()).getId();
				applicationName = ((LFWBasicTreeItem)LFWAMCPersTool.getCurrentTreeItem()).getItemName();
			}
			
		});
		
		return container;
	}

	private void setWindowData(Table t) {
		//列WindowID
		TableColumn tc1 = new TableColumn(t, SWT.CENTER);
		tc1.setText(M_editor.WindowListDialog_1);
		tc1.setWidth(223);
		//列WindowName
		TableColumn tc2 = new TableColumn(t, SWT.CENTER);
		tc2.setText(M_editor.WindowListDialog_2);
		tc2.setWidth(223);
		//列数据
		winConfList = LFWAMCConnector.getCacheWindows();
//		List<PageMeta> winConfList = LFWPersTool.getAllPagemeta();
		windowsort(winConfList);
		for(LfwWindow winConf : winConfList){
			TableItem item = new TableItem(t, SWT.NONE);
			String winId = "";
			if(winConf.getComponentId()==null||winConf.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
				winId = winConf.getId();
			else winId = winConf.getComponentId()+"."+winConf.getId();
			item.setText(new String[]{winId, winConf.getCaption()});
		}
		
	}
	private void showselwindow(String filter){
//		List<LfwWindow> winConfList = LFWAMCConnector.getCacheWindows();
		windowtable.removeAll();
//		windowsort(winConfList);
		for(LfwWindow winConf : winConfList){
			String winId = "";
			if(winConf.getComponentId()==null||winConf.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
				winId = winConf.getId();
			else winId = winConf.getComponentId()+"."+winConf.getId();
			if(winId.toLowerCase().indexOf(filter.toLowerCase())>-1){
				TableItem item = new TableItem(windowtable, SWT.NONE);
//				String winId = "";
//				if(winConf.getComponentId()==null||winConf.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
//					winId = winConf.getId();
//				else winId = winConf.getComponentId()+"."+winConf.getId();
				item.setText(new String[]{winId, winConf.getCaption()});
			}
			
		}
//		TableItem item = new TableItem(windowtable, SWT.NONE);
//		item.setText(new String[]{filter, filter});
	}
	
	private List<LfwWindow> windowsort(List<LfwWindow> winList){
		Collections.sort(winList);
		return winList;
	}
	
	protected void okPressed() {
		if(windowId == null || windowName == null){
			MessageDialog.openInformation(null, M_editor.WindowListDialog_3, M_editor.WindowListDialog_4);
			return;
		}
		super.okPressed();
	}

	public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public TreeItem getCurrentTreeItem() {
		return currentTreeItem;
	}

	public void setCurrentTreeItem(TreeItem currentTreeItem) {
		this.currentTreeItem = currentTreeItem;
	}

}
