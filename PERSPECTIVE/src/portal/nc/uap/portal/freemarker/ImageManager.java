package nc.uap.portal.freemarker;

import java.net.MalformedURLException;
import java.net.URL;

import nc.uap.portal.perspective.PortalPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;


public class ImageManager {

	public static final String IMG_MACRO = "icons/freemarker/userdefined_directive_call.gif";
	public static final String IMG_IMPORT = "icons/freemarker/import.gif";
	public static final String IMG_IMPORT_COLLECTION = "icons/import_collection.gif";
	public static final String IMG_FUNCTION = "icons/freemarker/function.gif";
	

	public static Image getImage(String filename) {
		if (null == filename) return null;
		ImageDescriptor temp = getImageDescriptor(filename);
		if(null!=temp) {
			return temp.createImage();
		} else {
			return null;
		}
	}
	
	public static ImageDescriptor getImageDescriptor(String filename) {
		if (null == filename) return null;
		try {
		URL url = new URL(PortalPlugin.getDefault().getDescriptor().getInstallURL(),
                  "icons/freemarker/" + filename);
                  return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException mue) {
			
		}
		return null;
	}

}
