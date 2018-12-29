package nc.uap.lfw.component;

import java.io.File;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.dev.LfwComponent;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EditComponentAction extends NodeAction{

	public EditComponentAction(){
		super(M_application.EditComponentAction_0);
	}
	public void run(){
		editComponent();
	}
	public void editComponent(){
		LFWComponentTreeItem item = (LFWComponentTreeItem)LFWPersTool.getCurrentTreeItem();
		LfwUIComponent component = item.getComponent();
		EditComponentDialog dialog = new EditComponentDialog(component, M_application.EditComponentAction_1);
		if(dialog.open()==IDialogConstants.OK_ID){
			try{
				String id = dialog.getId();
				String name = dialog.getName();
				String pack = dialog.getPackName();
				component.setId(id);
				component.setPack(pack);
				component.setName(name);
//				String packPath = "";
				if(pack.length()==0){
//					packPath = "/"+id;
				}
				else{					
//					packPath = "/"+pack.replace(".", "/")+"/"+id;
					id = pack+"."+id; //$NON-NLS-1$
				}
				IProject project = LFWPersTool.getCurrentProject();
				String moduleId = LfwCommonTool.getProjectModuleName(project);
				if(item.getType().equals(ILFWTreeNode.WINDOW)){
					Map<String,LfwComponent> componentMap = LFWAMCConnector.getCacheComponentMap(moduleId, item.getBcp().getName());
					LfwComponent componentModule = componentMap.get(id);
					if(componentModule.getWindowMap()!=null){
//						File windowFile = new File(item.getFile(), WEBPersConstants.AMC_WINDOW_PATH + packPath);
						File windowFile  = item.getFile();
						toXml(windowFile.getPath(), component);
						project.refreshLocal(2, null);
					}
					item.setText(name);
				}
				if(item.getType().equals(ILFWTreeNode.PUBLIC_VIEW)){
					Map<String,LfwComponent> componentMap = LFWAMCConnector.getCacheViewCompMap(moduleId, item.getBcp().getName());
					LfwComponent componentModule = componentMap.get(id);
					if(componentModule.getViewMap()!=null){
//						File viewFile = new File(item.getFile(), WEBPersConstants.AMC_PUBVIEW_PATH + packPath);
						File viewFile = item.getFile();
						toXml(viewFile.getPath(), component);
						project.refreshLocal(2, null);
					}
					item.setText(name);
				}
				
			}
			catch(Exception e){
				MainPlugin.getDefault().logError(e.getMessage(),e);
				return;
			}
		}
	}
	
	public void toXml(String path, LfwUIComponent component){
		try {
			Document doc = XmlCommonTool.createDocument();
			Element rootNode = doc.createElement("Component"); //$NON-NLS-1$
			doc.appendChild(rootNode);
			rootNode.setAttribute("id", component.getId()); //$NON-NLS-1$
			rootNode.setAttribute("name", component.getName()); //$NON-NLS-1$
			rootNode.setAttribute("pack", component.getPack()); //$NON-NLS-1$
			
			File folder = new File(path);
			File file = new File(folder,"component.cp"); //$NON-NLS-1$
			LFWPersTool.checkOutFile(file.getPath());
			if(!folder.exists()){
				folder.mkdirs();
			}
	    	XmlCommonTool.documentToXml(doc, file);
	    	
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}
		
	}
}
