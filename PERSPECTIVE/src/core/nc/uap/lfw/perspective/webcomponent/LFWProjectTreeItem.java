package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.perspective.action.CodePackPrefixAction;
import nc.uap.lfw.perspective.action.CreateByExcelAction;
import nc.uap.lfw.perspective.action.DocImportAction;
import nc.uap.lfw.perspective.action.LFWLaunchAction;
import nc.uap.lfw.perspective.action.NewBcpModuleAction;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWProjectModel;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;

public class LFWProjectTreeItem extends LFWBasicTreeItem implements ILFWTreeNode {
//	private static final String ProjectModelKey = "projectModel";
	private LFWProjectModel pm = null;
	private String moduleName = null;
	public LFWProjectTreeItem(Tree parent, LFWProjectModel pm) {
		super(parent, SWT.NONE);
		this.pm = pm;
		init();
	}
	
	protected void checkSubclass () {
	}
	private void init() {
//		File f = new File(pm.getJavaProject().getLocation().toFile(), pm.getProjectName());
//		if (!f.exists())
//			f.mkdirs();
		setText(pm.getProjectName()+" ["+getModuleName()+"]");
		setData(pm);
//		setData(ProjectModelKey, pm);
		setImage(getProjectImage());
	}

	private Image getProjectImage() {
//		ImageDescriptor imageDescriptor = MainPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "fmodel.png");
		return ImageProvider.fmodel;
	}

	public LFWProjectModel getProjectModel() {
		return (LFWProjectModel) getData();
	}

	public File getFile() {
		return pm.getMDRoot();
	}

	public void deleteNode() {
		MessageDialog.openInformation(null, "提示", "不能删除该" + WEBPersConstants.PAGEMETA_CN + "："+getText());
	}
	public String getIPathStr() {
		String IPath = getProjectModel().getProjectName()+"/METADATA";
		return IPath;
	}
	public String getModuleName(){
		if(moduleName == null){
			moduleName = LFWPersTool.getProjectModuleName(pm.getJavaProject());
		}
		return moduleName;
	}
	public void addMenuListener(IMenuManager manager) {
		LFWLaunchAction laucher = new LFWLaunchAction();
		manager.add(laucher);
//		DocImportAction importAction = new DocImportAction();
//		manager.add(importAction);
		NewBcpModuleAction bcpAction = new NewBcpModuleAction();
		manager.add(bcpAction);
		CodePackPrefixAction prefixAction = new CodePackPrefixAction();
		manager.add(prefixAction);
		
//		CreateByExcelAction createAction = new CreateByExcelAction();
//		manager.add(createAction);
		
	}
	public void expandTree(){
		Tree tree = LFWPersTool.getTree();
		tree.setSelection(this);
	}
	
	
}
