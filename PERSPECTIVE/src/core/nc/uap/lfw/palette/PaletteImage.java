package nc.uap.lfw.palette;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * PaletteµÄImage
 * @author zhangxya
 *
 */
public class PaletteImage {

	public static ImageDescriptor getRelationImgDescriptor() {
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "relation.png");
	}
	public static ImageDescriptor getEntityImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "entity.png");
	}
	public static ImageDescriptor getStartImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "start.gif");
	}
	public static ImageDescriptor getDependImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "depend.png");
	}
	
	public static ImageDescriptor getDeleteImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "delete.gif");
	}
	
	public static ImageDescriptor getCreateTreeImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "create.gif");
	}
	
	public static ImageDescriptor getCreateDsImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "create.gif");
	}
	
	public static ImageDescriptor getSelectedAllImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "checked.gif");
	}
	
	public static ImageDescriptor getUnCheckedImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "un_checked.gif");
	}
	
	public static ImageDescriptor getUnSelectedAllImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "cancel.gif");
	}
	
	public static ImageDescriptor getDialogImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "dialog.gif");
	}
	
	public static ImageDescriptor getCreateMenuImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "menubar.gif");
	}
	
	public static Image createDeleteImage(){
//		ImageDescriptor errorDes = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "err.gif");
		return ImageProvider.error;
	}
	
	public static ImageDescriptor getCreateGridImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "create.gif");
	}
	
	public static ImageDescriptor getCreateListViewImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "create.gif");
	}
	public static ImageDescriptor getCreatePaginationImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "create.gif");
	}
	
	public static ImageDescriptor getRefreshImgDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "refresh.gif");
	}
	
	public static ImageDescriptor getBar2DChartDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "create.gif");
	}
	
	public static ImageDescriptor getBar3DChartDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "dialog.gif");
	}
	
	public static ImageDescriptor getBarChartDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "chart.gif");
	}
		
	public static ImageDescriptor getEditorDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "editor.gif");
	}
	
	public static ImageDescriptor getPortletDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "portlet.gif");
	}
	
	public static ImageDescriptor getRefnodeDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "refnode.gif");
	}
	public static ImageDescriptor getOpenDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "form.gif");
	}
	public static ImageDescriptor getViewDescriptor(){
		return MainPlugin.loadImage(MainPlugin.ICONS_PATH, "listener.gif");
	}
	
}
