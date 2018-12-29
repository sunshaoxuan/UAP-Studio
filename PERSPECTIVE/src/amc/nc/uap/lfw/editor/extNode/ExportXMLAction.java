package nc.uap.lfw.editor.extNode;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import uap.lfw.lang.M_extNode;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWWfmFlwTypeTreeItem;
import nc.uap.wfm.vo.WfmProdefVO;

public class ExportXMLAction extends NodeAction{

	public ExportXMLAction(){
		super(M_extNode.ExportXMLAction_0000/*µ¼³öXML*/);
	}
	public void run() {
		TreeItem item = LFWPersTool.getCurrentTreeItem();
		if(item instanceof LFWWfmFlwTypeTreeItem){
			LFWWfmFlwTypeTreeItem flwTypeItem = (LFWWfmFlwTypeTreeItem)item;
			String pk = flwTypeItem.getType_pk();
			WfmProdefVO[] vos = LFWWfmConnector.getProDef(pk);
			if(vos.length>0){
				WfmProdefVO vo = null;
				boolean flag = false;
				for(WfmProdefVO def:vos){
					if(def.getIsnotstartup().booleanValue()){
						vo = def;
						flag = true;
						break;
					}
				}
				if(!flag)
					vo = vos[0];
				String xml = vo.getProcessstr();
				Shell shell = new Shell(Display.getCurrent());

				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				String[] ext = {"*.xml"};
				dialog.setFilterExtensions(ext);
				dialog.setFileName("prodef.xml");

				String filePath = dialog.open();
				if (filePath != null){
					File f = new File(filePath);
					try {
						FileUtilities.saveFile(f, xml, "utf-8");
					} catch (IOException e) {
					
					}
				}
				
			}
		}
	}
}
