package nc.uap.lfw.perspective.action;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LFWUtility;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.common.CompIdGenerator;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.MatchField;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.listener.FileUtil;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.webcomponent.LFWRefNodeTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.refnode.core.RefNodesFromPoolDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * 根据FieldRelation 生成参照
 * @author zhangxya
 *
 */
public class GeneratorRefNodeAction extends Action {
	
	private DatasetElementObj dsobj;
	private Dataset refds;
	public GeneratorRefNodeAction(DatasetElementObj dsobj, Dataset refds) {
		super(M_perspective.GeneratorRefNodeAction_0, PaletteImage.getCreateDsImgDescriptor());
		this.dsobj = dsobj;
		this.refds = refds;
	}
	
	public void run() {
		
		RefNodesFromPoolDialog pubDialog = new RefNodesFromPoolDialog(null, M_perspective.GeneratorRefNodeAction_1);
		LFWSeparateTreeItem lfwSeparaTreeItem = DataSetEditor.getActiveEditor().getWebSeparateTreeItem(WEBPersConstants.REFNODE);
		LfwView widget = LFWPersTool.getCurrentWidget();
		ViewModels viewModels = widget.getViewModels();
		
		if(pubDialog.open() == IDialogConstants.OK_ID){
			Dataset ds = dsobj.getDs();
			if(pubDialog.getSelectedRefNode() instanceof RefNode){
				RefNode pubRefNode = (RefNode)pubDialog.getSelectedRefNode();
				FieldRelation fieldRelation = null;
				FieldRelation[] frs = ds.getFieldRelations().getFieldRelations();
				for (int i = 0; i < frs.length; i++) {
					FieldRelation fr = frs[i];
					if(fr.getRefDatasetid().equals(refds.getId())){
						fieldRelation = fr;
						break;
					}
				}
				String refNodeId = CompIdGenerator.generateRefCompId(ds.getId(), fieldRelation.getWhereField().getValue());
				if(viewModels.getRefNode(refNodeId) != null){
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), M_perspective.GeneratorRefNodeAction_2, M_perspective.GeneratorRefNodeAction_3 + refNodeId + M_perspective.GeneratorRefNodeAction_4);
					return;
				}
				
				RefNode refnode = new RefNode();
				refnode.setId(refNodeId);
				refnode.setRefId(pubRefNode.getId());
				
				MatchField[] matchs = fieldRelation.getMatchFields();
				String readFields = ""; //$NON-NLS-1$
				String writeFields = ""; //$NON-NLS-1$
				int size = matchs.length;
				for (int j = 0; j < size; j++) {
					MatchField match = matchs[j];
					String readField = match.getReadField();
					String writeField = match.getWriteField();
					if(j != size - 1){
						readFields += readField + ","; //$NON-NLS-1$
						writeFields += writeField;
					}
					else{
						readFields += readField;
						writeFields += writeField;
					}
				}
				refnode.setWriteFields(writeFields);
				refnode.setReadFields(readFields);
//				refnode.setReadDs("masterDs");
				refnode.setWriteDs(ds.getId());
				viewModels.addRefNode(refnode);
				new LFWRefNodeTreeItem(lfwSeparaTreeItem, refnode, WEBPersConstants.COMPONENT_REFNODE);
				LFWPersTool.saveWidget(widget);
			}else{
				throw new LfwRuntimeException(M_perspective.GeneratorRefNodeAction_5);
			}
		}
		else if(pubDialog.isCreateNewRef()){
			Dataset ds = dsobj.getDs();
			RefTypeSelDialog dialog = new RefTypeSelDialog(null, M_perspective.GeneratorRefNodeAction_6, refds);
			if(dialog.open() == IDialogConstants.OK_ID){
				String refType = dialog.getRefType();
				//产生代码
				createClassForRefNode(dialog);
				
				FieldRelation fieldRelation = null;
				FieldRelation[] frs = ds.getFieldRelations().getFieldRelations();
				for (int i = 0; i < frs.length; i++) {
					FieldRelation fr = frs[i];
					if(fr.getRefDatasetid().equals(refds.getId())){
						fieldRelation = fr;
						break;
					}
				}
				if(fieldRelation != null){
					String refKey = fieldRelation.getWhereField().getValue();
					String refNodeId = CompIdGenerator.generateRefCompId(ds.getId(), refKey);
					
					RefNode refnode = new RefNode();
					refnode.setId(refNodeId);
					boolean toPublic = dialog.isToPublic();
					if(toPublic){
						String refId = generatePublicRefNode(dialog);
						if(refId == null || refId.equals("")){ //$NON-NLS-1$
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), M_perspective.GeneratorRefNodeAction_2, M_perspective.GeneratorRefNodeAction_7);
							return;
						}
						refnode.setRefId(refId);
					}
					else{
//						refnode.setRefModel(dialog.getModelClass());
						refnode.setPagemeta("reference"); //$NON-NLS-1$
						refnode.setReadDs("masterDs"); //$NON-NLS-1$
						
//						if(refType.equals(RefTypeSelDialog.GRIDTYPE))
//							refnode.setPath("reference/refgrid.jsp");
//						else if(refType.equals(RefTypeSelDialog.TREETYPE))
//							refnode.setPath("reference/reftree.jsp");
					}
					if(viewModels.getRefNode(refNodeId) != null)
						return;
				
					MatchField[] matchs = fieldRelation.getMatchFields();
					String readFields = ""; //$NON-NLS-1$
					String writeFields = ""; //$NON-NLS-1$
					int size = matchs.length;
					for (int j = 0; j < size; j++) {
						MatchField match = matchs[j];
						String readField = match.getReadField();
						String writeField = match.getWriteField();
						if(j != size - 1){
							readFields += readField + ","; //$NON-NLS-1$
							writeFields += writeField;
						}
						else{
							readFields += readField;
							writeFields += writeField;
						}
					}
					refnode.setWriteFields(writeFields);
					refnode.setReadFields(readFields);
					refnode.setWriteDs(ds.getId());
					viewModels.addRefNode(refnode);
					new LFWRefNodeTreeItem(lfwSeparaTreeItem, refnode, WEBPersConstants.COMPONENT_REFNODE);
					LFWPersTool.saveWidget(widget);
				}
			}
		}
	}

	private String generatePublicRefNode(RefTypeSelDialog dialog) {
		RefNode refnode = new RefNode();
		refnode.setId(dialog.getPubRefId());
		refnode.setPagemeta("reference"); //$NON-NLS-1$
		//判断如果存在
//		if(false){
//			boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "确认", "公共参照已经存在，是否要覆盖?");
//			if(result == false)
//				return null;
//		}
//		refnode.setRefModel(dialog.getModelClass());
		refnode.setReadDs("masterDs"); //$NON-NLS-1$
		refnode.setPageModel("nc.uap.lfw.reference.model.DefaultReferencePageModel"); //$NON-NLS-1$
		refnode.setDataListener("nc.uap.lfw.reference.processor.ReferenceDatasetListener"); //$NON-NLS-1$
//		String refType = dialog.getRefType();
//		if(refType.equals(RefTypeSelDialog.GRIDTYPE))
//			refnode.setPath("reference/refgrid.jsp");
//		else if(refType.equals(RefTypeSelDialog.TREETYPE))
//			refnode.setPath("reference/reftree.jsp");
		
		String projectPath = LFWPersTool.getProjectPath();
		String pubRefId = dialog.getPubRefId();
		String dir = pubRefId.substring(0, pubRefId.lastIndexOf(".")).replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String file = pubRefId.substring(pubRefId.lastIndexOf(".") + 1); //$NON-NLS-1$
		String filePath = projectPath + "/web/pagemeta/public/refnodes/" + dir; //$NON-NLS-1$
		
		String fileName = file + ".xml"; //$NON-NLS-1$
		
		String rootPath = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
//		LFWConnector.saveRefNodetoXml("/" + rootPath, filePath, fileName, refnode);
		//refreshTreeItem(refnode);
		return pubRefId;
	}

	private void createClassForRefNode(RefTypeSelDialog dialog) {
		String refType = dialog.getRefType();
		String modelClass = dialog.getModelClass();
		String tableName = dialog.getTableName();
		String refCode = dialog.getRefCode();
		String refPk = dialog.getRefPk();
		String refName = dialog.getRefName();
		String visibleFields = dialog.getVisibleFields();
		String pfield = dialog.getPFieldValue();
		String childfield = dialog.getChildFieldValue();
		String sourceFolder = dialog.getRootPath();
		
		// 保存Java文件
		String javaData = LFWConnector.generateRefNodeClass(refType, modelClass, tableName, refPk, refCode, refName, visibleFields, pfield, childfield);
		String packageName = modelClass.substring(0, modelClass.lastIndexOf(".")); //$NON-NLS-1$
		String className = modelClass.substring(modelClass.lastIndexOf(".") + 1); //$NON-NLS-1$
		String folderPath = LFWPersTool.getProjectPath() + "/" +  sourceFolder + "/" + packageName.replace('.', '/'); //$NON-NLS-1$ //$NON-NLS-2$
		FileUtil.saveToFile(folderPath, className + ".java", javaData); //$NON-NLS-1$
	}
}