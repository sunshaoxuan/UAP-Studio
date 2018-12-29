/**
 * 
 */
package nc.uap.lfw.perspective.listener;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.uap.lfw.core.event.conf.EventConf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.part.EditorPart;

/**
 * @author chouhl
 *
 */
@SuppressWarnings("restriction")
public class EventEditorHandler{

	// 最外层编辑器
	private EditorPart parentEdtor;
	
	/**
	 * 创建并打开事件方法编辑tab页
	 * @param event
	 * @param listenerName
	 * @param tabFolder
	 * @param editor
	 * @return
	 */
	public EventEditorControlComp createEventEditorItem(EventConf eventConf, CTabFolder tabFolder, EditorPart editor) {
		parentEdtor = editor;
		String itemText = eventConf.getMethodName() + ":" + eventConf.getName();
		CTabItem[] items = tabFolder.getItems();
		for (int i = 0, n = items.length; i < n; i++) {
			if (items[i].getText().equals(itemText)) {
				tabFolder.setSelection(i);
				return openEventEditorControl(eventConf, tabFolder, editor);
			}
		}
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		EventEditorControlComp eventCtrl = openEventEditorControl(eventConf, tabFolder, editor);
		item.setText(itemText);
		item.setControl(eventCtrl);
		item.setShowClose(true);
		tabFolder.setSelection(item);
		/**
		 * Tab页关闭事件
		 */
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void close(CTabFolderEvent event) {
				CTabItem currentItem = (CTabItem) event.item;
				EventEditorControlComp ctrl = (EventEditorControlComp) currentItem.getControl();
				if (parentEdtor.isDirty()) {
					// 保存所有事件代码
					((LFWBaseEditor)parentEdtor).saveEventScript();
				}
				((LFWBaseEditor)parentEdtor).removeEventCtrl(ctrl);
//				// 删除临时文件
//				FileUtil.deleteEventFile(ctrl);
			}
		});
		/**
		 * Event代码改变事件
		 */
		eventCtrl.getJsText().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (parentEdtor instanceof LFWBaseEditor)
					((LFWBaseEditor)parentEdtor).setDirtyTrue();
			}
		});
		if (parentEdtor instanceof LFWBaseEditor) {
			((LFWBaseEditor) parentEdtor).addEventCtrl(eventCtrl);
		}
		return eventCtrl;
	}
	/**
	 * 打开JS编辑器
	 * @param event
	 * @param listenerName
	 * @param parent
	 * @param editor
	 * @return
	 */
	public EventEditorControlComp openEventEditorControl(EventConf event, Composite parent, EditorPart editor) {
//		EditorSite site = new EditorSite(((WorkbenchPage) editor.getEditorSite()
//				.getPage()).getEditorReferences()[0], editor,
//				(WorkbenchPage) editor.getEditorSite().getPage(),
//				((EditorSite) editor.getEditorSite()).getEditorDescriptor());
//		EditorSite site = new EditorSite((MPart)((WorkbenchPage) editor.getEditorSite()
//				.getPage()).getEditorReferences()[0].getPart(true), editor,
//				 editor.getEditorSite().getPage().getReference(editor),
//				EditorDescriptor.createForProgram(editor.getTitle()).getConfigurationElement());
		
//		EditorDescriptor.createForProgram(editor.getTitle()).getConfigurationElement();
		
		EventEditorControlComp eventCtrl = new EventEditorControlComp(parent, SWT.NONE, null, event);
		// 设置编辑时临时文件的input
//		((LFWBaseEditor)parentEdtor).setFileEditorInput(input);
		return eventCtrl;
	}
	/**
	 * 保存事件代码
	 * @param ctrl
	 */
	public void saveEventScript(EventEditorControlComp eventCtrl) {
		String script = eventCtrl.getJsText().getText();
		// 更新XML文件
		eventCtrl.getEventConf().setScript(script);
		if (parentEdtor instanceof LFWBaseEditor){
			// 更新Event事件列表
			((LFWBaseEditor)parentEdtor).getViewPage().getEventPropertiesView().showPropertiesView();
		}
	}
	
}
