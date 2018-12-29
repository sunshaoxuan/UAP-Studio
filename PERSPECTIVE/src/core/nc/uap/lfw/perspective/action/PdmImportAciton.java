/**
 * 导入建库脚本
 */
package nc.uap.lfw.perspective.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDocTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMetadataTreeItem;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 导入建库脚本
 * @author guomq1
 * 2012-8-10
 */
public class PdmImportAciton extends Action{
	
	public PdmImportAciton(){
		super(M_perspective.PdmImportAciton_0);
	}
	public void run(){
		
	
     File file =openFile();
     if (file == null)
    	 return;
     try {
    	 LFWDirtoryTreeItem item = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
    	 String path = item.getFile().getPath();
//    	String  path = LFWAMCPersTool.getCurrentProject().getLocation().toOSString() + "/doc/design/";
    	saveFile(path,file);
	} catch (Exception e) {
		MainPlugin.getDefault().logError(e);
	}
	LFWPersTool.refreshCurrentPorject();
	}
	public static File openFile(){
		Shell shell = new Shell(Display.getCurrent());
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
	//	dialog.setFilterNames(new String[]{"excel 2007文件(.xlsx)"});
		dialog.setFilterNames(new String[]{"*.pdm"}); //$NON-NLS-1$
		
		
		dialog.setFilterExtensions(new String[]{"*.pdm"}); //$NON-NLS-1$
		String filterpath = dialog.getFilterPath();
		File dir = new File(filterpath);
		if(dir.isDirectory()){
		File[] files = dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".pdm"); //$NON-NLS-1$
			}
		}); }
		String filePath = dialog.open();
		
		if (filePath != null){
			File f = new File(filePath);
			return f;
		}
		return null;
	}
	public static void saveFile(String directory,File excelFile)throws Exception{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try{
			fis = new FileInputStream(excelFile);
			File parent = new File(directory);
			if (!parent.exists()){
				parent.mkdirs();
	        }
			if(parent.isDirectory()){
				File[] files = parent.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						// TODO Auto-generated method stub
						return name.endsWith(".pdm"); //$NON-NLS-1$
					}
				});
				File existFile = new File(parent,excelFile.getName());
				boolean flag = false;
				if(!existFile.exists()){					
                       flag = true;
				}else{
					MessageDialog md = new MessageDialog(null, M_perspective.PdmImportAciton_1, null, M_perspective.PdmImportAciton_2, 6, new String[]{M_perspective.PdmImportAciton_3,M_perspective.PdmImportAciton_4}, 0);
					md.open();
					if(md.getReturnCode() == md.OK){
						md.close();
	                for(File file :files){
	                	file.delete();
	                	flag = true;
	                }}else{
	                	md.close();
	                	flag = false;
	                }
				}
				
                if(flag == true){
				
                File f = new File(directory + "/"+excelFile.getName()); //$NON-NLS-1$
                fos = new FileOutputStream(f);
                
                byte[] buffer = new byte[10240];
        		int bytesRead = 0;
        		while ((bytesRead = fis.read(buffer)) > 0)
        		{
        			fos.write(buffer, 0, bytesRead);
        		}
        		fos.flush();
        		fos.close();
        		IProject project = LFWPersTool.getCurrentProject();
        		TreeItem selectItem = LFWPersTool.getCurrentTreeItem();
        		LFWMetadataTreeItem servdata = new LFWMetadataTreeItem(
        				selectItem, f, f.getName());
				IFile file = project.getFile(f.getPath().substring(
						project.getLocation().toString().length()));
				servdata.setMdFile(file);
		    	selectItem.setExpanded(true);
		    	project.refreshLocal(2, null);
                }
			}
	    }
	    catch (Exception e)
	    {
	    	MainPlugin.getDefault().logError(e);
	    }
		finally{
			try{
				if(fis != null)
					fis.close();	
				}
			catch(Exception e){
				MainPlugin.getDefault().logError(e);
			}
			try{
				if(fos != null)
					fos.close();
				}
			catch(Exception e){
				MainPlugin.getDefault().logError(e);
			}
		}

	}
}
