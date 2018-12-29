package nc.lfw.editor.menubar.page;

import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.MenubarGraph;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.lfw.editor.menubar.graph.MenubarGraphPart;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;

/**
 * 
 * @author guoweic
 *
 */
public class MenubarViewPage  extends LFWAbstractViewPage {

	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;
	private MenuItemPropertiesView cellPropertiesView = null;
	
	
	public void createControl(Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		voTabFolder = new TabFolder(comp, SWT.NONE);

		// 创建控件属性页
		cellPropTabItem = new TabItem(voTabFolder, SWT.NONE);
		cellPropTabItem.setText(M_menubar.MenubarViewPage_0);
		
		cellPropertiesView = new MenuItemPropertiesView(voTabFolder, SWT.NONE);
		cellPropTabItem.setControl(cellPropertiesView);
		
		voTabFolder.setSelection(cellPropTabItem);
		
		setVoTabFolder(voTabFolder);
		
		sl.topControl = voTabFolder;
		comp.layout();
	}


	
	public Control getControl() {
		return this.comp;
	}

	
	public void setFocus() {
		getControl().setFocus();
	}

	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// 为当前编辑器设置选中对象
		LFWBaseEditor.getActiveEditor().setCurrentSelection(selection);
		if (part instanceof MenubarEditor) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if (sel instanceof MenuElementPart) {
				Object model = ((MenuElementPart)sel).getModel();
				if(model instanceof MenubarElementObj){
					MenubarElementObj menubarObj = (MenubarElementObj) model;
					MenubarComp menubar = menubarObj.getMenubar();
					List<MenuItem> items = menubar.getMenuList();
					cellPropertiesView.setChildList(items);
					cellPropertiesView.getTv().setInput(items);
					cellPropertiesView.setMenuElement(false);
//					cellPropertiesView.getTv().expandAll();
					// 清空listener属性页内容
					//clearItemControl(getJsListenerItem());
					//voTabFolder.setSelection(cellPropTabItem);
					
					/**
					 * 选中Menubar本身的Listner事件
					 */
					// 取消所有其它子项选中状态
					LFWBaseEditor.getActiveEditor().getGraph().unSelectAllLabels();
					// 显示属性
					IViewPart[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews();
					PropertySheet sheet = null;
					for (int i = 0, n = views.length; i < n; i++) {
						if(views[i] instanceof PropertySheet) {
							sheet = (PropertySheet) views[i];
							break;
						}
					}
					if (sheet != null) {
						StructuredSelection select = new StructuredSelection(menubarObj);
						sheet.selectionChanged(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart(), select);
					}
					
					// 重新显示Listener内容
					if(MenubarEditor.getActiveMenubarEditor() != null){
						((MenubarGraph)MenubarEditor.getActiveMenubarEditor().getGraph())
								.reloadListenerFigure(menubar);
					}
				/******************/
				}
				else if(model instanceof MenuElementObj){
					MenuElementObj menuObj = (MenuElementObj) model;
					if(menuObj.getMenuItem()!=null){
						List<MenuItem> items = menuObj.getMenuItem().getChildList();
						cellPropertiesView.setChildList(items);
						cellPropertiesView.getTv().setInput(items);
						cellPropertiesView.setMenuElement(true);
	//					cellPropertiesView.getTv().expandAll();
						// 清空listener属性页内容
						clearItemControl(getJsListenerItem());
						voTabFolder.setSelection(cellPropTabItem);
					}
				}
				cellPropertiesView.setLfwElementPart((MenuElementPart) sel);
			} else if (sel instanceof MenubarGraphPart) {
				cellPropertiesView.setChildList(null);
				cellPropertiesView.getTv().setInput(null);
//				addEventPropertiesView(MenubarEditor.getActiveMenubarEditor().getMenubarTemp().getEventConfs(), MenubarEditor.getActiveMenubarEditor().getWidget().getControllerClazz());
			}
		}
	}

	public MenuItemPropertiesView getCellPropertiesView() {
		return cellPropertiesView;
	}

}
