/**
 * 
 */
package nc.uap.lfw.editor.common.tools;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;

/**
 * 
 * @author chouhl
 * 2011-11-4
 */
public class LFWSaveElementTool {
	
	/**
	 * ����Application����
	 * @param id
	 * @param caption
	 * @param controllerClazz
	 * @param sourcePackage
	 * @return
	 */
	public static Application createNewApplicationConf(String id, String caption, String controllerClazz, String sourcePackage){
		Application app = new Application();
		app.setId(id);
		app.setCaption(caption);
		app.setControllerClazz(controllerClazz);
		app.setSourcePackage(sourcePackage);
		//��ʵ·��
		String appPath = LFWAMCPersTool.getApplicationPath();
		String folderPath = LFWAMCPersTool.getCurrentFolderPath();
		String realPath = id;
		if(LFWAMCPersTool.getCurrentTreeItem() instanceof LFWVirtualDirTreeItem){
			if(appPath.length() < folderPath.length()){
				realPath = folderPath.substring(folderPath.indexOf(appPath) + appPath.length() + 1) + "/" + id;
			}
		}
		app.setRealPath(realPath);
		return app;
	}

	/**
	 * ����Application(����XML������Class)
	 * @param appConf
	 */
	public static void createApplication(Application appConf){
		String folderPath = LFWAMCPersTool.getCurrentFolderPath();
		String filePath = folderPath + File.separator + appConf.getId();
		int index = appConf.getControllerClazz().lastIndexOf(".");
		String packageName = null;
		if(index > 0){
			packageName = appConf.getControllerClazz().substring(0, index);
		}else{
			packageName = "";
		}
		String projectPath = LFWAMCPersTool.getLFWProjectPath();
		String className = appConf.getControllerClazz().substring(index + 1);
		String classFilePath = projectPath + File.separator + appConf.getSourcePackage() + packageName.replaceAll("\\.", "/");
		String classFileName = className + ".java";
		
		LFWAMCConnector.createApplication(packageName, className, classFilePath, classFileName, filePath, WEBPersConstants.AMC_APPLICATION_FILENAME, projectPath, appConf);
	}
	
	/**
	 * ����application��XML�ļ�
	 * @param application
	 */
	public static void saveApplication(Application appConf) {
		String folderPath = LFWAMCPersTool.getCurrentFolderPath();
		String projectPath = LFWAMCPersTool.getProjectPath();
		LFWAMCConnector.updateApplication(folderPath, WEBPersConstants.AMC_APPLICATION_FILENAME, projectPath, appConf);
	}
	
	/**
	 * ����application��XML�ļ�
	 * @param appConf
	 * @param folderPath
	 */
	public static void saveApplication(Application appConf, String folderPath){
		String projectPath = LFWAMCPersTool.getProjectPath();
		LFWAMCConnector.updateApplication(folderPath, WEBPersConstants.AMC_APPLICATION_FILENAME, projectPath, appConf);
	}
	
//	/**
//	 * ����Model
//	 * @param model
//	 */
//	public static void createModel(Model model){
//		String folderPath = LFWPersTool.getCurrentFolderPath();
//		String projectPath = LFWPersTool.getProjectPath();
//		String filePath = folderPath + "/" + model.getId();
//		LFWAMCConnector.createModel(filePath, WEBPersConstants.AMC_MODEL_FILENAME, projectPath, model);
//	}
	
	public static final String DEFAULT_PROCESSOR_CLASS_NAME = "nc.uap.lfw.core.event.AppRequestProcessor";
	/**
	 * ����WindowConf
	 * @param id
	 * @param caption
	 * @param controllerClazz
	 * @param sourcePackage
	 * @return
	 */
	public static LfwWindow createNewWindowConf(String id, String caption, String compId, String controllerClazz, String sourcePackage){
		//�½�Window
		LfwWindow winConf = new LfwWindow();
		if(LFWPersTool.getCurrentTreeItem() instanceof LFWApplicationTreeItem
				&& !LfwUIComponent.ANNOYUICOMPONENT.equals(compId)){
			winConf.setId(compId+"."+id);
		}else{
			winConf.setId(id);
		}
		winConf.setCaption(caption);
		winConf.setProcessorClazz(DEFAULT_PROCESSOR_CLASS_NAME);
		winConf.setControllerClazz(controllerClazz);
		winConf.setSourcePackage(sourcePackage);
		winConf.setWindowType("win");
//		TreeItem currentItem = LFWPersTool.getCurrentTreeItem();
//		LFWComponentTreeItem compItem = LFWPersTool.getComponentItem(currentItem);
//		LfwUIComponent component = compItem.getComponent();
//		String pack = component.getPack();
//		String compId = component.getId();
//		if(!LfwUIComponent.ANNOYUICOMPONENT.equals(compId)){
//			int index = compId.lastIndexOf(".");
//			String pack = null;
//			if(index>0){
//				pack = compId.substring(0,index);
//			}
//			if(pack!=null&pack.trim().length()>0)
//				winConf.setComponentId(pack+"."+compId);
//			else winConf.setComponentId(compId);				
//		}
		winConf.setComponentId(compId);
		//��ʵ·��
		String windowPath = LFWAMCPersTool.getWindowPath();
		String folderPath = LFWAMCPersTool.getCurrentFolderPath();
		String realPath = id;
		//��Application�д���window��Ĭ�Ͻڵ�������window��Ŀ¼��.
		if(LFWAMCPersTool.getCurrentTreeItem() instanceof LFWVirtualDirTreeItem){
			if(windowPath.length() < folderPath.length()){
				realPath = folderPath.substring(folderPath.indexOf(windowPath) + windowPath.length() + 1) + "/" + id;
			}
		}
		winConf.setRealPath(realPath);
		
		// ����Ĭ���¼�
//		EventConf eventConf = PageEvent.getOnClosedEvent();
//		eventConf.setEventStatus(EventConf.ADD_STATUS);
//		eventConf.setMethodName("sysWindowClosed");
//		eventConf.setSubmitRule(new EventSubmitRule());
//		winConf.addEventConf(eventConf);
		return winConf;
	}
	
	/**
	 * ����Window(����XML������Class)
	 * @param pagemeta
	 */
	public static void createPagemeta(LfwWindow winConf, UIMeta winUm, String filePath) {
		
		int index = winConf.getControllerClazz().lastIndexOf(".");
		String packageName = null;
		if(index > 0){
			packageName = winConf.getControllerClazz().substring(0, index);
		}else{
			packageName = "";
		}
		String projectPath = LFWAMCPersTool.getLFWProjectPath();
		String className = winConf.getControllerClazz().substring(index + 1);
		String classFilePath = projectPath + File.separator + winConf.getSourcePackage() + packageName.replaceAll("\\.", "/");
		String classFileName = className + ".java";

		
		LFWAMCConnector.createWindow(packageName, className, classFilePath, classFileName, filePath, WEBPersConstants.AMC_WINDOW_FILENAME, projectPath, winConf, winUm);
	
	}
	
	/**
	 * ����Pagemeta��XML�ļ�
	 * @param winConf
	 */
	public static void savePagemeta(LfwWindow winConf) {
		String folderPath = LFWPersTool.getCurrentFolderPath();
		String projectPath = LFWPersTool.getProjectPath();
		LFWAMCConnector.updateWindowToXml(folderPath, WEBPersConstants.AMC_WINDOW_FILENAME, projectPath, winConf);
	}
	
	/**
	 * ����PageMeta(XML�ļ���Class�ļ�)
	 * @param pageMeta
	 */
	public static void updateWindow(LfwWindow pageMeta){
		String folderPath = LFWPersTool.getCurrentFolderPath();
		String projectPath = LFWPersTool.getProjectPath();
		LFWAMCConnector.updateWindow(folderPath, WEBPersConstants.AMC_WINDOW_FILENAME, projectPath, pageMeta);
	}
	
	/**
	 * ����View(����XML������Class)
	 * @param widget
	 */
	public static void createView(LfwView viewConf, UIMeta uimeta) {
		String folderPath = LFWPersTool.getCurrentFolderPath();
		String filePath = folderPath + File.separator + viewConf.getId();
		createView(viewConf, uimeta, filePath);
	}

	public static void createView(LfwView viewConf, UIMeta uimeta, String filePath){
		String ctrlClazz = viewConf.getControllerClazz();
		int index = ctrlClazz == null ? -1 : ctrlClazz.lastIndexOf(".");
		String packageName = null;
		if(index > 0){
			packageName = viewConf.getControllerClazz().substring(0, index);
		}else{
			packageName = "";
		}
		
		String projectPath = LFWAMCPersTool.getLFWProjectPath();
		String className = null;
		String classFilePath = null;
		String classFileName = null;
		if(ctrlClazz != null && !ctrlClazz.equals("")){
			className = viewConf.getControllerClazz().substring(index + 1);
			classFilePath = projectPath + File.separator + viewConf.getSourcePackage() + packageName.replaceAll("\\.", "/");
			classFileName = className + ".java";
		}
		LFWAMCConnector.createView(packageName, className, classFilePath, classFileName, filePath, WEBPersConstants.AMC_VIEW_FILENAME, projectPath, viewConf, uimeta);
	}
	
	/**
	 * ����View(XML�ļ���Class�ļ�)
	 * @param viewConf
	 */
	public static void updateView(LfwView viewConf){
		String folderPath = LFWPersTool.getCurrentFolderPath();
		String projectPath = LFWPersTool.getProjectPath();
		LFWAMCConnector.updateView(folderPath, WEBPersConstants.AMC_VIEW_FILENAME, projectPath, viewConf);
	}
	
	/**
	 * ����UIMeta��XML�ļ���
	 * @param filePath
	 */
	public static void createUIMeta(String filePath){
		String fp = filePath.replaceAll("\\\\", "/");
		String id = fp.substring(fp.lastIndexOf("/") + 1) + "_um";
		UIMeta meta = new UIMeta();
//		meta.setAttribute(UIMeta.ISCHART, 0);
//		meta.setAttribute(UIMeta.ISJQUERY, 0);
//		meta.setAttribute(UIMeta.ISEXCEL, 0);
//		meta.setAttribute(UIMeta.JSEDITOR, 0);
		meta.setAttribute(UIMeta.ID, id);
		meta.setFlowmode(true);
		LFWAMCConnector.createUIMeta(filePath, WEBPersConstants.AMC_UIMETA_FILENAME, meta);
	}
	
}
