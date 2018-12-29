package nc.uap.lfw.tool;

import java.net.URL;
import java.util.HashMap;

//import nc.uap.lfw.mlr.MLRUpdateRecord;
import nc.uap.lfw.mlr.MLResSubstitution;
import nc.uap.lfw.plugin.Activator;
//import ncplugin.mlr.update.MLRUpdateRecord;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Í¼Æ¬¹¤³§
 * 
 * @author dingrf
 *
 */
public class ImageFactory{

	private static HashMap<String,ImageDescriptor> hmImageDesc = new HashMap<String,ImageDescriptor>();
	private static HashMap<String,Image> hmImage = new HashMap<String,Image>();

	public ImageFactory(){
	}

	public static ImageDescriptor getImageDesc(String path){
		ImageDescriptor imgDesc = (ImageDescriptor)hmImageDesc.get(path);
		if (imgDesc == null){
			imgDesc = createImageDescriptor(path);
			hmImageDesc.put(path, imgDesc);
		}
		return imgDesc;
	}

	public static Image getImage(String path){
		Image img = (Image)hmImage.get(path);
		if (img == null){
			ImageDescriptor imgDesc = getImageDesc(path);
			img = imgDesc.createImage();
			hmImage.put(path, img);
		}
		return img;
	}

	@SuppressWarnings("deprecation")
	private static ImageDescriptor createImageDescriptor(String path){
		URL url = Activator.getDefault().getDescriptor().getInstallURL();
		ImageDescriptor id = null;
		try{
			id = ImageDescriptor.createFromURL(new URL(url, path));
		}
		catch (Exception e){
			id = ImageDescriptor.getMissingImageDescriptor();
		}
		return id;
	}

	public static Image getCheckedImage(boolean isChecked){
		String path = null;
		if (isChecked)
			path = "icons/checked.gif";
		else
			path = "icons/unChecked.gif";
		return getImage(path);
	}

	public static Image getImageBySubstitutionState(MLResSubstitution sub){
		int state = sub.getState();
		String path = "";
		switch (state)
		{
		case 0: // '\0'
			path = "icons/externalize.gif";
			break;

		case 1: // '\001'
			path = "icons/externalized.gif";
			break;

		case 3:
			path = "icons/public.gif";
			break;
		case 4:
			path = "icons/delete.gif";
			break;
		case 5:
			path = "icons/warning.gif";
			break;
		case 6:
			path = "icons/repair.gif";
			break;
		case 7:
			path = "icons/ignored.gif";
			break;
		default:
			path = "icons/ignored.gif";
			break;
		}
		return getImage(path);
	}

}
