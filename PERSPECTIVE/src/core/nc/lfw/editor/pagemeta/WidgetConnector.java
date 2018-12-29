package nc.lfw.editor.pagemeta;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.uap.lfw.core.page.Connector;

/**
 * PagemetaµÄWidgetÁ¬½ÓÆ÷
 * @author guoweic
 *
 */
public class WidgetConnector extends Connection {

	private static final long serialVersionUID = 6219234075827649127L;
	 
	private Connector connector;

	public WidgetConnector(LFWBasicElementObj source, LFWBasicElementObj target) {
		super(source, target);
	}

	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

}
