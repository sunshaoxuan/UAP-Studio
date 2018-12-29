package nc.lfw.editor.widget;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.pagemeta.PagemetaGraph;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Widget元素对象
 * @author guoweic
 *
 */
public class WidgetElementObj extends LfwElementObjWithGraph {

	private static final long serialVersionUID = -8484960457664556121L;
	
	
	/*widget对象*/
	private LfwView widget = null;
	/*当前图像*/
	private WidgetElementFigure figure;
	/*plugoutDesc列表*/
	private List<PlugoutDescElementObj> plugoutCells = new ArrayList<PlugoutDescElementObj>();
	/*pluginDesc列表*/
	private List<PluginDescElementObj> pluginCells = new ArrayList<PluginDescElementObj>();
	
	private List<Connector> connectorList = new ArrayList<Connector>();
	
	private String errorMsg;

	// 是否是主Widget
//	private boolean isMainWidget = false;
//	private String refId = "";

	public static final String PROP_CHILD_ADD = "prop_child_add";
	public static final String PROP_CHILD_REMOVE = "prop_child_remove";

//	public static final String PROP_ISDIALOG = "element_ISDIALOG";
//	public static final String PROP_ISMAIN = "element_ISMAIN";
	public static final String PROP_WIDGET_ID = "WIDGET_ID";
//	public static final String PROP_HEIGHT = "WIDGET_height";
//	public static final String PROP_WIDTH = "WIDGET_width";
	public static final String PROP_I18NNAME="WIDGET_i18nname";

	/**
	 * 获取源Widget所有未使用信号
	 * @param connectorMap
	 * @param sourceWidget
	 * @return
	public static List<String> getUnUsedSignal(Map<String, Connector> connectorMap, WidgetElementObj sourceWidget) {
//		List<String> signalList = new ArrayList<String>();
//		List<String> resultList = new ArrayList<String>();
//		// 源Widget所有信号
//		Set<String> signalSet = sourceWidget.getWidget().getSignalMap().keySet();
//		for (String signalId : signalSet) {
//			signalList.add(signalId);
//			resultList.add(signalId);
//		}
//		// 迭代连接器对象
//		Iterator it = connectorMap.keySet().iterator();
//		while (it.hasNext()) {
//			Connector connector = connectorMap.get(it.next());
//			if (connector.getSource().equals(sourceWidget.getWidget().getId())) {  // 是相同源Widget的连接器对象
//				for (int i = 0, n = signalList.size(); i < n; i++) {
//					// 如果该信号已被使用，则从列表中删除
//					if (connector.getSignal().equals(signalList.get(i)))
//						resultList.remove(connector.getSignal());
//				}
//			}
//		}
//		return resultList;
		return null;
	}
	 */
	
//	public boolean addPlugoutCell(PlugoutDescElementObj cell) {
//		cell.setWidgetObj(this);
//		boolean b = plugoutCells.add(cell);
//		if (b) {
//			fireStructureChange(PROP_PLUGOUT_ADD, cell);
//		}
//		return b;
//	}
//
//	public boolean removePlugoutCell(PlugoutDescElementObj cell) {
//		boolean b = plugoutCells.remove(cell);
//		cell.setWidgetObj(null);
//		if (b) {
//			fireStructureChange(PROP_PLUGOUT_REMOVE, cell);
//		}
//		return b;
//	}
//
//	public List<PlugoutDescElementObj> getPlugoutCells() {
//		return plugoutCells;
//	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return super.getPropertyDescriptors();
	}

	public void setPropertyValue(Object id, Object value) {
		if (PROP_WIDGET_ID.equals(id)) {
			//TODO
			setId((String)value);
		} 
//		else if (PROP_ISDIALOG.equals(id)) {
////			if ((boolean) value.equals(0))
////				widget.setDialog(true);
////			else
////				widget.setDialog(false);
//			PagemetaEditor pmEditor = PagemetaEditor.getActivePagemetaEditor();
//			if (pmEditor != null) {
//				pmEditor.addTempWidget(widget);
//				pmEditor.setDirtyTrue();
//			} else {
//				WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
//				wdEditor.setDirtyTrue();
//			}
//			
//		}
//		else if(PROP_WIDTH.equals(id)){
//			widget.setWidth((String)value);
//			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
//			wdEditor.setDirtyTrue();
//		}
//		else if(PROP_HEIGHT.equals(id)){
//			widget.setHeight((String)value);
//			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
//			wdEditor.setDirtyTrue();
//		}
		else if(PROP_I18NNAME.equals(id)){
			widget.setI18nName((String)value);
			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
			wdEditor.setDirtyTrue();
		}
//		else if (PROP_ISMAIN.equals(id)) {
//			if ((boolean) value.equals("0"))
//				setMainWidget(true);
//			else
//				setMainWidget(false);
//		}
	}

	public Object getPropertyValue(Object id) {
		if (PROP_WIDGET_ID.equals(id)) {
			return widget.getId();
		} 
		else if(PROP_I18NNAME.equals(id))
			return widget.getI18nName() == null?"":widget.getI18nName();
		return null;
	}

	public void setElementAttribute(Element ele) {
		ele.setAttribute("id", getId());
	}

	public Object getEditableValue() {
		return this;
	}

	public Element createElement(Document doc) {

		return null;
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public WidgetElementFigure getFigure() {
		return figure;
	}

	public void setFigure(WidgetElementFigure figure) {
		this.figure = figure;
	}

	
	public WebElement getWebElement() {
		return widget;
	}

	public List<PlugoutDescElementObj> getPlugoutCells() {
		return plugoutCells;
	}

	public void addPlugoutCell(PlugoutDescElementObj cell) {
		plugoutCells.add(cell);
		firePlugChange();
	}

	public void removePlugoutCell(PlugoutDescElementObj cell) {
		plugoutCells.remove(cell);
		for (int i = 0 ; i < plugoutCells.size() ; i++){
			PlugoutDescElementObj plugoutCell = plugoutCells.get(i);
			Point p = new Point();
			p.x = plugoutCell.getLocation().x;
			p.y = plugoutCell.getWidgetObj().getLocation().y + i * 40;
			plugoutCell.setLocation(p);
		}
		firePlugChange();
	}

	public List<PluginDescElementObj> getPluginCells() {
		return pluginCells;
	}
	
	public void addPluginCell(PluginDescElementObj cell) {
		pluginCells.add(cell);
		firePlugChange();
	}
	
	public void removePluginCell(PluginDescElementObj cell) {
		pluginCells.remove(cell);
		for (int i = 0 ; i < pluginCells.size() ; i++){
			PluginDescElementObj pluginCell = pluginCells.get(i);
			Point p = new Point();
			p.x = pluginCell.getLocation().x;
			p.y = pluginCell.getWidgetObj().getLocation().y + i * 40;
			pluginCell.setLocation(p);
		}
		firePlugChange();
	}
	
	private void firePlugChange(){
		LfwBaseGraph graph = getGraph();
		if (graph instanceof WidgetGraph)
			((WidgetGraph)graph).fireWidgetPlugChange(widget);
		else if (graph instanceof PagemetaGraph)
			((PagemetaGraph)graph).fireWidgetPlugChange(widget);
	}

	public List<Connector> getConnectorList() {
		return connectorList;
	}

	public void setConnectorList(List<Connector> connectorList) {
		this.connectorList = connectorList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	

}
