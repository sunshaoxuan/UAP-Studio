/**
 * 
 */
package nc.uap.lfw.editor.application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.editor.window.WindowConfigObj;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * 
 * Application图形
 * @author chouhl
 *
 */
public class ApplicationGraph extends LfwBaseGraph {

	private static final long serialVersionUID = 6466250488809618771L;
	/**
	 * 每行显示图形数量
	 */
	public static final int number = 2;
	/**
	 * Application元素
	 */
	private Application webElement = null;
	/**
	 * Application图形集合
	 */
	private List<ApplicationObj> webElementCells = new ArrayList<ApplicationObj>();
	/**
	 * Window图形集合
	 */
	private List<WindowConfigObj> windowCells = new ArrayList<WindowConfigObj>();
	/**
	 * 当前工程
	 */
	private IProject project = null;
	/**
	 * 当前节点
	 */
	private TreeItem currentTreeItem = null;
	
	public ApplicationGraph() {
		super();
		this.project = LFWAMCPersTool.getCurrentProject();
		this.currentTreeItem = LFWAMCPersTool.getCurrentTreeItem();
	}

	public boolean addApplicationCell(ApplicationObj cell) {
		cell.setGraph(this);
		boolean b = webElementCells.add(cell);
		elementsCount += number;
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}
	
	public boolean removeApplicationCell(ApplicationObj cell) {
		boolean b = webElementCells.remove(cell);
		elementsCount -= number;
		cell.setGraph(null);
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
		}
		return b;
	}
	
	public boolean addWindowCell(WindowConfigObj cell) {
		cell.setGraph(this);
		boolean b = windowCells.add(cell);
		elementsCount += number;
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}
	
	public boolean removeWindowCell(WindowConfigObj cell) {
		boolean b = windowCells.remove(cell);
		elementsCount -= number;
		cell.setGraph(null);
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
		}
		return b;
	}
	
	/**
	 * 取消所有图形的所有子项选中状态
	 */
	public void unSelectAllLabels() {
		super.unSelectAllLabels();
		// Application图形
		for (ApplicationObj cell : webElementCells) {
			cell.getFigure().unSelectAllLabels();
		}
		// Window图形
		for(WindowConfigObj cell : windowCells){
			cell.getFigure().unSelectAllLabels();
		}
	}

	public List<ApplicationObj> getApplicationCells() {
		return webElementCells;
	}
	
	public List<WindowConfigObj> getWindowCells(){
		return windowCells;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[6];
		pds[0] = new NoEditableTextPropertyDescriptor(WEBPersConstants.PROP_ID, WEBPersConstants.ID);
		pds[0].setCategory(WEBPersConstants.BASIC);
		pds[1] = new TextPropertyDescriptor(WEBPersConstants.PROP_CAPTION, WEBPersConstants.CAPTION);
		pds[1].setCategory(WEBPersConstants.BASIC);
		pds[2] = new TextPropertyDescriptor(WEBPersConstants.PROP_I18NNAME, WEBPersConstants.I18NNAME);
		pds[2].setCategory(WEBPersConstants.BASIC);
		pds[3] = new TextPropertyDescriptor(WEBPersConstants.PROP_CONTROLLER_CLASS, WEBPersConstants.CONTROLLER_CLASS);
		pds[3].setCategory(WEBPersConstants.BASIC);
		pds[4] = new ComboBoxPropertyDescriptor(WEBPersConstants.PROP_SOURCE_PACKAGE, WEBPersConstants.SOURCE_PACKAGE, getSourcePackages());
		pds[4].setCategory(WEBPersConstants.BASIC);
		pds[5] = new ComboBoxPropertyDescriptor(WEBPersConstants.PROP_DEFAULT_WINDOW, WEBPersConstants.DEFAULT_WINDOW, getWindowIds());
		pds[5].setCategory(WEBPersConstants.BASIC);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	private String[] getWindowIds(){
		String[] windowIds = new String[windowCells.size() + 1];
		windowIds[0] = "";
		for(int i=0; i < windowCells.size(); i++){
			windowIds[i + 1] = windowCells.get(i).getWindowConfig().getId();
		}
		return windowIds;
	}
	
	private String[] getSourcePackages(){
		return LFWTool.getAllRootPackage().toArray(new String[0]);
	}
	
	private String caption = "";
	private String i18nName = "";
	private String sourcePackage = "";
	private String controllerClazz = "";
	private String defaultWindow = "";
	
	public Object getPropertyValue(Object id) {
		if(WEBPersConstants.PROP_ID.equals(id)){
			return webElement.getId() == null? "" : webElement.getId();
		}else if(WEBPersConstants.PROP_CAPTION.equals(id)) {
			return webElement.getCaption() == null ? "" : webElement.getCaption();
		}else if(WEBPersConstants.PROP_I18NNAME.equals(id)){
			return webElement.getI18nName() == null ? "" : webElement.getI18nName();
		}else if(WEBPersConstants.PROP_SOURCE_PACKAGE.equals(id)){
			if(webElement.getSourcePackage() == null){
				return 0;
			}
			String[] sourcePackages = getSourcePackages();
			for(int i=0; i < sourcePackages.length; i++){
				if(webElement.getSourcePackage().equals(sourcePackages[i])){
					return i;
				}
			}
			return 0;
		}else if(WEBPersConstants.PROP_CONTROLLER_CLASS.equals(id)){
			return webElement.getControllerClazz() == null ? "" : webElement.getControllerClazz();
		}else if(WEBPersConstants.PROP_DEFAULT_WINDOW.equals(id)){
			if(webElement.getDefaultWindowId() == null){
				return 0;
			}
			String[] windowIds = getWindowIds();
			for(int i=1; i < windowIds.length; i++){
				if(webElement.getDefaultWindowId().equals(windowIds[i])){
					return i;
				}
			}
			webElement.setDefaultWindowId(null);
			return 0;
		}else{
			return null;
		}
	}

	public void setPropertyValue(Object id, Object value) {
		if(WEBPersConstants.PROP_CAPTION.equals(id)){
			setCaption((String)value);
		}else if(WEBPersConstants.PROP_I18NNAME.equals(id)){
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
		}else if(WEBPersConstants.PROP_DEFAULT_WINDOW.equals(id)){
			if(value instanceof Integer){
				String[] windowIds = getWindowIds();
				if(windowIds.length > (Integer)value){
					setDefaultWindow(windowIds[((Integer)value).intValue()]);
				}
			}
		}
	}

	public Application getApplication() {
		return webElement;
	}

	public void setApplication(Application webElement) {
		this.webElement = webElement;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
		if (!caption.equals(webElement.getCaption())) {
			webElement.setCaption(caption);
//			ApplicationEditor editor = ApplicationEditor.getActiveEditor();
//			editor.setDirtyTrue();
		}
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		this.i18nName = name;
		if (!i18nName.equals(webElement.getI18nName())) {
			webElement.setI18nName(i18nName);
			ApplicationEditor editor = ApplicationEditor.getActiveEditor();
			editor.setDirtyTrue();
		}
	}

	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
		if(!controllerClazz.equals(webElement.getControllerClazz())){
			webElement.setControllerClazz(controllerClazz);
			ApplicationEditor editor = ApplicationEditor.getActiveEditor();
			editor.setDirtyTrue();
			modifyEvent(null, controllerClazz);
		}
	}
	
	public String getSourcePackage() {
		return sourcePackage;
	}

	public void setSourcePackage(String sourcePackage) {
		this.sourcePackage = sourcePackage;
		if(!sourcePackage.equals(webElement.getSourcePackage())){
			webElement.setSourcePackage(sourcePackage);
			ApplicationEditor editor = ApplicationEditor.getActiveEditor();
			editor.setDirtyTrue();
			modifyEvent(sourcePackage, null);
		}
	}
	
	public String getDefaultWindow() {
		return defaultWindow;
	}

	public void setDefaultWindow(String defaultWindow) {
		this.defaultWindow = defaultWindow;
		if (!defaultWindow.equals(webElement.getDefaultWindowId())) {
			webElement.setDefaultWindowId(defaultWindow);
			ApplicationEditor editor = ApplicationEditor.getActiveEditor();
			editor.setDirtyTrue();
		}
	}

	public IProject getProject() {
		return project;
	}

	public TreeItem getCurrentTreeItem() {
		return currentTreeItem;
	}

	/**
	 * 修改事件所属类名称和类路径
	 */
	private void modifyEvent(String sourcePackage, String controllerClazz){
		if(sourcePackage == null || sourcePackage.trim().length() == 0){
			sourcePackage = webElement.getSourcePackage();
		}
		if(controllerClazz == null || controllerClazz.trim().length() == 0){
			controllerClazz = webElement.getControllerClazz();
		}
		EventConf[] events = webElement.getEventConfs();
		if(events != null && events.length > 0){
			for(EventConf event : events){
				if(event.getEventStatus() == EventConf.ADD_STATUS || event.getEventStatus() == EventConf.DEL_STATUS){
					String projectPath = LFWAMCPersTool.getLFWProjectPath();
					int index = controllerClazz.lastIndexOf(".");
					String packageName = null;
					if(index > 0){
						packageName = controllerClazz.substring(0, index);
					}else{
						packageName = "";
					}
					String className = controllerClazz.substring(index + 1);
					String classFilePath = projectPath + File.separator + sourcePackage + packageName.replaceAll("\\.", "/");
					String classFileName = className + ".java";
					event.setClassFilePath(classFilePath);
					event.setClassFileName(classFileName);
				}
			}
		}
	}
	
}
