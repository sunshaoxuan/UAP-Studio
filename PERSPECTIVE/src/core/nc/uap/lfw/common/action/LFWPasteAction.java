package nc.uap.lfw.common.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.common.tools.LfwGlobalEditorInfo;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.ChartComp;
//import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.ListViewComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.PaginationComp;
import nc.uap.lfw.core.comp.ProgressBarComp;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_lfw_core;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWComboTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWContextMenuTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDSTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMenubarCompTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefNodeTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 粘贴命令
 * @author zhangxya
 *
 */
public class LFWPasteAction extends Action {
	
	public LFWPasteAction(String message) {
		super(M_lfw_core.LFWPasteAction_0 + message, PaletteImage.getCreateDsImgDescriptor());
	}
	
	public void run() {
		String key = LFWCopyAction.COPYKEY;
		WebElement webEle = (WebElement) LfwGlobalEditorInfo.getAttr(key);
		webEle.setFrom(null);
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		//数据集
		LfwView widget = LFWPersTool.getCurrentWidget();
		if(webEle instanceof Dataset){
			Dataset dataset = (Dataset) webEle;
			try {
				//添加拷贝的数据集节点
				LFWDSTreeItem dsTreeItem = (LFWDSTreeItem) view.addDSTreeNode(dataset.getId(), dataset.getCaption());
				dsTreeItem.setData(dataset);
				//保存数据集到widget中
				widget.getViewModels().addDataset(dataset);
				LFWPersTool.saveWidget(widget);
			} catch (Exception e) {
				Shell shell = new Shell(Display.getCurrent());
				MainPlugin.getDefault().logError(e.getMessage(), e);
				String title = M_lfw_core.LFWPasteAction_1;
				String message = e.getMessage();
				MessageDialog.openError(shell, title, message);
			}
		}
		else if(webEle instanceof BaseRefNode){
			BaseRefNode refNode = (BaseRefNode) webEle;
			try {
				//添加拷贝的数据集节点
				LFWRefNodeTreeItem refNodeTreeItem = (LFWRefNodeTreeItem) view.addRefNode(refNode.getId());
				refNodeTreeItem.setData(refNode);
				//保存数据集到widget中
				widget.getViewModels().addRefNode(refNode);
				LFWPersTool.saveWidget(widget);
			} catch (Exception e) {
				Shell shell = new Shell(Display.getCurrent());
				MainPlugin.getDefault().logError(e.getMessage(), e);
				String title = M_lfw_core.LFWPasteAction_1;
				String message = e.getMessage();
				MessageDialog.openError(shell, title, message);
			}
		}
		//下拉数据集
		else if(webEle instanceof ComboData){
			dealComboData((ComboData)webEle, view, widget);
		}//表格
		else if(webEle instanceof GridComp){
			dealGridComp((GridComp)webEle, view, widget);
		}//表单
		else if(webEle instanceof FormComp){
			dealFromComp((FormComp)webEle, view, widget);
		}
		//树
		else if(webEle instanceof TreeViewComp){
			dealTreeComp((TreeViewComp)webEle, view, widget);
		}
		//labelcomp
		else if(webEle instanceof LabelComp){
			dealLabelComp((LabelComp)webEle, view, widget);
		}
		//iframe
		else if(webEle instanceof IFrameComp){
			dealIframComp((IFrameComp)webEle, view, widget);
		}
		//textcomp
		else if(webEle instanceof TextComp){
			dealTextComp((TextComp)webEle, view, widget);
		}
		//excelcomp
//		else if(webEle instanceof ExcelComp)
//			dealExcelComp((ExcelComp)webEle, view, widget);
		//Buttoncomp
		else if(webEle instanceof ButtonComp)
			dealButtonComp((ButtonComp)webEle, view, widget);
		//imagecomp
		else if(webEle instanceof ImageComp)
			dealImageComp((ImageComp)webEle, view ,widget);
		//toobarComp
		else if(webEle instanceof ToolBarComp)
			dealToolBarComp((ToolBarComp)webEle, view, widget);
		//chartcomp
		else if(webEle instanceof ChartComp)
			dealChartComp((ChartComp)webEle, view, widget);
		//linkcomp
		else if(webEle instanceof LinkComp)
			dealLinkComp((LinkComp)webEle, view, widget);
		//progressbar
		else if(webEle instanceof ProgressBarComp)
			dealProgressComp((ProgressBarComp)webEle, view, widget);
		//selfdefcomp
		else if(webEle instanceof SelfDefComp)
			dealSelfDefComp((SelfDefComp)webEle, view, widget);
		//menubar
		else if(webEle instanceof MenubarComp)
			dealMenuBar((MenubarComp)webEle, view, widget);
		else if(webEle instanceof ContextMenuComp)
			dealContextMenu((ContextMenuComp)webEle, view, widget);
		//listview
		else if(webEle instanceof ListViewComp)
			dealListViewComp((ListViewComp)webEle, view, widget);
		//pagination
		else if(webEle instanceof PaginationComp)
			dealPagination((PaginationComp)webEle, view, widget);
	}
	


	/**
	 * 处理菜单的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealMenuBar(MenubarComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		//来自页面的菜单
		try {
		if(widget == null){
			LfwWindow pm = LFWPersTool.getCurrentPageMeta();
			
			// 保存该Menubar到pagemeta.pm文件中
//			pm.getViewMenus().addMenuBar(webEle);
			LFWPersTool.savePagemeta(pm);
			//添加菜单
			TreeItem[] tis = LFWPersTool.getTree().getSelection();
			if (tis == null || tis.length == 0)
				return;
			LFWSeparateTreeItem menusItem = (LFWSeparateTreeItem) tis[0];
			new LFWMenubarCompTreeItem(menusItem, webEle);
		}
		//来自widget
		else{
			if(widget.getViewMenus().getMenuBar(webEle.getId())!=null){
				MessageDialog.openError(null, M_lfw_core.LFWPasteAction_2, M_lfw_core.LFWPasteAction_3+webEle.getId()+M_lfw_core.LFWPasteAction_4);
				return;
			}
				
			// 保存该Menubar到widget.um文件中
			widget.getViewMenus().addMenuBar(webEle);
			LFWPersTool.saveWidget(widget);
			TreeItem[] tis = LFWPersTool.getTree().getSelection();
			if (tis == null || tis.length == 0)
				return;
			LFWSeparateTreeItem menusItem = (LFWSeparateTreeItem) tis[0];
			LFWMenubarCompTreeItem newItem = new LFWMenubarCompTreeItem(menusItem, webEle);
			newItem.setFromWidget(true);
			}
		}
		catch (Exception e) {
			String title =WEBPersConstants.NEW_MENUBAR;
			String message = e.getMessage();
			Shell shell = new Shell(Display.getCurrent());
			MessageDialog.openError(shell, title, message);
		}
		
	}
	
	/**
	 * 处理右键菜单的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealContextMenu(ContextMenuComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		//来自页面的菜单
				try {
				if(widget == null){
					LfwWindow pm = LFWPersTool.getCurrentPageMeta();
					
					// 保存该Menubar到pagemeta.pm文件中
//					pm.getViewMenus().addMenuBar(webEle);
					LFWPersTool.savePagemeta(pm);
					//添加菜单
					TreeItem[] tis = LFWPersTool.getTree().getSelection();
					if (tis == null || tis.length == 0)
						return;
					LFWSeparateTreeItem menusItem = (LFWSeparateTreeItem) tis[0];
					new LFWContextMenuTreeItem(menusItem, LFWTool.getWEBProjConstantValue("CONTEXT_MENUCOMP_ELEMENT"),webEle); //$NON-NLS-1$
				}
				//来自widget
				else{
					if(widget.getViewMenus().getContextMenu(webEle.getId())!=null){
						MessageDialog.openError(null, M_lfw_core.LFWPasteAction_2, M_lfw_core.LFWPasteAction_3+webEle.getId()+M_lfw_core.LFWPasteAction_4);
						return;
					}
						
					// 保存该Menubar到widget.um文件中
					widget.getViewMenus().addContextMenu(webEle);
					LFWPersTool.saveWidget(widget);
					TreeItem[] tis = LFWPersTool.getTree().getSelection();
					if (tis == null || tis.length == 0)
						return;
					LFWSeparateTreeItem menusItem = (LFWSeparateTreeItem) tis[0];
					LFWContextMenuTreeItem newItem = new LFWContextMenuTreeItem(menusItem, LFWTool.getWEBProjConstantValue("CONTEXT_MENUCOMP_ELEMENT"),webEle); //$NON-NLS-1$
//					newItem.setFromWidget(true);
					}
				}
				catch (Exception e) {
					String title =WEBPersConstants.NEW_CONTEXT_MENU;
					String message = e.getMessage();
					Shell shell = new Shell(Display.getCurrent());
					MessageDialog.openError(shell, title, message);
				}
	}

	/**
	 * 处理自定义控件
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealSelfDefComp(SelfDefComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的SelfDefComp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addSelfdefTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存SelfDefComp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}


	/**
	 * 处理进度条ProgressBarComp的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealProgressComp(ProgressBarComp webEle,
			LFWExplorerTreeView view, LfwView widget) {
		try {
			//添加拷贝的ProgressBarComp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addProgressBarTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存ProgressBarComp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	/**
	 * 处理分页条PaginationComp的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealPagination(PaginationComp webEle,
			LFWExplorerTreeView view, LfwView widget) {
		try {
			//添加拷贝的ProgressBarComp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addPaginationTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存ProgressBarComp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * LinkComp的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealLinkComp(LinkComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的LinkComp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addLinkTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存LinkComp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	/**
	 * LinkComp的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealListViewComp(ListViewComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的LinkComp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addListViewTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存LinkComp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * 处理chartcomp的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealChartComp(ChartComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的chartcomp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addChartTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存chartcomp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	/**
	 * 处理toolbarcomp的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealToolBarComp(ToolBarComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的toolbarcomp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addToolbarTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存toolbarcomp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}


	/**
	 * 处理ImageComp的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealImageComp(ImageComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的ImageComp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addImageTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存ImageComp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * 处理button的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealButtonComp(ButtonComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的Button节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addButtonTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存Button到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * 处理excelcomp控件的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
//	private void dealExcelComp(ExcelComp webEle, LFWExplorerTreeView view,
//			LfwWidget widget) {
//		try {
//			//添加拷贝的ExcelComp节点
//			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addExcelTreeNode(webEle.getId());
//			componentTreeItem.setData(webEle);
//			//保存ExcelComp到widget中
//			widget.getViewComponents().addComponent(webEle);
//			LFWPersTool.saveWidget(widget);
//		} catch (Exception e) {
//			Shell shell = new Shell(Display.getCurrent());
//			MainPlugin.getDefault().logError(e.getMessage(), e);
//			String title = "粘贴数据信息";
//			String message = e.getMessage();
//			MessageDialog.openError(shell, title, message);
//		}
//	}

	/**
	 * 处理textcomp
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealTextComp(TextComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的textcomp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addTextCompTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存textcomp到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * 处理IFram的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealIframComp(IFrameComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的Iframcomp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addIFrameTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存数据集到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * 处理Labelcomp的粘贴
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealLabelComp(LabelComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的Labelcomp节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addLabelTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存数据集到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * 处理树拷贝
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealTreeComp(TreeViewComp webEle, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的树节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addTreeNode(webEle.getId());
			componentTreeItem.setData(webEle);
			//保存数据集到widget中
			widget.getViewComponents().addComponent(webEle);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	/**
	 * 处理form的拷贝
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealFromComp(FormComp formComp, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝Form节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addFormTreeNode(formComp.getId());
			componentTreeItem.setData(formComp);
			//保存数据集到widget中
			widget.getViewComponents().addComponent(formComp);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	/**
	 * 处理grid拷贝
	 * @param webEle
	 * @param view
	 * @param widget
	 */
	private void dealGridComp(GridComp gridComp, LFWExplorerTreeView view,
			LfwView widget) {
		try {
			//添加拷贝的Grid节点
			LFWWebComponentTreeItem componentTreeItem = (LFWWebComponentTreeItem) view.addGridTreeNode(gridComp.getId());
			componentTreeItem.setData(gridComp);
			//保存数据集到widget中
			widget.getViewComponents().addComponent(gridComp);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * 处理下拉框的拷贝
	 * @param webEle
	 */
	private void dealComboData(ComboData combo, LFWExplorerTreeView view, LfwView widget) {
		try {
			//添加拷贝的下拉数据节点
			LFWComboTreeItem comboTreeItem = (LFWComboTreeItem) view.addComboNode(combo.getId());
			comboTreeItem.setData(combo);
			//保存数据集到widget中
			widget.getViewModels().addComboData(combo);
			LFWPersTool.saveWidget(widget);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			MainPlugin.getDefault().logError(e.getMessage(), e);
			String title = M_lfw_core.LFWPasteAction_1;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

}

