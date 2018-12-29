/**
 * 新建初始化脚本
 */
package nc.uap.lfw.perspective.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.webcomponent.LFWBusinessCompnentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDocTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMetadataTreeItem;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新建初始化脚本
 * @author guomq1
 * 2012-8-13
 */
public class InitScriptImportAciton extends Action {
	public InitScriptImportAciton(){
		super(M_perspective.InitScriptImportAciton_0);
	}
	public void run(){
		
			IProject project = LFWPersTool.getCurrentProject();
//			String  path = LFWAMCPersTool.getProjectPath()+"/build/script/"; //$NON-NLS-1$
			LFWDirtoryTreeItem item = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
	    	String path = item.getFile().getPath();
//			LFWBusinessCompnentTreeItem busiCompnent = LFWPersTool.getCurrentBusiCompTreeItem();
//			path += "/" + busiCompnent.getText().substring(busiCompnent.getText().indexOf(WEBProjConstants.BUSINESSCOMPONENT) + 
//					WEBProjConstants.BUSINESSCOMPONENT.length() + 1, busiCompnent.getText().length() -1 )+ "/build/script/";
			
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			boolean flag = true;
			File f = new File(path + "/items.xml"); //$NON-NLS-1$
			TreeItem selectItem = LFWPersTool.getCurrentTreeItem();
			if(f.exists()){
				MessageDialog md = new MessageDialog(null, M_perspective.InitScriptImportAciton_1, null, M_perspective.InitScriptImportAciton_2, 6, new String[]{M_perspective.InitScriptImportAciton_3,M_perspective.InitScriptImportAciton_4}, 0);
				md.open();
				if(md.getReturnCode() == md.OK){
					md.close();
	            	f.delete();
	            	flag = true;
	            	((LFWDirtoryTreeItem)selectItem.getItem(0)).deleteNode();
	            }else{
	            	md.close();
	            	flag = false;
	            }								
			}
//			BufferedWriter bw = null;
			if(flag == true){
				InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/buildScript/items.xml");
			try {
//				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true)));
				
				String content = FileUtilities.fetchFileContent(ins, "UTF-8");
				FileUtilities.saveFile(f, content, "UTF-8");
//				
//			} catch (Exception e) {
//				MainPlugin.getDefault().logError(e.getMessage());
//			}
//			try {
//				bw.write("<?xml version=\"1.0\" encoding='gb2312'?>\n"); //$NON-NLS-1$
//				bw.write("<items docType=\"SDP_SCRIPT_ITEM\">\n"); //$NON-NLS-1$
//				bw.write("</items>"); //$NON-NLS-1$
//				bw.close();
				
				
        		LFWMetadataTreeItem servdata = new LFWMetadataTreeItem(
        				selectItem, f, f.getName());
				IFile ifile = project.getFile(f.getPath().substring(
						project.getLocation().toString().length()));
				servdata.setMdFile(ifile);
		    	selectItem.setExpanded(true);
		    	project.refreshLocal(2, null);
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage());
			}finally{
				if(ins != null)
					try {
						ins.close();
					} 
					catch (IOException e) {
						MainPlugin.getDefault().logError(e);
					}
			}
			
		}
		
	}

}
