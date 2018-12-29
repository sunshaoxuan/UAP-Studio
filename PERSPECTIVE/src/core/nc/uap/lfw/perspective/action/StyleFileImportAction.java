package nc.uap.lfw.perspective.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_perspective;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.SimpleWildcardTester;

import javax.swing.JFileChooser;


public class StyleFileImportAction extends Action{
	
	public StyleFileImportAction(){
		super(M_perspective.StyleFileImportAction_0);
	}
	public void run(){
     File file =openFile();
     if (file == null)
    	 return;
     try {
    	String  path = LFWAMCPersTool.getProjectWithBcpPath();
		unzipFile(path + "/web/",file); //$NON-NLS-1$
	} catch (Exception e) {
		MainPlugin.getDefault().logError(e);
	}
	LFWPersTool.refreshCurrentPorject();
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
	public static void unzipFile(String directory,File zip)throws Exception{
		ZipInputStream zis = null;
		try{
			zis = new ZipInputStream(new FileInputStream(zip));
			ZipEntry ze = zis.getNextEntry();
			File parent = new java.io.File(directory);
			if (!parent.exists()){
				parent.mkdirs();
	        }
	        while (ze != null)
	        {
	        	if (ze.getName().endsWith("/")){ //$NON-NLS-1$
	        		File f = new File(directory + ze.getName());
	        		if (!f.exists())
	        			f.mkdir();
	        	}else{
	        		String name = ze.getName();
	        		File child = new File(parent, name);
	        		FileOutputStream output = null;
	        		try{
		        		output = new FileOutputStream(child);
		        		byte[] buffer = new byte[10240];
		        		int bytesRead = 0;
		        		while ((bytesRead = zis.read(buffer)) > 0)
		        		{
		        			output.write(buffer, 0, bytesRead);
		        		}
		        		output.flush();
	        		}catch(Exception e){
	        			MainPlugin.getDefault().logError(e);
	        		}
	        		if(output != null)
	        			output.close();
	        	}	
	            ze = zis.getNextEntry();
	          }
	          zis.close();
	    }
	    catch (IOException e)
	    {
	    	MainPlugin.getDefault().logError(e);
	    }
		finally{
			if(zis != null)
				zis.close();
		}

	}
}
