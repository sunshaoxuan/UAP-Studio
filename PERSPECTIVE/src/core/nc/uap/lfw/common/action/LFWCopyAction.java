package nc.uap.lfw.common.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.common.tools.LfwGlobalEditorInfo;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_lfw_core;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * ¿½±´×é¼þ
 * @author zhangxya
 *
 */
public class LFWCopyAction extends Action {
	
	public static String COPYKEY = "$copyMessage"; //$NON-NLS-1$
	
	private class CopyCommand extends Command{
		public CopyCommand(){
		}
		
		public void execute() {
			redo();
		}
		
		public void redo() {
		}
		
		public void undo() {
		}
		
	}

	public LFWCopyAction(String title) {
		super(M_lfw_core.LFWCopyAction_0 + title,  PaletteImage.getCreateDsImgDescriptor());
	}
	
	public void run() {
		String key = COPYKEY;
		LFWBasicTreeItem treeItem = (LFWBasicTreeItem) LFWPersTool.getCurrentTreeItem();
		WebElement webEle = (WebElement) treeItem.getData();
		LfwGlobalEditorInfo.addAttribute(key, webEle);
	}
}

