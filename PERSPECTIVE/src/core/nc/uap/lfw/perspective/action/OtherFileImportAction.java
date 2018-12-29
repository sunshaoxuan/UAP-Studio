package nc.uap.lfw.perspective.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDocTreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 工程下导入其他文件
 * @author guomq1
 * 2012-8-9
 */

public class OtherFileImportAction extends NodeAction{
	
	public OtherFileImportAction(){
		super(M_perspective.OtherFileImportAction_0);
	}
	public void run(){
     File file =openFile();
     if (file == null)
    	 return;
     try {
        String  path = ((LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem()).getFile().getPath()+"/";
//    	String  path = LFWAMCPersTool.getCurrentProject().getLocation().toOSString() + "/doc/other/"; //$NON-NLS-1$
    	saveFile(path,file);
    	
	} catch (Exception e) {
		MainPlugin.getDefault().logError(e);
	}
	}
	public static File openFile(){
		Shell shell = new Shell(Display.getCurrent());
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);

		String filePath = dialog.open();
		if (filePath != null){
			File f = new File(filePath);
			return f;
		}
		return null;
	}
	public static void saveFile(String directory,File file)throws Exception{
		FileInputStream fis = null;
		 FileOutputStream fos = null;
		try{
			fis = new FileInputStream(file);
			File parent = new java.io.File(directory);
			if (!parent.exists()){
				parent.mkdirs();
	        }
            File f = new File(directory + file.getName());
            boolean newItem = false;
            
            if(f.exists()){
				MessageDialog md = new MessageDialog(null, M_perspective.OtherFileImportAction_1, null, M_perspective.OtherFileImportAction_2, 6, new String[]{M_perspective.OtherFileImportAction_3,M_perspective.OtherFileImportAction_4}, 0);
				md.open();
				if(md.getReturnCode() == md.OK){
					md.close();
            	    f.delete();
            	    
                	
            	    }else{
            	    	md.close();
            	    	return;
            	    }
            }
            else{
            	newItem = true;
            }
            fos = new FileOutputStream(f);
            
            byte[] buffer = new byte[10240];
    		int bytesRead = 0;
    		while ((bytesRead = fis.read(buffer)) > 0)
    		{
    			fos.write(buffer, 0, bytesRead);
    		}
    		fos.flush();
    		fos.close();
    		if(newItem){
	    		TreeItem selectItem = LFWPersTool.getCurrentTreeItem();
	        	LFWDocTreeItem docItem = new LFWDocTreeItem(selectItem,
	    				f, f.getName());
	        	selectItem.setExpanded(true);
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
