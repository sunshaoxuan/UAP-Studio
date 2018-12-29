package nc.uap.lfw.md.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_lfw_component;
import nc.uap.lfw.perspective.webcomponent.LFWDocTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMdDirTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMdFileTreeItem;

public class ImportMdFile extends NodeAction{

	public ImportMdFile(){
		super(M_lfw_component.ImportMdFile_0);
	}
	public void run(){
	     File file =openFile();
	     if (file == null)
	    	 return;
	     try {
	    	String  path = ((LFWMdDirTreeItem)LFWPersTool.getCurrentTreeItem()).getFile().getPath();
	    	saveFile(path,file);
	    	
	    	
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
		LFWPersTool.refreshCurrentPorject();
		}
		public static File openFile(){
			Shell shell = new Shell(Display.getCurrent());
			FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		//	dialog.setFilterNames(new String[]{"excel 2007нд╪Ч(.xlsx)"});
	
			dialog.setFilterExtensions(new String[]{"*.bmf","*.bpf"}); //$NON-NLS-1$ //$NON-NLS-2$
			String filterpath = dialog.getFilterPath();
			File dir = new File(filterpath);
			if(dir.isDirectory()){
			File[] files = dir.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					if(name.endsWith(".bmf")||name.endsWith(".bpf")){ //$NON-NLS-1$ //$NON-NLS-2$
						return true;
					}
					return false;
				}
			}); }
			String filePath = dialog.open();
			
			if (filePath != null){
				File f = new File(filePath);
				return f;
			}
			return null;
		}
		public static void saveFile(String directory,File selectFile)throws Exception{
			try{
//				FileInputStream fis = new FileInputStream(excelFile);
				File parent = new File(directory);
				
				TreeItem selectItem = LFWPersTool.getCurrentTreeItem();
				
				if (!parent.exists()){
					parent.mkdirs();
		        }
				if(parent.isDirectory()){
					File[] files = parent.listFiles(new FilenameFilter() {
						
						@Override
						public boolean accept(File dir, String name) {
							if(name.endsWith(".bmf")||name.endsWith(".bpf")){ //$NON-NLS-1$ //$NON-NLS-2$
								return true;
							}
							return false;
						}
					});
					
					
					for(File file:files){
						if(selectFile.getName().equals(file.getName())){
							if(MessageDialog.openConfirm(null, "", M_lfw_component.ImportMdFile_2)){ //$NON-NLS-1$
								TreeItem[] items = selectItem.getItems();
								for(TreeItem item:items){
									if(selectFile.getName().equals(item.getText())){
										((LFWMdFileTreeItem)item).deleteNode();
										break;
									}
								}
								break;
							}
							else return;
						}
					}
//	                File f = new File(directory + selectFile.getName());
	                FileUtilities.copyFileToDir(directory, selectFile, selectFile.getName());
	                IProject project = LFWPersTool.getCurrentProject();
//	                project.refreshLocal(2, null);
	                File f = new File(directory+"/"+selectFile.getName()); //$NON-NLS-1$
	                
	    	    	LFWMdFileTreeItem docItem = new LFWMdFileTreeItem(selectItem,
	    					f, f.getName());
	    	    	IFile mdfile = project.getFile(f.getPath()
							.substring(
									project.getLocation().toString()
											.length()));
	    	    	docItem.setMdFile(mdfile);
	    	    	if(selectFile.getName().endsWith(".bmf")) //$NON-NLS-1$
	    	    		docItem.setType(WEBPersConstants.AMC_MD_FILENAME);
	    	    	else 
	    	    		docItem.setType(WEBPersConstants.AMC_BPF_FILENAME);
	    	    	selectItem.setExpanded(true);
//	                FileOutputStream fos = new FileOutputStream(f);
//	                
//	                byte[] buffer = new byte[10240];
//	        		int bytesRead = 0;
//	        		while ((bytesRead = fis.read(buffer)) > 0)
//	        		{
//	        			fos.write(buffer, 0, bytesRead);
//	        		}
//	        		fos.flush();
//	        		fos.close();
	               }
				
		    }
		    catch (Exception e)
		    {
		    	MainPlugin.getDefault().logError(e);
		    }

		}
}
