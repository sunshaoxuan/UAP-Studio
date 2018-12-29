package nc.uap.lfw.component;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateComponentAction extends NodeAction{

	public CreateComponentAction(){
		super(M_application.CreateComponentAction_0);
	}
	public void run(){
		createComponent();
	}
	public void createComponent(){
		IProject proj = LFWPersTool.getCurrentProject();
		boolean prefixCheck = CodeRuleChecker.checkForProject(proj);
		if(!prefixCheck)
			return;
		CreateComponentDialog dialog = new CreateComponentDialog(null, M_application.CreateComponentAction_0);
		if(dialog.open()==IDialogConstants.OK_ID){
			String id = dialog.getId();
			String name = dialog.getName();
			String pack = dialog.getPackName();
			LfwUIComponent component = new LfwUIComponent();
			component.setId(id);
			component.setPack(pack);
			component.setName(name);			
			String displayName = ("".equals(pack))?id:pack+"."+id; //$NON-NLS-1$ //$NON-NLS-2$
			displayName = name+"["+displayName+"]"; //$NON-NLS-1$ //$NON-NLS-2$
			LFWDirtoryTreeItem parentItem = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
//			LFWComponentTreeItem componentTreeItem = new LFWComponentTreeItem(parentItem, parentItem.getFile(), component, displayName);
//			componentTreeItem.setBcp(parentItem.getBcp());			
			String packPath = ""; //$NON-NLS-1$
			if(pack.length()==0){
				packPath = "/"+id; //$NON-NLS-1$
			}
			else
				packPath = "/"+pack.replace(".", "/")+"/"+id; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			
			if(parentItem.getType().equals(ILFWTreeNode.WINDOW_COMPONENT)){
				
				File windowFile = new File(parentItem.getFile(), packPath);
				
				LFWComponentTreeItem componentTreeItem = new LFWComponentTreeItem(parentItem, windowFile, component, displayName);
				componentTreeItem.setBcp(parentItem.getBcp());	
				componentTreeItem.setType(ILFWTreeNode.WINDOW);
				
//				LFWDirtoryTreeItem windowNode = new LFWDirtoryTreeItem(componentTreeItem,windowFile, WEBPersConstants.WINDOW);
//				windowNode.setType(LFWDirtoryTreeItem.WINDOW_FOLDER);
//				windowNode.setBcp(parentItem.getBcp());
				toXml(windowFile.getPath(), component);
			}
			else if(parentItem.getType().equals(ILFWTreeNode.PUBVIEW_COMPONENT)){
				
				File pubviewFile = new File(parentItem.getFile(),packPath);
				
				LFWComponentTreeItem componentTreeItem = new LFWComponentTreeItem(parentItem, pubviewFile, component, displayName);
				componentTreeItem.setBcp(parentItem.getBcp());	
				componentTreeItem.setType(ILFWTreeNode.PUBLIC_VIEW);
//				LFWDirtoryTreeItem pubviewNode = new LFWDirtoryTreeItem(componentTreeItem, pubviewFile,WEBPersConstants.PUBLIC_VIEW);
//				pubviewNode.setType(LFWDirtoryTreeItem.PUBLIC_VIEW_FOLDER);
//				pubviewNode.setBcp(parentItem.getBcp());
				toXml(pubviewFile.getPath(), component);
			}
			LFWPersTool.refreshCurrentPorject();
		}
	}
	public void toXml(String path, LfwUIComponent component){
		try {
			Document doc = XmlCommonTool.createDocument();
			Element rootNode = doc.createElement("Component"); //$NON-NLS-1$
			doc.appendChild(rootNode);
			rootNode.setAttribute("id", component.getId()); //$NON-NLS-1$
			rootNode.setAttribute("name", component.getName()); //$NON-NLS-1$
			rootNode.setAttribute("pack", component.getPack()); //$NON-NLS-1$
			
			File folder = new File(path);
			File file = new File(folder,"component.cp"); //$NON-NLS-1$
			if(!folder.exists()){
				folder.mkdirs();
			}
	    	XmlCommonTool.documentToXml(doc, file);
	    	
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}
		
	}
}
