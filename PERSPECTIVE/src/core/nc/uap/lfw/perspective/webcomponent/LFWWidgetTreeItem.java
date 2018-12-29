package nc.uap.lfw.perspective.webcomponent;

import java.io.File;
import java.util.List;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.editor.widget.EditWidgetAction;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.DeleteUIAction;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.extNode.LangResourceAction;
import nc.uap.lfw.editor.extNode.OpenXMLEditorAction;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.action.DeleteWidgetAction;
import nc.uap.lfw.perspective.action.RenameWidgetAction;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.publicview.DelPublicViewAction;
import nc.uap.lfw.publicview.EditorPublicViewAction;
import nc.uap.lfw.publicview.PublicViewUIGuideAction;
//import nc.uap.lfw.widget.DelPoolWidgetAction;
import nc.uap.lfw.window.view.ViewUIGuideAction;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * widget 树组件
 * 
 * @author zhangxya
 * 
 */
public class LFWWidgetTreeItem extends LFWDirtoryTreeItem {
	private LfwView widget = null;

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public LFWWidgetTreeItem(TreeItem parentItem, File file, LfwView widget,
			String text) {
		super(parentItem, file, text);
		this.widget = widget;
		// setData(file);
		// setText(text);
		// setImage(getDirImage());
	}

	public LFWWidgetTreeItem(TreeItem parentItem, File file, LfwView widget) {
		super(parentItem, file, null);
		this.widget = widget;
		setData(file);
		setText(file.getName());
		setImage(getDirImage());
	}

	protected void checkSubclass() {
	}

	protected Image getDirImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(
//				WEBProjPlugin.ICONS_PATH, "widget.gif");
		return ImageProvider.widget;
	}

	public File getFile() {
		return (File) getData();
	}

	public void deleteNode() {
		FileUtilities.deleteFile(getFile());
		dispose();

	}

	public String getIPathStr() {
		String parentIPath = "";
		TreeItem parent = getParentItem();
		if (parent instanceof ILFWTreeNode) {
			parentIPath = ((ILFWTreeNode) parent).getIPathStr();
		}
		return parentIPath + "/" + getFile().getName();

	}

	public void addMenuListener(IMenuManager manager) {
		LFWDirtoryTreeItem item = (LFWDirtoryTreeItem) LFWPersTool
				.getTopList(LFWPersTool.getCurrentTreeItem());
		if (!item.getType().equals(LFWDirtoryTreeItem.MLR_FOLDER)) {
			final LFWExplorerTreeView view = LFWExplorerTreeView
					.getLFWExploerTreeView(null);
			final MenuManager uiMenuManager = new MenuManager(
					WEBPersConstants.UI_DESIGN,
					PaletteImage.getCreateMenuImgDescriptor(),
					WEBPersConstants.UI_DESIGN);
			uiMenuManager.setRemoveAllWhenShown(true);

			// 获取当前Pagemeta
			LFWPageMetaTreeItem pmTreeItem = LFWPersTool
					.getCurrentPageMetaTreeItem();

			final File resFile = this.getFile();
			
			if(widget.getProvider()!=null){
				DeleteWidgetAction deleteWidgetAction = new DeleteWidgetAction();
				manager.add(deleteWidgetAction);
				return;
			}
			
			if (pmTreeItem == null) {// publicview
				uiMenuManager.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						PublicViewUIGuideAction viewUIGuide = new PublicViewUIGuideAction();
						DeleteUIAction deleteUI = new DeleteUIAction();
						deleteUI.setSite(view.getViewSite());
						OpenXMLEditorAction openFileAction = new OpenXMLEditorAction("wd", resFile); 
						OpenXMLEditorAction openUmFileAction = new OpenXMLEditorAction("um", resFile); 
						manager.add(viewUIGuide);
						manager.add(openFileAction);
						manager.add(openUmFileAction);
						manager.add(deleteUI);
					}
				});
				manager.add(uiMenuManager);
				EditorPublicViewAction editPVAction = new EditorPublicViewAction();
				editPVAction.setTreeItem(this);
				DelPublicViewAction delWidgetAction = new DelPublicViewAction();
				manager.add(editPVAction);
				manager.add(delWidgetAction);
				return;
			}
			// view
			if (isNotRefView()) {
				uiMenuManager.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						ViewUIGuideAction viewUIGuide = new ViewUIGuideAction();
						DeleteUIAction deleteViewUI = new DeleteUIAction();
						OpenXMLEditorAction openWdFileAction = new OpenXMLEditorAction("wd", resFile); 
						OpenXMLEditorAction openUmFileAction = new OpenXMLEditorAction("um", resFile); 
						deleteViewUI.setSite(view.getViewSite());
						manager.add(viewUIGuide);
						manager.add(openWdFileAction);
						manager.add(openUmFileAction);
						manager.add(deleteViewUI);
					}
				});
				manager.add(uiMenuManager);
			}

			LFWWidgetTreeItem widgettree = this;
			LFWDirtoryTreeItem parentTreeItem = (LFWDirtoryTreeItem) widgettree
					.getParentItem();
			File file = ((LFWDirtoryTreeItem) parentTreeItem).getFile();
			// 文件夹路径
			String folderPath = file.getPath();
			LfwWindow pm = pmTreeItem.getPm();
			String funnode = (String) pm
					.getExtendAttributeValue(WEBPersConstants.FUNC_CODE);
			String filePath = folderPath + "/" + widgettree.getWidget().getId();

			// final NCQueryTemplageAction ncQueryTemplateAction = new
			// NCQueryTemplageAction();
			// ncQueryTemplateAction.setFunnode(funnode);
			// ncQueryTemplateAction.setFilePath(filePath);
			//
			// ncMenuManager.addMenuListener(new IMenuListener() {
			// public void menuAboutToShow(IMenuManager manager) {
			// //关联NC模板
			// manager.add(publishNCAction);
			// manager.add(ncQueryTemplateAction);
			// EditNCTemplateAction editNCAction = new EditNCTemplateAction();
			// manager.add(editNCAction);
			// //取消关联nc单据模板
			// CancelNCTemplateAction cancelNC = new CancelNCTemplateAction();
			// manager.add(cancelNC);
			// //取消关联nc查询模板
			// CancelNCQryTemplateAction cancelNCQry = new
			// CancelNCQryTemplateAction();
			// manager.add(cancelNCQry);
			// }
			// });
			// 设置Widget编辑器参数
			EditWidgetAction editWidgetAction = new EditWidgetAction();
			DeleteWidgetAction deleteWidgetAction = new DeleteWidgetAction();
			RenameWidgetAction renameWidgetAction = new RenameWidgetAction();
			editWidgetAction.setTreeItem(this);

			if (isNotRefView()) {
				manager.add(editWidgetAction);
				manager.add(renameWidgetAction);
			}
			manager.add(deleteWidgetAction);

			// //组件引入action
			// if(!LFWTool.NEW_VERSION.equals(getLfwVersion())){
			// NewMdDsFormComponent mdCompnentAction = new
			// NewMdDsFormComponent();
			// manager.add(mdCompnentAction);
			// }
		}
	}

	public void mouseDoubleClick() {
		LFWDirtoryTreeItem item = (LFWDirtoryTreeItem) LFWPersTool
				.getTopList(LFWPersTool.getCurrentTreeItem());
		if (!item.getType().equals(LFWDirtoryTreeItem.MLR_FOLDER)) {
			if (ILFWTreeNode.POOLWIDGETFOLDER.equals(this.getType())) {
				EditorPublicViewAction editPVAction = new EditorPublicViewAction();
				editPVAction.setTreeItem(this);
				editPVAction.run();
//				if (getItemCount() == 0) {
//					this.removeAll();
//					LFWExplorerTreeView.getLFWExploerTreeView(null)
//							.detalWidgetTreeItem(this, this.getFile(), widget);
//				}
			} else if (ILFWTreeNode.POOLREFNODEFOLDER.equals(this.getType())) {
				String refId = this.getWidget().getRefId();
				if (refId != null && refId.startsWith("..")) {
					refId = refId.substring(3);
					List<TreeItem> tiList = LFWAMCPersTool.getAllChildren(
							LFWAMCPersTool.getCurrentProject(), this,
							ILFWTreeNode.PUBLIC_VIEW_FOLDER);
					if (tiList != null && tiList.size() > 0) {
						for (TreeItem ti : tiList) {
							if (ti instanceof LFWWidgetTreeItem) {
								if (refId.equals(((LFWWidgetTreeItem) ti)
										.getWidget().getId())) {
									EditorPublicViewAction editPVAction = new EditorPublicViewAction();
									editPVAction
											.setTreeItem((LFWWidgetTreeItem) ti);
									editPVAction.run();
									LFWAMCPersTool.getTree().select(ti);
									if (ti.getItemCount() == 0) {
										((LFWWidgetTreeItem) ti).removeAll();
										LFWExplorerTreeView
												.getLFWExploerTreeView(null)
												.detalWidgetTreeItem(
														((LFWWidgetTreeItem) ti),
														((LFWWidgetTreeItem) ti)
																.getFile(),
														((LFWWidgetTreeItem) ti)
																.getWidget());
									}
								}
							}
						}
					}
				}
			} else {
				if (isNotRefView()) {
					EditWidgetAction editWidgetAction = new EditWidgetAction();
					editWidgetAction.setTreeItem(this);
					editWidgetAction.run();
				}
			}
		}
		else{

			removeAll();
			IProject project = LFWPersTool.getCurrentProject();
			String path = LFWPersTool.getBcpPath(this);			
			String id = this.getId();
			String bcp = null;
			String nameStr = null;
			if (path == null) {
				path = LFWPersTool.getProjectPath().replace("/", "\\");
				nameStr = project.getName().toLowerCase() + "_nodes\\p_" + id + "-res.properties";
			}
			else{
				bcp = path.substring(path.lastIndexOf("\\") + 1);
				nameStr = bcp.toLowerCase() + "_nodes\\p_" + id + "-res.properties";
			}
			

			StringBuilder simpStr = new StringBuilder(path);
			simpStr.append("\\resources\\lang\\simpchn\\").append(nameStr);
//			StringBuilder tradStr = new StringBuilder(path);
//			tradStr.append("\\resources\\lang\\tradchn\\").append(nameStr);
//			StringBuilder engStr = new StringBuilder(path);
//			engStr.append("\\resources\\lang\\english\\").append(nameStr);

			LFWDirtoryTreeItem simpchnNode = new LFWDirtoryTreeItem(this,
					simpStr, "简体资源");
			simpchnNode.setType("langType");
//			LFWDirtoryTreeItem tradchnNode = new LFWDirtoryTreeItem(this,
//					tradStr, "繁体资源");
//			tradchnNode.setType("langType");
//			LFWDirtoryTreeItem englishNode = new LFWDirtoryTreeItem(this,
//					engStr, "英文资源");
//			englishNode.setType("langType");

			String simpPath = simpchnNode.getObject().toString();
			IFile resFile = null;
			if(bcp!=null){
				resFile = project.getFile(simpPath.substring(simpPath.indexOf("\\" + bcp + "\\")));
			}
			else resFile = project.getFile(simpPath.substring(simpPath.indexOf("\\resources\\")));		
			if (resFile.isAccessible()) {
				LFWMetadataTreeItem simpresNode = new LFWMetadataTreeItem(
						simpchnNode, simpchnNode.getObject(), id
								+ "-res.properties");
				simpresNode.setMdFile(resFile);
			}
//			String tradPath = tradchnNode.getObject().toString();
//			if(bcp!=null){
//				resFile = project.getFile(tradPath.substring(tradPath.indexOf("\\" + bcp + "\\")));
//			}
//			else resFile = project.getFile(tradPath.substring(tradPath.indexOf("\\resources\\")));	
//			if (resFile.isAccessible()) {
//				LFWMetadataTreeItem tradresNode = new LFWMetadataTreeItem(
//						tradchnNode, tradchnNode.getObject(), id
//								+ "-res.properties");
//				tradresNode.setMdFile(resFile);
//			}
//			String englishPath = englishNode.getObject().toString();
//			if(bcp!=null){
//				resFile = project.getFile(englishPath.substring(englishPath.indexOf("\\" + bcp + "\\")));
//			}
//			else resFile = project.getFile(englishPath.substring(englishPath.indexOf("\\resources\\")));	
//			if (resFile.isAccessible()) {
//				LFWMetadataTreeItem englishresNode = new LFWMetadataTreeItem(
//						englishNode, englishNode.getObject(), id
//						+ "-res.properties");
//				englishresNode.setMdFile(resFile);
//			}
			this.setExpanded(true);
		
		}
	}

	private boolean isNotRefView() {
		boolean isNotRefView = true;
		if (widget.getRefId() == null || widget.getRefId().startsWith("..")) {
			return false;
		}
		// String ctx =
		// LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
		// Map<String, Map<String, LfwWidget>> input =
		// RefDatasetData.getPoolWidgets("/" + ctx);
		// Iterator<String> keys = input.keySet().iterator();
		// if(keys != null){
		// while(keys.hasNext()){
		// LfwWidget pv =
		// input.get(keys.next()).get(widget.getRefId().replaceFirst("../",
		// ""));
		// if(pv != null){//当前View引用了PublicView
		// isNotRefView = false;
		// break;
		// }
		// }
		// }
		return isNotRefView;
	}

}