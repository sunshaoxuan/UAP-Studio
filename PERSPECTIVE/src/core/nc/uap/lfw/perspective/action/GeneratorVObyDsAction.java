package nc.uap.lfw.perspective.action;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.listener.FileUtil;
import nc.uap.lfw.perspective.model.DatasetElementObj;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * 根据数据集生成VO
 * @author zhangxya
 *
 */
public class GeneratorVObyDsAction extends Action{
	private DatasetElementObj dsobj = null;
	public GeneratorVObyDsAction(DatasetElementObj dsobj) {
		super("根据数据集生成VO",PaletteImage.getCreateDsImgDescriptor());
		this.dsobj = dsobj;
	}

	public void run() {
		GeneratorCodeDialog dialog = new GeneratorCodeDialog(new Shell(), "根据数据集生成VO");
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID){
			Dataset ds = dsobj.getDs();
			String rootPath = dialog.getRootPath();
			String tableName = dialog.getTableName();
			String fullClassName = ds.getVoMeta();
			if(fullClassName == null || fullClassName.equals("")){
				MessageDialog.openError(null, "错误提示", "请设置数据集的voMeta!");
				return;
			}
			String primaryKey = null;
			Field[] fields = ds.getFieldSet().getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if(field.isPrimaryKey() && (field.getField() != null && !field.getField().equals("")))
					primaryKey = field.getField();
			}
			if(primaryKey == null){
				MessageDialog.openError(null, "错误提示", "请设置数据集的主键字段!");
				return;
			}
			String javaData = LFWConnector.generatorVO(fullClassName, tableName, primaryKey, ds);
			String folderPath = LFWPersTool.getProjectPath() + "/" +  rootPath + "/" + fullClassName.substring(0, fullClassName.lastIndexOf(".")).replace('.', '/');
			FileUtil.saveToFile(folderPath, fullClassName.substring(fullClassName.lastIndexOf(".") + 1) + ".java", javaData);
			boolean isSql = dialog.getIsSql();
			if(isSql)
				generateTable();
		}
		else if(result == IDialogConstants.CANCEL_ID)
			return;
		}
	
	private void generateTable(){
		
	}
}


