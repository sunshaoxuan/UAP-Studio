package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import javax.swing.JOptionPane;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.EditNodeAction;
import nc.lfw.editor.pagemeta.RefreshNodeAction;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.AMCPublishNCAction;
import nc.uap.lfw.application.ApplicationNodeAction;
import nc.uap.lfw.application.DeleteApplicationNodeAction;
import nc.uap.lfw.application.EditApplicationNodeAction;
import nc.uap.lfw.application.ExcelAppNodeAction;
import nc.uap.lfw.application.ManualAppNodeAction;
import nc.uap.lfw.application.PreviewAction;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.component.CreateComponentAction;
import nc.uap.lfw.component.RefreshComponentAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.CreateVirtualFolderAction;
import nc.uap.lfw.core.base.DeleteUIAction;
import nc.uap.lfw.core.base.DeleteVirtualFolderAction;
import nc.uap.lfw.core.base.RefreshAMCNodeGroupAction;
import nc.uap.lfw.dataset.NewPoolDsAction;
import nc.uap.lfw.design.itf.BusinessComponent;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.extNode.CreateBuildFileAction;
import nc.uap.lfw.editor.extNode.CreateTemplateAction;
import nc.uap.lfw.editor.extNode.CreateWfmCateAction;
import nc.uap.lfw.editor.extNode.JavaLangAction;
import nc.uap.lfw.editor.extNode.JavaLangRecoverAction;
import nc.uap.lfw.editor.extNode.LangResourceAction;
import nc.uap.lfw.editor.extNode.OpenXMLEditorAction;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.action.DesignExcelImportAction;
import nc.uap.lfw.perspective.action.InitScriptImportAciton;
import nc.uap.lfw.perspective.action.MapImportAction;
import nc.uap.lfw.perspective.action.OtherFileImportAction;
import nc.uap.lfw.perspective.action.PdmImportAciton;
import nc.uap.lfw.perspective.commands.RefreshPubDataAction;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeBuilder;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.publicview.DelPublicViewAction;
import nc.uap.lfw.ref.TemplateRefNodeAction;
import nc.uap.lfw.refnode.NewPoolRefNodeAction;
import nc.uap.lfw.template.TemplateWindowNodeAction;
import nc.uap.lfw.window.DeleteWindowNodeAction;
import nc.uap.lfw.window.RefreshWindowNodeAction;
import nc.uap.lfw.window.WindowUIGuideAction;
import nc.uap.lfw.window.view.ImportViewAction;
import nc.uap.lfw.window.view.ViewNodeAction;
import nc.uap.lfw.window.view.ViewNodeFromPublicViewAction;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;

import uap.lfw.smartmw.TomcatEmbedServer;

public class LFWDirtoryTreeItem extends LFWBasicTreeItem implements
		ILFWTreeNode {

	/**
	 * 文件夹类型
	 */
	private String type = ""; //$NON-NLS-1$

	public Object object;
	
	public BusinessComponent bcp;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public LFWDirtoryTreeItem(TreeItem parentItem, Object object, String text) {
		super(parentItem, SWT.NONE);
		this.object = object;
		setData(object);
		setText(text);
		setImage(getDirImage());
	}

	public LFWDirtoryTreeItem(TreeItem parentItem, File file) {
		this(parentItem, file, file.getName());
	}
	public LFWDirtoryTreeItem(Tree parent, File file){
		super(parent, SWT.NONE);
		this.object = file;
		setData(file);
	}

	protected void checkSubclass() {
	}

	protected Image getDirImage() {
		ImageDescriptor imageDescriptor = null;
		Image currentImage = null;
//		if (object instanceof RefNodeConf) {
//			currentImage = ImageProvider.refnode;
//		} 
		if(type!= null && !"".equals(type)){ //$NON-NLS-1$
			 if(type.equals(LFWDirtoryTreeItem.UIS_FOLDER)){
				 currentImage = ImageProvider.uinodes;
			}
			else if(type.equals(LFWDirtoryTreeItem.APPLICATION_FOLDER)||type.endsWith(LFWDirtoryTreeItem.COMPONENT)||type.equals(LFWDirtoryTreeItem.WINDOW_FOLDER)||type.equals(LFWDirtoryTreeItem.PUBLIC_VIEW_FOLDER)||type.equals(LFWDirtoryTreeItem.REFINFO)){
				currentImage = ImageProvider.uinode;
			}
			
			else if(type.equals(LFWDirtoryTreeItem.SRC_FOLDER)){
				currentImage = ImageProvider.src;
			}
			else if(type.equals(LFWDirtoryTreeItem.FUNC_FOLDER)){
				currentImage = ImageProvider.refnode;
			}
			else if(type.equals(LFWDirtoryTreeItem.WFM_FOLDER)){
				currentImage = ImageProvider.wfm;
			}
			else if(type.equals(LFWDirtoryTreeItem.TEMPLATE_FOLDER)){
				currentImage = ImageProvider.template;
			}
			else if(type.equals(LFWDirtoryTreeItem.QUERY_FOLDER)){
				currentImage = ImageProvider.query;
			}
			else if(type.equals(LFWDirtoryTreeItem.PRINT_FOLDER)){
				currentImage = ImageProvider.print;
			}
			else if(type.equals(LFWDirtoryTreeItem.MLR_FOLDER)){
				currentImage = ImageProvider.page_state;
			}
			else if(type.equals(LFWDirtoryTreeItem.SERVICE_FOLDER)){
				currentImage = ImageProvider.service;
			}
			else if(type.equals(LFWDirtoryTreeItem.BUILD_FOLDER)){
				currentImage = ImageProvider.editor;
			}
			else if(type.equals(LFWDirtoryTreeItem.DOC_FOLDER)){
				currentImage = ImageProvider.doc;
			}
			else if(type.equals(LFWDirtoryTreeItem.BUILDDISK_FOLDER)){
				currentImage = ImageProvider.pageflow;
			}
			else if(type.equals(LFWDirtoryTreeItem.PROJECTS_FOLDER)){
				currentImage = ImageProvider.refforder;
			}
			
		}
		if(currentImage == null){
			currentImage = ImageProvider.pages;
		}
		return currentImage;
	}

	public File getFile() {
//		return (File) getData();
		return new File(getData().toString());
	}

	public void deleteNode() {
		FileUtilities.deleteFile(getFile());
		LFWAMCPersTool.refreshCurrentPorject();
		dispose();
	}

	public String getIPathStr() {
		String parentIPath = ""; //$NON-NLS-1$
		TreeItem parent = getParentItem();
		if (parent instanceof ILFWTreeNode) {
			parentIPath = ((ILFWTreeNode) parent).getIPathStr();
		}
		return parentIPath + "/" + getFile().getName(); //$NON-NLS-1$

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
//		Image img = getImage();
//		if(img != null)
//			img.dispose();
		setImage(getDirImage());
	}

	public void addMenuListener(IMenuManager manager) {
		final LFWExplorerTreeView view = LFWExplorerTreeView
				.getLFWExploerTreeView(null);
		if (type.equals(LFWDirtoryTreeItem.POOLDSFOLDER)
				|| type.equals(LFWDirtoryTreeItem.PARENT_PUB_DS_FOLDER)) {
			NewPoolDsAction newPoolDsAction = new NewPoolDsAction();
			manager.add(newPoolDsAction);
		} else if (type.equals(LFWDirtoryTreeItem.POOLREFNODEFOLDER)
				|| type.equals(LFWDirtoryTreeItem.PARENT_PUB_REF_FOLDER)) {
			NewPoolRefNodeAction newPoolRefNodeAction = new NewPoolRefNodeAction();
			manager.add(newPoolRefNodeAction);
		}
		// else if (type.equals(LFWDirtoryTreeItem.PARENT_PUB_WIDGET_FOLDER)) {
		// NewPoolWidgetAction widgetAction = new NewPoolWidgetAction();
		// manager.add(widgetAction);
		// }
		// else if (type.equals(LFWDirtoryTreeItem.PARENT_NODE_FOLDER)) {
		// NewNodeAction newNodeAction = new NewNodeAction();
		// NewVirtualDirAction newVirDirAction = new NewVirtualDirAction();
		// RefreshNodeGroupAction refreshNodeGroupAction = new
		// RefreshNodeGroupAction();
		// manager.add(newNodeAction);
		// manager.add(newVirDirAction);
		// manager.add(refreshNodeGroupAction);
		// }
		// else if (type.equals(LFWDirtoryTreeItem.PAGEFLOW_FOLDER)) {
		// NewPageFlowAction newPageFlowAction = new NewPageFlowAction();
		// manager.add(newPageFlowAction);
		// }
		else if (type.equals(LFWDirtoryTreeItem.PARENT_PUB_REF_FOLDER)
				|| type.equals(LFWDirtoryTreeItem.PARENT_PUB_DS_FOLDER)) {
			RefreshPubDataAction pubDataAction = new RefreshPubDataAction();
			manager.add(pubDataAction);
		} else if (type.equals(LFWDirtoryTreeItem.NODE_FOLDER)) {
			final MenuManager uiMenuManager = new MenuManager(
					WEBPersConstants.UI_DESIGN);
			uiMenuManager.setRemoveAllWhenShown(true);

			EditNodeAction editNodeAction = new EditNodeAction();
			// DeleteNodeAction deleteAction = new DeleteNodeAction();
			RefreshNodeAction refreshNodeAction = new RefreshNodeAction();
			// manager.add(newWidgetAction);
			manager.add(editNodeAction);
			// manager.add(deleteAction);
			manager.add(refreshNodeAction);
		} else if (type.equals(LFWDirtoryTreeItem.PUB_REF_FOLDER)) {

//		} else if (type.equals(LFWDirtoryTreeItem.METADATA_FOLDER)){
//			NewDirAction newdirAction = new NewDirAction();
//			manager.add(newdirAction);
			
		}else if (type.equals(LFWDirtoryTreeItem.FUNC_FOLDER)) {
			ExcelAppNodeAction excelApplicationNodeAction = new ExcelAppNodeAction();
			manager.add(excelApplicationNodeAction);
			ManualAppNodeAction manualAppNodeAction = new ManualAppNodeAction();
			manager.add(manualAppNodeAction);

		}else if (type.equals(LFWDirtoryTreeItem.WFM_FOLDER)) {
			CreateWfmCateAction newWfmCateAction = new CreateWfmCateAction();
			manager.add(newWfmCateAction);
		}else if(type.equals(LFWDirtoryTreeItem.QUERY_FOLDER)){
//			CreateTemplateAction newTemplateAction = new CreateTemplateAction(LFWDirtoryTreeItem.QUERY_FOLDER);
//			manager.add(newTemplateAction);
		} else if(type.equals(LFWDirtoryTreeItem.PRINT_FOLDER)){
//			CreateTemplateAction newTemplateAction = new CreateTemplateAction(LFWDirtoryTreeItem.PRINT_FOLDER);
//			manager.add(newTemplateAction);
		}else if (type.equals(LFWDirtoryTreeItem.BUILDDISK_FOLDER)) {
			CreateBuildFileAction newBuildFileAction = new CreateBuildFileAction(this);
			manager.add(newBuildFileAction);
		} else if (type.equals(LFWDirtoryTreeItem.JAVALANG_FOLDER)) {
			JavaLangAction resourceAction = new JavaLangAction();
			JavaLangRecoverAction langRecoverAction = new JavaLangRecoverAction();
			manager.add(resourceAction);
			manager.add(langRecoverAction);
		}else if(type.equals(LFWDirtoryTreeItem.DOC_FOLDER)){
			File file = this.getFile();
			if("design".equals(file.getName())){ //$NON-NLS-1$
				DesignExcelImportAction deia = new DesignExcelImportAction();
				manager.add(deia);
			}else if("other".equals(file.getName())||"documents".equals(file.getName())){ //$NON-NLS-1$ //$NON-NLS-2$
				OtherFileImportAction ofia = new OtherFileImportAction();
				manager.add(ofia);
			}
			
		}else if(type.equals(LFWDirtoryTreeItem.PDM_FOLDER)){
//			File file = this.getFile();
			//			if ("建库脚本".equals(file.getName()))
			PdmImportAciton pi = new PdmImportAciton();
			manager.add(pi);
		}else if(type.equals(SCRIPT_FOLDER)){
			InitScriptImportAciton isia = new InitScriptImportAciton();
			manager.add(isia);
		}else if(type.equals(MAPPING_FOLDER)){
			MapImportAction mia = new MapImportAction();
			manager.add(mia);
		}
		else {
			addAMCMenuListener(manager);
		}
	}

	public void addAMCMenuListener(IMenuManager manager) {
		LFWDirtoryTreeItem item = (LFWDirtoryTreeItem) LFWPersTool
				.getTopList(LFWPersTool.getCurrentTreeItem());
		if (!item.getType().equals(LFWDirtoryTreeItem.MLR_FOLDER)) {
			final LFWExplorerTreeView view = LFWExplorerTreeView
					.getLFWExploerTreeView(null);
			if (type.equals(ILFWTreeNode.APPLICATION_FOLDER)) {
				// 新建
				ApplicationNodeAction appNodeAction = new ApplicationNodeAction();
				// 新建虚拟目录
				CreateVirtualFolderAction createVirFolderAction = new CreateVirtualFolderAction(
						type);
				
				// 删除虚拟目录
				DeleteVirtualFolderAction deleteVirFolderAction = new DeleteVirtualFolderAction();
				// 刷新
				RefreshAMCNodeGroupAction refreshAMCNodeGroupAction = new RefreshAMCNodeGroupAction(
						WEBPersConstants.AMC_APPLICATION_PATH,
						ILFWTreeNode.APPLICATION,
						WEBPersConstants.AMC_APPLICATION_FILENAME,getBcp());
				// AMCPublishNCAction manageMenuCategory = new
				// AMCPublishNCAction(WEBProjConstants.MANAGE_MENU_CATEGORY);
				// 右键菜单功能
				manager.add(appNodeAction);
				
//				manager.add(createVirFolderAction);
				// if(!(this instanceof LFWVirtualDirTreeItem)){
				// manager.add(manageMenuCategory);
				// }
				if (this instanceof LFWVirtualDirTreeItem) {
					manager.add(deleteVirFolderAction);
				}
				manager.add(refreshAMCNodeGroupAction);
			} else if (type.equals(ILFWTreeNode.APPLICATION)) {
				
//				//按规划创建
//				ExcelAppNodeAction excelApplicationNodeAction = new ExcelAppNodeAction();
//				
//				DelExcelAppNodeAction delExcelApplicationNodeAction = new DelExcelAppNodeAction();
				
				
				//模板创建
				TemplateWindowNodeAction tempWindowNodeAction  = new TemplateWindowNodeAction();
				
				PreviewAction previewAction = new PreviewAction();
				//打开APP文件
				OpenXMLEditorAction openAppAction = new OpenXMLEditorAction("app",object); //$NON-NLS-1$
				// 编辑
				EditApplicationNodeAction editNodeAction = new EditApplicationNodeAction();
				// 删除
				DeleteApplicationNodeAction deleteNodeAction = new DeleteApplicationNodeAction();
				// 发布节点
				MenuManager ncMenuManager = new MenuManager(
						WEBPersConstants.PUBLISH_NODE);
				ncMenuManager.setRemoveAllWhenShown(true);
				AMCPublishNCAction registerFunNode = new AMCPublishNCAction(
						WEBPersConstants.REGISTER_FUNCTION_NODE);
				AMCPublishNCAction registerMenu = new AMCPublishNCAction(
						WEBPersConstants.REGISTER_MENU);
				AMCPublishNCAction registerFormType = new AMCPublishNCAction(
						WEBPersConstants.REGISTER_FORM_TYPE);
				// 右键菜单功能
//				manager.add(excelApplicationNodeAction);
//				manager.add(delExcelApplicationNodeAction);
				manager.add(tempWindowNodeAction);
				manager.add(previewAction);
				manager.add(editNodeAction);
				manager.add(openAppAction);
				manager.add(deleteNodeAction);
//				manager.add(registerFunNode);
//				manager.add(registerMenu);
//				manager.add(registerFormType);
			} 
			else if(type.equals(ILFWTreeNode.WINDOW_COMPONENT)||type.equals(ILFWTreeNode.PUBVIEW_COMPONENT)){
				CreateComponentAction createAction = new CreateComponentAction();
				manager.add(createAction);
				RefreshComponentAction refreshAction = new RefreshComponentAction();
				manager.add(refreshAction);
//			}else if (type.equals(ILFWTreeNode.WINDOW_FOLDER)) {
//				// 新建
//				WindowNodeAction windowNodeAction = new WindowNodeAction();
//				// 新建虚拟目录
//				CreateVirtualFolderAction createVirFolderAction = new CreateVirtualFolderAction(
//						type);
//				// 删除虚拟目录
//				DeleteVirtualFolderAction deleteVirFolderAction = new DeleteVirtualFolderAction();
//				// 刷新
//				RefreshAMCNodeGroupAction refreshAMCNodeGroupAction = new RefreshAMCNodeGroupAction(
//						WEBPersConstants.AMC_WINDOW_PATH, ILFWTreeNode.WINDOW,
//						WEBPersConstants.AMC_WINDOW_FILENAME,getBcp());
//				// 右键菜单功能
//				manager.add(windowNodeAction);
//				manager.add(createVirFolderAction);
//				if (this instanceof LFWVirtualDirTreeItem) {
//					manager.add(deleteVirFolderAction);
//				}
//				manager.add(refreshAMCNodeGroupAction);
			} else if (type.equals(ILFWTreeNode.WINDOW)) {
				// UI设计
				final MenuManager uiMenuManager = new MenuManager(
						WEBPersConstants.UI_DESIGN,
						PaletteImage.getCreateMenuImgDescriptor(),
						WEBPersConstants.UI_DESIGN);
				uiMenuManager.setRemoveAllWhenShown(true);
				uiMenuManager.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						DeleteUIAction deleteUI = new DeleteUIAction();
						deleteUI.setSite(view.getViewSite());
						// UIGuideAction uiGuild = new UIGuideAction();
						WindowUIGuideAction uiGuide = new WindowUIGuideAction();
						// uiGuide.setSite(view.getViewSite());
						OpenXMLEditorAction openPmAction = new OpenXMLEditorAction("pm",object); //$NON-NLS-1$
						OpenXMLEditorAction openUmAction = new OpenXMLEditorAction("um",object); //$NON-NLS-1$
						manager.add(uiGuide);
						manager.add(openPmAction);
						manager.add(openUmAction);
						manager.add(deleteUI);
					}
				});
				// 新建View
				ViewNodeAction viewNodeAction = new ViewNodeAction();
				
				ImportViewAction ImportAction = new ImportViewAction();
				// 删除Window
				DeleteWindowNodeAction deleteAction = new DeleteWindowNodeAction();
				// EditWindowNodeAction editNodeAction = new
				// EditWindowNodeAction();
				// 刷新
				RefreshWindowNodeAction refreshNodeAction = new RefreshWindowNodeAction();
				// 编辑Window
				EditNodeAction editNodeAction = new EditNodeAction();
				// RefreshNodeAction refreshNodeAction = new
				// RefreshNodeAction();
				// 从publicview引用
				ViewNodeFromPublicViewAction viewNodeFromPVAction = new ViewNodeFromPublicViewAction();
				// 右键菜单功能
				manager.add(uiMenuManager);
				manager.add(viewNodeAction);
				manager.add(ImportAction);
				manager.add(viewNodeFromPVAction);
				manager.add(editNodeAction);
				manager.add(deleteAction);
//				manager.add(refreshNodeAction);
				
//			} else if (type.equals(ILFWTreeNode.PUBLIC_VIEW_FOLDER)) {
//				// 新建
//				PublicViewNodeAction publicViewAction = new PublicViewNodeAction();
//				// RefreshAMCNodeGroupAction refreshAMCNodeGroupAction = new
//				// RefreshAMCNodeGroupAction(WEBProjConstants.AMC_PUBLIC_VIEW_PATH,
//				// ILFWTreeNode.PUBLIC_VIEW,
//				// WEBProjConstants.AMC_PUBLIC_VIEW_FILENAME);
//				RefreshAMCNodeGroupAction refreshAMCNodeGroupAction = new RefreshAMCNodeGroupAction(
//						WEBPersConstants.AMC_PUBVIEW_PATH, ILFWTreeNode.PUBLIC_VIEW,
//						WEBPersConstants.AMC_WINDOW_FILENAME,getBcp());
//				// 右键菜单功能
//				manager.add(publicViewAction);
//				manager.add(refreshAMCNodeGroupAction);
			} else if (type.equals(ILFWTreeNode.PUBLIC_VIEW)) {
				// UI设计
				final MenuManager uiMenuManager = new MenuManager(
						WEBPersConstants.UI_DESIGN);
				uiMenuManager.setRemoveAllWhenShown(true);
				// uiMenuManager.addMenuListener(new IMenuListener() {
				// public void menuAboutToShow(IMenuManager manager) {
				// DeleteUIGuildAction deleteGuild = new DeleteUIGuildAction();
				// deleteGuild.setSite(view.getViewSite());
				// UIGuideAction uiGuild = new UIGuideAction();
				// uiGuild.setSite(view.getViewSite());
				// ViewUIGuideAction viewUIGuide = new ViewUIGuideAction();
				// manager.add(viewUIGuide);
				// // manager.add(uiGuild);
				// manager.add(deleteGuild);
				// }
				// });
				// editWidgetAction.setTreeItem(this);
				// 删除
				
				DelPublicViewAction delWidgetAction = new DelPublicViewAction();
				// 右键菜单功能
				manager.add(uiMenuManager);
				manager.add(delWidgetAction);
			}else if(type.equals(ILFWTreeNode.REFINFO)){
//				CreateRefInfoAction createAction = new CreateRefInfoAction(this);
				TemplateRefNodeAction tempRefNodeAction = new TemplateRefNodeAction(this);
				manager.add(tempRefNodeAction);
//				manager.add(createAction);
			}
			
			
				
		}
		else{
			LFWDirtoryTreeItem currentItem = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
			if(!getType().equals("langType")&&(!currentItem.getType().equals(LFWDirtoryTreeItem.MLR_FOLDER))&&(currentItem.getType().equals(LFWDirtoryTreeItem.LANG_FOLDER)||((LFWDirtoryTreeItem)currentItem.getParentItem()).getType().equals(LFWDirtoryTreeItem.LANG_FOLDER))){ //$NON-NLS-1$
				LangResourceAction resourceAction = new LangResourceAction();
				manager.add(resourceAction);
			}
		}
	}

	/**
	 * 判断是否是子节点
	 * 
	 * @param ti
	 * @return
	 */
	private boolean isNotChildNode(TreeItem ti) {
		TreeItem parentTreeItem = ti.getParentItem();
		if (parentTreeItem instanceof LFWDirtoryTreeItem) {
			if (!((LFWDirtoryTreeItem) parentTreeItem).getType().equals(
					LFWDirtoryTreeItem.NODE_FOLDER))
				return true;
			else
				return false;
		} else
			return false;

	}

	public void mouseDoubleClick() {
		LFWExplorerTreeView view = LFWExplorerTreeView
				.getLFWExploerTreeView(null);
		if (view.hasOpened(this)) {
			return;
		}
		IProject project = LFWPersTool.getCurrentProject();
		File file = null;
//		if (getParentItem() instanceof LFWBusinessCompnentTreeItem) {
//			LFWBusinessCompnentTreeItem projectTreeItem = (LFWBusinessCompnentTreeItem) getParentItem();
		if(LfwCommonTool.isBCPProject(project)){
			LFWBusinessCompnentTreeItem projectTreeItem = LFWPersTool.getCurrentBusiCompTreeItem();
			if(projectTreeItem!=null)
				file = projectTreeItem.getFile();
		} else {
//			LFWProjectTreeItem projectTreeItem = (LFWProjectTreeItem) getParentItem();
//			file = projectTreeItem.getFile();
			file = new File(project.getLocation().toOSString());
		}
//		if (getType().equals(LFWDirtoryTreeItem.PARENT_PUB_REF_FOLDER)) {
//			LFWExplorerTreeBuilder lfwExplorer = LFWExplorerTreeBuilder
//					.getInstance();
//			File dsRefNodeFile = new File(file, WEBProjConstants.PUBLIC_REFNODE);
//			lfwExplorer.initRefNodeSubTree(this, dsRefNodeFile, project
//					.getLocation().toString());
//		}
//		// 公共数据集
//		else if (getType().equals(LFWDirtoryTreeItem.PARENT_PUB_DS_FOLDER)) {
//			LFWExplorerTreeBuilder lfwExplorer = LFWExplorerTreeBuilder
//					.getInstance();
//			File dsFile = new File(file, WEBProjConstants.PUBLIC_DATASET);
//			lfwExplorer.initDsSubTree(this, dsFile, project.getLocation()
//					.toString());
//		}
		// 源码
		if (getType().equals(LFWDirtoryTreeItem.SRC_FOLDER)) {
			IWorkbenchPage wbp = WEBPersPlugin.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IViewPart view2 = wbp.findView(JavaUI.ID_PACKAGES);

			((PackageExplorerPart) view2).openInActivePerspective();
			if(view2!=null){	
				((PackageExplorerPart) view2).collapseAll();
				TreeViewer tree = ((PackageExplorerPart) view2).getTreeViewer();
				String proName = project.getName();
				JavaProject jProject = new JavaProject();
				jProject.setProject(project);
				Object[] expandObj = new Object[1];
				expandObj[0] = jProject;
				tree.setExpandedElements(expandObj);
			}
			

//		} else if (getType().equals(LFWDirtoryTreeItem.QUERY_FOLDER)) {
//			IWorkbenchPage wbp = WEBProjPlugin.getDefault().getWorkbench()
//					.getActiveWorkbenchWindow().getActivePage();
//			try {
//				SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput(
//						type);
//				wbp.openEditor(editorInput, "Templates");
//			} catch (PartInitException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else if (getType().equals(LFWDirtoryTreeItem.PRINT_FOLDER)) {
//			IWorkbenchPage wbp = WEBProjPlugin.getDefault().getWorkbench()
//					.getActiveWorkbenchWindow().getActivePage();
//			try {
//				SimpleBrowserEditorInput editorInput = new SimpleBrowserEditorInput(
//						type);
//				wbp.openEditor(editorInput, "Templates");
//			} catch (PartInitException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

	}
	public void expandTree(){
		LFWExplorerTreeBuilder lfwExplorer = LFWExplorerTreeBuilder
				.getInstance();
		TreeItem item = this;
		while(item.getParentItem()!=null){
			item = item.getParentItem();
		}
		IProject project = null;
		if(item instanceof LFWProjectTreeItem){
			project = ((LFWProjectTreeItem)item).getProjectModel().getJavaProject();
		}
//		IProject project = LFWPersTool.getCurrentProject();
		if(getType().equals(LFWDirtoryTreeItem.WFM_FOLDER)){
			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD")) //$NON-NLS-1$
			lfwExplorer.initWfmTree(this,project,getBcp());
		}
		if(getType().equals(LFWDirtoryTreeItem.FUNC_FOLDER)){
			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD")) //$NON-NLS-1$
			lfwExplorer.initFuncTree(this,project,getBcp());
		}
		else if(getType().equals(LFWDirtoryTreeItem.QUERY_FOLDER)){
			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD")) //$NON-NLS-1$
			lfwExplorer.initQryTempTree(this,project,getBcp());
		}
		else if(getType().equals(LFWDirtoryTreeItem.PRINT_FOLDER)){
			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD")) //$NON-NLS-1$
			lfwExplorer.initPrintTempTree(this,project,getBcp());
		}else if(getType().equals(LFWDirtoryTreeItem.UIS_FOLDER)){
			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD")){ //$NON-NLS-1$
//				int count = 0;
//				while(!TomcatEmbedServer.isStarted){
//					try {
//						Thread.sleep(1000);
//						count++;
//						if(count == 120){
//							JOptionPane.showMessageDialog(null,"启动超时");
//							break;
//						}
//					} catch (InterruptedException e) {
//						MainPlugin.getDefault().logError(e);
//					}					
//				}
				if(!TomcatEmbedServer.isStarted){
					MessageDialog.openInformation(null, "", M_perspective.LFWDirtoryTreeItem_17); //$NON-NLS-1$
					return;
				}
				else
					lfwExplorer.initAMCProjectTree(this, null, null,null,null,null, project, getBcp());
			}			
		}else if(getType().equals(LFWDirtoryTreeItem.LANG_FOLDER)){
			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD")) //$NON-NLS-1$
			lfwExplorer.initLangTree(this, null, null,null,null,null, project, getBcp());
		}
//		else if(getType().equals(LFWDirtoryTreeItem.WINDOW_FOLDER)){
//			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD"))
//			lfwExplorer.refreshAMCNodeSubTree(this,null,LFWDirtoryTreeItem.WINDOW,WEBProjConstants.AMC_WINDOW_FILENAME,project,getBcp());
//		}else if(getType().equals(LFWDirtoryTreeItem.PUBLIC_VIEW_FOLDER)){
//			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD"))
//			lfwExplorer.initTempTree(this,project,getBcp());
//		}
		else if(getType().equals(LFWDirtoryTreeItem.PROJECTS_FOLDER)){
			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD")) //$NON-NLS-1$
				lfwExplorer.initRefProjTree(this);
		}
		else if(getType().equals(LFWDirtoryTreeItem.WINDOW)){
			if(this.getItemCount()==1 && ((LFWDirtoryTreeItem)this.getItem(0)).getType().equals("LOAD")){ //$NON-NLS-1$
				Tree tree = LFWPersTool.getTree();
				tree.setSelection(this);
				lfwExplorer.initViewsByWindowNode(this);
			}
		}
	}

	public BusinessComponent getBcp() {
		return bcp;
	}

	public void setBcp(BusinessComponent bcp) {
		this.bcp = bcp;
	}
	
}
