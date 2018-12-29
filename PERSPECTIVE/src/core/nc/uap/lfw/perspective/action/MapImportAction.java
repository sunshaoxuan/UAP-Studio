/**
 * 导入目录映射module.map
 */
package nc.uap.lfw.perspective.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
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
 * 导入目录映射module.map
 * @author guomq1
 * 2012-8-13
 */
public class MapImportAction extends Action {
	public MapImportAction(){
		super(M_perspective.MapImportAction_0);
	}
	public void run(){
		IProject project = LFWPersTool.getCurrentProject();
		LFWDirtoryTreeItem item = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
    	String path = item.getFile().getPath();
		String module =LFWPersTool.getProjectModuleName(project);
//		LFWBusinessCompnentTreeItem busiCompnent = LFWPersTool.getCurrentBusiCompTreeItem();
//		path += "/" + busiCompnent.getText().substring(busiCompnent.getText().indexOf(WEBProjConstants.BUSINESSCOMPONENT) + 
//				WEBProjConstants.BUSINESSCOMPONENT.length() + 1, busiCompnent.getText().length() -1 )+ "/build/";
		
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		boolean flag = true;
		File f = new File(path + "/module.map"); //$NON-NLS-1$
		if(f.exists()){
			MessageDialog md = new MessageDialog(null, M_perspective.MapImportAction_1, null, M_perspective.MapImportAction_2, 6, new String[]{M_perspective.MapImportAction_3,M_perspective.MapImportAction_4}, 0);
			md.open();
			if(md.getReturnCode() == md.OK){
				md.close();
            	f.delete();
            	flag = true;
            }else{
            	md.close();
            	flag = false;
            }
		}
		BufferedWriter bw = null;
		if(flag == true){
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true)));
			
		} catch (FileNotFoundException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		try {
			bw.write("#Module Name\n"); //$NON-NLS-1$
			
			bw.write("module_name=" + module + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			bw.write("#<DIRMAP>[Script category-export directory mapping, such as :'pub_alerttype=alerttype', 'pub_alerttype' is master table name, 'alerttype' is export directory]"); //$NON-NLS-1$
			bw.close();
			TreeItem selectItem = LFWPersTool.getCurrentTreeItem();
    		LFWMetadataTreeItem servdata = new LFWMetadataTreeItem(
    				selectItem, f, f.getName());
			IFile ifile = project.getFile(f.getPath().substring(
					project.getLocation().toString().length()));
			servdata.setMdFile(ifile);
	    	selectItem.setExpanded(true);
	    	project.refreshLocal(2, null);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		}
		if(bw != null)
			try {
				bw.close();
			} catch (IOException e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
	}
}
