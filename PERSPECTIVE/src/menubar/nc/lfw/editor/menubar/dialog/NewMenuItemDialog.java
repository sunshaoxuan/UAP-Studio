package nc.lfw.editor.menubar.dialog;

import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.contextmenubar.ContextMenuGrahp;
import nc.lfw.editor.menubar.MenubarGraph;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author guoweic
 *
 */
public class NewMenuItemDialog extends DialogWithTitle {

	private Text idText;
	private String itemId;

	private Text textText;
	private String text;
	
	private MenuItem parentMenuItem;
	private LfwElementObjWithGraph menuObj;
	
	
	public NewMenuItemDialog(Shell parentShell, String title) {
		super(parentShell, title);

	}

	protected void okPressed() {
		itemId = idText.getText().trim();
		if (itemId == null || "".equals(itemId)) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_menubar.NewMenuItemDialog_0, M_menubar.NewMenuItemDialog_1);
			idText.setFocus();
			return;
		}
		//TODO 唯一性校验
//		List<LfwWidgetConf> list = graph.getPagemeta().getWidgetConfList();
//		for (int i = 0, n = list.size(); i < n; i++) {
//			if (list.get(i).getId().equals(subMenuId)) {
//				MessageDialog.openWarning(this.getShell(), "提示", "该子菜单已存在！");
//				idText.setFocus();
//				return;
//			}
//		}
		//TODO 唯一性校验
//		((MenubarGraph)menuObj.getGraph()).getChildrenList().
		
	
		LfwBaseGraph graph = menuObj.getGraph();
		List<MenuElementObj> menuElementObjs = null;
		if(graph.getCells().get(0) instanceof MenubarElementObj){
			MenubarElementObj obj = (MenubarElementObj)graph.getCells().get(0);
			for(MenuItemObj menuItem : obj.getChildrenList()){
				if(menuItem.getMenuItem().getId().equals(itemId)){
					MessageDialog.openInformation(null, M_menubar.AddMenuItemAction_2, M_menubar.AddMenuItemAction_3);
					return;
				}
			}
			menuElementObjs = ((MenubarGraph)graph).getAllChildren();
		}
		else if(graph.getCells().get(0) instanceof ContextMenuElementObj){
			ContextMenuElementObj obj = (ContextMenuElementObj)graph.getCells().get(0);
			for(MenuItemObj menuItem : obj.getChildrenList()){
				if(menuItem.getMenuItem().getId().equals(itemId)){
					MessageDialog.openInformation(null, M_menubar.AddMenuItemAction_2, M_menubar.AddMenuItemAction_3);
					return;
				}
			}
			menuElementObjs = ((ContextMenuGrahp)graph).getAllChildren();
		}
		
		if(menuElementObjs!=null&&menuElementObjs.size()>0){
			for(MenuElementObj obj:menuElementObjs){
				for(MenuItem menuItem:obj.getChildrenItems()){
					if(menuItem.getId().equals(itemId)){
						MessageDialog.openInformation(null, M_menubar.AddMenuItemAction_2, M_menubar.AddMenuItemAction_3);
						return;
					}
				}
			}
		}

		try{
			LFWTool.idCheck(itemId);
		}catch(Exception e){
			MessageDialog.openError(null, "", e.getMessage());
			return;
		}
		
		text = textText.getText().trim();
		if (text == null || "".equals(text)) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_menubar.NewMenuItemDialog_0, M_menubar.NewMenuItemDialog_2);
			textText.setFocus();
			return;
		}
		super.okPressed();
	}

	protected Point getInitialSize() {
		return new Point(350,180);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText("ID:"); //$NON-NLS-1$
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(createGridData(200, 3));

		new Label(container, SWT.NONE).setText(M_menubar.NewMenuItemDialog_3);
		textText = new Text(container, SWT.BORDER);
		textText.setLayoutData(createGridData(200, 3));
		
		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}

	public Text getIdText() {
		return idText;
	}

	public void setParentMenuItem(MenuItem parentMenuItem) {
		this.parentMenuItem = parentMenuItem;
	}

	public MenuItem getParentMenuItem() {
		return parentMenuItem;
	}

	
	public String getItemId() {
		return itemId;
	}

	public String getText() {
		return text;
	}
	public void setMenuObj(LfwElementObjWithGraph menuObj) {
		this.menuObj = menuObj;
	}

}
