package nc.uap.lfw.palette;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.datasets.core.DsRelationConnection;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.editor.window.WindowConfigObj;
import nc.uap.lfw.form.DatasetToFormConnection;
import nc.uap.lfw.grid.DatasetToGridConnection;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.lang.M_palette;
import nc.uap.lfw.listview.DatasetToListViewConnection;
import nc.uap.lfw.pagination.DatasetToPaginationConnection;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.refnode.RefNodeElementObj;
import nc.uap.lfw.refnoderel.DatasetFieldElementObj;
import nc.uap.lfw.refnoderel.RefNodeRelConnection;
import nc.uap.lfw.tree.TreeTopLevelConnection;
import nc.uap.lfw.tree.core.TreeLevelElementObj;
import nc.uap.lfw.tree.treelevel.TreeLevelChildConnection;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.events.MouseEvent;

/**
 * 工具Palette
 * 
 * @author zhangxya
 * 
 */
public class PaletteFactory {
	public static class TemplateCreationTool extends CreationTool {
		
		protected boolean handleButtonUp(int button) {
			if (button == 1) {
				setUnloadWhenFinished(false);
			} else {
				setUnloadWhenFinished(true);
			}
			boolean b = super.handleButtonUp(button);
			return b;
		}

	}

	public static class CustomConnectionCreationTool extends
			ConnectionCreationTool {

		
		protected boolean handleButtonUp(int button) {
			if (button == 1) {
				setUnloadWhenFinished(false);
			} else {
				setState(STATE_TERMINAL);
				setUnloadWhenFinished(true);
			}
			return super.handleButtonUp(button);
		}

	}

	public static class CustomSelectionTool extends SelectionTool {

		
		public void mouseUp(MouseEvent me, EditPartViewer viewer) {
			if (me.stateMask == 589824) {
				super.getTargetEditPart().performRequest(
						new CtrlMouseUpRequest());
			} else {
				super.mouseUp(me, viewer);
			}
		}

	}

	public static PaletteRoot createPaletteRoot() {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createDatasetBusinessComponentPalette());
		paletteRoot.add(createConnectionPalette());
		return paletteRoot;
	}
	
	
	private static ToolEntry createDsRelationConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_0,
				M_palette.PaletteFactory_1, new CreationFactory() {
					public Object getNewObject() {
						return DsRelationConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
						.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
	
	/**
	 * 创建Grid工具箱
	 * @return
	 */
	public static PaletteRoot createGridPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		//paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createGridComponentPalette());
		paletteRoot.add(createGridRelationPalette());
		return paletteRoot;
	}
	
	/**
	 * 创建ListView工具箱
	 * @return
	 */
	public static PaletteRoot createListViewPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		//paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createListViewComponentPalette());
		paletteRoot.add(createListViewRelationPalette());
		return paletteRoot;
	}
	
	/**
	 * 创建Pagination工具箱
	 * @return
	 */
	public static PaletteRoot createPaginationPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		//paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createPaginationComponentPalette());
		paletteRoot.add(createPaginationRelationPalette());
		return paletteRoot;
	}
	
	
	/**
	 * 创建excel工具箱
	 * @return
	 */
	public static PaletteRoot createExcelPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		//paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createExcelComponentPalette());
		paletteRoot.add(createExcelRelationPalette());
		return paletteRoot;
	}
	
	
	/**
	 * 创建refnode关系工具箱
	 * @return
	 */
	public static PaletteRoot createRefNodeRelPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(creatRefnodeRelComponentPalette());
		paletteRoot.add(createRefnodeRelRelationPalette());
		return paletteRoot;
	}
	
	/**
	 * 创建refnode关系组件
	 * @return
	 */
	private static PaletteContainer creatRefnodeRelComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_2);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_3,
				M_palette.PaletteFactory_4, DatasetFieldElementObj.class,
				new SimpleFactory(DatasetFieldElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		
		entries.add(entityTE);
		
		ToolEntry detailTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_5,
				M_palette.PaletteFactory_6, RefNodeElementObj.class,
				new SimpleFactory(RefNodeElementObj.class), PaletteImage.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		detailTE.setToolClass(TemplateCreationTool.class);
		entries.add(detailTE);
		drawer.addAll(entries);
		return drawer;

	}
	
	/**
	 * 创建参照关系
	 * @return
	 */
	private static PaletteContainer createRefnodeRelRelationPalette(){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_7);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry relationREF = createRenodeRelConnection();
		entries.add(relationREF);
		drawer.addAll(entries);
		return drawer;
	}
	
	private static ToolEntry createRenodeRelConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_8,
				M_palette.PaletteFactory_9, new CreationFactory() {
					public Object getNewObject() {
						return RefNodeRelConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
	/**
	 * 创建toolbar工具箱
	 * @return
	 */
	public static PaletteRoot createToolbarPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createToolbarElePalette());
		return paletteRoot;
	}
	
	/**
	 * 创建Form工具箱
	 * @return
	 */
	public static PaletteRoot createFormPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		//paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createFormComponentPalette());
		paletteRoot.add(createFormRelationPalette());
		return paletteRoot;
	}
	
	private static ToolEntry createFormConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_10,
				M_palette.PaletteFactory_11, new CreationFactory() {
					public Object getNewObject() {
						return DatasetToFormConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
						.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
	/**
	 * form关联工具箱
	 * @return
	 */
	private static PaletteContainer createFormRelationPalette(){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_12);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry relationREF = createFormConnection();
		entries.add(relationREF);
		drawer.addAll(entries);
		return drawer;
	}
	
	
	/**
	 * 创建tree工具箱
	 * @return
	 */
	public static PaletteRoot createTreePalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		//paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createTreeComponentPalette());
		paletteRoot.add(createTreeRelationPalette());
		return paletteRoot;
	}
	
	private static PaletteContainer createTreeComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_13);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_14,
				M_palette.PaletteFactory_15, TreeLevelElementObj.class,
				new SimpleFactory(TreeLevelElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		entries.add(entityTE);
		ToolEntry entityCon = createContextMenuEntry();
		entries.add(entityCon);
		drawer.addAll(entries);
		return drawer;

	}
	
	/**
	 * tree 关联工具箱
	 * @return
	 */
	private static PaletteContainer createTreeRelationPalette(){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_16);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry relationREF = createTreeConnection();
		entries.add(relationREF);
		ToolEntry relationChild = createTreeLevelConnection();
		entries.add(relationChild);
		drawer.addAll(entries);
		return drawer;
	}
	
	private static ToolEntry createTreeConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_17,
				M_palette.PaletteFactory_18, new CreationFactory() {
					public Object getNewObject() {
						return TreeTopLevelConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
						.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	

	/**
	 * grid 关联工具箱
	 * @return
	 */
	private static PaletteContainer createGridRelationPalette(){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_19);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry relationREF = createGridConnection();
		//ToolEntry contextRelation = createContextConnection();
		entries.add(relationREF);
		//entries.add(contextRelation);
		
		ToolEntry relationTreeREF = createGridLevelConnection();
		entries.add(relationTreeREF);
		//ToolEntry relationChild = createTreeLevelConnection();
		//entries.add(relationChild);
		
		drawer.addAll(entries);
		return drawer;
	}
	
	/**
	 * listview 关联工具箱
	 * @return
	 */
	private static PaletteContainer createListViewRelationPalette(){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_19);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry relationREF = createListViewConnection();
		entries.add(relationREF);	
		drawer.addAll(entries);
		return drawer;
	}
	

	/**
	 * Pagination 关联工具箱
	 * @return
	 */
	private static PaletteContainer createPaginationRelationPalette(){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_19);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry relationREF = createPaginationConnection();
		entries.add(relationREF);	
		drawer.addAll(entries);
		return drawer;
	}
	
	private static ToolEntry createGridLevelConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_20,
				M_palette.PaletteFactory_21, new CreationFactory() {
					public Object getNewObject() {
						return TreeTopLevelConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
						.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
	/**
	 * excel关联关系工具箱
	 * @return
	 */
	private static PaletteContainer createExcelRelationPalette(){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_22);
//		List<ToolEntry> entries = new ArrayList<ToolEntry>();
//		ToolEntry relationREF = createExcelConnection();
//		//ToolEntry contextRelation = createContextConnection();
//		entries.add(relationREF);
//		//entries.add(contextRelation);
//		drawer.addAll(entries);
		return drawer;
	}
	
	/**
	 * grid关联数据集
	 * @return
	 */
	private static ToolEntry createGridConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_23,
				M_palette.PaletteFactory_24, new CreationFactory() {
					public Object getNewObject() {
						return DatasetToGridConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
						.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
	/**
	 * listview关联数据集
	 * @return
	 */
	private static ToolEntry createListViewConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_23,
				M_palette.PaletteFactory_24, new CreationFactory() {
					public Object getNewObject() {
						return DatasetToListViewConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
						.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
	/**
	 * pagination关联数据集
	 * @return
	 */
	private static ToolEntry createPaginationConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_23,
				M_palette.PaletteFactory_24, new CreationFactory() {
					public Object getNewObject() {
						return DatasetToPaginationConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
						.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
	
	/**
	 * 关联数据集
	 * @return
	 */
//	private static ToolEntry createExcelConnection() {
//		ToolEntry relationTE = new ConnectionCreationToolEntry("关联数据集",
//				"创建关联数据集关系", new CreationFactory() {
//					public Object getNewObject() {
//						return DatasetToExcelConnection.class;
//					}
//
//					public Object getObjectType() {
//						return new Integer(1);
//					}
//				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
//						.getRelationImgDescriptor());
//		relationTE.setToolClass(CustomConnectionCreationTool.class);
//		return relationTE;
//	}
	
	
	/**
	 * 关联右键菜单
	 * @return
	 */
//	private static ToolEntry createContextConnection() {
//		ToolEntry relationTE = new ConnectionCreationToolEntry("关联右键菜单",
//				"创建关联右键菜单关系", new CreationFactory() {
//					public Object getNewObject() {
//						return GridToContextMenuConnection.class;
//					}
//
//					public Object getObjectType() {
//						return new Integer(1);
//					}
//				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
//						.getRelationImgDescriptor());
//		relationTE.setToolClass(CustomConnectionCreationTool.class);
//		return relationTE;
//	}
	
	/**
	 * 
	 * grid业务组件工具箱
	 */
	private static PaletteContainer createGridComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_25);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_26,
				M_palette.PaletteFactory_27, RefDatasetElementObj.class,
				new SimpleFactory(RefDatasetElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		
		entries.add(entityTE);
		
		entries.add(createGridLevelEntry());
		
		ToolEntry entityCon = createContextMenuEntry();
		
		entries.add(entityCon);
		//
		drawer.addAll(entries);
		return drawer;

	}
	
	/**
	 * 
	 * listview业务组件工具箱
	 */
	private static PaletteContainer createListViewComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_25);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_26,
				M_palette.PaletteFactory_27, RefDatasetElementObj.class,
				new SimpleFactory(RefDatasetElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		
		entries.add(entityTE);
		
		//
		drawer.addAll(entries);
		return drawer;

	}
	/**
	 * 
	 * Pagination业务组件工具箱
	 */
	private static PaletteContainer createPaginationComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_25);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_26,
				M_palette.PaletteFactory_27, RefDatasetElementObj.class,
				new SimpleFactory(RefDatasetElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		
		entries.add(entityTE);
		
		//
		drawer.addAll(entries);
		return drawer;

	}
	
	/**
	 * 
	 * form业务组件工具箱
	 */
	private static PaletteContainer createFormComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_28);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_29,
				M_palette.PaletteFactory_30, RefDatasetElementObj.class,
				new SimpleFactory(RefDatasetElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		
		entries.add(entityTE);
				
		ToolEntry entityCon = createContextMenuEntry();
		
		entries.add(entityCon);
		//
		drawer.addAll(entries);
		return drawer;

	}
	/**
	 * gridLevel业务组件
	 * @return
	 */
	private static ToolEntry createGridLevelEntry() {
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_31,
				M_palette.PaletteFactory_32, GridLevelElementObj.class,
				new SimpleFactory(GridLevelElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		return entityTE;
	}
	
	/**
	 * 
	 * excel业务组件工具箱
	 */
	private static PaletteContainer createExcelComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_33);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_34,
				M_palette.PaletteFactory_35, RefDatasetElementObj.class,
				new SimpleFactory(RefDatasetElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		
		entries.add(entityTE);
		
//		ToolEntry entityCon = createContextMenuEntry();
//		
//		entries.add(entityCon);
		//
		drawer.addAll(entries);
		return drawer;

	}
	
	/**
	 * 关联右键菜单component
	 * @return
	 */
	private static ToolEntry createContextMenuEntry(){
		ToolEntry entityCon= new CombinedTemplateCreationEntry(M_palette.PaletteFactory_36,
				M_palette.PaletteFactory_37, ContextMenuElementObj.class,
				new SimpleFactory(ContextMenuElementObj.class), PaletteImage
						.getCreateMenuImgDescriptor(), PaletteImage
						.getCreateMenuImgDescriptor());
		entityCon.setToolClass(TemplateCreationTool.class);
		return entityCon;
	}
	
	/**
	 * 创建toolbar组件工具箱
	 * @return
	 */
	private static PaletteContainer createToolbarElePalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_38);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityCon= new CombinedTemplateCreationEntry(M_palette.PaletteFactory_39,
				M_palette.PaletteFactory_40, ContextMenuElementObj.class,
				new SimpleFactory(ContextMenuElementObj.class), PaletteImage
						.getCreateMenuImgDescriptor(), PaletteImage
						.getCreateMenuImgDescriptor());
		entityCon.setToolClass(TemplateCreationTool.class);
		entries.add(entityCon);
		drawer.addAll(entries);
		return drawer;

	}
	
	
	/**
	 * 创建ds间关系工具箱
	 * @return
	 */
	public static PaletteRoot createDatasetsPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createDsFieldRalationPalette());
		return paletteRoot;
	}

	
	/**
	 * 创建Pagemeta编辑器工具箱
	 * @author guoweic
	 * @return
	 */
	public static PaletteRoot createPagemetaPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createPagemetaBusinessComponentPalette());
		//TODO
//		paletteRoot.add(createPagemetaConnectionPalette());
		return paletteRoot;
	}
	
	/**
	 * 创建Widget编辑器工具箱
	 * @author guoweic
	 * @return
	 */
	public static PaletteRoot createWidgetPalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createWidgetBusinessComponentPalette());
		return paletteRoot;
	}
	
	public static PaletteRoot createBasePalette() {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createBasePalette(paletteRoot));
		return paletteRoot;
	}

	
//	/**
//	 * 创建PageFlow编辑器工具箱
//	 * @author guoweic
//	 * @return
//	 */
//	public static PaletteRoot createPageFlowPalette() {
//		PaletteRoot paletteRoot = new PaletteRoot();
//		paletteRoot.add(createBasePalette(paletteRoot));
//		paletteRoot.add(createPageFlowBusinessComponentPalette());
//		paletteRoot.add(createPageFlowConnectionPalette());
//		return paletteRoot;
//	}
	
	private static PaletteContainer createBasePalette(PaletteRoot root) {
		PaletteContainer container = new PaletteGroup(M_palette.PaletteFactory_41);
		SelectionToolEntry entry = new SelectionToolEntry(M_palette.PaletteFactory_42);
		entry.setToolClass(CustomSelectionTool.class);
		container.add(entry);
		return container;
	}

	private static PaletteContainer createDsFieldRalationPalette(){
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_43);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry relationREF = createDsRelationConnection();
		entries.add(relationREF);
		drawer.addAll(entries);
		return drawer;
	}
	private static PaletteContainer createConnectionPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_44);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();

//		ToolEntry relationTE = new ConnectionCreationToolEntry("关联",
//				"创建一个新的关联关系", new CreationFactory() {
//					public Object getNewObject() {
//						return Connection.class;
//					}
//
//					public Object getObjectType() {
//						return new Integer(1);
//					}
//				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
//						.getRelationImgDescriptor());
//		relationTE.setToolClass(CustomConnectionCreationTool.class);
		ToolEntry relationTE = createConnection();
		entries.add(relationTE);
		relationTE.setToolClass(CustomConnectionCreationTool.class);
//		ToolEntry relationREF = new ConnectionCreationToolEntry("子关联",
//				"创建一个子关联关系", new CreationFactory() {
//					public Object getNewObject() {
//						return ChildConnection.class;
//					}
//
//					public Object getObjectType() {
//						return new Integer(1);
//					}
//				}, PaletteImage.getDependImgDescriptor(), PaletteImage
//						.getDependImgDescriptor());
//
//		relationREF.setToolClass(CustomConnectionCreationTool.class);
		ToolEntry relationREF = createSubConnection();
		entries.add(relationREF);
		////.setToolClass(CustomConnectionCreationTool.class);
		drawer.addAll(entries);
		return drawer;

	}
	
	private static ToolEntry createTreeLevelConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_45,
				M_palette.PaletteFactory_46, new CreationFactory() {
					public Object getNewObject() {
						return TreeLevelChildConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getDependImgDescriptor(), PaletteImage
						.getDependImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}

	/**
	 * 创建Dataset编辑器的业务组件工具箱
	 * @return
	 */
	private static PaletteContainer createDatasetBusinessComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_47);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		ToolEntry entityTE = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_48,
				M_palette.PaletteFactory_49, RefDatasetElementObj.class,
				new SimpleFactory(RefDatasetElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		entries.add(entityTE);
		//
		drawer.addAll(entries);
		return drawer;

	}

	/**
	 * 创建Pagemeta编辑器的业务组件工具箱
	 * @author guoweic
	 * @return
	 */
	private static PaletteContainer createPagemetaBusinessComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_50);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		
		ToolEntry entityTE = new CombinedTemplateCreationEntry(LFWTool.getViewText(null),
				M_palette.PaletteFactory_51 + LFWTool.getViewText(null), WidgetElementObj.class,
				new SimpleFactory(WidgetElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		
		entityTE.setToolClass(TemplateCreationTool.class);
		
		entries.add(entityTE);
//		ToolEntry entityTE_window = new CombinedTemplateCreationEntry("内联window",
//				"关联一个window", WindowObj.class,
//				new SimpleFactory(WindowObj.class), PaletteImage
//				.getEntityImgDescriptor(), PaletteImage
//				.getEntityImgDescriptor());
//		entityTE_window.setToolClass(TemplateCreationTool.class);
//		entries.add(entityTE_window);
		drawer.addAll(entries);
		return drawer;

	}
//
//	/**
//	 * 创建PageFlow编辑器的业务组件工具箱
//	 * @author guoweic
//	 * @return
//	 */
//	private static PaletteContainer createPageFlowBusinessComponentPalette() {
//		PaletteDrawer drawer = new PaletteDrawer("业务组件工具箱");
//		List<ToolEntry> entries = new ArrayList<ToolEntry>();
//
//		ToolEntry startTE = new CombinedTemplateCreationEntry("开始",
//				"创建一个新的开始节点", PfStartElementObj.class,
//				new SimpleFactory(PfStartElementObj.class), PaletteImage
//						.getStartImgDescriptor(), PaletteImage
//						.getStartImgDescriptor());
//		startTE.setToolClass(TemplateCreationTool.class);
//		entries.add(startTE);
//
////		ToolEntry pageTE = new CombinedTemplateCreationEntry(WEBProjConstants.PAGEFLOW_PAGE_CN,
////				"创建一个新的" + WEBProjConstants.PAGEFLOW_PAGE_CN, PfPageElementObj.class,
////				new SimpleFactory(PfPageElementObj.class), PaletteImage
////						.getEntityImgDescriptor(), PaletteImage
////						.getEntityImgDescriptor());
////		pageTE.setToolClass(TemplateCreationTool.class);
////		entries.add(pageTE);
////
////		ToolEntry decesionTE = new CombinedTemplateCreationEntry(WEBProjConstants.PAGEFLOW_DECISION_CN,
////				"创建一个新的" + WEBProjConstants.PAGEFLOW_DECISION_CN, PfDecisionElementObj.class,
////				new SimpleFactory(PfDecisionElementObj.class), PaletteImage
////						.getEntityImgDescriptor(), PaletteImage
////						.getEntityImgDescriptor());
////		decesionTE.setToolClass(TemplateCreationTool.class);
////		entries.add(decesionTE);
//		
//		drawer.addAll(entries);
//		return drawer;
//
//	}
	
	/**
	 * 创建Widget编辑器的业务组件工具箱
	 * @author guoweic
	 * @return
	 */
	private static PaletteContainer createWidgetBusinessComponentPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_56);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
//		ToolEntry entityTE = new CombinedTemplateCreationEntry(LFWExplorerTreeView.WIDGET_CN,
//				"创建一个新的" + LFWExplorerTreeView.WIDGET_CN, WidgetElementObj.class,
//				new SimpleFactory(WidgetElementObj.class), PaletteImage
//						.getEntityImgDescriptor(), PaletteImage
//						.getEntityImgDescriptor());
//		entityTE.setToolClass(TemplateCreationTool.class);
//		
//		entries.add(entityTE);
		ToolEntry entityTE_window = new CombinedTemplateCreationEntry(M_palette.PaletteFactory_57,
				M_palette.PaletteFactory_58, WindowConfigObj.class,
				new SimpleFactory(WindowConfigObj.class), PaletteImage
				.getEntityImgDescriptor(), PaletteImage
				.getEntityImgDescriptor());
		entityTE_window.setToolClass(TemplateCreationTool.class);
		entries.add(entityTE_window);
		
		drawer.addAll(entries);
		return drawer;

	}
	
	/**
	 * 创建Pagemeta编辑器的关联关系工具箱
	 * @author guoweic
	 * @return
	 */
	private static PaletteContainer createPagemetaConnectionPalette() {
		PaletteDrawer drawer = new PaletteDrawer(M_palette.PaletteFactory_53);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		entries.add(createConnection());
		drawer.addAll(entries);
		return drawer;
	}
	
	/**
	 * 创建PageFlow编辑器的关联关系工具箱
	 * @author guoweic
	 * @return
	 */
//	private static PaletteContainer createPageFlowConnectionPalette() {
//		PaletteDrawer drawer = new PaletteDrawer("关联关系工具箱");
//		List<ToolEntry> entries = new ArrayList<ToolEntry>();
//		entries.add(createConnection());
//		drawer.addAll(entries);
//		return drawer;
//	}
	

	
	/**
	 * 创建Listener图标
	 * @author guoweic
	 * @return
	private static ToolEntry createListenerComponentPalette() {
		ToolEntry entityTE = new CombinedTemplateCreationEntry("Listener",
				"创建一个新的Listener", ListenerElementObj.class,
				new SimpleFactory(ListenerElementObj.class), PaletteImage
						.getEntityImgDescriptor(), PaletteImage
						.getEntityImgDescriptor());
		entityTE.setToolClass(TemplateCreationTool.class);
		return entityTE;
	}
	 */
	
	/**
	 * 创建关联关系
	 * @author guoweic
	 * @return
	 */
	private static ToolEntry createConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_54,
				M_palette.PaletteFactory_55, new CreationFactory() {
					public Object getNewObject() {
//						return WidgetConnector.class;
						return Connection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getRelationImgDescriptor(), PaletteImage
						.getRelationImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
	/**
	 * 创建子关联关系图标
	 * @author guoweic
	 * @return
	 */
	private static ToolEntry createSubConnection() {
		ToolEntry relationTE = new ConnectionCreationToolEntry(M_palette.PaletteFactory_59,
				M_palette.PaletteFactory_60, new CreationFactory() {
					public Object getNewObject() {
						return ChildConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, PaletteImage.getDependImgDescriptor(), PaletteImage
						.getDependImgDescriptor());
		relationTE.setToolClass(CustomConnectionCreationTool.class);
		return relationTE;
	}
	
}
