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

	// �����༭��
	private EditorPart parentEdtor;
	
	/**
	 * ���������¼������༭tabҳ
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
		 * Tabҳ�ر��¼�
		 */
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void close(CTabFolderEvent event) {
				CTabItem currentItem = (CTabItem) event.item;
				EventEditorControlComp ctrl = (EventEditorControlComp) currentItem.getControl();
				if (parentEdtor.isDirty()) {
					// ���������¼�����
					((LFWBaseEditor)parentEdtor).saveEventScript();
				}
				((LFWBaseEditor)parentEdtor).removeEventCtrl(ctrl);
//				// ɾ����ʱ�ļ�
//				FileUtil.deleteEventFile(ctrl);
			}
		});
		/**
		 * Event����ı��¼�
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
	 * ��JS�༭��
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
		// ���ñ༭ʱ��ʱ�ļ���input
//		((LFWBaseEditor)parentEdtor).setFileEditorInput(input);
		return eventCtrl;
	}
	/**
	 * �����¼�����
	 * @param ctrl
	 */
	public void saveEventScript(EventEditorControlComp eventCtrl) {
		String script = eventCtrl.getJsText().getText();
		// ����XML�ļ�
		eventCtrl.getEventConf().setScript(script);
		if (parentEdtor instanceof LFWBaseEditor){
			// ����Event�¼��б�
			((LFWBaseEditor)parentEdtor).getViewPage().getEventPropertiesView().showPropertiesView();
		}
	}
	
}