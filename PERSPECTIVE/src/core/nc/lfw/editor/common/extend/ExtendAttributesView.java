package nc.lfw.editor.common.extend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_pagemeta;
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

/**
 * 视图下扩展属性视图
 * 
 * @author guoweic
 *
 */
public class ExtendAttributesView extends Composite {

	private LFWBaseEditor editor = null;

	private TableViewer tv = null;
	
	private WebElement webElement;
	
	private List<ExtAttribute> attributeList;

	public ExtendAttributesView(Composite parent, int style, WebElement webElement) {
		super(parent, style);
		this.webElement = webElement;
		createPartControl(this);
	}

	private Action editExtendAttributeAction;
	
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
		createColumn(table, layout, 80, SWT.NONE, M_pagemeta.ExtendAttributesView_0);
		createColumn(table, layout, 80, SWT.NONE, M_pagemeta.ExtendAttributesView_1);
		createColumn(table, layout, 200, SWT.NONE, M_pagemeta.ExtendAttributesView_2);
		
		showPropertiesView();
		
		// 功能操作
		Action addExtendAttributeAction = new AddExtendAttributeAction(this);
		editExtendAttributeAction = new EditExtendAttributeAction(this);
		Action delExtendAttributeAction = new DelExtendAttributeAction(this);
		// 功能菜单
		MenuManager mm = new MenuManager();
		mm.add(addExtendAttributeAction);
//		mm.add(editExtendAttributeAction);
		mm.add(delExtendAttributeAction);
		Menu menu = mm.createContextMenu(table);
		table.setMenu(menu);
		// 功能工具条
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(addExtendAttributeAction);
//		tbm.add(editExtendAttributeAction);
		tbm.add(delExtendAttributeAction);
		tbm.update(true);
		vf.setTopLeft(tb);
		
		vf.setContent(tv.getControl());
		
		// 双击某行后，进行编辑
		tv.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				editExtendAttributeAction.run();
			}
		});
		
	}
	
	/**
	 * 显示内容
	 */
	public void showPropertiesView() {
		tv.setContentProvider(new TableViewContentProvider());
		tv.setLabelProvider(new ExtAttributesTableViewLabelProvider());
		
		Map<String, ExtAttribute> attributeMap = webElement.getExtendMap();
		attributeList = new ArrayList<ExtAttribute>();
		Set<String> keySet = attributeMap.keySet();
		for (String key : keySet) {
			ExtAttribute att = (ExtAttribute) attributeMap.get(key);
			attributeList.add(att);
		}
		tv.setInput(attributeList);
	}
	
	/**
	 * 保存扩展属性
	 * @param attribute
	 */
	public void saveExtendAttribute(ExtAttribute attribute) {
		// 更新事件列表
		for (int i = 0, n = attributeList.size(); i < n; i++) {
			if (attribute.getKey().equals(attributeList.get(i).getKey())) {
				attributeList.remove(i);
				break;
			}
		}
		attributeList.add(attribute);
		webElement.getExtendMap().put(attribute.getKey(), attribute);
		// 保存
//		editor.doSave(editor.getMonitor());
		editor.setDirtyTrue();
		// 刷新表格
		tv.setInput(attributeList);
	}
	
	/**
	 * 删除扩展属性
	 * @param attribute
	 */
	public void deleteExtendAttribute(ExtAttribute attribute) {
		// 更新事件列表
		for (int i = 0, n = attributeList.size(); i < n; i++) {
			if (attribute.getKey().equals(attributeList.get(i).getKey())) {
				attributeList.remove(i);
				break;
			}
		}
		webElement.getExtendMap().remove(attribute.getKey());
		// 保存
//		editor.doSave(editor.getMonitor());
		editor.setDirtyTrue();
		// 刷新表格
		tv.setInput(attributeList);
	}

	private void createColumn(Table table, TableLayout layout, int width,
			int align, String text) {
		layout.addColumnData(new ColumnWeightData(width));
		new TableColumn(table, align).setText(text);
	}

	public TableViewer getTv() {
		return tv;
	}

	public void setTv(TableViewer tv) {
		this.tv = tv;
	}

	public LFWBaseEditor getEditor() {
		return editor;
	}

	public void setEditor(LFWBaseEditor editor) {
		this.editor = editor;
	}

	public List<ExtAttribute> getAttributeList() {
		return attributeList;
	}

}
