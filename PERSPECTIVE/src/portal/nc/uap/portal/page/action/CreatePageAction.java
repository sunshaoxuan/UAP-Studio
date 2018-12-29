package nc.uap.portal.page.action;

import java.io.File;
import java.io.InputStream;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.page.dialog.CreatePageDialog;
import nc.uap.portal.perspective.PortalPlugin;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;

public class CreatePageAction extends Action{
	public CreatePageAction() {
		super(PortalProjConstants.CREATE_PAGE, PaletteImage.getCreateGridImgDescriptor());
	}
	
	public void run(){
		
		CreatePageDialog dialog = new CreatePageDialog(null, M_portal.CreatePageAction_0);
		if (dialog.open() == IDialogConstants.OK_ID) {					
			int index = dialog.getSelection();
			CpMenuCategoryVO menuCate = LFWWfmConnector.getMenuCategory()[index];
			MainPlugin.getDefault().logError(menuCate.getId());
//			Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/portalpage/admin.pml"); //$NON-NLS-1$
//			InputStream ins = LfwCommonTool.getResourceAsStream("templates/portalpage/admin.pml");
			byte[] b = new byte[1638400];
			int bytesReader = 0;
			String tempcontent = ""; //$NON-NLS-1$
			StringBuffer content = new StringBuffer();
			try{
				while(true){
					bytesReader = ins.read(b,0,1638400);
					if(bytesReader==-1){
						break;
					}
					String lineContent =""; //$NON-NLS-1$
					lineContent = new String(b,"UTF-8"); //$NON-NLS-1$
					content.append(lineContent);
				}
					tempcontent = content.toString().trim();
					
					String sourcecontent = tempcontent;
					
					IProject project = LFWPersTool.getCurrentProject();
					String menuCateId =  menuCate.getId();
					sourcecontent = replaceTemp(sourcecontent,"level","1"); //$NON-NLS-1$ //$NON-NLS-2$
					sourcecontent = replaceTemp(sourcecontent,"menuCategory",menuCate.getPk_menucategory()); //$NON-NLS-1$
					sourcecontent = replaceTemp(sourcecontent, "menuGroupName",menuCate.getTitle()); //$NON-NLS-1$
					String bcpPath = LFWAMCPersTool.getProjectPath();
					String bcp = bcpPath.substring(bcpPath.lastIndexOf("/")+1,bcpPath.length()); //$NON-NLS-1$
//					String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/pml"; 
					String filePath = project.getLocation().toOSString()+"/"+bcp+"/src/portalspec/" + bcp + "/portalspec/pml";  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					String fileName = menuCateId.replace("_", ""); //$NON-NLS-1$ //$NON-NLS-2$
					File folder = new File(filePath);
					if(!folder.exists()) 
						folder.mkdirs();
			    	File file = new File(filePath + "/" + fileName + ".pml"); //$NON-NLS-1$ //$NON-NLS-2$
			    	try {
			    		PortalPlugin.getDefault().logInfo("creating pml, id:" + fileName); //$NON-NLS-1$
//			    		FileUtils.writeStringToFile(file, xml, "UTF-8");
			    		FileUtilities.saveFile(file, sourcecontent,"UTF-8");	 //$NON-NLS-1$
			    		PortalPlugin.getDefault().logInfo("creating pml, id:" + fileName + ":done"); //$NON-NLS-1$ //$NON-NLS-2$
			    	} 
			    	catch (Exception e) {
			    		PortalPlugin.getDefault().logError(e.getMessage());
			    	}
			    	project.refreshLocal(2, null);
					LFWAMCPersTool.refreshCurrentPorject();
				
			}
			catch (Exception e) {
				PortalPlugin.getDefault().logError(e.getMessage());
			}

	}
		
	}
	/*
	 * 替换模板文件中的标签
	 */
	public String replaceTemp(String content, String markersign, String replacecontent){
		markersign = "${"+markersign+"}"; //$NON-NLS-1$ //$NON-NLS-2$
		String target = content;
		while(target.indexOf(markersign)>-1){
			target = target.replace(markersign, replacecontent);
		}
		return target.trim();
	}

}
