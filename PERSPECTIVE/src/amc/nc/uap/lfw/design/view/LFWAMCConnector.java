/**
 * 
 */
package nc.uap.lfw.design.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.WidgetEditorInput;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.dev.LfwComponent;
import nc.uap.lfw.core.dev.LfwDevModel;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.AMCServiceObj;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.itf.ILfwAMCDesignDataProvider;
import nc.uap.lfw.editor.common.editor.LFWBrowserEditor;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.editor.common.tools.ViewEventTool;
import nc.uap.lfw.editor.publicview.PublicViewBrowserEditor;
import nc.uap.lfw.editor.publicview.PublicViewEditorInput;
import nc.uap.lfw.editor.view.ViewBrowserEditor;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.perspective.project.ILFWTreeNode;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;

/**
 * 
 * UAPWEB服务连接类
 * @author chouhl
 *
 */
public class LFWAMCConnector extends LFWConnector {
	
	/**
	 * 获取上下文
	 * @return
	 */
	private static String getContextPath(){
		String ctxPath = LfwCommonTool.getLfwProjectCtx(LFWAMCPersTool.getCurrentProject());
		if(!ctxPath.startsWith("/")){
			ctxPath = "/" + ctxPath;
		}
		return ctxPath;
	}
	
	/**
	 * 检查文件
	 * @param filePath
	 * @param fileName
	 */
	private static void checkFile(String filePath, String fileName){
		String path = filePath + "/" + fileName;
		MainPlugin.getDefault().logInfo("文件路径：" + path);
		LFWAMCPersTool.checkOutFile(path);
	}
	
	/************************OperateWebElementXML************************/
	/**
	 * 更新Application（XML文件）
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param app
	 */
	public static void updateApplicationToXml(String filePath, String fileName, String projectPath, Application app){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.UpdateApplicationXml);
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		amcServiceObj.setCurrentProjPath(projectPath);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		amcServiceObj.setAppConf(app);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		LFWAMCPersTool.refreshCurrentPorject();
	}
	
	/**
	 * 更新Application（类文件、XML文件）
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param app
	 */
	public static void updateApplication(String filePath, String fileName, String projectPath, Application app){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		//操作类型
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.UpdateAppXmlAndController);
		//Application
		amcServiceObj.setAppConf(app);
		//当前工程路径
		amcServiceObj.setCurrentProjPath(projectPath);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		//XML文件
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		//事件
		EventConf[] events = app.getEventConfs();
		if(events != null && events.length > 0){
			Map<String, List<EventConf>> eventsMap = new HashMap<String, List<EventConf>>();
			for(EventConf event : events){
				if(event.getClassFilePath() != null && event.getClassFileName() != null){
					List<EventConf> list = eventsMap.get(event.getClassFilePath() + File.separator + event.getClassFileName());
					if(list == null){
						list = new ArrayList<EventConf>();
					}
					list.add(event);
					eventsMap.put(event.getClassFilePath() + File.separator + event.getClassFileName(), list);
				}
			}
			String key = null;
			Iterator<String> keys = eventsMap.keySet().iterator();
			while(keys.hasNext()){
				key = keys.next();
				MainPlugin.getDefault().logInfo("类文件路径：" + key);
				LFWAMCPersTool.checkOutFile(key);
			}
			amcServiceObj.setEventsMap(eventsMap);
		}
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		if(events != null && events.length > 0){
			for(EventConf event : events){
				if(event.getEventStatus() == EventConf.ADD_STATUS){
					event.setEventStatus(EventConf.NORMAL_STATUS);
				}else if(event.getEventStatus() == EventConf.DEL_STATUS){
					app.removeEventConf(event.getName(), event.getMethodName());
				}
			}
		}
		LFWAMCPersTool.refreshCurrentPorject();
	}
	
	/**
	 * 创建application（生成XML文件和Controller类文件）
	 * @param packageName
	 * @param className
	 * @param classFilePath
	 * @param classFileName
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param appConf
	 */
	public static void createApplication(String packageName, String className, String classFilePath, String classFileName, String filePath, String fileName, String projectPath, Application appConf){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		//操作类型
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.CreateApplicationXml);
		//ApplicationConf
		amcServiceObj.setAppConf(appConf);
		//当前工程路径
		amcServiceObj.setCurrentProjPath(projectPath);
		//XML文件
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		//Controller类文件
		checkFile(classFilePath, classFileName);
		amcServiceObj.setPackageName(packageName);
		amcServiceObj.setClassName(className);
		amcServiceObj.setClassFilePath(classFilePath);
		amcServiceObj.setClassFileName(classFileName);
		//上下文
		String ctxPath = getContextPath();
		amcServiceObj.setCtxPath(ctxPath);
		//UAPWEB服务
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		//刷新当前工程
		LFWAMCPersTool.refreshCurrentPorject();
	}
	
	public static void createApplicationWithoutCheck(String packageName, String className, String classFilePath, String classFileName, String filePath, String fileName, String projectPath, Application appConf){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		//操作类型
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.CreateApplicationXml);
		//ApplicationConf
		amcServiceObj.setAppConf(appConf);
		//当前工程路径
		amcServiceObj.setCurrentProjPath(projectPath);
		//XML文件
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		//Controller类文件
		amcServiceObj.setPackageName(packageName);
		amcServiceObj.setClassName(className);
		amcServiceObj.setClassFilePath(classFilePath);
		amcServiceObj.setClassFileName(classFileName);
		//上下文
		String ctxPath = getContextPath();
		amcServiceObj.setCtxPath(ctxPath);
		//UAPWEB服务
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		//刷新当前工程
//		LFWAMCPersTool.refreshCurrentPorject();
	}
	
//	/**
//	 * 创建model（生成XML文件）
//	 * @param filePath
//	 * @param fileName
//	 * @param projectPath
//	 * @param model
//	 */
//	public static void createModel(String filePath, String fileName, String projectPath, Model model){
//		AMCServiceObj amcServiceObj = new AMCServiceObj();
//		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.CreateModelXml);
//		checkFile(filePath, fileName);
//		amcServiceObj.setFilePath(filePath);
//		amcServiceObj.setFileName(fileName);
//		amcServiceObj.setCurrentProjPath(projectPath);
//		//上下文
//		amcServiceObj.setRootPath(getContextPath());
//		amcServiceObj.setModel(model);
//		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
//		LFWAMCPersTool.refreshCurrentPorject();
//	}
	
	/**
	 * 创建window（生成XML文件和Controller类文件）
	 * @param packageName
	 * @param className
	 * @param classFilePath
	 * @param classFileName
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param pageMeta
	 */
	public static void createWindow(String packageName, String className, String classFilePath, String classFileName, String filePath, String fileName, String projectPath, LfwWindow pageMeta, UIMeta uimeta){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		//操作类型
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.CreateWindowXml);
		//PageMeta
		amcServiceObj.setPageMeta(pageMeta);
		//当前工程路径
		amcServiceObj.setCurrentProjPath(projectPath);
		//XML文件
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		//Controller类文件
		checkFile(classFilePath, classFileName);
		amcServiceObj.setPackageName(packageName);
		amcServiceObj.setClassName(className);
		amcServiceObj.setClassFilePath(classFilePath);
		amcServiceObj.setClassFileName(classFileName);
		//上下文
		String ctxPath = getContextPath();
		amcServiceObj.setCtxPath(ctxPath);
//		//流式布局
//		amcServiceObj.setFlowlayout(isFlowlayout);
		amcServiceObj.setUimeta(uimeta);
		//UAPWEB服务
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		//刷新当前工程
		LFWAMCPersTool.refreshCurrentPorject();
	}
	
	/**
	 * 更新Window（XML文件）
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param winConf
	 */
	public static void updateWindowToXml(String filePath, String fileName, String projectPath, LfwWindow winConf){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.UpdateWindowXml);
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		amcServiceObj.setCurrentProjPath(projectPath);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		amcServiceObj.setPageMeta(winConf);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		LFWPersTool.refreshCurrentPorject();
	}
	
	/**
	 * 更新Window（类文件、XML文件）
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param app
	 */
	public static void updateWindow(String filePath, String fileName, String projectPath, LfwWindow winConf){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		//操作类型
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.UpdateWinXmlAndController);
		//Window
		amcServiceObj.setPageMeta(winConf);
		//当前工程路径
		amcServiceObj.setCurrentProjPath(projectPath);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		//XML文件
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		//事件
		EventConf[] events = winConf.getEventConfs();
		if(events != null && events.length > 0){
			Map<String, List<EventConf>> eventsMap = new HashMap<String, List<EventConf>>();
			for(EventConf event : events){
				if(event.getClassFilePath() != null && event.getClassFileName() != null){
					List<EventConf> list = eventsMap.get(event.getClassFilePath() + File.separator + event.getClassFileName());
					if(list == null){
						list = new ArrayList<EventConf>();
					}
					list.add(event);
					eventsMap.put(event.getClassFilePath() + File.separator + event.getClassFileName(), list);
				}
			}
			String key = null;
			Iterator<String> keys = eventsMap.keySet().iterator();
			while(keys.hasNext()){
				key = keys.next();
				MainPlugin.getDefault().logInfo("类文件路径：" + key);
				LFWAMCPersTool.checkOutFile(key);
			}
			amcServiceObj.setEventsMap(eventsMap);
		}
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		if(events != null && events.length > 0){
			for(EventConf event : events){
				if(event.getEventStatus() == EventConf.ADD_STATUS){
					event.setEventStatus(EventConf.NORMAL_STATUS);
				}else if(event.getEventStatus() == EventConf.DEL_STATUS){
					winConf.removeEventConf(event.getName(), event.getMethodName());
				}
			}
		}
		LFWAMCPersTool.refreshCurrentPorject();
	}
	
	/**
	 * 创建View（生成XML文件和Controller类文件）
	 * @param packageName
	 * @param className
	 * @param classFilePath
	 * @param classFileName
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param viewConf
	 * @param isFlowlayout
	 */
	public static void createView(String packageName, String className, String classFilePath, String classFileName, String filePath, String fileName, String projectPath, LfwView viewConf, UIMeta uimeta){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.CreateViewXml);

		amcServiceObj.setLfwWidget(viewConf);
		
		amcServiceObj.setCurrentProjPath(projectPath);
		
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		
		if(classFileName != null)
			checkFile(classFilePath, classFileName);
		amcServiceObj.setPackageName(packageName);
		amcServiceObj.setClassName(className);
		amcServiceObj.setClassFilePath(classFilePath);
		amcServiceObj.setClassFileName(classFileName);
		
//		amcServiceObj.setFlowlayout(isFlowlayout);
//		amcServiceObj.setCreateUIMeta(createUIMeta);
		
		amcServiceObj.setUimeta(uimeta);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		
		LFWAMCPersTool.refreshCurrentPorject();
	}
	
	/**
	 * 更新View（类文件、XML文件）
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param app
	 */
	public static void updateView(String filePath, String fileName, String projectPath, LfwView viewConf){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		//操作类型
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.UpdateViewXmlAndController);
		//View
		amcServiceObj.setLfwWidget(viewConf);
		//当前工程路径
		amcServiceObj.setCurrentProjPath(projectPath);
		//XML文件
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		//事件
		EventConf[] events = ViewEventTool.getAllEvents(viewConf, null);
		if(events != null && events.length > 0){
			Map<String, List<EventConf>> eventsMap = new HashMap<String, List<EventConf>>();
			for(EventConf event : events){
				if(event.getClassFilePath() != null && event.getClassFileName() != null){
					List<EventConf> list = eventsMap.get(event.getClassFilePath() + File.separator + event.getClassFileName());
					if(list == null){
						list = new ArrayList<EventConf>();
					}
					list.add(event);
					eventsMap.put(event.getClassFilePath() + File.separator + event.getClassFileName(), list);
				}
			}
			String key = null;
			Iterator<String> keys = eventsMap.keySet().iterator();
			while(keys.hasNext()){
				key = keys.next();
				MainPlugin.getDefault().logInfo("类文件路径：" + key);
				LFWAMCPersTool.checkOutFile(key);
			}
			amcServiceObj.setEventsMap(eventsMap);
		}
		//sessionId
		LFWBrowserEditor editorPart = null;
		List<LFWBrowserEditor> editors = LFWTool.getAllWebEditors();
		if(editors != null && editors.size() > 0){
			for(LFWBrowserEditor editor : editors){
				if(editor instanceof ViewBrowserEditor){
					IEditorInput input = ((ViewBrowserEditor)editor).getEditorInput();
					if(input instanceof WidgetEditorInput){
						if(viewConf.getPagemeta() != null && viewConf.getPagemeta().getId().equals(((WidgetEditorInput)input).getWidget().getPagemeta().getId())){
							if(viewConf.getId().equals(((WidgetEditorInput)input).getWidget().getId())){
								amcServiceObj.setSessionId(((ViewBrowserEditor)editor).getSessionId());
								editorPart = editor;
								break;
							}
						}
					}
				}else if(editor instanceof PublicViewBrowserEditor){
					IEditorInput input = ((PublicViewBrowserEditor)editor).getEditorInput();
					if(input instanceof PublicViewEditorInput){
						if(viewConf.getId().equals(((PublicViewEditorInput)input).getWidget().getId())){
							amcServiceObj.setSessionId(((PublicViewBrowserEditor)editor).getSessionId());
							editorPart = editor;
							break;
						}
					}
				}
			}
		}
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		if(events != null && events.length > 0){
			for(EventConf event : events){
				if(event.getEventStatus() == EventConf.ADD_STATUS){
					event.setEventStatus(EventConf.NORMAL_STATUS);
				}else if(event.getEventStatus() == EventConf.DEL_STATUS){
					ViewEventTool.removeEvent(viewConf, event.getName(), event.getMethodName());
				}
			}
		}
		if(editorPart != null){
			if(editorPart instanceof ViewBrowserEditor){
				((ViewBrowserEditor)editorPart).execute("refreshDs();");
			}else if(editorPart instanceof PublicViewBrowserEditor){
				((PublicViewBrowserEditor)editorPart).execute("refreshDs();");
			}
		}
		LFWAMCPersTool.refreshCurrentPorject();
	}
	
	/**
	 * 更新View（XML文件）
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param viewConf
	 */
	public static void updateViewToXml(String filePath, String fileName, String projectPath, LfwView viewConf){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.UpdateWidgetXml);
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		amcServiceObj.setCurrentProjPath(projectPath);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		amcServiceObj.setLfwWidget(viewConf);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		
		LFWPersTool.refreshCurrentPorject();
	}
	
	/**
	 * 创建UIMeta（XML文件）
	 * @param folderPath
	 */
	public static void createUIMeta(String filePath, String fileName, UIMeta meta){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.CreateUIMeta);
		checkFile(filePath, fileName);
		amcServiceObj.setFilePath(filePath);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		amcServiceObj.setUimeta(meta);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}	
	
	/**
	 * 保存session中PageMeta和UIMeta到XML文件中
	 * @param sessionId
	 * @param pageMetaId
	 * @param nodePath
	 */
	public static void savePageMetaAndUIMetaFromSessionCache(String sessionId, String pageMetaId, String nodePath){
		checkFile(nodePath, WEBPersConstants.AMC_WINDOW_FILENAME);
		checkFile(nodePath, WEBPersConstants.AMC_UIMETA_FILENAME);
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.SavePageMetaAndUIMetaFromSessionCache);
		amcServiceObj.setSessionId(sessionId);
		LfwWindow pageMeta = new LfwWindow();
		pageMeta.setId(pageMetaId);
		amcServiceObj.setPageMeta(pageMeta);
		amcServiceObj.setAmcNodePath(nodePath);
		//上下文
		amcServiceObj.setRootPath(getContextPath());

		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	/**
	 * 保存session中View和UIMeta到XML文件中
	 * @param sessionId
	 * @param pageMetaId
	 * @param nodePath
	 */
	public static void saveWidgetAndUIMetaFromSessionCache(String sessionId, String pageMetaId, String widgetId, String nodePath){
		checkFile(nodePath, WEBPersConstants.AMC_VIEW_FILENAME);
		checkFile(nodePath, WEBPersConstants.AMC_UIMETA_FILENAME);
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElementXMLType(AMCServiceObj.SaveWidgetAndUIMetaFromSessionCache);
		amcServiceObj.setSessionId(sessionId);
		LfwWindow pageMeta = new LfwWindow();
		pageMeta.setId(pageMetaId);
		amcServiceObj.setPageMeta(pageMeta);
		LfwView widget = new LfwView();
		widget.setId(widgetId);
		amcServiceObj.setLfwWidget(widget);
		amcServiceObj.setAmcNodePath(nodePath);
		//上下文
		amcServiceObj.setRootPath(getContextPath());
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElementXML(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
//	/************************OperateVO************************/
//	/**
//	 * 获取在Model节点中已存在的组件
//	 * @return
//	 */
//	public static List<MdComponnetVO> getExistComponents(){
//		AMCServiceObj amcServiceObj = new AMCServiceObj();
//		amcServiceObj.setOperateVO(AMCServiceObj.GetExistComponentIds);
//		String projPath = LFWAMCPersTool.getProjectPath();
//		amcServiceObj.setCurrentProjPath(projPath);
//		amcServiceObj.setItemType(ILFWTreeNode.MODEL);
//		amcServiceObj.setAmcNodePath(WEBProjConstants.AMC_MODEL_PATH);
//		amcServiceObj.setSuffix(".mod");
//		amcServiceObj.setTagName(WEBProjConstants.MODEL_SUB);
//		return getAMCDesignDataProvider().operateVO(amcServiceObj).getComponentVOList();
//	}
	
	/************************OperateWebElement************************/
	/**
	 * 获取树节点名称
	 * @param projPaths
	 * @param itemType
	 * @param nodePath
	 * @return
	 */
	/*
	public static Map<String, String>[] getTreeNodeNames(String[] projPaths, String itemType, String nodePath){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetAMCNames);
		amcServiceObj.setProjPaths(projPaths);
		amcServiceObj.setItemType(itemType);
		if(ILFWTreeNode.APPLICATION.equals(itemType)){
			if(nodePath == null){
				nodePath = WEBPersConstants.AMC_APPLICATION_PATH;
			}
			amcServiceObj.setAmcNodePath(nodePath);
			amcServiceObj.setSuffix(".app");
			amcServiceObj.setTagName(WEBPersConstants.APPLICATION_SUB);
		}else if(ILFWTreeNode.WINDOW.equals(itemType)){
			if(nodePath == null){
				nodePath = WEBPersConstants.AMC_WINDOW_PATH;
			}
			amcServiceObj.setAmcNodePath(nodePath);
			amcServiceObj.setSuffix(".pm");
			amcServiceObj.setTagName(WEBPersConstants.WINDOW_SUB);
		}else if(ILFWTreeNode.PUBLIC_VIEW.equals(itemType)){
			if(nodePath == null){
				nodePath = WEBPersConstants.AMC_PUBLIC_VIEW_PATH;
			}
			amcServiceObj.setAmcNodePath(nodePath);
			amcServiceObj.setSuffix(".wd");
			amcServiceObj.setTagName(WEBPersConstants.PUBLIC_VIEW_TAGNAME);
		}
		return getAMCDesignDataProvider().operateWebElement(amcServiceObj).getWebElementNames();
	}
	
	*/
	
	/**
	 * 获取Application下包含的所有Window
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	/*
	public static List<LfwWindow> getAppWindowList(String filePath, String fileName){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetAppWindowList);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		return getAMCDesignDataProvider().operateWebElement(amcServiceObj).getPmList();
	}
	*/
	
	/**
	 * 获取Application（读取XML文件）
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static Application getApplication(String filePath, String fileName){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetApplication);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Application app = getAMCDesignDataProvider().operateWebElement(amcServiceObj).getAppConf();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return app;
	}
	
//	/**
//	 * 获取Window
//	 * @param paramMap
//	 * @param userinfoMap
//	 * @return
//	 */
//	public static LfwWindow getWindow(Map<String, Object> paramMap){
//		AMCServiceObj amcServiceObj = new AMCServiceObj();
//		amcServiceObj.setOperateWebElement(AMCServiceObj.GetWindowWithWidget);
//		amcServiceObj.setParamMap(paramMap);
////		amcServiceObj.setUserInfoMap(userInfoMap);
//		return getAMCDesignDataProvider().operateWebElement(amcServiceObj).getPageMeta();
//	}
	
	/**
	 * 获取Window（读取XML文件）
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static LfwWindow getWindow(String filePath, String fileName){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetWindow);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		LfwWindow win = getAMCDesignDataProvider().operateWebElement(amcServiceObj).getPageMeta();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return win;
	}
	
	/**
	 * 获取View（读取XML文件）
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static LfwView getView(String filePath, String fileName){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetView);
		amcServiceObj.setFilePath(filePath);
		amcServiceObj.setFileName(fileName);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		LfwView view = getAMCDesignDataProvider().operateWebElement(amcServiceObj).getLfwWidget();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return view;
	}
	
	/**
	 * 获取UIMeta（读取XML文件）
	 * @param filePath
	 * @return
	 */
	public static UIMeta getUIMeta(String filePath){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetUIMeta);
		amcServiceObj.setFilePath(filePath);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		UIMeta uimeta = getAMCDesignDataProvider().operateWebElement(amcServiceObj).getUimeta();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return uimeta;
	}
	
	/**
	 * 获取同一上下文的所有Window
	 * @return
	 */
	public static List<LfwWindow> getCacheWindows(){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetCacheWindows);
		String ctxPath = getContextPath();
		amcServiceObj.setCtxPath(ctxPath);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List<LfwWindow> windowList = getAMCDesignDataProvider().operateWebElement(amcServiceObj).getPmList();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return windowList;
	}
	/**
	 * 刷新application
	 */
//	public static void refreshApplication(String ctxPath,Application app){
//		getAMCDesignDataProvider().refreshApplication(ctxPath,app);
//	}
//	public static void refreshPagemeta(String ctxPath,LfwWindow pm){
//		getAMCDesignDataProvider().refreshPagemeta(ctxPath,pm);
//	}
	
	
	/**
	 * 更新Session中的View
	 * @param viewConf
	 */
	public static void updateViewSessionCache(LfwView viewConf, UIMeta uimeta, Map<String, UIElement> elementMap){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		//操作类型
		amcServiceObj.setOperateWebElement(AMCServiceObj.UpdateSessionCacheForView);
		//View
		amcServiceObj.setLfwWidget(viewConf);
		//UIMeta
		amcServiceObj.setUimeta(uimeta);
		//UIElement
		amcServiceObj.setElementMap(elementMap);
		
		amcServiceObj.setPageMeta(viewConf.getWindow());
		//事件
		EventConf[] events = ViewEventTool.getAllEvents(viewConf, null);
		if(events != null && events.length > 0){
			Map<String, List<EventConf>> eventsMap = new HashMap<String, List<EventConf>>();
			for(EventConf event : events){
				if(event.getClassFilePath() != null && event.getClassFileName() != null){
					List<EventConf> list = eventsMap.get(event.getClassFilePath() + File.separator + event.getClassFileName());
					if(list == null){
						list = new ArrayList<EventConf>();
					}
					list.add(event);
					eventsMap.put(event.getClassFilePath() + File.separator + event.getClassFileName(), list);
				}
			}
			String key = null;
			Iterator<String> keys = eventsMap.keySet().iterator();
			while(keys.hasNext()){
				key = keys.next();
				MainPlugin.getDefault().logInfo("类文件路径：" + key);
				LFWAMCPersTool.checkOutFile(key);
			}
			amcServiceObj.setEventsMap(eventsMap);
		}
		if(uimeta != null){
			events = ViewEventTool.getAllEvents(null, uimeta);
			if(events != null && events.length > 0){
				Map<String, List<EventConf>> eventsMap = amcServiceObj.getEventsMap();
				if(eventsMap == null){
					eventsMap = new HashMap<String, List<EventConf>>();
				}
				for(EventConf event : events){
					if(event.getClassFilePath() != null && event.getClassFileName() != null){
						List<EventConf> list = eventsMap.get(event.getClassFilePath() + File.separator + event.getClassFileName());
						if(list == null){
							list = new ArrayList<EventConf>();
						}
						list.add(event);
						eventsMap.put(event.getClassFilePath() + File.separator + event.getClassFileName(), list);
					}
				}
				String key = null;
				Iterator<String> keys = eventsMap.keySet().iterator();
				while(keys.hasNext()){
					key = keys.next();
					MainPlugin.getDefault().logInfo("类文件路径：" + key);
					LFWAMCPersTool.checkOutFile(key);
				}
				amcServiceObj.setEventsMap(eventsMap);
			}
		}
		//sessionId
		List<LFWBrowserEditor> editors = LFWTool.getAllWebEditors();
		if(editors != null && editors.size() > 0){
			for(LFWBrowserEditor editor : editors){
				if(editor instanceof ViewBrowserEditor){
					IEditorInput input = ((ViewBrowserEditor)editor).getEditorInput();
					if(input instanceof WidgetEditorInput){
						if(viewConf.getPagemeta() != null && viewConf.getPagemeta().getId().equals(((WidgetEditorInput)input).getWidget().getPagemeta().getId())){
							if(viewConf.getId().equals(((WidgetEditorInput)input).getWidget().getId())){
								amcServiceObj.setSessionId(((ViewBrowserEditor)editor).getSessionId());
								break;
							}
						}
					}
				}else if(editor instanceof PublicViewBrowserEditor){
					IEditorInput input = ((PublicViewBrowserEditor)editor).getEditorInput();
					if(input instanceof PublicViewEditorInput){
						if(viewConf.getId().equals(((PublicViewEditorInput)input).getWidget().getId())){
							amcServiceObj.setSessionId(((PublicViewBrowserEditor)editor).getSessionId());
							break;
						}
					}
				}
			}
		}
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElement(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	
	/**
	 * 获取同一上下文的所有公共View
	 * @param ctx
	 * @return
	 */
//	public static Map<String, Map<String, LfwView>>  getAllPublicViews(){
//		String ctx = getContextPath();
//		Map<String, Map<String, LfwView>> cacheMap = LFWConnector.getAllPoolWidgets(ctx);
//		if(cacheMap != null){
//			Set<String> keys = cacheMap.keySet();
//			List<String> list = new ArrayList<String>();
//			for(String key : keys){
//				if(ctx != null && !ctx.equals(key)){
//					list.add(key);
//				}
//			}
//			for(String key : list){
//				cacheMap.remove(key);
//			}
//		}
//		return cacheMap;
//	}
	
	/**
	 * 清除session
	 * @param sessionId
	 */
	public static void clearSessionCache(String sessionId){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.ClearSession);
		amcServiceObj.setSessionId(sessionId);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElement(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	/**
	 * 从session中获取PageMeta
	 * @param sessionId
	 * @param pageMetaId
	 * @return
	 */
	public static LfwWindow getPageMetaFromSessionCache(String sessionId, String pageMetaId){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetPageMetaFromSessionCache);
		amcServiceObj.setSessionId(sessionId);
		LfwWindow pm = new LfwWindow();
		pm.setId(pageMetaId);
		amcServiceObj.setPageMeta(pm);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElement(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return amcServiceObj.getPageMeta();
	}
	
	/**
	 * 设置PageMeta到session中
	 * @param sessionId
	 * @param pageMeta
	 */
	public static void setPageMetaToSessionCache(String sessionId, LfwWindow pageMeta){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.SetPageMetaToSessionCache);
		amcServiceObj.setSessionId(sessionId);
		amcServiceObj.setPageMeta(pageMeta);
		
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getAMCDesignDataProvider().operateWebElement(amcServiceObj);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static Map<String, LfwComponent> getCacheComponentMap(String ctx, String moduleId, String businessCompId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String, LfwComponent> componentMap = getAMCDesignDataProvider().getCacheComponentMap(ctx, moduleId, businessCompId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return componentMap;
	}
	public static Map<String, LfwComponent> getCacheComponentMap(String moduleId, String businessCompId){
		String ctx = getContextPath();
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String, LfwComponent> componentMap = getCacheComponentMap(ctx, moduleId, businessCompId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return componentMap;
	}
	public static Map<String,LfwComponent> getCacheViewCompMap(String ctx , String moduleId, String businessCompId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String,LfwComponent> componentMap = getAMCDesignDataProvider().getCacheViewCompMap(ctx, moduleId, businessCompId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return componentMap;
	}
	public static Map<String,LfwComponent> getCacheViewCompMap(String moduleId, String businessCompId){
		String ctx = getContextPath();
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String,LfwComponent> componentMap = getCacheViewCompMap(ctx, moduleId, businessCompId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return componentMap;
	}
	public static Map<String,Application> getApplications(String moduleId,String businessCompId){
		String ctx = getContextPath();
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String,Application> appMap = getAMCDesignDataProvider().getApplicationMap(ctx, moduleId, businessCompId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appMap;
	}
	public static Map<String,LfwView> getPublicViews(String ctxPath){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String,LfwView> viewMap = getAMCDesignDataProvider().getPublicViews(ctxPath);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return viewMap;
	}
	public static Application getApplicationById(String appId){
		String ctx = getContextPath();
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Application app = getAMCDesignDataProvider().getApplicationById(ctx, appId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return app;
	}
	public static LfwWindow getWindowById(String winId){
		String ctx = getContextPath();
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		LfwWindow window = getAMCDesignDataProvider().getWindowById(ctx, winId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return window;
	}
	
	public static UIMeta getUImetaById(String uiMetaId){
		String ctx = getContextPath();
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		UIMeta uimeta = getAMCDesignDataProvider().getUImetaById(ctx, uiMetaId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return uimeta;
	}
	public static Map<String, LfwDevModel> getLfwDevModelCache(String ctxPath){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String, LfwDevModel> modelMap = getAMCDesignDataProvider().getLfwDevModelCache(ctxPath);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return modelMap;
	}
	
	public static final String KEY_PAGEMETA = "key_pagemeta";
	public static final String KEY_UIMETA = "key_pageuimeta";
	/**
	 * 从session中获取元素
	 * @param pageMeta
	 * @param uiMeta
	 */
	public static Map<String, Object> getElementFromSessionCache(String pageMetaId, String sessionId){
		AMCServiceObj amcServiceObj = new AMCServiceObj();
		amcServiceObj.setOperateWebElement(AMCServiceObj.GetElementFromSessionCache);
		LfwWindow pm = new LfwWindow();
		pm.setId(pageMetaId);
		amcServiceObj.setPageMeta(pm);
		amcServiceObj.setSessionId(sessionId);
		amcServiceObj = getAMCDesignDataProvider().operateWebElement(amcServiceObj);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_PAGEMETA, amcServiceObj.getPageMeta());
		map.put(KEY_UIMETA, amcServiceObj.getUimeta());
		return map;
	}
	
	/**
	 * 连接UAPWEB服务
	 * @return
	 */
	private static ILfwAMCDesignDataProvider getAMCDesignDataProvider() {
//		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		try {		
			Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
			Class<?> c = Class.forName("nc.uap.lfw.design.impl.LfwAMCDesignDataProviderImpl", true, ClassPathProvider.getClassLoader());			
			return (ILfwAMCDesignDataProvider) c.newInstance();
		} 
		catch (Exception e) {
			MessageDialog.openWarning(null, "", "Web Service is not loaded currectly,please restart studio");
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		return null;
	}
		
}
