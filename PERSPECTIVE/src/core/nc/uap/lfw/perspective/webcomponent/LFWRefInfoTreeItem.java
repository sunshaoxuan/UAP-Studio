package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.editor.extNode.DeleteRefInfoAction;
import nc.uap.lfw.ref.model.IConst;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;

public class LFWRefInfoTreeItem extends LFWDirtoryTreeItem{

	String refClass = null;
	IFile classFile = null;
	LfwRefInfoVO refInfo = null;
	IProject project = null;
	String classType = null;
	
	public LFWRefInfoTreeItem(TreeItem parentItem,IProject project, File folder, LfwRefInfoVO refInfo,String text) {
		this(parentItem, IConst.MODEL_CLASSTYPE, project, folder, refInfo, text);
	}
	
	public LFWRefInfoTreeItem(TreeItem parentItem,String classType, IProject project, File folder, LfwRefInfoVO refInfo,String text) {
		super(parentItem, folder, text);
		this.refInfo = refInfo;
		this.project = project;
		this.classType = classType;
		setImage(getRefImage());
		setRefClass(classType);
	}
	
	private Image getRefImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "file.png");
		return ImageProvider.file;
	}
	public IFile getClassFile() {		
		return this.classFile;		
	}
	public String getRefClass() {
		return refClass;
	}
	public void setRefClass(File refClass){
		IFile file = project.getFile(refClass.getPath().substring(project.getLocation().toOSString().length()));
		this.classFile= file;
		setData(refClass);
	}
	public void setRefClass(String classType) {
		if(classType.equals(IConst.CONTROL_CLASSTYPE)){
			this.refClass = getRefInfo().getRefControlClass();
		}
		else if(classType.equals(IConst.MODEL_CLASSTYPE)){ 
			this.refClass = getRefInfo().getRefclass();
		}
		else
			return;
		String classPath = ((File)getData()).getPath()+"/"+refClass.replace(".", "/")+".java";
		IFile file = project.getFile(classPath.substring(project.getLocation().toOSString().length()));
		this.classFile= file;
		setData(new File(classPath));
	}
	public void addMenuListener(IMenuManager manager) {
		DeleteRefInfoAction deleteAction = new DeleteRefInfoAction(this);
		manager.add(deleteAction);
	}
	public void mouseDoubleClick() {
		try {
			EditorUtility.openInEditor(classFile,true);
		} catch (PartInitException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}
	public LfwRefInfoVO getRefInfo() {
		return refInfo;
	}
	public void setRefInfo(LfwRefInfoVO refInfo) {
		this.refInfo = refInfo;
	}
	public void setClassFile(IFile classFile) {
		this.classFile = classFile;
	}
	

}
