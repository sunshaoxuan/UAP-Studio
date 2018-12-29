package nc.lfw.editor.pagemeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.editor.window.WindowObj;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class PagemetaGraph extends LfwBaseGraph {

	private static final long serialVersionUID = 1L;
	
	public static final String PROP_CHILD_ADD = "prop_child_add";
	public static final String PROP_CHILD_REMOVE = "prop_child_remove";

	public static final String PROP_WIDGETPLUG_CHANGE = "prop_widgetplug_change";
	
	public static final String PROP_WINDOWPLUG_CHANGE = "prop_windowplug_change";
	
	public static final String PROP_CONNECT_CHANGE = "prop_widgetplug_change";
	
	private LfwWindow pagemeta = null;

	private List<WidgetElementObj> widgetCells = new ArrayList<WidgetElementObj>();
	
	/**
	 * Window图形集合
	 */
	private List<WindowObj> windowCells = new ArrayList<WindowObj>();
	
	public int inlineWindowCount = 0;
	
	private String mainWidgetId = "";
	private String caption = "";
	private String i18nName = "";
	private String sourcePackage = "";
	private String controllerClazz = "";
	private String processor = "";
	private String pagemodel = "";
	private LFWBaseEditor editor = null;
	
//	private List<PageStateElementObj> pageStateCells = new ArrayList<PageStateElementObj>();
	
	public PagemetaGraph() {
		super();
	}

	public boolean addWidgetCell(WidgetElementObj cell) {
		cell.setGraph(this);
		boolean b = widgetCells.add(cell);
		elementsCount++;
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}
	public boolean addWindowCell(WindowObj cell) {
		cell.setGraph(this);
		boolean b = windowCells.add(cell);
		inlineWindowCount++;
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}

	public boolean removeWidgetCell(WidgetElementObj cell) {
		boolean b = widgetCells.remove(cell);
		elementsCount--;
		cell.setGraph(null);
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
		}
		return b;
	}

	public List<WidgetElementObj> getWidgetCells() {
		return widgetCells;
	}
	public List<WindowObj> getWindowCells() {
		return windowCells;
	}

	public void addConnector(Connector conn){
		pagemeta.getConnectorMap().put(conn.getId(), conn);
		fireStructureChange(PROP_CONNECT_CHANGE, conn);
	}
	
	public void removeConnector(Connector conn){
		pagemeta.getConnectorMap().remove(conn.getId());
		fireStructureChange(PROP_CONNECT_CHANGE, conn);
	}
	
	/**
	 * 取消所有图形的所有子项选中状态
	 */
	public void unSelectAllLabels() {
		super.unSelectAllLabels();
		// Widget图形
		for (int i = 0, n = widgetCells.size(); i < n; i++) {
			WidgetElementObj widgetObj = widgetCells.get(i);
			widgetObj.getFigure().unSelectAllLabels();
		}
//		// 页面状态图形
//		for (int i = 0, n = pageStateCells.size(); i < n; i++) {
//			PageStateElementObj pageStateObj = pageStateCells.get(i);
//			pageStateObj.getFigure().unSelectAllLabels();
//		}
	}

	public String getPagemodel() {
		return pagemodel;
	}

	public void setPagemodel(String pagemodel) {
		this.pagemodel = pagemodel;
		String pageModel = LFWPersTool.getPageModel();
		if (!pagemodel.equals(pageModel)) {
			LFWBaseEditor editor = getEditor();
			if(editor != null)
				editor.setDirtyTrue();
		}
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = null;
		if(LFWAMCPersTool.getCurrentTreeItem() instanceof LFWPageMetaTreeItem){
			pds = new PropertyDescriptor[5];
			pds[0] = new NoEditableTextPropertyDescriptor(WEBPersConstants.PROP_ID, WEBPersConstants.ID);
			pds[0].setCategory(WEBPersConstants.BASIC);
			pds[1] = new TextPropertyDescriptor(WEBPersConstants.PROP_CAPTION, WEBPersConstants.CAPTION);
			pds[1].setCategory(WEBPersConstants.BASIC);
			pds[2] = new TextPropertyDescriptor(WEBPersConstants.PROP_I18NNAME, WEBPersConstants.I18NNAME);
			pds[2].setCategory(WEBPersConstants.BASIC);
			pds[3] = new ComboBoxPropertyDescriptor(WEBPersConstants.PROP_SOURCE_PACKAGE, WEBPersConstants.SOURCE_PACKAGE, getSourcePackages());
			pds[3].setCategory(WEBPersConstants.BASIC);
			pds[4] = new TextPropertyDescriptor(WEBPersConstants.PROP_CONTROLLER_CLASS, WEBPersConstants.CONTROLLER_CLASS);
			pds[4].setCategory(WEBPersConstants.BASIC);
		}else{
			pds = new PropertyDescriptor[4];
			pds[0] = new TextPropertyDescriptor(WEBPersConstants.PROP_CAPTION, WEBPersConstants.CAPTION);
			pds[0].setCategory(WEBPersConstants.BASIC);
			pds[1] = new TextPropertyDescriptor(WEBPersConstants.PROP_I18NNAME, WEBPersConstants.I18NNAME);
			pds[1].setCategory(WEBPersConstants.BASIC);
			pds[2] = new TextPropertyDescriptor(WEBPersConstants.PROP_PROCESS_CLASS, WEBPersConstants.PROCESS_CLASS);
			pds[2].setCategory(WEBPersConstants.BASIC);
			pds[3] = new TextPropertyDescriptor(WEBPersConstants.PROP_PAGEMODEL, WEBPersConstants.PAGEMODEL);
			pds[3].setCategory(WEBPersConstants.BASIC);
		}
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}

	public void setPropertyValue(Object id, Object value) {
		if(LFWAMCPersTool.getCurrentTreeItem() instanceof LFWPageMetaTreeItem){
			if(WEBPersConstants.PROP_CAPTION.equals(id)) {
				setCaption((String)value);
			}else if (WEBPersConstants.PROP_I18NNAME.equals(id)) {
				setI18nName((String)value);
			}else if(WEBPersConstants.PROP_SOURCE_PACKAGE.equals(id)){
				if(value instanceof Integer){
					String[] sourcePackages = getSourcePackages();
					if(sourcePackages.length > (Integer)value){
						setSourcePackage(sourcePackages[((Integer)value).intValue()]);
					}
				}
			}else if(WEBPersConstants.PROP_CONTROLLER_CLASS.equals(id)){
				setControllerClazz((String)value);
			}
		}else{
			if (WEBPersConstants.PROP_CAPTION.equals(id)) {
				setCaption((String) value);
			} else if (WEBPersConstants.PROP_I18NNAME.equals(id)) {
				setI18nName((String) value);
			} else if (WEBPersConstants.PROP_PROCESS_CLASS.equals(id)) {
				setProcessor((String) value);
			}else if(WEBPersConstants.PROP_PAGEMODEL.equals(id))
				setPagemodel((String)value);
		}
	}

	public Object getPropertyValue(Object id) {
		if(WEBPersConstants.PROP_ID.equals(id)){
			return pagemeta.getId() == null? "" : pagemeta.getId();
		}else if(WEBPersConstants.PROP_CAPTION.equals(id)) {
			return pagemeta.getCaption() == null ? "" : pagemeta.getCaption();
		}else if (WEBPersConstants.PROP_I18NNAME.equals(id)) {
			return pagemeta.getI18nName() == null ? "" : pagemeta.getI18nName();
		}else if(WEBPersConstants.PROP_SOURCE_PACKAGE.equals(id)){
			if(pagemeta.getSourcePackage() == null){
				return 0;
			}
			String[] sourcePackages = getSourcePackages();
			for(int i=0; i < sourcePackages.length; i++){
				if(pagemeta.getSourcePackage().equals(sourcePackages[i])){
					return i;
				}
			}
//			pagemeta.setSourcePackage(sourcePackages[0]);
			return 0;
		}else if (WEBPersConstants.PROP_CONTROLLER_CLASS.equals(id)) {
			return pagemeta.getControllerClazz() == null ? "" : pagemeta.getControllerClazz();
		}else if (WEBPersConstants.PROP_PROCESS_CLASS.equals(id)) {
			return pagemeta.getProcessorClazz() == null ? "" : pagemeta.getProcessorClazz();
		}else if(WEBPersConstants.PROP_PAGEMODEL.equals(id)){
			return getPagemodel();
		}else{
			return null;
		}
	}
	
	private String[] getSourcePackages(){
		return LFWTool.getAllRootPackage().toArray(new String[0]);
	}
	
	public String getMainWidgetId() {
		return mainWidgetId;
	}

//	public void setMainWidgetId(String mainWidgetId) {
//		this.mainWidgetId = mainWidgetId;
//		if (!mainWidgetId.equals(pagemeta.getMasterWidget())) {
//			pagemeta.setMasterWidget(mainWidgetId);
//			PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
//			editor.setDirtyTrue();
//		}
//	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
		if (!caption.equals(pagemeta.getCaption())) {
			pagemeta.setCaption(caption);
			LFWBaseEditor editor = getEditor();
			if(editor != null)
				editor.setDirtyTrue();
		}
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
		if (!name.equals(pagemeta.getI18nName())) {
			pagemeta.setI18nName(name);
//			PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
			LFWBaseEditor editor = getEditor();
			if(editor != null)
				editor.setDirtyTrue();
		}
	}
	
	public String getSourcePackage() {
		return sourcePackage;
	}

	public void setSourcePackage(String sourcePackage) {
		this.sourcePackage = sourcePackage;
		if(!sourcePackage.equals(pagemeta.getSourcePackage())){
			pagemeta.setSourcePackage(sourcePackage);
			LFWBaseEditor editor = getEditor();
			if(editor != null)
				editor.setDirtyTrue();
		}
	}

	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
		if(!controllerClazz.equals(pagemeta.getControllerClazz())){
			pagemeta.setControllerClazz(controllerClazz);
			LFWBaseEditor editor = getEditor();
			if(editor != null)
				editor.setDirtyTrue();
		}
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
		if (!processor.equals(pagemeta.getProcessorClazz())) {
			pagemeta.setProcessorClazz(processor);
			LFWBaseEditor editor = getEditor();
			if(editor != null)
				editor.setDirtyTrue();
		}
	}

	public LfwWindow getPagemeta() {
		return pagemeta;
	}

	public void setPagemeta(LfwWindow pagemeta) {
		this.pagemeta = pagemeta;
	}
	
	

//	public boolean addPageStateCell(PageStateElementObj cell) {
//		cell.setGraph(this);
//		boolean b = pageStateCells.add(cell);
//		//elementsCount++;
//		// 占用一行，2个图标位置
//		elementsCount += 2;
//		if (b) {
//			fireStructureChange(PROP_CHILD_ADD, cell);
//		}
//		return b;
//	}
//
//	public boolean removePageStateCell(PageStateElementObj cell) {
//		boolean b = pageStateCells.remove(cell);
//		//elementsCount--;
//		// 占用一行，2个图标位置
//		elementsCount -= 2;
//		cell.setGraph(null);
//		if (b) {
//			fireStructureChange(PROP_CHILD_REMOVE, cell);
//		}
//		return b;
//	}
//
//	public List<PageStateElementObj> getPageStateCells() {
//		return pageStateCells;
//	}
	
	public LFWBaseEditor getEditor() {
		return editor;
	}

	public void setEditor(LFWBaseEditor editor) {
		this.editor = editor;
	}

	public void fireWidgetPlugChange(LfwView widget){
		fireStructureChange(PROP_WIDGETPLUG_CHANGE, widget);
	}
	public void fireWidgetPlugChange(LfwWindow window){
		fireStructureChange(PROP_WINDOWPLUG_CHANGE, window);
	}
}
