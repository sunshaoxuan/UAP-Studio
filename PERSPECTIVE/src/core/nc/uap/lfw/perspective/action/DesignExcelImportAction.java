package nc.uap.lfw.perspective.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.webcomponent.LFWDocTreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;


/**
 * 工程下导入excel 2007设计文件
 * @author guomq1
 * 2012-8-9
 */


public class DesignExcelImportAction extends NodeAction{
	
	public DesignExcelImportAction(){
		super(M_perspective.DesignExcelImportAction_0);
	}
	public void run(){
     File file =openFile();
     if (file == null)
    	 return;
     try {
    	String  path = LFWAMCPersTool.getCurrentProject().getLocation().toOSString() + "/doc/design/"; //$NON-NLS-1$
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
		dialog.setFilterNames(new String[]{"*.xlsx"}); //$NON-NLS-1$
		
		
		dialog.setFilterExtensions(new String[]{"*.xlsx"}); //$NON-NLS-1$
		String filterpath = dialog.getFilterPath();
		File dir = new File(filterpath);
		if(dir.isDirectory()){
		File[] files = dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".xlsx"); //$NON-NLS-1$
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
						return name.endsWith(".xlsx"); //$NON-NLS-1$
					}
				});
				
				boolean flag = false;
				if(files.length == 0){
					
                       flag = true;
				}else{
					MessageDialog md = new MessageDialog(null, M_perspective.DesignExcelImportAction_1, null, M_perspective.DesignExcelImportAction_2, 6, new String[]{M_perspective.DesignExcelImportAction_3,M_perspective.DesignExcelImportAction_4}, 0);
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
				
                File f = new File(directory + excelFile.getName());
                fos = new FileOutputStream(f);
                
                byte[] buffer = new byte[10240];
        		int bytesRead = 0;
        		while ((bytesRead = fis.read(buffer)) > 0)
        		{
        			fos.write(buffer, 0, bytesRead);
        		}
        		fos.flush();
        		fos.close();
        		TreeItem selectItem = LFWPersTool.getCurrentTreeItem();
            	LFWDocTreeItem docItem = new LFWDocTreeItem(selectItem,
        				f, f.getName());
            	selectItem.setExpanded(true);
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
