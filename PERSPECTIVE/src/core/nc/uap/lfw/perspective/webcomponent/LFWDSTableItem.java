package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.data.Dataset;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * dsµÄTableItem
 * @author zhangxya
 *
 */
public class LFWDSTableItem extends TableItem{

	public LFWDSTableItem(Table parent, Dataset ds) {
		super(parent, SWT.NONE);
		setData(ds);
		setText(ds.getId());
		setImage(getFileImage());
	}

	
	protected void checkSubclass () {
	}
	private Image getFileImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "groups.gif");
		return ImageProvider.groups;
	}

		
	public File getFile() {
		return (File) getData();
	}

	public void deleteNode() {
		FileUtilities.deleteFile(getFile());
		dispose();

}

}
