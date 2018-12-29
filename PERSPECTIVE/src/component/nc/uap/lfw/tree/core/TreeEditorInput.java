package nc.uap.lfw.tree.core;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_tree;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * Ê÷±à¼­Æ÷input
 * @author zhangxya
 *
 */
public class TreeEditorInput  extends ElementEditorInput{

	public TreeEditorInput(TreeViewComp treeViewComp, LfwView widget, LfwWindow pagemeta){
		super(treeViewComp, widget, pagemeta);
	}
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_tree.TreeEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_tree.TreeEditorInput_0;
	}
}
