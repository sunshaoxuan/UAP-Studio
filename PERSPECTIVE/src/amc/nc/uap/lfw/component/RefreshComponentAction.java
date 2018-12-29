package nc.uap.lfw.component;

import org.eclipse.core.resources.IProject;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.design.itf.BusinessComponent;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeBuilder;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

public class RefreshComponentAction extends NodeAction{

	public RefreshComponentAction(){
		super(M_application.RefreshComponentAction_0);
	}
	public void run(){
		refresh();
	}
	public void refresh(){		
		LFWDirtoryTreeItem parentItem = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
		parentItem.removeAll();
		BusinessComponent bcp = parentItem.getBcp();
		IProject project = LFWPersTool.getCurrentProject();
		LFWExplorerTreeBuilder builder = LFWExplorerTreeBuilder.getInstance();
		if(parentItem.getType().equals(ILFWTreeNode.WINDOW_COMPONENT))
			builder.refreshCompNodeSubTree(parentItem, project, bcp);		
		else if(parentItem.getType().equals(ILFWTreeNode.PUBVIEW_COMPONENT))
			builder.refreshCompViewSubTree(parentItem, project, bcp);		
	}
	
}
