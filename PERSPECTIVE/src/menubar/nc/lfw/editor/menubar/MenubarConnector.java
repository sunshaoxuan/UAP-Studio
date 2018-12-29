package nc.lfw.editor.menubar;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;

/**
 * MenuÁ¬½ÓÆ÷
 * @author guoweic
 *
 */
public class MenubarConnector extends Connection {
	
	private static final long serialVersionUID = 1L;
	public static final String ID_SUFFIX = "_PARENT_RELATION";

	public MenubarConnector(LFWBasicElementObj source, LFWBasicElementObj target) {
		super(source, target);
	}

}
