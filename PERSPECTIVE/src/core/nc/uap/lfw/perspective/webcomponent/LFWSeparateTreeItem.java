package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.editor.common.tools.LfwGlobalEditorInfo;
import nc.lfw.editor.contextmenubar.actions.NewContextMenuAction;
import nc.lfw.editor.menubar.action.NewMenubarAction;
import nc.uap.lfw.button.commands.NewButtonAction;
import nc.uap.lfw.chart.actions.NewChartAction;
import nc.uap.lfw.combodata.NewComboAction;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.action.LFWCopyAction;
import nc.uap.lfw.common.action.LFWPasteAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.ChartComp;
//import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.ListViewComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.PaginationComp;
import nc.uap.lfw.core.comp.ProgressBarComp;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.form.commands.NewFormAction;
import nc.uap.lfw.grid.commands.NewGridAction;
import nc.uap.lfw.grid.commands.NewGridMenuAction;
import nc.uap.lfw.iframe.commands.NewIFrameAction;
import nc.uap.lfw.image.commands.NewImageAction;
import nc.uap.lfw.label.commands.NewLabelAction;
import nc.uap.lfw.linkcomp.commands.NewLinkCompAction;
import nc.uap.lfw.listview.commands.NewListViewAction;
import nc.uap.lfw.pagination.commands.NewPaginationAction;
import nc.uap.lfw.perspective.action.ClearRefAction;
import nc.uap.lfw.perspective.action.EditDatasetsAction;
import nc.uap.lfw.perspective.action.NewDsAction;
import nc.uap.lfw.perspective.action.NewRefDsAction;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.progressbar.commands.NewProgressBarAction;
import nc.uap.lfw.refnode.NewRefnodeAction;
import nc.uap.lfw.refnoderel.NewRefNodeRelAction;
import nc.uap.lfw.selfdefcomp.commands.NewSelfdefCompAction;
import nc.uap.lfw.textcomp.commands.NewTextCompAction;
import nc.uap.lfw.tree.NewTreeAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class LFWSeparateTreeItem extends LFWBasicTreeItem implements ILFWTreeNode {
	public LFWSeparateTreeItem(TreeItem parentItem, File file) {
		super(parentItem, SWT.NONE);
		setData(file);
		setText(file.getName());
		setImage(getDirImage());
	}
	protected void checkSubclass () {
	}
	private Image getDirImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "groups.gif");
		return ImageProvider.groups;
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
		if(parent instanceof ILFWTreeNode){
			parentIPath = ((ILFWTreeNode)parent).getIPathStr();
		}
		return parentIPath+"/"+getFile().getName();
		
	}
	
	public void addMenuListener(IMenuManager manager){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		String key = LFWCopyAction.COPYKEY;
		WebElement webEle = null;
		if(LfwGlobalEditorInfo.getAttr(key) != null)
			webEle = (WebElement) LfwGlobalEditorInfo.getAttr(key);
		//粘贴
		LFWPasteAction pasteAction = null;
		if(getText().equals(WEBPersConstants.CONTEXT_EN)){
			ClearRefAction clearAction = new ClearRefAction();
			manager.add(clearAction);
		}
		else if (getText().equals(WEBPersConstants.MENUS_MANAGER_CN)){
			LFWDirtoryTreeItem parentItem = (LFWDirtoryTreeItem) this.getParentItem();
			if (parentItem.getType().equals(LFWDirtoryTreeItem.NODE_FOLDER)) {
				NewMenubarAction newMenubarAction = new NewMenubarAction();
				newMenubarAction.setTreeView(view.getTreeView());
				newMenubarAction.setFromWidget(false);
				manager.add(newMenubarAction);
				
				pasteAction = new LFWPasteAction(WEBPersConstants.MENUBAR_CN);
				manager.add(pasteAction);
			}
		}
		//数据集
		else if (getText().equals(LFWTool.getWEBProjConstantValue("DATASET"))){
			NewDsAction newdsAction = new NewDsAction();
			manager.add(newdsAction);
			NewRefDsAction refdsAction = new NewRefDsAction();
			manager.add(refdsAction);
			EditDatasetsAction editdatasetsAction = new EditDatasetsAction();
			editdatasetsAction.setLfwDatasetsTreeItem(this);
			manager.add(editdatasetsAction);
			if(webEle != null && webEle instanceof Dataset){
				pasteAction = new LFWPasteAction(LFWTool.getWEBProjConstantValue("DATASET"));
				manager.add(pasteAction);
			}
		}
		//下拉数据集
		else if (getText().equals(LFWTool.getWEBProjConstantValue("COMBODATA"))){
			NewComboAction combodsAction = new NewComboAction();
			manager.add(combodsAction);
			if(webEle != null && webEle instanceof ComboData){
				pasteAction = new LFWPasteAction(LFWTool.getWEBProjConstantValue("COMBODATA"));
				manager.add(pasteAction);
			}
		}
		//参照
		else if (getText().equals(LFWTool.getWEBProjConstantValue("REFNODE"))){
			NewRefnodeAction refnodeAction = new NewRefnodeAction();
			manager.add(refnodeAction);
			NewRefNodeRelAction refNodeRelAction = new NewRefNodeRelAction();
			manager.add(refNodeRelAction);
			if(webEle != null && webEle instanceof IRefNode){
				pasteAction = new LFWPasteAction(LFWTool.getWEBProjConstantValue("REFNODE"));
				manager.add(pasteAction);
			}
		}
		//组件
		else if (getText().equals(LFWTool.getWEBProjConstantValue("COMPONENTS"))){
			NewGridAction newgridAction = new NewGridAction();
			manager.add(newgridAction);
			NewGridMenuAction newGridMenuAction = new NewGridMenuAction();
			manager.add(newGridMenuAction);
			newGridMenuAction.setTreeView(view.getTreeView());
			newGridMenuAction.setFromWidget(true);
			NewListViewAction newlistviewAction = new NewListViewAction();
			manager.add(newlistviewAction);
//			NewExcelAction newexcelAction = new NewExcelAction();
//			manager.add(newexcelAction);
			NewTreeAction newtreeAction = new NewTreeAction();
			manager.add(newtreeAction);
			NewFormAction newformAction = new NewFormAction();
			manager.add(newformAction);
			NewButtonAction newbuttonAction = new NewButtonAction();
			manager.add(newbuttonAction);
			NewTextCompAction newtextCompAction = new NewTextCompAction();
			manager.add(newtextCompAction);
			NewIFrameAction iframeAction = new NewIFrameAction();
			manager.add(iframeAction);
			NewLabelAction newLabelAction = new NewLabelAction();
			manager.add(newLabelAction);
			NewImageAction newImageAction  = new NewImageAction();
			manager.add(newImageAction);
//			NewToolBarAction toolbarAction = new NewToolBarAction();
//			manager.add(toolbarAction);
			NewLinkCompAction linkAction = new NewLinkCompAction();
			manager.add(linkAction);
//			NewProgressBarAction progressAction = new NewProgressBarAction();
//			manager.add(progressAction);
			NewSelfdefCompAction selfdefAction = new NewSelfdefCompAction();
			manager.add(selfdefAction);
			NewChartAction chartAction = new NewChartAction();
			manager.add(chartAction);
			NewContextMenuAction newContextction = new NewContextMenuAction();
			NewMenubarAction newMenubarAction = new NewMenubarAction();
			manager.add(newContextction);
			newMenubarAction.setTreeView(view.getTreeView());
			newMenubarAction.setFromWidget(true);
			manager.add(newMenubarAction);
			NewPaginationAction newPageAction = new NewPaginationAction();
			manager.add(newPageAction);
			
			if(webEle != null && webEle instanceof WebComponent){
				if(webEle instanceof GridComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_GRID);
				if(webEle instanceof ListViewComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_LISTVIEW);
//				else if(webEle instanceof ExcelComp)
//					pasteAction = new LFWPasteAction(WEBProjConstants.COMPONENT_EXCEL);
				else if(webEle instanceof TreeViewComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_TREE);
				else if(webEle instanceof FormComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_FORM);
				else if(webEle instanceof ButtonComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_BUTTON);
				
				else if(webEle instanceof TextComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_TEXTCOMP);
				else if(webEle instanceof IFrameComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_IFRAMECOMP);
				else if(webEle instanceof LabelComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_LABEL);
				
				else if(webEle instanceof ImageComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_IMAGE);
//				else if(webEle instanceof ToolBarComp)
//					pasteAction = new LFWPasteAction(WEBProjConstants.COMPONENT_TOOLBAR);
				else if(webEle instanceof LinkComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_LINKCOMP);
				else if(webEle instanceof ProgressBarComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_PROGRESSBAR);
				else if(webEle instanceof SelfDefComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_SELFDEFCOMP);
				else if(webEle instanceof ChartComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_CHART);
				else if(webEle instanceof MenubarComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_MENUBAR);
				else if(webEle instanceof ContextMenuComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_CONTEXTMENU);
				else if(webEle instanceof PaginationComp)
					pasteAction = new LFWPasteAction(WEBPersConstants.COMPONENT_PAGINATION);
				if(pasteAction != null){
					manager.add(pasteAction);
				}
			}
		}
	} 
}