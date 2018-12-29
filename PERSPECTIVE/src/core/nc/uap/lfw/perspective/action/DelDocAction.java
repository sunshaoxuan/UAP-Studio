package nc.uap.lfw.perspective.action;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWDocTreeItem;

public class DelDocAction extends NodeAction{
	public DelDocAction(){
		setText(WEBPersConstants.DELETE);
	}
	public void run() {
		LFWDocTreeItem node = (LFWDocTreeItem)LFWPersTool.getCurrentTreeItem();
		File docFile = node.getFile();
		FileUtilities.deleteFile(docFile);
		node.deleteNode();		
	}
}
