package nc.lfw.editor.common;

import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.perspective.listener.SubmitRuleDialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * 提交规则
 * @author dingrf
 *
 */
public class SubmitRulePropertyDescriptor extends AbstractDialogPropertyDescriptor {

	
	private LfwWindow pageMeta = null;
	
	private LfwView widget = null;
	
	private EventSubmitRule  submitRule = null; 
	
	private String plugId = null;
	
	/**
	 * @param id
	 * @param displayName
	 */
	public SubmitRulePropertyDescriptor(Object id, String displayName,String plugId , LfwWindow pageMeta, EventSubmitRule  submitRule) {
		super(id, displayName);
		this.pageMeta = pageMeta;
		this.submitRule = submitRule;
		this.plugId = plugId;
//		if (this.submitRule == null)
//			this.submitRule = new EventSubmitRule();
	}
	public SubmitRulePropertyDescriptor(Object id, String displayName, String plugId, LfwView widget, EventSubmitRule  submitRule) {
		super(id, displayName);
		this.widget = widget;
		this.submitRule = submitRule;
		this.plugId = plugId;
//		if (this.submitRule == null)
//			this.submitRule = new EventSubmitRule();
	}
	
	protected Object openDialogBox(Object obj, Control cellEditorWindow) {
		SubmitRuleDialog dialog = new SubmitRuleDialog(cellEditorWindow.getShell());
//		dialog.setPagemeta(pageMeta);
		dialog.setSubmitRule(submitRule);
		dialog.setWidget(widget);
		if(widget==null)
			return null;
		if (dialog.open() == Dialog.OK) {
//			eventConf.setSubmitRule(dialog.getMainContainer().getSubmitRule());
			submitRule = dialog.getMainContainer().getSubmitRule();
			if(widget.getPluginDesc(plugId)!=null){
				widget.getPluginDesc(plugId).setSubmitRule(submitRule);
			}
			else{
				widget.getPlugoutDesc(plugId).setSubmitRule(submitRule);
			}
			LFWBaseEditor.getActiveEditor().setDirtyTrue();
			return null;
		}
		return null;
	}
	
	
}
