package nc.uap.lfw.editor.extNode;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.model.parser.LfwUIDtdHelper;
import nc.uap.lfw.lang.M_editor;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;

public class OpenXMLEditorAction extends NodeAction {

	private String openType = null;
	private Object currentItem = null;
	public OpenXMLEditorAction(String openType,Object item){
		super(M_editor.OpenXMLEditorAction_0+(openType.equals("um")?M_editor.OpenXMLEditorAction_1:M_editor.OpenXMLEditorAction_2)+M_editor.OpenXMLEditorAction_3); //$NON-NLS-2$
		this.openType = openType;
		this.currentItem = item;
	}
	public void run(){
		
		try{
			IProject project = LFWPersTool.getCurrentProject();
			if(openType.equals("pm")){ //$NON-NLS-1$
				if(currentItem instanceof File){
					File resFile = null;
					File resFolder = (File)currentItem;
					if(resFolder.isDirectory()){
						for(File file :resFolder.listFiles())
							if(file.getName().endsWith(".pm")) //$NON-NLS-1$
								resFile = file;							
					}
					if(resFile!=null){
						String cx = FileUtilities.fetchFileContent(resFile,"UTF-8").trim(); //$NON-NLS-1$
						if(cx.indexOf(LfwUIDtdHelper.getWindowDocType())<0){
							int index = cx.indexOf("\n")+1; //$NON-NLS-1$
							cx = cx.substring(0,index) + LfwUIDtdHelper.getWindowDocType() + "\n" + cx.substring(index); //$NON-NLS-1$
							LfwCommonTool.checkOutFile(resFile.getPath());
							FileUtilities.saveFile(resFile, cx, "UTF-8");							 //$NON-NLS-1$
							project.refreshLocal(2, null);
						}
						
						if(resFile.getPath().indexOf(project.getLocation().toOSString())>-1){
							IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
							file.setCharset("UTF-8", null); //$NON-NLS-1$
							IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							try {
								IDE.openEditor(workbenchPage, file,"tk.eclipse.plugin.xmleditor.editors.XMLEditor"); //$NON-NLS-1$
							} catch (PartInitException e) {
								MainPlugin.getDefault().logError(e.getMessage(),e);
							}
						}
						else{
							IEditorInput input = new FileStoreEditorInput(EFS.getLocalFileSystem().getStore(resFile.toURI()));
							String editorId = "tk.eclipse.plugin.xmleditor.editors.XMLEditor";
							try {
								MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
							} catch (PartInitException e) {
								MainPlugin.getDefault().logError(e.getMessage());
							}
						}
					}
				}
			}
			else if(openType.equals("um")){ //$NON-NLS-1$
				if(currentItem instanceof File){
					File resFile = null;
					File resFolder = (File)currentItem;
					if(resFolder.isDirectory()){
						for(File file :resFolder.listFiles())
							if(file.getName().endsWith(".um")) //$NON-NLS-1$
								resFile = file;							
					}
					if(resFile!=null){
						String cx = FileUtilities.fetchFileContent(resFile,"UTF-8").trim(); //$NON-NLS-1$
						if(cx.indexOf(LfwUIDtdHelper.getUmDocType())<0){
							int index = cx.indexOf("\n")+1; //$NON-NLS-1$
							cx = cx.substring(0,index) + LfwUIDtdHelper.getUmDocType() + "\n"+cx.substring(index); //$NON-NLS-1$
							LfwCommonTool.checkOutFile(resFile.getPath());
							FileUtilities.saveFile(resFile, cx, "UTF-8"); //$NON-NLS-1$
							project.refreshLocal(2, null);
						}
						
//						IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
//						file.setCharset("UTF-8", null); //$NON-NLS-1$
//						IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//						try {
//							IDE.openEditor(workbenchPage, file,"tk.eclipse.plugin.xmleditor.editors.XMLEditor"); //$NON-NLS-1$
//						} catch (PartInitException e) {
//							MainPlugin.getDefault().logError(e.getMessage(),e);
//						}
						if(resFile.getPath().indexOf(project.getLocation().toOSString())>-1){
							IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
							file.setCharset("UTF-8", null); //$NON-NLS-1$
							IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							try {
								IDE.openEditor(workbenchPage, file,"tk.eclipse.plugin.xmleditor.editors.XMLEditor"); //$NON-NLS-1$
							} catch (PartInitException e) {
								MainPlugin.getDefault().logError(e.getMessage(),e);
							}
						}
						else{
							IEditorInput input = new FileStoreEditorInput(EFS.getLocalFileSystem().getStore(resFile.toURI()));
							String editorId = "tk.eclipse.plugin.xmleditor.editors.XMLEditor";
							try {
								MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
							} catch (PartInitException e) {
								MainPlugin.getDefault().logError(e.getMessage());
							}
						}
					}
				}
			}
			else if(openType.equals("wd")){ //$NON-NLS-1$
				if(currentItem instanceof File){
					File resFile = null;
					File resFolder = (File)currentItem;
					if(resFolder.isDirectory()){
						for(File file :resFolder.listFiles())
							if(file.getName().endsWith(".wd")) //$NON-NLS-1$
								resFile = file;							
					}
					if(resFile!=null){
						String cx = FileUtilities.fetchFileContent(resFile,"UTF-8").trim(); //$NON-NLS-1$
						if(cx.indexOf(LfwUIDtdHelper.getViewDocType())<0){
							int index = cx.indexOf("\n")+1; //$NON-NLS-1$
							cx = cx.substring(0,index)+ LfwUIDtdHelper.getViewDocType() + "\n"+cx.substring(index); //$NON-NLS-1$
							LfwCommonTool.checkOutFile(resFile.getPath());
							FileUtilities.saveFile(resFile, cx, "UTF-8"); //$NON-NLS-1$
							project.refreshLocal(2, null);
						}						
//						IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
//						file.setCharset("UTF-8", null); //$NON-NLS-1$
//						IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//						try {
//							IDE.openEditor(workbenchPage, file,"tk.eclipse.plugin.xmleditor.editors.XMLEditor"); //$NON-NLS-1$
//						} catch (PartInitException e) {
//							MainPlugin.getDefault().logError(e.getMessage(),e);
//						}
						if(resFile.getPath().indexOf(project.getLocation().toOSString())>-1){
							IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
							file.setCharset("UTF-8", null); //$NON-NLS-1$
							IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							try {
								IDE.openEditor(workbenchPage, file,"tk.eclipse.plugin.xmleditor.editors.XMLEditor"); //$NON-NLS-1$
							} catch (PartInitException e) {
								MainPlugin.getDefault().logError(e.getMessage(),e);
							}
						}
						else{
							IEditorInput input = new FileStoreEditorInput(EFS.getLocalFileSystem().getStore(resFile.toURI()));
							String editorId = "tk.eclipse.plugin.xmleditor.editors.XMLEditor";
							try {
								MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
							} catch (PartInitException e) {
								MainPlugin.getDefault().logError(e.getMessage());
							}
						}
					}
				}
			}
			else if(openType.equals("app")){ //$NON-NLS-1$
				if(currentItem instanceof File){
					File resFile = null;
					File resFolder = (File)currentItem;
					if(resFolder.isDirectory()){
						for(File file :resFolder.listFiles())
							if(file.getName().endsWith(".app")) //$NON-NLS-1$
								resFile = file;							
					}
					if(resFile!=null){
						String cx = FileUtilities.fetchFileContent(resFile,"UTF-8").trim(); //$NON-NLS-1$
						if(cx.indexOf(LfwUIDtdHelper.getAppDocType())<0){
							int index = cx.indexOf("\n")+1; //$NON-NLS-1$
							cx = cx.substring(0,index)+ LfwUIDtdHelper.getAppDocType() + "\n"+cx.substring(index); //$NON-NLS-1$
							LfwCommonTool.checkOutFile(resFile.getPath());
							FileUtilities.saveFile(resFile, cx, "UTF-8"); //$NON-NLS-1$
							project.refreshLocal(2, null);
						}						
//						IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
//						file.setCharset("UTF-8", null); //$NON-NLS-1$
//						IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//						try {
//							IDE.openEditor(workbenchPage, file,"tk.eclipse.plugin.xmleditor.editors.XMLEditor"); //$NON-NLS-1$
//						} catch (PartInitException e) {
//							MainPlugin.getDefault().logError(e.getMessage(),e);
//						}
						if(resFile.getPath().indexOf(project.getLocation().toOSString())>-1){
							IFile file = project.getFile(resFile.getPath().substring(project.getLocation().toOSString().length()));
							file.setCharset("UTF-8", null); //$NON-NLS-1$
							IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							try {
								IDE.openEditor(workbenchPage, file,"tk.eclipse.plugin.xmleditor.editors.XMLEditor"); //$NON-NLS-1$
							} catch (PartInitException e) {
								MainPlugin.getDefault().logError(e.getMessage(),e);
							}
						}
						else{
							IEditorInput input = new FileStoreEditorInput(EFS.getLocalFileSystem().getStore(resFile.toURI()));
							String editorId = "tk.eclipse.plugin.xmleditor.editors.XMLEditor";
							try {
								MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
							} catch (PartInitException e) {
								MainPlugin.getDefault().logError(e.getMessage());
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}
}
