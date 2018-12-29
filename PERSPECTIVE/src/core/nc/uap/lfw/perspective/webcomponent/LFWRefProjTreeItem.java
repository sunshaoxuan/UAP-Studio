package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;


public class LFWRefProjTreeItem extends LFWDirtoryTreeItem {

	
	public LFWRefProjTreeItem(Tree parent, File file) {
		super(parent,file);
		setImage(ImageProvider.refforder);
		
	}	
	protected void checkSubclass () {
		
	}
	

}
