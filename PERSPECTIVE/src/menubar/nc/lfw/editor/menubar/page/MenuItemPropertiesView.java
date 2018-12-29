package nc.lfw.editor.menubar.page;

import java.util.List;

import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.action.DownMenuItemPropAction;
import nc.lfw.editor.menubar.action.EditMenuItemAction;
import nc.lfw.editor.menubar.action.UpMenuItemPropAction;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;
import nc.uap.lfw.perspective.editor.TableViewContentProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.EditorPart;

/**
 * 视图下单元格属性视图
 * 
 * @author guoweic
 *
 */
public class MenuItemPropertiesView extends Composite {
	
	private EditorPart editor = null;

	private TableViewer tv = null;
	
	private List<MenuItem> childList = null; 

	private MenuElementPart lfwElementPart = null;
	
	private MenuItemPropertiesView oThis;
	
	private boolean isMenuElement = false;
	
	
	public MenuElementPart getLfwElementPart() {
		return lfwElementPart;
	}

	public void setLfwElementPart(MenuElementPart lfwElementPart) {
		this.lfwElementPart = lfwElementPart;
	}

	public MenuItemPropertiesView(Composite parent, int style) {
		super(parent, style);
		this.oThis = this;
		createPartControl(this);
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ViewForm vf = new ViewForm(parent, SWT.NONE);
		vf.setLayout(new FillLayout());
		tv = new TableViewer(vf, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		createColumn(table, layout, 70, SWT.NONE, "ID"); //$NON-NLS-1$
		createColumn(table, layout, 80, SWT.NONE, M_menubar.MenuItemPropertiesView_1);
//		createColumn(table, layout, 100, SWT.NONE, "接受单据状态");
//		createColumn(table, layout, 100, SWT.NONE, "接受业务状态");
//		createColumn(table, layout, 100, SWT.NONE, "可见单据状态");
//		createColumn(table, layout, 100, SWT.NONE, "可见业务状态");
		createColumn(table, layout, 100, SWT.NONE, M_menubar.MenuItemPropertiesView_2);
		createColumn(table, layout, 100, SWT.NONE, M_menubar.MenuItemPropertiesView_3);
		createColumn(table, layout, 110, SWT.NONE, M_menubar.MenuItemPropertiesView_4);
		createColumn(table, layout, 110, SWT.NONE, M_menubar.MenuItemPropertiesView_5);
		createColumn(table, layout, 110, SWT.NONE, M_menubar.MenuItemPropertiesView_6);
		createColumn(table, layout, 110, SWT.NONE, M_menubar.MenuItemPropertiesView_7);
		createColumn(table, layout, 80, SWT.NONE, M_menubar.MenuItemPropertiesView_8);
		createColumn(table, layout, 100, SWT.NONE, M_menubar.MenuItemPropertiesView_9);
		createColumn(table, layout, 80, SWT.NONE, M_menubar.MenuItemPropertiesView_10);
		
		showPropertiesView();
		
		// 功能操作
//		Action addItem = new AddMenuItemAction(null);
//		Action delItem = new DelMenuItemAction(this);
		Action editItem = new EditMenuItemAction(this);
		Action upItem = new UpMenuItemPropAction(this);
		Action downItem = new DownMenuItemPropAction(this);
		// 功能菜单
		MenuManager mm = new MenuManager();
//		mm.add(addItem);
		Menu menu = mm.createContextMenu(table);
		table.setMenu(menu);
		// 功能工具条
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
//		tbm.add(addItem);
//		tbm.add(delItem);
		tbm.add(editItem);
		tbm.add(upItem);
		tbm.add(downItem);
		
		tbm.update(true);
		vf.setTopLeft(tb);
		vf.setContent(tv.getControl());
		
		
		// 双击事件行后，打开参数编辑器
		tv.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				Action editItem = new EditMenuItemAction(oThis);
				editItem.run();
			}
		});
		
	}
	
	/**
	 * 显示内容
	 */
	public void showPropertiesView() {
		tv.setContentProvider(new TableViewContentProvider());
		tv.setLabelProvider(new MenuItemPropertiesViewProvider());
		
		tv.setInput(childList);
	}

	private void createColumn(Table table, TableLayout layout, int width,
			int align, String text) {
		layout.addColumnData(new ColumnWeightData(width));
		new TableColumn(table, align).setText(text);
	}

	/**
	 * 保存参数
	 * @param item
	 */
	public void saveChildItem(MenuItem item) {
		// 更新参数列表
		for (int i = 0, n = childList.size(); i < n; i++) {
			if (item.getId().equals(childList.get(i).getId())) {
				childList.remove(i);
				childList.add(i, item);
				break;
			}
		}
		// 刷新表格
		tv.refresh();
		if(MenubarEditor.getActiveMenubarEditor() != null){
			MenubarEditor.getActiveMenubarEditor().setDirtyTrue();
			// 刷新图像显示
			MenubarEditor.getActiveMenubarEditor().refreshAllElementObj();
		}
		else if(ContextMenuEditor.getActiveMenubarEditor() != null){
			ContextMenuEditor.getActiveMenubarEditor().setDirtyTrue();
			ContextMenuEditor.getActiveMenubarEditor().refreshAllElementObj();
		}
		
	
	}
	
	/**
	 * 删除参数
	 * @param item
	public void deleteParam(MenuItem item) {
		// 更新参数列表
		for (int i = 0, n = childList.size(); i < n; i++) {
			if (item.getId().equals(childList.get(i).getId())) {
				childList.remove(i);
				break;
			}
		}
		((CommandEditor)editor).setDirtyTrue();
		// 刷新表格
		tv.setInput(childList);
		// 更新编辑器头部显示内容
		refreshEditorControlHeadText();
	}
	 */
	
	public TableViewer getTv() {
		return tv;
	}

	public void setTv(TableViewer tv) {
		this.tv = tv;
	}

	public EditorPart getEditor() {
		return editor;
	}

	public void setEditor(EditorPart editor) {
		this.editor = editor;
	}

	public List<MenuItem> getChildList() {
		return childList;
	}

	public void setChildList(List<MenuItem> childList) {
		this.childList = childList;
	}

	public boolean isMenuElement() {
		return isMenuElement;
	}

	public void setMenuElement(boolean isMenuElement) {
		this.isMenuElement = isMenuElement;
	}

}
