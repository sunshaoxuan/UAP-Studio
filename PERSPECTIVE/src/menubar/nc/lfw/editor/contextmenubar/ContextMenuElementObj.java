package nc.lfw.editor.contextmenubar;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * 右键菜单model
 * @author zhangxya
 *
 */
public class ContextMenuElementObj extends LfwElementObjWithGraph {

	private static final long serialVersionUID = -1888132777640590427L;
	
	public static final String PROP_ITEM_ID = "ITEM_ID"; //$NON-NLS-1$
	public static final String PROP_ITEM_TEXT = "ITEM_TEXT"; //$NON-NLS-1$
	public static final String PROP_ITEM_MENU = "ITEM_MENU"; //$NON-NLS-1$
	public static final String PROP_ITEM_PARAM = "ITEM_PARAM_"; //$NON-NLS-1$
	
	private ContextMenuComp menubar;
	private List<MenuItemObj> childrenList = new ArrayList<MenuItemObj>();
	
	// 引用Event参数列表
//	List<CmdParameter> refParamList = null;
	
	/**
	 * 当前选中的子项
	 */
	private MenuItem currentItem;
	
	private ContextMenuElementObjFigure figure;

	public ContextMenuComp getMenubar() {
		return menubar;
	}

	public static final String PROP_DATASET_ELEMENT = "dataset_element"; //$NON-NLS-1$
	
	public static final String WIDGETID_EN = "widgetid"; //$NON-NLS-1$
	public static final String DSID_EN = "dsid"; //$NON-NLS-1$

	public void setMenubar(ContextMenuComp menubar) {
		this.menubar = menubar;
		fireStructureChange(PROP_DATASET_ELEMENT, menubar);
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		int paramsCount = 0;
		// 清空参数列表
//		refParamList = null;
//		if (null != currentItem) {
//			Map<String, JsListenerConf> listenerMap = currentItem.getListenerMap();
//			for (String listenerKey : listenerMap.keySet()) {
//				JsListenerConf listener = listenerMap.get(listenerKey);
//				Map<String, EventHandlerConf> eventMap = listener.getEventHandlerMap();
//				for (String eventKey : eventMap.keySet()) {
//					if (eventMap.get(eventKey) instanceof RefEventHandlerConf) {
//						refParamList = ((RefEventHandlerConf)eventMap.get(eventKey)).getCmdParamList();
//						paramsCount += refParamList.size();
//					}
//				}
//			}
//		}
		PropertyDescriptor[] pds = new PropertyDescriptor[4 + paramsCount];
		pds[0] = new NoEditableTextPropertyDescriptor(PROP_ID, "ID"); //$NON-NLS-1$
		pds[0].setCategory(M_menubar.ContextMenuElementObj_0); //$NON-NLS-1$
		pds[1] = new TextPropertyDescriptor(PROP_ITEM_MENU, M_menubar.ContextMenuElementObj_1); //$NON-NLS-1$
		pds[1].setCategory(M_menubar.ContextMenuElementObj_0); //$NON-NLS-1$
		pds[2] = new NoEditableTextPropertyDescriptor(PROP_ITEM_ID, "ID"); //$NON-NLS-1$
		pds[2].setCategory(M_menubar.ContextMenuElementObj_2); //$NON-NLS-1$
		pds[3] = new TextPropertyDescriptor(PROP_ITEM_TEXT, M_menubar.ContextMenuElementObj_3); //$NON-NLS-1$
		pds[3].setCategory(M_menubar.ContextMenuElementObj_2); //$NON-NLS-1$
		//TODO
//		if (null == refParamList)
//			refParamList = new ArrayList<CmdParameter>();
//		if (null != currentItem) {
//			int index = 3;
//			for (Parameter parameter : refParamList) {
//				if (parameter.getName().toLowerCase().indexOf(WIDGETID_EN) != -1) {  // 是WidgetId参数
//					pds[index] = new ComboBoxPropertyDescriptor(PROP_ITEM_PARAM + parameter.getName(), parameter.getName(), getWidgetIdArray());
//				} else if (parameter.getName().toLowerCase().indexOf(DSID_EN) != -1) {  // 是DsId参数
//					//TODO
//					String currentWidgetId = getCurrentWidgetId();
//					pds[index] = new ComboBoxPropertyDescriptor(PROP_ITEM_PARAM + parameter.getName(), parameter.getName(), getDsIdArray(currentWidgetId));
//				} else {
//					pds[index] = new TextPropertyDescriptor(PROP_ITEM_PARAM + parameter.getName(), parameter.getName());
//				}
//				pds[index].setCategory("菜单项参数");
//				
//				index++;
//			}
//		}
		
		return pds;
	}

	public Object getPropertyValue(Object id) {
		ContextMenuComp menu = this.getMenubar();
		if (PROP_ID.equals(id)) {
			return menu.getId() == null ? "" : menu.getId(); //$NON-NLS-1$
		} else if (PROP_ITEM_MENU.equals(id)) {
			return menu.getContextMenu() == null ? "" : menu.getContextMenu(); //$NON-NLS-1$
		} else if (PROP_ITEM_ID.equals(id)) {
			if (null != currentItem)
				return currentItem.getId();
		} else if (PROP_ITEM_TEXT.equals(id)) {
			if (null != currentItem)
				return currentItem.getText();
		} else if (((String)id).indexOf(PROP_ITEM_PARAM) != -1) {
//			if (null != currentItem) {
//				String name = ((String)id).substring(((String)id).lastIndexOf('_') + 1);
//				for (CmdParameter parameter : refParamList) {
//					if (name.equals(parameter.getName())) {
//						if (name.toLowerCase().indexOf(WIDGETID_EN) != -1) {  // 是WidgetId参数
//							for (int i = 0, n = widgetIdArray.length; i < n; i++) {
//								if (null != parameter.getValue() && widgetIdArray[i].equals(parameter.getValue())) {
//									return i;
//								}
//							}
//							return 0;
//						} else if (name.toLowerCase().indexOf(DSID_EN) != -1) {  // 是DsId参数
//							//TODO
//							String currentWidgetId = getCurrentWidgetId();
//							String[] dsIdArray = getDsIdArray(currentWidgetId);
//							for (int i = 0, n = dsIdArray.length; i < n; i++) {
//								if (null != parameter.getValue() && dsIdArray[i].equals(parameter.getValue())) {
//									return i;
//								}
//							}
//							return 0;
//						} else {
//							return parameter.getValue() == null ? "" : parameter.getValue();
//						}
//					}
//				}
//				return "";
//			}
		}
		return ""; //$NON-NLS-1$
	}

	
	public void setPropertyValue(Object id, Object value) {		
//			if (null != currentItem) {
//				String name = ((String)id).substring(((String)id).lastIndexOf('_') + 1);
//				for (CmdParameter parameter : refParamList) {
//					if (name.equals(parameter.getName())) {
//						//TODO
//						if (name.toLowerCase().indexOf(WIDGETID_EN) != -1) {  // 是WidgetId参数
//							//TODO
//							parameter.setValue(widgetIdArray[((Integer) value).intValue()]);
//							// 重新显示属性内容
//							MenubarElementFigure.reloadPropertySheet(this);
//						} else if (name.toLowerCase().indexOf(DSID_EN) != -1) {  // 是DsId参数
//							//TODO
//							parameter.setValue(getDsIdArray(getCurrentWidgetId())[((Integer) value).intValue()]);
//						} else {
//							parameter.setValue((String) value);
//						}
//					}
//				}
//			}
		if(ContextMenuEditor.getActiveMenubarEditor()==null){
			MessageDialog.openWarning(null, M_menubar.ContextMenuElementObj_4, M_menubar.ContextMenuElementObj_5); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else{
			ContextMenuComp menu = this.getMenubar();
			if (PROP_ID.equals(id)) {
				menu.setId((String) value);
			} else if (PROP_ITEM_MENU.equals(id)) {
				menu.setContextMenu((String) value);
			} else if (PROP_ITEM_ID.equals(id)) {
				if (null != currentItem)
					currentItem.setId((String) value);
			} else if (PROP_ITEM_TEXT.equals(id)) {
				if (null != currentItem)
					currentItem.setText((String) value);
			} else if (((String)id).indexOf(PROP_ITEM_PARAM) != -1) {
			}
			ContextMenuEditor.getActiveMenubarEditor().refreshAllElementObj();
			ContextMenuEditor.getActiveMenubarEditor().setDirtyTrue();
		}
	}

	public Object getEditableValue() {
		return this;
	}

	public void addChild(MenuItemObj obj) {
		// 如果有该子菜单，则只改变该子菜单的子菜单对象
		for (int i = 0, n = childrenList.size(); i < n; i++) {
			if (obj.getMenuItem().getId().equals(childrenList.get(i).getMenuItem().getId())) {
				childrenList.get(i).setChild(obj.getChild());
				return;
			}
		}
		childrenList.add(obj);
		// 如果已有该项，则不增加
		if (!menubar.getItemList().contains(obj.getMenuItem()))
			menubar.addMenuItem(obj.getMenuItem());
	}

	public List<MenuItemObj> getChildrenList() {
		return childrenList;
	}

	public boolean removeChild(MenuItemObj obj) {
		menubar.getItemList().remove(obj.getMenuItem());
		return childrenList.remove(obj);
	}

	
	public WebElement getWebElement() {
		return menubar;
	}

	public ContextMenuElementObjFigure getFigure() {
		return figure;
	}

	public void setFigure(ContextMenuElementObjFigure figure) {
		this.figure = figure;
	}

	public void setCurrentItem(MenuItem currentItem) {
		this.currentItem = currentItem;
	}
}
