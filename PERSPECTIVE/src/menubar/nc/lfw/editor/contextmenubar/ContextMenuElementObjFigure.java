package nc.lfw.editor.contextmenubar;

import java.util.Iterator;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.menubar.MenuItemLabel;
import nc.lfw.editor.menubar.MenubarConnector;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * 右键菜单Figure
 * @author zhangxya
 *
 */
public class ContextMenuElementObjFigure extends LFWBaseRectangleFigure {

	private ContextMenuElementObj menubarObj;

	// 默认大小
	private Dimension dimen;
	// 总高度
	private int height = 0;

	private static Color bgColor = new Color(null, 109, 115, 203);

	public ContextMenuElementObjFigure(LfwElementObjWithGraph ele) {
		super(ele);
		menubarObj = (ContextMenuElementObj) ele;
		setTypeLabText(M_menubar.ContextMenuElementObjFigure_0); //$NON-NLS-1$
		setBackgroundColor(bgColor);
		menubarObj.setFigure(this);
		if(menubarObj.getMenubar() != null)
			setTitleText(menubarObj.getMenubar().getId(), menubarObj.getMenubar().getId());
		addItems();
		markError(menubarObj.validate());
		// 设置大小和位置
		Point point = menubarObj.getLocation();
		dimen = menubarObj.getSize();
		this.height += 3 * LINE_HEIGHT;
		setBounds(new Rectangle(point.x, point.y, dimen.width,
				dimen.height < this.height ? this.height : dimen.height));

	}

	/**
	 * 显示所有子项
	 */
	private void addItems() {
		ContextMenuComp menubar = menubarObj.getMenubar();
		if(menubar == null)
			return;
		List<MenuItem> itemList = menubar.getItemList();
		if (itemList != null) {
			Iterator<MenuItem> it = itemList.iterator();
			while (it.hasNext()) {
				MenuItem item = it.next();
				MenuItemLabel label = new MenuItemLabel(item);
				addToContent(label);
				this.height += LINE_HEIGHT;
				addItemLabelListener(label);
			}
		}
	}

	/**
	 * 增加子项Label的事件
	 * 
	 * @param label
	 */
	private void addItemLabelListener(MenuItemLabel label) {
		label.addMouseListener(new MouseListener.Stub() {
			public void mouseDoubleClicked(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {
				MenuItemLabel currentLabel = (MenuItemLabel) e.getSource();
				// 取消所有其它子项选中状态
				LFWBaseEditor.getActiveEditor().getGraph().unSelectAllLabels();
				// 选中该子项
				selectLabel(currentLabel);

				// 显示属性
				MenuItem currentItem = (MenuItem) ((MenuItemLabel) currentLabel).getEditableObj();
				menubarObj.setCurrentItem(currentItem);
				
				// 重新显示属性内容
				reloadPropertySheet(menubarObj);
				//新版显示事件
				LFWBaseEditor.getActiveEditor().getViewPage().setWebElement(currentItem);
				LFWBaseEditor.getActiveEditor().getViewPage().addEventPropertiesView(currentItem.getEventConfs(), LFWAMCPersTool.getCurrentWidget().getControllerClazz());
				
				// 重新显示Listener内容
				if(ContextMenuEditor.getActiveMenubarEditor() != null){
					((ContextMenuGrahp)ContextMenuEditor.getActiveMenubarEditor().getGraph())
							.reloadListenerFigure((MenuItem)currentLabel.getEditableObj());
				}
			}
		});
	}

	/**
	 * 增加子项
	 * 
	 * @param signal
	 */
	public void addItem(MenuItem menuItem) {
		ContextMenuComp menubar = ContextMenuEditor.getActiveMenubarEditor().getMenubarTemp();//getActiveMenubarEditor().getMenubarTemp();
		int index = 0;
		if (null != menubar.getItemList())
			index = menubar.getItemList().size();
		menubar.addMenuItem(menuItem);
		MenuItemObj menuItemObj = new MenuItemObj();
		menuItemObj.setMenuItem(menuItem);
		menuItemObj.setId(menuItem.getId());
		menubarObj.addChild(menuItemObj);
		MenuItemLabel label = new MenuItemLabel(menuItem);
		addToContent(label, index);
		addItemLabelListener(label);
		this.height += LINE_HEIGHT;
		resizeHeight();
	}

	/**
	 * 删除子项
	 * 
	 * @param label
	 */
	public void deleteItem(MenuItemLabel label) {
		MenuItem item = (MenuItem) label.getEditableObj();
		ContextMenuComp menubar = ContextMenuEditor.getActiveMenubarEditor().getMenubarTemp();
		if (menubar.getItemList().contains(item)) {
			menubar.getItemList().remove(item);
			// 删除相关连接
			ContextMenuEditor editor = ContextMenuEditor.getActiveMenubarEditor();
			MenubarConnector connector = editor.getConnector(item.getId());
			if (null != connector) {
				editor.removeConnector(item.getId());
				connector.disConnect();
			}
		}
		
		getContentFigure().remove(label);
		this.height -= LINE_HEIGHT;
		resizeHeight();
	}
	
	/**
	 * 刷新图像
	 */
	public void refresh() {
		IFigure title = (IFigure) getTitleFigure().getChildren().get(1);
		getTitleFigure().remove(title);
		setTitleText(menubarObj.getMenubar().getId(), menubarObj.getMenubar().getId());
		refreshItems();
	}
	
	/**
	 * 刷新所有子项的显示
	 */
	public void refreshItems() {
		// 移除全部原有项
		while (getContentFigure().getChildren().size() > 0) {
			Object child = getContentFigure().getChildren().get(0);
			if (child instanceof MenuItemLabel) {
				getContentFigure().remove((MenuItemLabel) child);
				this.height -= LINE_HEIGHT;
			}
		}
		// 显示所有子项
		addItems();
	}
	

//	private void removeCommands(MenuItem item) {
//		PageMeta pagemeta = LFWPersTool.getCurrentPageMeta();
//		Map<String, JsListenerConf> listenerMap = item.getListenerMap();
//		for (String listenerKey : listenerMap.keySet()) {
//			Map<String, EventHandlerConf> eventMap = listenerMap.get(listenerKey).getEventHandlerMap();
//			for (String eventKey : eventMap.keySet()) {
//				if (eventMap.get(eventKey) instanceof RefEventHandlerConf) {
//					String refId = ((RefEventHandlerConf)eventMap.get(eventKey)).getRefId();
//					pagemeta.removeCommand(refId);
//				}
//			}
//		}
//	}

	/**
	 * 重新设置高度
	 */
	private void resizeHeight() {
		setSize(dimen.width, dimen.height < this.height ? this.height
				: dimen.height);
	}

	
	protected String getTypeText() {
		return null;
	}

}
