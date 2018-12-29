package nc.lfw.editor.common;

import nc.lfw.editor.common.extend.ExtendAttributesView;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.editor.propertiesview.EventPropertiesView;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.lang.M_lfw_core;
import nc.uap.lfw.perspective.views.ILFWViewPage;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;

/**
 * ViewPage基类
 * @author guoweic
 *
 */
public abstract class LFWAbstractViewPage extends Page implements ILFWViewPage {

	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;
	private Composite cellPropertiesView = null;
	private TabItem jsListenerItem = null;
	public Composite getComp() {
		return comp;
	}

	public void setComp(Composite comp) {
		this.comp = comp;
	}

	public TabFolder getVoTabFolder() {
		return voTabFolder;
	}

	/**
	 * 设置当前模型视图布局对象
	 * @param voTabFolder
	 */
	public void setVoTabFolder(TabFolder voTabFolder) {
		this.voTabFolder = voTabFolder;
	}
	
	public Composite getCellPropertiesView() {
		return cellPropertiesView;
	}

	public void setCellPropertiesView(Composite cellPropertiesView) {
		this.cellPropertiesView = cellPropertiesView;
	}

	public StackLayout getSl() {
		return sl;
	}

	public void setSl(StackLayout sl) {
		this.sl = sl;
	}

	public TabItem getCellPropTabItem() {
		return cellPropTabItem;
	}

	public void setCellPropTabItem(TabItem cellPropTabItem) {
		this.cellPropTabItem = cellPropTabItem;
	}
	
	/**
	 * 清空TabItem内容
	 * @author guoweic
	 * @param item
	 */
	protected void clearItemControl(TabItem item) {
		if (item != null)
			item.setControl(null);
	}

	
	public Control getControl() {
		return this.comp;
	}

	
	public void setFocus() {
		getControl().setFocus();
	}
	
	/**
	 * 增加控件属性页内容
	 * @author guoweic
	 */
	protected void addCellPropItemControl() {
		Control control = null;
		try { 
			control = cellPropTabItem.getControl();
		} catch (Exception e) {
			return;
		} finally {
			if (control == null) {
				if(cellPropertiesView != null)
					cellPropTabItem.setControl(cellPropertiesView);
			}
			voTabFolder.setSelection(cellPropTabItem);
		}
	}
	
//	/**
//	 * @author guoweic
//	 */

//	
//	private ListenerPropertiesView listenerPropertiesView = null;
//
//	public ListenerPropertiesView getListenerPropertiesView() {
//		return listenerPropertiesView;
//	}
//	
//	/**
//	 * 增加事件内容页
//	 * @author guoweic
//	 */
//	protected void addJsListenerItem(JsListenerConf jsListener) {
//		if (jsListenerItem == null) {
//			jsListenerItem = new TabItem(voTabFolder, SWT.NONE);
//			jsListenerItem.setText("事件");
//		}
//		listenerPropertiesView = new ListenerPropertiesView(voTabFolder, SWT.NONE, jsListener);
//		jsListenerItem.setControl(listenerPropertiesView);
//			voTabFolder.setSelection(jsListenerItem);
//	}
//	
//	/**
//	 * 增加事件内容页
//	 */
//	public void addListenerPropertiesView(JsListenerConf jsListener) {
//		Composite comp = getComp();
//		getSl().topControl = getVoTabFolder();
//		// 增加事件内容页
//		addJsListenerItem(jsListener);
//		// 清空控件属性页内容
////		clearItemControl(cellPropTabItem);
//		// 在listener属性表格页面中，记录当前编辑器页面
//		getListenerPropertiesView().setEditor((LFWBaseEditor) LFWBaseEditor.getActiveEditor());
//		if (comp != null) {
//			comp.layout();
//			if (!comp.isVisible())
//				comp.setVisible(true);
//		}
//	}
	
	private EventPropertiesView eventPropertiesView = null;
	
	private WebElement webElement = null;
	
	private UIElement uiElement = null;
	
	private UIMeta uimeta = null;
	/**
	 * v61，增加事件内容页
	 * @param eventConfs
	 * @param controllerClazz
	 */
	public void addEventPropertiesView(EventConf[] eventConfs, String controllerClazz){
		getSl().topControl = getVoTabFolder();
		// 增加事件内容页
		TabItem jsListenerItem = getJsListenerItem();
		if (jsListenerItem == null) {
			jsListenerItem = new TabItem(getVoTabFolder(), SWT.NONE);
			jsListenerItem.setText(WEBPersConstants.EVENT);
			setJsListenerItem(jsListenerItem);
		}
		
		Composite compo = null;
		if(eventConfs == null){
			eventConfs = new EventConf[0];
//			compo = new Composite(getVoTabFolder(), SWT.NONE);
			eventPropertiesView = new EventPropertiesView(getVoTabFolder(), SWT.NONE, eventConfs, controllerClazz, webElement, uiElement, uimeta);
			eventPropertiesView.setEditor(LFWBaseEditor.getActiveEditor());
			compo = eventPropertiesView;
		}
		else{
			eventPropertiesView = new EventPropertiesView(getVoTabFolder(), SWT.NONE, eventConfs, controllerClazz, webElement, uiElement, uimeta);
			eventPropertiesView.setEditor(LFWBaseEditor.getActiveEditor());
			compo = eventPropertiesView;
		}
		jsListenerItem.setControl(compo);
		getVoTabFolder().setSelection(jsListenerItem);
		
	}
	
	public WebElement getWebElement() {
		return webElement;
	}

	public void setWebElement(WebElement webElement) {
		this.webElement = webElement;
	}

	public UIElement getUiElement() {
		return uiElement;
	}

	public void setUiElement(UIElement uiElement) {
		this.uiElement = uiElement;
	}

	public UIMeta getUimeta() {
		return uimeta;
	}

	public void setUimeta(UIMeta uimeta) {
		this.uimeta = uimeta;
	}

	/**
	 * @author guoweic
	 */
	private TabItem extendAttributesItem = null;
	
	private ExtendAttributesView extendAttributesView = null;
	
	/**
	 * 增加扩展属性内容页
	 * @author guoweic
	 */
	protected void addExtendAttributesItem(WebElement webElement) {
		if (extendAttributesItem == null) {
			extendAttributesItem = new TabItem(voTabFolder, SWT.NONE);
			extendAttributesItem.setText(M_lfw_core.LFWAbstractViewPage_0);
		}
		extendAttributesView = new ExtendAttributesView(voTabFolder, SWT.NONE, webElement);
		extendAttributesItem.setControl(extendAttributesView);
		voTabFolder.setSelection(extendAttributesItem);
	}
	
	/**
	 * 增加扩展属性内容页
	 */
	public void addExtendAttributesView(WebElement webElement) {
		Composite comp = getComp();
		getSl().topControl = getVoTabFolder();
		// 增加扩展属性内容页
		addExtendAttributesItem(webElement);
		// 清空控件属性页内容
//		clearItemControl(cellPropTabItem);
		// 清空事件页内容
//		clearItemControl(jsListenerItem);
		// 在扩展属性表格页面中，记录当前编辑器页面
		getExtendAttributesView().setEditor((LFWBaseEditor) LFWBaseEditor.getActiveEditor());
		if (comp != null) {
			comp.layout();
			if (!comp.isVisible())
				comp.setVisible(true);
		}
	}
	
	public abstract void createControl(Composite parent);

	public abstract void selectionChanged(IWorkbenchPart part, ISelection selection);

	public TabItem getJsListenerItem() {
		return jsListenerItem;
	}
	
	public void setJsListenerItem(TabItem jsListenerItem){
		this.jsListenerItem = jsListenerItem;
	}

	public ExtendAttributesView getExtendAttributesView() {
		return extendAttributesView;
	}

	public EventPropertiesView getEventPropertiesView() {
		return eventPropertiesView;
	}

	public void setEventPropertiesView(EventPropertiesView eventPropertiesView) {
		this.eventPropertiesView = eventPropertiesView;
	}

}
