/**
 * 
 */
package nc.uap.lfw.core.base;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.dev.LfwComponent;
import nc.uap.lfw.design.itf.BusinessComponent;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeBuilder;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.core.resources.IProject;

/**
 * 
 * ˢ�½ڵ����
 * @author chouhl
 *
 */
public class RefreshAMCNodeGroupAction extends NodeAction{

	/**
	 * �ڵ㱣���ļ����·��
	 */
	private String path = null;
	/**
	 * �ڵ�����
	 */
	private String itemType = null;
	/**
	 * �ڵ㱣���ļ�����
	 */
	private String fileName = null;
	
	/**
	 * �������
	 */
	private BusinessComponent bcp = null;
	
	public RefreshAMCNodeGroupAction(String path, String itemType, String fileName,BusinessComponent bcp) {
		super(WEBPersConstants.REFRESH, WEBPersConstants.REFRESH);
		setImageDescriptor(PaletteImage.getRefreshImgDescriptor());
		this.path = path;
		this.itemType = itemType;
		this.fileName = fileName;
		this.bcp = bcp;
	}

	public void run() {
		refreshNodes();
	}
	
	private void refreshNodes() {
		//��ȡ��ǰ����
		IProject project = LFWAMCPersTool.getCurrentProject();
		//��ȡѡ��Ľڵ�
		LFWDirtoryTreeItem parent = (LFWDirtoryTreeItem) LFWAMCPersTool.getCurrentTreeItem();
		//�Ƴ������ӽڵ�,���¼���
		parent.removeAll();
//		//��ȡ��ǰ����·��
//		String projPath = LFWAMCPersTool.getProjectPath();
//		String indexOfStr = null;
//		if(ILFWTreeNode.APPLICATION.equals(itemType)){
//			indexOfStr = WEBProjConstants.AMC_APPLICATION_PATH.replaceAll("\\\\", "/");
//		}
//		else if(ILFWTreeNode.WINDOW.equals(itemType)){
//			indexOfStr = WEBProjConstants.AMC_WINDOW_PATH.replaceAll("\\\\", "/");
//		}else if(ILFWTreeNode.PUBLIC_VIEW.equals(itemType)){
//			indexOfStr = WEBProjConstants.AMC_PUBLIC_VIEW_PATH.replaceAll("\\\\", "/");
//		}
//		int index = -1;
//		if(indexOfStr != null){
//			index = LFWAMCPersTool.getCurrentFolderPath().lastIndexOf(indexOfStr);
//		}
//		if(index >= 0){
//			path = LFWAMCPersTool.getCurrentFolderPath().substring(index);
//		}
//		String path = parent.getFile().getAbsolutePath();
//		Map<String, String>[] amcNodeNames = LFWAMCConnector.getTreeNodeNames(new String[]{path}, itemType, "");
//		if(amcNodeNames != null && amcNodeNames.length > 0){
//			LFWExplorerTreeBuilder.getInstance().refreshAMCNodeSubTree(parent, amcNodeNames[0], itemType, fileName, project, bcp);
			if(itemType.equals(ILFWTreeNode.APPLICATION)){
				LFWExplorerTreeBuilder.getInstance().refreshAppsNodeSubTree(parent, project, bcp);
			}
			else{
				String moduleName = LfwCommonTool.getProjectModuleName(project);
				String bcpName = bcp.getName();
				Map<String, LfwComponent> componentMap = new HashMap<String, LfwComponent>();
//				componentMap = LFWAMCConnector.getCacheComponentMap(moduleName, bcpName);
				if(parent instanceof LFWComponentTreeItem){
					LFWComponentTreeItem compItem = (LFWComponentTreeItem)parent;
					String compId = compItem.getComponent().getId();
					String pack = compItem.getComponent().getPack();
					if(pack!=null&&!pack.equals(""))
						compId = pack+"."+compId;
					if(itemType.equals(ILFWTreeNode.WINDOW)){
						componentMap = LFWAMCConnector.getCacheComponentMap(moduleName, bcpName);
						if(componentMap != null)
							LFWExplorerTreeBuilder.getInstance().initWindowsByComponent(parent, componentMap.get(compId));
					}
					else if(itemType.equals(ILFWTreeNode.PUBLIC_VIEW)){
						componentMap = LFWAMCConnector.getCacheViewCompMap(moduleName, bcpName);
						if(componentMap != null)
							LFWExplorerTreeBuilder.getInstance().initPublicViewsByComponent(parent, componentMap.get(compId));
					}	
				}							
			}						
//		}
//	else{
//			MessageDialog.openInformation(null, "ˢ��", "��ǰ�ڵ����ӽڵ�");
//		}
	}
	
	
}
