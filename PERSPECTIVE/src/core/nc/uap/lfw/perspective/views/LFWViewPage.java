package nc.uap.lfw.perspective.views;

import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.uap.lfw.button.ButtonEditor;
import nc.uap.lfw.chart.core.ChartGraphPart;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.iframe.IFrameEditor;
import nc.uap.lfw.image.ImageEditor;
import nc.uap.lfw.label.LabelEditor;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.linkcomp.LinkCompEditor;
import nc.uap.lfw.parts.DatasetGraphPart;
import nc.uap.lfw.parts.LFWElementPart;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.selfdefcomp.core.SelfDefCompEditor;
import nc.uap.lfw.textcomp.TextCompEditor;
import nc.uap.lfw.tree.core.TreeEditor;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

/**
 * LFW页面实现类
 * 
 * @author zhangxya
 * 
 */
public class LFWViewPage extends LFWAbstractViewPage {

	private Composite comp = null;
	private StackLayout sl = new StackLayout();
	private TabFolder voTabFolder = null;
	private TabItem cellPropTabItem = null;
	
	public void createControl(Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(sl);
		voTabFolder = new TabFolder(comp, SWT.NONE);
		
		setVoTabFolder(voTabFolder);

		// 创建控件属性页
		cellPropTabItem = new TabItem(voTabFolder, SWT.NONE);
		cellPropTabItem.setText(M_perspective.LFWViewPage_0);
		setCellPropTabItem(cellPropTabItem);
		addCellPropItemControl();
		sl.topControl = voTabFolder;
		comp.layout();
		
		setCellPropertiesView(new CellPropertiesView(voTabFolder, SWT.NONE));
	}

	public Control getControl() {
		return this.comp;
	}

	
	public void setFocus() {
		getControl().setFocus();
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		EventConf[] eventConfs = null;
		String controllerClazz = null;
		if (part instanceof PropertySheet)
			return;
		// 为当前编辑器设置选中对象
		if(DataSetEditor.getActiveEditor() != null){
			DataSetEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = DataSetEditor.getActiveEditor().getDsElementObj().getDs().getEventConfs();
			controllerClazz = DataSetEditor.getActiveEditor().getWidget().getControllerClazz();
		}
		else if(TreeEditor.getActiveEditor() != null){
			TreeEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = TreeEditor.getActiveEditor().getTreeElement().getTreeComp().getEventConfs();
			controllerClazz = TreeEditor.getActiveEditor().getWidget().getControllerClazz();
		}
		else if(ButtonEditor.getActiveEditor() != null){
			ButtonEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = ButtonEditor.getActiveEditor().getButtonElement().getButtonComp().getEventConfs();
			controllerClazz = ButtonEditor.getActiveEditor().getWidgetTreeItem().getWidget().getControllerClazz();
		}
		else if(TextCompEditor.getActiveEditor() != null){
			TextCompEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = TextCompEditor.getActiveEditor().getTextElement().getTextComp().getEventConfs();
			controllerClazz = TextCompEditor.getActiveEditor().getWidget().getControllerClazz();
		}
		else if(IFrameEditor.getActiveEditor() != null){
			IFrameEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = IFrameEditor.getActiveEditor().getIframeElement().getIframecomp().getEventConfs();
			controllerClazz = IFrameEditor.getActiveEditor().getWidgetTreeItem().getWidget().getControllerClazz();
		}
		else if(LabelEditor.getActiveEditor() != null){
			LabelEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = LabelEditor.getActiveEditor().getLabelElement().getLabelComp().getEventConfs();
			controllerClazz = LabelEditor.getActiveEditor().getWidget().getControllerClazz();
		}
		else if(ImageEditor.getActiveEditor() != null){
			ImageEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = ImageEditor.getActiveEditor().getImageElement().getImageComp().getEventConfs();
			controllerClazz = ImageEditor.getActiveEditor().getWidgetTreeItem().getWidget().getControllerClazz();
		}
		else if(LinkCompEditor.getActiveEditor() != null){
			LinkCompEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = LinkCompEditor.getActiveEditor().getLinkElement().getLinkComp().getEventConfs();
			controllerClazz = LinkCompEditor.getActiveEditor().getWidgetTreeItem().getWidget().getControllerClazz();
		}
		else if(SelfDefCompEditor.getActiveEditor() != null){
			SelfDefCompEditor.getActiveEditor().setCurrentSelection(selection);
			eventConfs = SelfDefCompEditor.getActiveEditor().getSelfdefElement().getSelfDefComp().getEventConfs();
			controllerClazz = SelfDefCompEditor.getActiveEditor().getWidget().getControllerClazz();
		}
		//partName = "模型视图";
		if (part == null || selection == null) {
			return;
		} else if (part instanceof DataSetEditor) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if (sel instanceof LFWElementPart) {
				// (DatasetElementObj) dsobj = (DatasetElementObj)sel.;
				LFWElementPart lfwEle = (LFWElementPart) sel;
				Object model = lfwEle.getModel();
				if (model instanceof DatasetElementObj) {
					DatasetElementObj dsobj = (DatasetElementObj) model;
					Dataset ds = dsobj.getDs();
					dsobj.setId(ds.getId());
//					Field[] fields = ds.getFieldSet().getFields();
//					ArrayList<Field> datas = new ArrayList<Field>();
//					if (fields != null) {
//						for (int i = 0; i < fields.length; i++) {
//							datas.add(fields[i]);
//						}
//					}
					List<Field> datas = ds.getFieldSet().getFieldList();
					((CellPropertiesView) getCellPropertiesView()).getTv().setInput(datas);
					((CellPropertiesView) getCellPropertiesView()).setLfwElementPart(lfwEle);
					((CellPropertiesView) getCellPropertiesView()).getTv().expandAll();
					
					sl.topControl = voTabFolder;
					//partName = "(属性)";
					// 增加控件属性页内容
					setCellPropTabItem(cellPropTabItem);
					addCellPropItemControl();
					// 清空listener属性页内容
					clearItemControl(getJsListenerItem());
					comp.layout();
					if (!comp.isVisible())
						comp.setVisible(true);
					
				}
			} else if (sel instanceof DatasetGraphPart) {
				addEventPropertiesView(eventConfs, controllerClazz);
			} else {
				comp.setVisible(false);
			}
		}else if(part instanceof LFWBaseEditor){
			StructuredSelection ss = (StructuredSelection) selection;
			Object sel = ss.getFirstElement();
			if(sel instanceof LfwCanvasGraphPart){
				addEventPropertiesView(eventConfs, controllerClazz);
			}else if(sel instanceof ChartGraphPart){
				addEventPropertiesView(eventConfs, controllerClazz);
			}
		}
	}
}
