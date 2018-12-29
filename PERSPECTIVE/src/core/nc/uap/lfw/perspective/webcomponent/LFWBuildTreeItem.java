package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.action.BuildDomainDiskAction;
import nc.uap.lfw.build.action.CompileDomainAction;
import nc.uap.lfw.build.action.DeleteDomainAction;
import nc.uap.lfw.editor.extNode.AddIntoDomainAction;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class LFWBuildTreeItem extends LFWDirtoryTreeItem{

	private ImageDescriptor imageDescriptor = null;
	private File resFile = null;
	
	public LFWBuildTreeItem(TreeItem parentItem, File file, String text) {
		super(parentItem, file, text);
		this.resFile = file;
	}
	protected Image getDirImage() {
//		imageDescriptor = WEBProjPlugin.loadImage(
//					WEBProjPlugin.ICONS_PATH, "page.gif");
		return ImageProvider.page;
	}
	@SuppressWarnings("restriction")
	public void mouseDoubleClick(){
//		IFile file = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(resFile.toURI())[0];
//		try {
//			EditorUtility.openInEditor(file);
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		IEditorInput input = new FileStoreEditorInput(EFS.getLocalFileSystem().getStore(resFile.toURI()));
//		IEditorInput input = new FileEditorInput(file);
//		String editorId = "nc.uap.lfw.build.editor.BuildConfigEditor";
		String editorId = "org.eclipse.ui.DefaultTextEditor";
		try {
//			EditorUtility.openInEditor(file,true);
			MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
		} catch (PartInitException e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}
	}
	public void addMenuListener(IMenuManager manager) {
		if(this.getType().equals(LFWDirtoryTreeItem.DOMAIN_BUILD_CONFIG)){
			AddIntoDomainAction domainAction = new AddIntoDomainAction(this);
			manager.add(domainAction);
//			IExtensionPoint extPoint = Platform.getExtensionRegistry().getExtensionPoint("nc.uap.lfw.tools.perspective.antLaunch");
//			if (extPoint != null) {
//				IExtension[] extensions = extPoint.getExtensions();
//				for (IExtension extension : extensions) {
//					try {
//						NodeAction compileAction = (NodeAction)extension.getConfigurationElements()[0].createExecutableExtension("class");
//						manager.add(compileAction);
//					} catch (Exception e) {
//						MainPlugin.getDefault().logError(e.getMessage(),e);
//					}
//				}
//			}
//			else{
//				MainPlugin.getDefault().logError("没有扩展点");
//			}
			CompileDomainAction compileAction = new CompileDomainAction(this);
			manager.add(compileAction);
			BuildDomainDiskAction BuildAction = new BuildDomainDiskAction(this);
			manager.add(BuildAction);
			DeleteDomainAction deleteAction = new DeleteDomainAction(this);
			manager.add(deleteAction);
		}
	}

}
