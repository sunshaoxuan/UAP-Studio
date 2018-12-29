package nc.uap.lfw.perspective.listener;

import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.lang.M_listenr;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.model.DatasetElementObj;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * JsListener的Event列表的参数列编辑器
 * 
 * @author guoweic
 *
 */
public class ExtendParameterCellEidtor extends DialogCellEditor {

	private Composite composite;
	
	private TableViewer tv;
	
	public ExtendParameterCellEidtor(Composite parent, TableViewer tv) {
		this(parent, SWT.NONE);
		this.tv = tv;
	}

	public ExtendParameterCellEidtor(Composite parent, int style) {
		super(parent, style);
		doSetValue(""); //$NON-NLS-1$
	}

	protected Control createContents(Composite cell) {
		composite = new Composite(cell, getStyle());
		return composite;
	}

	protected Object openDialogBox(Control cellEditorWindow) {
//		if (jsListener instanceof DatasetListener) {
			IStructuredSelection selection = (IStructuredSelection) tv.getSelection();
			EventConf jsEventObj = (EventConf) selection.getFirstElement();
//			
			if (DatasetEvent.ON_BEFORE_DATA_CHANGE.equals(jsEventObj.getName())
					|| DatasetEvent.ON_AFTER_DATA_CHANGE.equals(jsEventObj.getName())) {  // 数据改变前后事件
			
				LfwParameter param = jsEventObj.getExtendParam(EventConf.PARAM_DATASET_FIELD_ID);
				
				if (param == null) {
					param = new LfwParameter();
					param.setName(EventConf.PARAM_DATASET_FIELD_ID);
					jsEventObj.addExtendParam(param);
				}
					
				DataChangeParamDialog dialog = new DataChangeParamDialog(cellEditorWindow.getShell());
				
				DatasetElementObj dsobj = (DatasetElementObj) DataSetEditor.getActiveEditor().getGraph().getCells().get(0);
				
				Dataset ds = dsobj.getDs();
				dialog.setDataset(ds);
				dialog.setExtendParameter(param);
				
				if (dialog.open() == Dialog.OK) {
					param.setDesc(dialog.getMainContainer().getExtendParameter().getDesc());
					
//					LFWBaseEditor.getActiveEditor().saveListener(jsListener.getId(), jsEventObj, jsListener);
//					String[] prop = new String[1];
//					prop[0] = "附加参数";
//					tv.update(jsEventObj, prop);
					tv.refresh(jsEventObj);
//					tv.refresh();
					LFWBaseEditor.getActiveEditor().setDirtyTrue();
					
					return null;
				}
				
				
				
			}
			else{
				MessageDialog.openInformation(null, M_listenr.ExtendParameterCellEidtor_0, M_listenr.ExtendParameterCellEidtor_1+DatasetEvent.ON_BEFORE_DATA_CHANGE+M_listenr.ExtendParameterCellEidtor_2+DatasetEvent.ON_AFTER_DATA_CHANGE+M_listenr.ExtendParameterCellEidtor_3);
			}
//		}
		return null;
	}

}
