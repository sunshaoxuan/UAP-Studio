package nc.lfw.editor.widget;

import nc.uap.lfw.core.page.LfwView;

import org.eclipse.draw2d.Label;
import org.eclipse.jface.action.Action;

/**
 * ±à¼­×ÓÏî
 * @author guoweic
 *
 */
public class EditWidgetItemAction extends Action {
	
	private WidgetElementObj widgetObj;
	private Label label;
	private String itemId;
	
	public EditWidgetItemAction(Label label, WidgetElementObj widgetObj, String itemId) {
		setText("±à¼­ " + itemId);
		setToolTipText("±à¼­");
		this.widgetObj = widgetObj;
		this.label = label;
		this.itemId = itemId;
	}
	
	
	public void run() {
		editItem();
	}
	
	private void editItem() {
		LfwView widget = widgetObj.getWidget();
//		if (label instanceof SignalLabel) {
//			
//			
//			// ±£´æwidget
////			widget.getSignalMap().remove(((LfwSignal) ((SignalLabel) label).getEditableObj()).getId());
////			PagemetaEditor.getActivePagemetaEditor().saveWidget(widget);
//			WidgetEditor.getActiveWidgetEditor().getGraph().setWidget(widget);
//		} else if (label instanceof SlotLabel) {
//			SlotEditorHandler editorHandler = new SlotEditorHandler();
//			editorHandler.createSlotEditorItem(label, widgetObj, itemId);
//			
//			// ±£´æwidget
////			widget.getSlotMap().remove(((LfwSlot) ((SlotLabel) label).getEditableObj()).getId());
////			PagemetaEditor.getActivePagemetaEditor().saveWidget(widget);
//			WidgetEditor.getActiveWidgetEditor().getGraph().setWidget(widget);
//		}
		WidgetEditor.getActiveWidgetEditor().setDirtyTrue();
	}

}
