package nc.uap.lfw.editor.common.editor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Tree;

import nc.uap.lfw.editor.extNode.AddIntoDomainAction;
import nc.uap.lfw.editor.extNode.CreateDomainFileAction;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;

public class LFWDomainTreeItem extends LFWBasicTreeItem{

	public LFWDomainTreeItem(Tree parent, int style) {
		super(parent, style);
		setImage(ImageProvider.pageflow);
	}
	
	protected void checkSubclass () {
		
	}
	@Override
	public void addMenuListener(IMenuManager manager) {
		
		CreateDomainFileAction buildAction = new CreateDomainFileAction(this);
		manager.add(buildAction);
	}

}
