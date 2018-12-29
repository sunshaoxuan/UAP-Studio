package nc.lfw.editor.common.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.pagemeta.WindowEditor;
import nc.lfw.lfwtools.perspective.MainPlugin;

public class CheckTools {
	
	public static boolean checkAllEditorSaved(){
		IEditorPart[] editors = MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getDirtyEditors();
		if(editors!=null&&editors.length>0)
			return false;		
		else return true;
	}
	public static boolean checkEditorSavedByBcp(){
		return false;
		
	}
	public static boolean checkEditorDisposed(){
		return false;		
	}
	public static IEditorPart[] getAllLFWEditor(){
		IWorkbenchPage page = MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart[] editors = page.getEditors();
		List<IEditorPart> editorList = new ArrayList<IEditorPart>();
		
		if(editors!=null&&editors.length>0){
			for(IEditorPart editor:editors){
				if(editor instanceof LFWBaseEditor||editor instanceof WindowEditor){
					editorList.add(editor);
				}
			}
		}
		if(editorList.size()>0){
			return editorList.toArray(new IEditorPart[0]);
		}
		return null;
	}
	public static void saveEditor(IEditorPart editor){
		IWorkbenchPage page = MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.saveEditor(editor, true);
	}
	public static void saveEditors(){
		IWorkbenchPage page = MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.saveAllEditors(false);
	}
	public static void closeEditors(){
		IWorkbenchPage page = MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart[] editors = page.getEditors();
		if(editors!=null){
//			page.closeAllEditors(true);
			for(IEditorPart editor:editors){
				if(editor instanceof LFWBaseEditor||editor instanceof WindowEditor){
					page.closeEditor(editor, false);
				}			
			}
		}
	}
	public static boolean beforeRefresh(){
		if(!CheckTools.checkAllEditorSaved()){
			if(MessageDialog.openConfirm(null, "提示", "刷新前请保存已打开的编辑器，是否自动保存")){
				CheckTools.saveEditors();
			}
			else
				return false;		
		}
		if(getAllLFWEditor()!=null){
			if(MessageDialog.openConfirm(null, "提示", "刷新会导致已打开的编辑器失效。是否自动关闭编辑器？"))
				CheckTools.closeEditors();
			}
		return true;
	}

}
