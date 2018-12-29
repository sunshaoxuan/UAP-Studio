package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LFWUtility;
import nc.uap.lfw.common.action.LFWCopyAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.FieldRelations;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.RefMdDataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.action.DeleteDsAction;
import nc.uap.lfw.perspective.action.RefreshDsAction;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

/**
 *Ds 
 * @author zhangxya
 *
 */
@SuppressWarnings("restriction")
public class LFWDSTreeItem  extends LFWBasicTreeItem implements ILFWTreeNode{
	
	private Dataset ds;
	public Dataset getDs() {
		return ds;
	}
	public void setDs(Dataset ds) {
		this.ds = ds;
	}
	public LFWDSTreeItem(TreeItem parentItem, Dataset ds, String name) {
		super(parentItem, SWT.NONE);
		this.ds = ds;
		setData(ds);
		if(ds.getCaption() != null)
			setText(name + "[" + ds.getCaption() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		else
			setText(name);
		setImage(getFileImage());
	}
	protected void checkSubclass () {
	}
	private Image getFileImage() {
//		ImageDescriptor imageDescriptor = null;
		Image currentImage = null;
		if(ds instanceof IRefDataset)
			currentImage = ImageProvider.refds;
		else
			currentImage = ImageProvider.ds;
		return currentImage;
	}
	private IFile ifile = null;
	
	public IFile getIFile(){
		if(ifile == null){
			IPath path = new Path(getIPathStr());
			ifile = IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFile(path);

		}
		return ifile;
	}


		
	public File getFile() {
		return (File) getData();
	}

	public void deleteNode(LFWDSTreeItem dsTreeItem) {
		LfwView lfwwidget = LFWPersTool.getCurrentWidget();
		if(lfwwidget != null){
			Dataset ds = (Dataset)dsTreeItem.getData();
			DatasetRelations dsRelations = lfwwidget.getViewModels().getDsrelations();
			if(dsRelations != null){
				DatasetRelation[] dsRelationList = dsRelations.getDsRelations();
				if(dsRelationList != null && dsRelationList.length > 0){
					for (int i = 0; i < dsRelationList.length; i++) {
						DatasetRelation dsRelation = dsRelationList[i];
						if((dsRelation.getMasterDataset() != null && dsRelation.getMasterDataset().equals(ds.getId()))
							|| (dsRelation.getDetailDataset() != null && dsRelation.getDetailDataset().equals(ds.getId()))){
							MessageDialog.openError(null, M_perspective.LFWDSTreeItem_0, M_perspective.LFWDSTreeItem_1 + dsRelation.getId() + "!"); //$NON-NLS-2$ //$NON-NLS-3$
							return;
						}
					}
				}
			}
			
			WebComponent[] webComponents = lfwwidget.getViewComponents().getComponents();
			for (int i = 0; i < webComponents.length; i++) {
				WebComponent component = webComponents[i];
				if(component instanceof FormComp){
					FormComp form = (FormComp) component;
					if(form.getDataset() != null && form.getDataset().equals(ds.getId())){
						MessageDialog.openError(null, M_perspective.LFWDSTreeItem_0, M_perspective.LFWDSTreeItem_2 + form.getId() + "!"); //$NON-NLS-2$ //$NON-NLS-3$
						return;
					}
				}
				else if(component instanceof GridComp){
					GridComp grid = (GridComp) component;
					if(grid.getDataset() != null && grid.getDataset().equals(ds.getId())){
						MessageDialog.openError(null, M_perspective.LFWDSTreeItem_0, M_perspective.LFWDSTreeItem_3 + grid.getId() + "!"); //$NON-NLS-2$ //$NON-NLS-3$
						return;
					}
				}
				else if(component instanceof TreeViewComp){
					TreeViewComp tree = (TreeViewComp) component;
					if(tree.getTopLevel() != null){
						TreeLevel topLevel = tree.getTopLevel();
						if(topLevel.getDataset() != null && topLevel.getDataset().equals(ds.getId())){
							MessageDialog.openError(null, M_perspective.LFWDSTreeItem_0, M_perspective.LFWDSTreeItem_4 + tree.getId() + ",TreeLevel" + //$NON-NLS-2$ //$NON-NLS-3$
									"为" + topLevel.getId() +  "!"); //$NON-NLS-1$ //$NON-NLS-2$
							return;
						}
						TreeLevel childLevel = topLevel.getChildTreeLevel();
						while(childLevel != null){
							if(childLevel.getDataset() != null && childLevel.getDataset().equals(ds.getId())){
								MessageDialog.openError(null, "提示", "此数据集和树进行了绑定,不能删除, 树的ID为" + tree.getId() + ",TreeLevel" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
										"为" + childLevel.getId() +  "!"); //$NON-NLS-1$ //$NON-NLS-2$
								return;
							}
							childLevel = childLevel.getChildTreeLevel();
						}
					}
				}
			}
			
			ViewModels viewModel = lfwwidget.getViewModels();
			//删除ds关联的数据库中引用数据库
			Dataset[] datasets = viewModel.getDatasets();
			if(datasets != null && datasets.length > 0){
				for (int i = 0; i < datasets.length; i++) {
					if((datasets[i] instanceof MdDataset)&&!(datasets[i] instanceof RefMdDataset)){
						MdDataset mdata = (MdDataset)datasets[i];
						FieldRelation[] fRelations = mdata.getFieldRelations().getFieldRelations();
						if(fRelations!=null&&fRelations.length>0){
							for (int j = 0; j < fRelations.length; j++) {
								if(ds.getId().equals(fRelations[j].getRefDataset())){
//									mdata.getFieldRelations().removeFieldRelation(fRelations[j].getId());
									MessageDialog.openError(null, M_perspective.LFWDSTreeItem_0, M_perspective.LFWDSTreeItem_5 + mdata.getId() + "!"); //$NON-NLS-2$ //$NON-NLS-3$
									return;
								}
										
							}
						}
					}
				}
			}
			Dataset dsdata = viewModel.getDataset(ds.getId());
			if(dsdata == null)
				return;
			if(dsdata != null)
				viewModel.removeDataset(ds.getId());
			//删除ds关联的combo
			ComboData[] combodatas = viewModel.getComboDatas();
			if(combodatas != null && combodatas.length > 0){
				for (int i = 0; i < combodatas.length; i++) {
					ComboData comdata = combodatas[i];
					if(comdata.getId().startsWith("comboComp_" + ds.getId())) //$NON-NLS-1$
						viewModel.removeComboData(comdata.getId());
				}
			}
			//删除ds关联的refnode
			IRefNode[] refNodes = viewModel.getRefNodes();
			if(refNodes != null && refNodes.length > 0){
				for (int i = 0; i < refNodes.length; i++) {
					IRefNode refNode = refNodes[i];
					if(refNode.getId().startsWith("refNode_" + ds.getId())) //$NON-NLS-1$
						viewModel.removeRefNode(refNode.getId());
				}
			}
			
			LFWPersTool.saveWidget(lfwwidget);
			
			DataSetEditor dsEditor = DataSetEditor.getActiveEditor();
			if(dsEditor != null){
				dsEditor.RemoveOriginalCombdatas(ds.getId());
				dsEditor.RemoveOriginalRefNodes(ds.getId());
			}
			else{
				LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(dsTreeItem);
				RemoveOriginalCombdatas(widgetTreeItem, ds.getId());
				RemoveOriginalRefNodes(widgetTreeItem, ds.getId());
				
			}
			//dsEditor.dispose();
		}
//		else{
//			String projectPath = LFWPersTool.getProjectPath();
//			TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
//			String directory = treeItem.getText();
//			Dataset ds = (Dataset)dsTreeItem.getData();
//			TreeItem parent = (TreeItem)treeItem.getParentItem();
//			while (parent != null && parent instanceof LFWDirtoryTreeItem) {
//				if (parent.getText().equals("公共数据集"))
//					break;
//				directory = ((LFWDirtoryTreeItem)parent).getFile().getName() + "/" + directory;
//				//treeItem = parent;
//				parent = (TreeItem)parent.getParentItem();
//			}
//			String filePath = projectPath + "/web/pagemeta/public/dspool";
//			if(directory.lastIndexOf("/") != -1){
//				String folder = directory.substring(0, directory.lastIndexOf("/"));
//				filePath = projectPath + "/web/pagemeta/public/dspool/" + folder;
//			}
//			String fileName = treeItem.getText() + ".xml";
//			File file = new File(filePath + "/" + fileName);
//			FileUtilities.deleteFile(file);
//			LFWPersTool.getCurrentProject().getLocation();
//			//String rootPath = LFWPersTool.getProjectModuleName(project);
//			String rootPath = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
//			LFWConnector.removeDsFromPool("/" + rootPath, ds);
//		}
		dispose();
	}
	
	
	/**
	 *  删除原来的combodata
	 * @param id
	 */
	public void RemoveOriginalCombdatas(LFWWidgetTreeItem widgetTreeItem, String id){
		LFWSeparateTreeItem lfwSeparaTreeItem = null;
		TreeItem[] separasTreeItems = widgetTreeItem.getItems();
		for (int i = 0; i < separasTreeItems.length; i++) {
			TreeItem item = separasTreeItems[i];
			if(item instanceof LFWSeparateTreeItem){
				LFWSeparateTreeItem seitem = (LFWSeparateTreeItem) item;
				if(seitem.getText().equals(WEBPersConstants.COMBODATA)){
					lfwSeparaTreeItem = seitem;
					break;
				}
				
			}
		}
		if(lfwSeparaTreeItem != null){
			TreeItem[] treeItems = lfwSeparaTreeItem.getItems();
			for (int i = 0; i < treeItems.length; i++) {
				ComboData combo = (ComboData) treeItems[i].getData();
				if(combo.getId().startsWith("combo_" + id)) //$NON-NLS-1$
					treeItems[i].dispose();
			}
		}
	}
	
	/**
	 * 删除原来的refnodes
	 * @param id
	 */
	public void RemoveOriginalRefNodes(LFWWidgetTreeItem widgetTreeItem, String id){
		LFWSeparateTreeItem lfwSeparaTreeItem = null;
		TreeItem[] separasTreeItems = widgetTreeItem.getItems();
		for (int i = 0; i < separasTreeItems.length; i++) {
			TreeItem item = separasTreeItems[i];
			if(item instanceof LFWSeparateTreeItem){
				LFWSeparateTreeItem seitem = (LFWSeparateTreeItem) item;
				if(seitem.getText().equals(WEBPersConstants.REFNODE)){
					lfwSeparaTreeItem = seitem;
					break;
				}
				
			}
		}
		if(lfwSeparaTreeItem != null){
			TreeItem[] treeItems = lfwSeparaTreeItem.getItems();
			for (int i = 0; i < treeItems.length; i++) {
				if(treeItems[i].getData() instanceof IRefNode){
					IRefNode refnode = (IRefNode) treeItems[i].getData();
					if(refnode.getId().startsWith("refnode_" + id)) //$NON-NLS-1$
						treeItems[i].dispose();
				}
			}
		}
	}
	
	
	
	public String getIPathStr() {
		String parentIPath = ""; //$NON-NLS-1$
		TreeItem parent = getParentItem();
		if(parent instanceof ILFWTreeNode){
			parentIPath = ((ILFWTreeNode)parent).getIPathStr();
		}
		return parentIPath+"/"+getText(); //$NON-NLS-1$
		
	}
	public void deleteNode() {
		
	}
	
	public void addMenuListener(IMenuManager manager){
		Dataset dataset = (Dataset)getData();
		//拷贝ds命令
		LFWCopyAction coypAction = new LFWCopyAction(WEBPersConstants.DATASET);
		manager.add(coypAction);
		if(dataset.getFrom() != null)
			return;
		//删除ds命令
		DeleteDsAction deleteDsAction = new DeleteDsAction();
		manager.add(deleteDsAction);
		
		//更新ds命令
//		RefreshDsAction refreshDsAction = new RefreshDsAction();
//		manager.add(refreshDsAction);
	}
	
	public void mouseDoubleClick(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);		
		TreeItem parentDs = getParentItem();
		if(parentDs instanceof LFWSeparateTreeItem)
			view.openDsEdit(this);
		else 
			view.openPoolDsEdit(this);		
	}
}

