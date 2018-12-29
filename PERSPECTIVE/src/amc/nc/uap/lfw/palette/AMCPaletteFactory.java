/**
 * 
 */
package nc.uap.lfw.palette;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.editor.application.ApplicationObj;
import nc.uap.lfw.editor.window.WindowConfigObj;

import nc.uap.lfw.lang.M_palette;

import org.eclipse.core.resources.IProject;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * AMC工具箱类
 * @author chouhl
 *
 */
public class AMCPaletteFactory extends PaletteFactory {
	
	/**
	 * 创建Application编辑器工具箱
	 * @author chouhl
	 * @return
	 */
	public static PaletteRoot createApplicationPalette(IProject project, TreeItem currentTreeItem){
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createApplicationBusinessComponentPalette());
		paletteRoot.add(createApplicationWindowListPalette(project, currentTreeItem));
		return paletteRoot;
	}
	
	/**
	 * 基本工具箱——选择
	 * @param root
	 * @return
	 */
	private static PaletteContainer createBasePalette(PaletteRoot root) {
		PaletteContainer container = new PaletteGroup("Base Palette"); //$NON-NLS-1$
		SelectionToolEntry entry = new SelectionToolEntry(M_palette.AMCPaletteFactory_0);
		entry.setToolClass(CustomSelectionTool.class);
		container.add(entry);
		return container;
	}
	
	/**
	 * Application业务组件工具箱——创建Window
	 * @author chouhl
	 * @return
	 */
	private static PaletteContainer createApplicationBusinessComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.AMCPaletteFactory_1);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.AMCPaletteFactory_2,
				M_palette.AMCPaletteFactory_3, ApplicationObj.class,
				new SimpleFactory(ApplicationObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		entries.add(entityTE);
		drawer.addAll(entries);
		return drawer;
	}
	
	/**
	 * Application Window列表——选择Window
	 * @param project
	 * @param currentTreeItem
	 * @return
	 */
	private static PaletteContainer createApplicationWindowListPalette(IProject project, TreeItem currentTreeItem){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.AMCPaletteFactory_4);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.AMCPaletteFactory_5,
				M_palette.AMCPaletteFactory_6, WindowConfigObj.class,
				new SimpleFactory(WindowConfigObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		entries.add(entityTE);
		drawer.addAll(entries);
		return drawer;
	}
	
}
