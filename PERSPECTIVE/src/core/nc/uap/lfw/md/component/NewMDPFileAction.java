package nc.uap.lfw.md.component;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_lfw_component;
import nc.uap.lfw.perspective.project.LFWExplorerTreeBuilder;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.yonyou.studio.mdp.facade.MDUIFacade;

public class NewMDPFileAction extends NodeAction
{
  public NewMDPFileAction()
  {
    super(M_lfw_component.NewMDPFileAction_0);
  }

  public void run()
  {
	LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
	IProject project = LFWPersTool.getCurrentProject();
    if (view == null)
      return;
    Tree tree = view.getExplorerTree();
    TreeItem[] selTIs = tree.getSelection();
    Shell shell = new Shell(Display.getCurrent());
    try
    {
      if ((selTIs == null) || (selTIs.length == 0))
        throw new Exception(M_lfw_component.NewMDPFileAction_1);
      TreeItem selTI = selTIs[0];

      File parentFile = ((LFWDirtoryTreeItem)selTI).getFile();
      if (parentFile.isFile())
        throw new Exception(M_lfw_component.NewMDPFileAction_2);
      
      
      IFolder folder = project.getFolder(parentFile.getPath().substring(
				project.getLocation().toString().length()));
//      MDDPFacade.getInstance().createModel(project, folder, MetaDataType.BMF);
      MDUIFacade.createModelOfBMF(project, folder);
      selTI.removeAll();
      LFWExplorerTreeBuilder.getInstance().initMdTree((LFWDirtoryTreeItem)selTI, null, null, null, null, null, project, null);
//      refreshItems(project,selTI);
//      NewMdFileDialog dilog = new NewMdFileDialog(shell,project);
//      if (dilog.open() != 0) return;
//      String fileName = dilog.getFileName();
//      if ((fileName == null) || (fileName.trim().length() <= 0)) return;
//      fileName = fileName.trim();
//      if (!(fileName.toLowerCase().endsWith(".bmf"))) //$NON-NLS-1$
//        fileName = fileName + ".bmf"; //$NON-NLS-1$
//
//      File f = new File(parentFile, fileName);
//      if (!(f.exists())) {
//    	MDUIFacade.createJGraph(filename)
//        JGraph graph = getNewGraph(f);
//        String versionType = dilog.getVersiontype();
//        String programCode = dilog.getProgramCode();
//        graph.setIndustry(dilog.getIndustry());
//        graph.setVersionType(versionType);
//        graph.setProgramCode(programCode);
//        String str = JGraphSerializeTool.serializeToString(graph);
//        InputStream in = new ByteArrayInputStream(
//                str.getBytes("UTF-8")); //$NON-NLS-1$
//        LFWMdFileTreeItem fileTreeItem =  new LFWMdFileTreeItem(
//        		selTI, f, f.getName()); 
//        fileTreeItem.setType(WEBPersConstants.AMC_MD_FILENAME);
//        IFile mdfile = project.getFile(f.getPath()
//				.substring(
//						project.getLocation().toString()
//								.length()));
//        mdfile.create(in, true, null);
//        fileTreeItem.setMdFile(mdfile);
//        selTI.setExpanded(true);
//        fileTreeItem.setExpanded(true);
//        return;
//      }
//      throw new Exception(M_lfw_component.NewMDPFileAction_3 + f.getPath());
    }
    catch (Exception e)
    {
      String title = M_lfw_component.NewMDPFileAction_4;
      String message = e.getMessage();
      MessageDialog.openError(shell, title, message);
    }
  }

//  private JGraph getNewGraph(File f) throws Exception {
//    JGraph graph = JGraph.getBMFJGraph();
//    IProject project = LFWPersTool.getCurrentProject();
//    String moduleName = LfwCommonTool.getProjectModuleName(project);
//    graph.setNameSpace(moduleName);
//    graph.setOwnModule(moduleName);
//    String name = f.getName();
//    name = name.substring(0, name.length() - ".bmf".length()); //$NON-NLS-1$
//    graph.setName(name);
//    graph.setDisplayName(name);
//
//    return graph;
//  }

}
