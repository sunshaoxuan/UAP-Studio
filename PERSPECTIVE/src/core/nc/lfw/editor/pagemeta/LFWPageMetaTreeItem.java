package nc.lfw.editor.pagemeta;

import java.io.File;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.lang.M_pagemeta;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMetadataTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;
import nc.uap.lfw.window.RefreshWindowNodeAction;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class LFWPageMetaTreeItem extends LFWDirtoryTreeItem {

	private LfwWindow pm = null;
	private static String BASE_PATH = "\\web\\html\\nodes\\"; //$NON-NLS-1$

	public LFWPageMetaTreeItem(TreeItem parentItem, Object object, String text) {
		super(parentItem, object, text);
	}

	protected Image getDirImage() {
		return ImageProvider.page;
	}

	public LfwWindow getPm() {
		if (pm == null) {
//			String projectPath = LFWAMCPersTool.getProjectPath();
			// 获取当前Pagemeta
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			paramMap.put(WebConstant.PROJECT_PATH_KEY, projectPath);
			String pageIdKey = LFWPersTool.getCurrentFolderPath(this).substring(LFWPersTool.getCurrentFolderPath(this).lastIndexOf(BASE_PATH) + 16);
//			paramMap.put(WebConstant.PAGE_ID_KEY, pageIdKey);
//			paramMap.put(WebConstant.PARENT_PAGE_ID_KEY, getParentPm());

			try {
				pm = LFWConnector.getPageMeta(pageIdKey);
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
				MessageDialog.openError(null, M_pagemeta.LFWPageMetaTreeItem_0, e.getMessage());
			}
		}
		if (pm != null) {
			LfwView[] widgets = pm.getViews();
			for (int i = 0; i < widgets.length; i++) {
				LfwView widget = widgets[i];
				Dataset[] dss = widget.getViewModels().getDatasets();
				IRefNode[] refnodes = widget.getViewModels().getRefNodes();
				WebComponent[] grids = widget.getViewComponents()
						.getComponentByType(GridComp.class);
				WebComponent[] forms = widget.getViewComponents()
						.getComponentByType(FormComp.class);
				if (dss != null) {
					for (int j = 0; j < dss.length; j++) {
						Dataset ds = dss[j];
						String dsCaption = ds.getCaption();
						if (dsCaption != null && !"".equals(dsCaption)) { //$NON-NLS-1$
							if (refnodes != null) {
								for (int k = 0; k < refnodes.length; k++) {
									IRefNode refnode = refnodes[k];
									if (refnode instanceof NCRefNode
											&& refnode.getId().indexOf(
													ds.getId()) != -1) {
										NCRefNode ncRefNode = (NCRefNode) refnode;
										if (ncRefNode.getText() != null
												&& !"".equals(ncRefNode //$NON-NLS-1$
														.getText()))
											continue;
										else
											ncRefNode.setText(dsCaption + "_" //$NON-NLS-1$
													+ ncRefNode.getRefcode());
									}
								}
							}
							if (forms != null) {
								for (int k = 0; k < forms.length; k++) {
									FormComp form = (FormComp) forms[k];
									if (form.getDataset() != null
											&& form.getDataset().equals(
													ds.getId())) {
										if (form.getCaption() != null
												&& !"".equals(form.getCaption())) //$NON-NLS-1$
											continue;
										else
											form.setCaption(dsCaption);
									}
								}
							}
							if (grids != null) {
								for (int k = 0; k < grids.length; k++) {
									GridComp grid = (GridComp) grids[k];
									if (grid.getDataset() != null
											&& grid.getDataset().equals(
													ds.getId())) {
										if (grid.getCaption() != null
												&& !"".equals(grid.getCaption())) //$NON-NLS-1$
											continue;
										else
											grid.setCaption(dsCaption);
									}
								}
							}
						}
					}
				}
			}
		}
		return pm;
	}

	public void setPm(LfwWindow pm) {
		this.pm = pm;
	}

	public LFWPageMetaTreeItem(TreeItem parentItem, File file) {
		super(parentItem, file);
	}

	public LFWPageMetaTreeItem(TreeItem parentItem, File file, String text) {
		super(parentItem, file, text);
		setText(text);
	}

	public String getPageId() {
		String folderPath = getFile().getPath();
		int index = folderPath.lastIndexOf("\\"); //$NON-NLS-1$
		return folderPath.substring(index + 1);
	}

	public String getPageId(TreeItem dir) {
		if (dir instanceof LFWDirtoryTreeItem) {
			LFWDirtoryTreeItem dird = (LFWDirtoryTreeItem) dir;
			String folderPath = dird.getFile().getPath();
			int index = folderPath.lastIndexOf("\\"); //$NON-NLS-1$
			return folderPath.substring(index + 1);
		} else
			return ""; //$NON-NLS-1$
	}

	public String getFullPageId() {
		String pageId = getPageId();
		TreeItem parentTreeItem = this.getParentItem();
		String parentId = null;
		while (parentTreeItem != null) {
			if (parentTreeItem instanceof LFWPageMetaTreeItem) {
				parentId = getPageId(parentTreeItem);
				pageId = parentId + "\\" + pageId; //$NON-NLS-1$
				parentTreeItem = parentTreeItem.getParentItem();
			} else if (parentTreeItem instanceof LFWVirtualDirTreeItem) {
				LFWVirtualDirTreeItem ptreeItem = (LFWVirtualDirTreeItem) parentTreeItem;
				String folderPath = ptreeItem.getFile().getPath();
				int index = folderPath.lastIndexOf("\\"); //$NON-NLS-1$
				parentId = folderPath.substring(index + 1);
				pageId = parentId + "\\" + pageId; //$NON-NLS-1$
				parentTreeItem = parentTreeItem.getParentItem();
			} else
				break;
		}
		return pageId;
	}

	public String getParentPageId() {
		if (this.getParentItem() != null
				&& this.getParentItem() instanceof LFWPageMetaTreeItem) {
			LFWPageMetaTreeItem ptreeItem = (LFWPageMetaTreeItem) this
					.getParentItem();
			return ptreeItem.getPageId();
		}
		return null;
	}

	public LfwWindow getParentPm() {
		if (this.getParentItem() != null
				&& this.getParentItem() instanceof LFWPageMetaTreeItem) {
			LFWPageMetaTreeItem ptreeItem = (LFWPageMetaTreeItem) this
					.getParentItem();
			return ptreeItem.getPm();
		}
		return null;
	}

	public void deleteNode() {
		// PageMeta pagemeta = LFWPersTool.getCurrentPageMeta();
		// if(pagemeta.getExtendAttribute(ExtAttrConstants.FUNC_CODE) != null &&
		// pm.getExtendAttribute(ExtAttrConstants.FUNC_CODE).getValue() !=
		// null){
		// String funnode = (String)
		// pm.getExtendAttribute(ExtAttrConstants.FUNC_CODE).getValue();
		// NCConnector.deleteFunNode(funnode);
		// }
		super.deleteNode();
	}

	public void mouseDoubleClick() {
		LFWDirtoryTreeItem item = (LFWDirtoryTreeItem) LFWPersTool
				.getTopList(LFWPersTool.getCurrentTreeItem());
		if (!item.getType().equals(LFWDirtoryTreeItem.MLR_FOLDER)) {
//			RefreshWindowNodeAction refreshNodeAction = new RefreshWindowNodeAction();
//			refreshNodeAction.run();
			EditNodeAction editNodeAction = new EditNodeAction();
			editNodeAction.run();
		} else {
			removeAll();
			IProject project = LFWPersTool.getCurrentProject();
			String path = LFWPersTool.getBcpPath(this);			
			String id = this.getId();
			String bcp = null;
			String nameStr = null;
			if (path == null) {
				path = LFWPersTool.getProjectPath().replace("/", "\\"); //$NON-NLS-1$ //$NON-NLS-2$
				nameStr = project.getName().toLowerCase() + "_nodes\\w_" + id + "-res.properties"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			else{
				bcp = path.substring(path.lastIndexOf("\\") + 1); //$NON-NLS-1$
				nameStr = bcp.toLowerCase() + "_nodes\\w_" + id + "-res.properties"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			

			StringBuilder simpStr = new StringBuilder(path);
			simpStr.append("\\resources\\lang\\simpchn\\").append(nameStr); //$NON-NLS-1$
//			StringBuilder tradStr = new StringBuilder(path);
//			tradStr.append("\\resources\\lang\\tradchn\\").append(nameStr); //$NON-NLS-1$
//			StringBuilder engStr = new StringBuilder(path);
//			engStr.append("\\resources\\lang\\english\\").append(nameStr); //$NON-NLS-1$

			LFWDirtoryTreeItem simpchnNode = new LFWDirtoryTreeItem(this,
					simpStr, M_pagemeta.LFWPageMetaTreeItem_1);
			simpchnNode.setType("langType"); //$NON-NLS-1$
			
//			LFWDirtoryTreeItem tradchnNode = new LFWDirtoryTreeItem(this,
//					tradStr, M_pagemeta.LFWPageMetaTreeItem_2);
//			tradchnNode.setType("langType"); //$NON-NLS-1$
//			LFWDirtoryTreeItem englishNode = new LFWDirtoryTreeItem(this,
//					engStr, M_pagemeta.LFWPageMetaTreeItem_3);
//			englishNode.setType("langType"); //$NON-NLS-1$

			String simpPath = simpchnNode.getObject().toString();
			IFile resFile = null;
			if(bcp!=null){
				resFile = project.getFile(simpPath.substring(simpPath.indexOf("\\" + bcp + "\\"))); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else resFile = project.getFile(simpPath.substring(simpPath.indexOf("\\resources\\")));		 //$NON-NLS-1$
			if (resFile.isAccessible()) {
				LFWMetadataTreeItem simpresNode = new LFWMetadataTreeItem(
						simpchnNode, simpchnNode.getObject(), id
								+ "-res.properties"); //$NON-NLS-1$
				simpresNode.setMdFile(resFile);
			}
//			String tradPath = tradchnNode.getObject().toString();
//			if(bcp!=null){
//				resFile = project.getFile(tradPath.substring(tradPath.indexOf("\\" + bcp + "\\"))); //$NON-NLS-1$ //$NON-NLS-2$
//			}
//			else resFile = project.getFile(tradPath.substring(tradPath.indexOf("\\resources\\")));	 //$NON-NLS-1$
//			if (resFile.isAccessible()) {
//				LFWMetadataTreeItem tradresNode = new LFWMetadataTreeItem(
//						tradchnNode, tradchnNode.getObject(), id
//								+ "-res.properties"); //$NON-NLS-1$
//				tradresNode.setMdFile(resFile);
//			}
//			String englishPath = englishNode.getObject().toString();
//			if(bcp!=null){
//				resFile = project.getFile(englishPath.substring(englishPath.indexOf("\\" + bcp + "\\"))); //$NON-NLS-1$ //$NON-NLS-2$
//			}
//			else resFile = project.getFile(englishPath.substring(englishPath.indexOf("\\resources\\")));	 //$NON-NLS-1$
//			if (resFile.isAccessible()) {
//				LFWMetadataTreeItem englishresNode = new LFWMetadataTreeItem(
//						englishNode, englishNode.getObject(), id
//						+ "-res.properties"); //$NON-NLS-1$
//				englishresNode.setMdFile(resFile);
//			}
			this.setExpanded(true);
		}

	}
}
