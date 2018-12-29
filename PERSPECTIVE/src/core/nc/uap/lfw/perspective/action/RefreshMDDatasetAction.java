package nc.uap.lfw.perspective.action;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.FieldRelations;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.RefMdDataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.editor.DataSetEditor;

import org.eclipse.jface.action.Action;

/**
 * 增补refnode
 * @author zhangxya
 *
 */
public class RefreshMDDatasetAction  extends Action {
	
	private MdDataset mdds;
	private LfwView widget = null;
	public RefreshMDDatasetAction(MdDataset mdds) {
		super("增补生成RefNode", PaletteImage.getCreateDsImgDescriptor());
		setText("增补生成RefNode");
		setToolTipText("增补生成RefNode");
		this.mdds = mdds;
	}

	
	public void run() {
		List<RefNode> refnodeList = LFWConnector.getAllNCRefNode(mdds);
		widget = LFWPersTool.getCurrentWidget();
		ViewModels viewModels = widget.getViewModels();
		for (int i = 0; i < refnodeList.size(); i++) {
			RefNode refNode = (RefNode) refnodeList.get(i);
			IRefNode refNodeold = viewModels.getRefNode(refNode.getId());
			if(refNodeold != null){
				viewModels.removeRefNode(refNodeold.getId());
			}
			viewModels.addRefNode(refNode);
		}
	}
	
	private void putAllRefDs(MdDataset mdds){
		FieldRelations oldFieldRelations = mdds.getFieldRelations();
		//获取所有的Fieldrelation
		List<FieldRelation> fieldRelations = LFWConnector.getNCFieldRelations(mdds);
		List<String> newfrIds = new ArrayList<String>();
		for (int i = 0; i < fieldRelations.size(); i++) {
			String frId = fieldRelations.get(i).getId();
			newfrIds.add(frId);
		}
		if(oldFieldRelations != null){
			if(oldFieldRelations.getFieldRelations() != null && oldFieldRelations.getFieldRelations().length > 0){
				FieldRelation[] oldFieldRelation = oldFieldRelations.getFieldRelations();
				//如果新的FieldRelation中不包含此旧的FieldRelations中的Id,则将原来的删除
				for (int j = 0; j < oldFieldRelation.length; j++) {
					String oldfrId = oldFieldRelation[j].getId();
					if(!newfrIds.contains(oldfrId))
						oldFieldRelations.removeFieldRelation(oldfrId);
				}
				for (int i = 0; i < fieldRelations.size(); i++) {
					FieldRelation fr = fieldRelations.get(i);
					String frId = fr.getId();
					//原来已存在此FieldRelation
					if(oldFieldRelations.getFieldRelation(frId) != null){
						continue;
					}
					//新加的FileldRelation,直接加入
					else{
						oldFieldRelations.addFieldRelation(fr);
					}
				}
			}
		}
		//如果原来没有FieldRelation,则将FieldRelation直接加入
		else{
			for (int i = 0; i < fieldRelations.size(); i++) {
				FieldRelation fr = (FieldRelation) fieldRelations.get(i);
				mdds.getFieldRelations().addFieldRelation(fr);
			}
		}
		List refdsList = LFWConnector.getNCRefMdDataset(mdds);
		widget = LFWPersTool.getCurrentWidget();
		if(widget != null){
			ViewModels viewModels = widget.getViewModels();
			if(refdsList != null && refdsList.size() > 0){
				for (int i = 0; i < refdsList.size(); i++) {
					RefMdDataset refmdDs = (RefMdDataset) refdsList.get(i);
					RefMdDataset refmdDsold = (RefMdDataset) viewModels.getDataset(refmdDs.getId());
					if(refmdDsold != null)
						viewModels.removeDataset(refmdDs.getId());
					viewModels.addDataset(refmdDs);
				}
			}
			//得到ds编辑器
			DataSetEditor dsEditor = DataSetEditor.getActiveEditor();
			//重新调整Listener位置
//			if(dsEditor != null)
//				dsEditor.repaintListenerPositon();
		}
	}

}
