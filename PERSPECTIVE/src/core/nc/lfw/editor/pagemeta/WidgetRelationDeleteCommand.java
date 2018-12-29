package nc.lfw.editor.pagemeta;

import nc.lfw.editor.common.Connection;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwWindow;

import org.eclipse.gef.commands.Command;

/**
 * 删除Widget连接关系
 * 
 * @author guoweic
 * 
 */
public class WidgetRelationDeleteCommand extends Command {
	private Connection conn = null;

	public WidgetRelationDeleteCommand(Connection relation) {
		super();
		this.conn = relation;
		setLabel("delete relation");
	}

	
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return super.canExecute();
	}

	
	public void execute() {
		redo();
	}

	
	public void redo() {
		conn.disConnect();
		delData();
	}

	
	public void undo() {
		conn.connect();
	}

	private void delData() {
		if (conn instanceof WidgetConnector) {
			PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
			LfwWindow pagemeta = editor.getGraph().getPagemeta();
			// 删除相关的connector
			Connector connector = ((WidgetConnector) conn).getConnector();
			pagemeta.getConnectorMap().remove(connector.getId());
			// 保存pagemeta
//			editor.savePagemeta(pagemeta);
			editor.setDirtyTrue();
		}
	}

}
