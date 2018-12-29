/**
 * 
 */
package nc.uap.lfw.perspective.action;

import java.util.List;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.RefreshNodeAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.RefMdDataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDSTreeItem;

import org.eclipse.jface.dialogs.MessageDialog;
/**
 * 刷新DataSet命令
 * @author guomq1
 *
 */
public class RefreshDsAction extends NodeAction {
	public RefreshDsAction() {
		setText(WEBPersConstants.REFRESH_DATASET);
		setToolTipText(WEBPersConstants.REFRESH_DATASET);
	}
	private LfwView widget = null;


    public void run(){
    	
    	widget = LFWPersTool.getCurrentWidget();
        Dataset ds = widget.getViewModels().getDataset(((LFWDSTreeItem)LFWPersTool.getCurrentTreeItem()).getDs().getId());
	//更新所有的refds
        if(ds instanceof MdDataset){
        	MdDataset mdds = (MdDataset)ds;
		    List refdsList = LFWConnector.getNCRefMdDataset(mdds);
			ViewModels viewModels = widget.getViewModels();
	        int flag = 0;
			
			if(refdsList != null && refdsList.size() > 0){
				for (int i = 0; i < refdsList.size(); i++) {
					RefMdDataset refmdDs = (RefMdDataset) refdsList.get(i);//从元数据获取引用数据集
					RefMdDataset refmdDsold = (RefMdDataset) viewModels.getDataset(refmdDs.getId());//元数据中和当前的ds有没有匹配的
					if(refmdDsold != null){
						//有匹配的，更新相关refnode和combodata
						if(refreshRefNode(mdds)&&refreshComboData(mdds)){
							flag =1;
							
						}
	
				}
					viewModels.addDataset(refmdDs);
				}
		}
			if(flag==1){
			MessageDialog.openInformation(null, "提示", "更新成功！");}
			
		}

		LFWPersTool.saveWidget(widget);
		// 刷新节点
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		RefreshNodeAction.refreshNode(view, LFWAMCPersTool.getTree());
	}
    @SuppressWarnings("unchecked")
    private boolean refreshRefNode(MdDataset mdds){
		List<RefNode> refnodeList = LFWConnector.getAllNCRefNode(mdds);
		
		ViewModels viewModels = widget.getViewModels();
		IRefNode[] refNodeolds = viewModels.getRefNodes();
		for (int j = 0; j < refnodeList.size(); j++) {
			RefNode refNode = refnodeList.get(j);
			boolean flag = false;

				for(int k =0;k<refNodeolds.length;k++){
					IRefNode	refNodeold = refNodeolds[k];

				while(refNodeold.equals(refNode)){flag =true;break;}
				if(flag== false)viewModels.removeRefNode(refNodeold.getId());
				}
				if(flag == false)viewModels.addRefNode(refNode);					
		}
		return true;
    }
    @SuppressWarnings("unchecked")
    private boolean refreshComboData(MdDataset mdds){

		List<ComboData> combodataList = LFWConnector.getAllNcComboData(mdds);
		ViewModels viewModels = widget.getViewModels();
		ComboData[] comboolds = viewModels.getComboDatas();

		boolean flag = false;
		for (int i = 0; i < combodataList.size(); i++) {
			ComboData combo = combodataList.get(i);
			for(int k =0;k<comboolds.length;k++){
			ComboData	comboold = comboolds[k];
			while(comboold.equals(combo)){flag =true;break;}
			if(flag== false)viewModels.removeComboData(comboold.getId());
			}
			if(flag == false)viewModels.addComboData(combo);

			
		}
		return true;
    }
}


