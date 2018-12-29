package nc.uap.lfw.md.component;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_lfw_component;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMdDirTreeItem;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class NewMdDirAction extends NodeAction
{
  public NewMdDirAction()
  {
    super(M_lfw_component.NewDirAction_0);
  }

  public void run()
  {
    LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
    IProject project = LFWPersTool.getCurrentProject();
    if (view == null)
      return;
    Shell shell = new Shell(Display.getCurrent());
    InputDialog input = new InputDialog(shell, M_lfw_component.NewDirAction_0, M_lfw_component.NewDirAction_1, "", null); //$NON-NLS-3$
    if (input.open() == 0) {
      String dirName = input.getValue();
      if ((dirName != null) && (dirName.trim().length() > 0)) {
        dirName = dirName.trim();
        try {
        	Tree tree = view.getTreeView().getTree();
    		TreeItem[] selTIs = tree.getSelection();
    		if (selTIs == null || selTIs.length == 0)
    			throw new Exception(M_lfw_component.NewDirAction_2);
    		TreeItem selTI = selTIs[0];
    		File parentFile = ((ILFWTreeNode) selTI).getFile();// (File)
    		// selTI.getData();
    		if (parentFile.isFile())
    			throw new Exception(M_lfw_component.NewDirAction_3);
    		File f = new File(parentFile, dirName);
    		if (!f.exists()) {    		
	    		IFolder newFolder = project.getFolder(f.getPath().substring(
	    							project.getLocation().toString().length()));
	    		newFolder.create(true, true, null);
	    		TreeItem ti = new LFWMdDirTreeItem(selTI, f);
	    		// 设置类型
	    		((LFWMdDirTreeItem) ti).setType(ILFWTreeNode.METADATA_FOLDER);
	    		//guoweic: 菜单显示有问题
	    		selTI.setExpanded(true);
    		}
    		else{
    			MessageDialog.openError(null, "错误", "已有同名目录或文件名存在，请重新输入");
    			return;
    		}
        } catch (Exception e) {
          String title = M_lfw_component.NewDirAction_0;
          String message = e.getMessage();
          MessageDialog.openError(shell, title, message);
        }
      }
    }
  }
}
