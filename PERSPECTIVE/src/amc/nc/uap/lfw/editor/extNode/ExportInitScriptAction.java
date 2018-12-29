package nc.uap.lfw.editor.extNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.Workbench;

import com.yonyou.studio.common.disk.initdata.model.InitDataCfg;
import com.yonyou.studio.common.disk.initdata.model.MainTableCfg;
import com.yonyou.studio.common.disk.initdata.model.SubTableCfg;
import com.yonyou.studio.common.disk.initdata.model.TableMapping;
import com.yonyou.studio.common.disk.initdata.tablestruct.service.PluginInnerTableStructQueryService;
import com.yonyou.studio.common.disk.initdata.util.XStreamParser;
import com.yonyou.uap.studio.connection.ConnectionService;
import com.yonyou.uap.studio.connection.exception.ConnectionException;

import uap.lfw.lang.M_proj;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWMetadataTreeItem;
import nc.uap.studio.pub.db.IQueryService;
import nc.uap.studio.pub.db.IScriptService;
import nc.uap.studio.pub.db.QueryService;
import nc.uap.studio.pub.db.ScriptService;
import nc.uap.studio.pub.db.model.TableStructure;
import nc.uap.studio.pub.db.query.SqlQueryResultSet;
import nc.uap.studio.pub.db.script.export.InitDataExportStratege2;


public class ExportInitScriptAction extends NodeAction {

	
	private static final IScriptService service = new ScriptService();
	private static final IQueryService queryService = new QueryService();
	
	
	public ExportInitScriptAction() {
		super("����Ԥ�ýű�");
	}

	public void run() {
		LFWMetadataTreeItem fileItem = (LFWMetadataTreeItem) LFWPersTool.getCurrentTreeItem();
		String bcpId = LFWPersTool.getBcpId(fileItem);
		IFolder folder = LFWPersTool.getCurrentProject().getFolder(bcpId);
		IFile file = fileItem.getMdFile();
		Shell shell = Workbench.getInstance().getActiveWorkbenchWindow().getShell();
		
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setFilterPath(file.getProject()
				.getLocation().toOSString());
		String open = dialog.open();
		if(open==null){
			return;
		}
		
		final IFolder initCfgRoot = folder.getFolder(new Path(IInitDataRuleConst.ROOT_DIR));
		
		String ds = "design";
		List<InitDataInfo> confs = null;
		try {
			confs = getInitDataInfo(initCfgRoot, file);
		} catch (CoreException e3) {
			ErrorDialog.openError(
					shell,
					M_proj.ExportComponentInitDataAction_0001/*ʧ��*/,
					MessageFormat.format(M_proj.ExportComponentInitDataAction_0003/*����Ԥ����������ʧ�ܡ�*/,
							folder.getLocation()), e3.getStatus());
			return;
		}
		File rootFolder = folder.getLocation().toFile();
		rootFolder.mkdirs();
		Connection con = null;
		try {
			con = ConnectionService.getConnection(ds);
		} catch (ConnectionException e2) {
			final Status status = new Status(IStatus.ERROR,
					WEBProjPlugin.PLUGIN_ID, null, e2);
			ErrorDialog.openError(shell, M_proj.ExportComponentInitDataAction_0001/*ʧ��*/,
					MessageFormat.format(M_proj.ExportComponentInitDataAction_0004/*���ݿ�����ʧ�ܡ�*/, folder.getLocation()),
					status);
			return;
		}

		Map<String, List<String>> mlTableInfo = null;
		try {
			mlTableInfo = getMLTableInfo(ds);
		} catch (ConnectionException e1) {
			final Status status = new Status(IStatus.ERROR,
					WEBProjPlugin.PLUGIN_ID, null, e1);
			ErrorDialog.openError(shell, M_proj.ExportComponentInitDataAction_0001/*ʧ��*/,
					MessageFormat.format(M_proj.ExportComponentInitDataAction_0004/*���ݿ�����ʧ�ܡ�*/, folder.getLocation()),
					status);
			return;
		}

		try {
			for (InitDataInfo conf : confs) {
				TableStructure struct = conf.getStruct();
				Map<String, String> tableNoMap = conf.getTableNoMap();

				SqlQueryResultSet rs = null;
				try {
					rs = queryService.query(conf.getTable(),
							conf.getWhere(), struct, con);
				} catch (CoreException e) {
					WEBProjPlugin.getDefault().getLog().log(e.getStatus());
				}
				if (rs == null) {
					continue;
				}

				InitDataExportStratege2 strategy = new InitDataExportStratege2(
						new Path(open).toFile(), conf.isBusiness(),
						conf.getMapName(), conf.getStruct(), tableNoMap,
						"simpchn", mlTableInfo); 

				service.export(rs, strategy);
			}
			if(MessageDialog.openConfirm(shell, "", "����SQLԤ�ýű��ɹ���ȷ��Ҫ������Ŀ¼��")){
				LfwCommonTool.openFolder(new Path(open).toFile());
			}
		} catch (Exception e) {
//			e.printStackTrace();
			IStatus status = new Status(IStatus.ERROR, WEBProjPlugin.PLUGIN_ID,
					null, e);
			ErrorDialog.openError(shell, M_proj.ExportComponentInitDataAction_0001/*ʧ��*/,
					MessageFormat.format(M_proj.ExportComponentInitDataAction_0004/*���ݿ�����ʧ�ܡ�*/, folder.getLocation()),
					status);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
		
	}
	public List<InitDataInfo> getInitDataInfo(IFolder cfgRoot, IFile file)
			throws CoreException {
		List<InitDataInfo> cfgs = new ArrayList<InitDataInfo>();

		List<InitDataCfg> configurations = XStreamParser.getInitDataCfgs(file);
		final List<TableMapping> mappingCfgs = getMappingCfgs(cfgRoot);
		final List<TableMapping> pubMappingCfgs = getPubMappingCfgs();
		final List<MainTableCfg> mainTableCfgs = getMainTableCfgs(cfgRoot);
		final List<MainTableCfg> pubMainTableCfgs = getPubMainTableCfgs();
		for (InitDataCfg cfg : configurations) {
			final String tableName = cfg.getTableName();
			InitDataInfo conf = new InitDataInfo();
			conf.setBusiness(true);
			conf.setTable(tableName);
			conf.setWhere(cfg.getWhereCondition());

			// pubMapping
			for (TableMapping mapping : pubMappingCfgs) {
				if (mapping.getKey().equalsIgnoreCase(tableName)) {
					conf.setMapName(mapping.getValue());
					conf.setBusiness(false);
					break;
				}
			}
			if (conf.isBusiness()) {
				// mapping
				for (TableMapping mapping : mappingCfgs) {
					if (mapping.getKey().equalsIgnoreCase(tableName)) {
						conf.setMapName(mapping.getValue());
						break;
					}
				}
			}
			if (conf.getMapName() == null
					|| conf.getMapName().trim().length() == 0) {
				conf.setMapName(tableName);
			}

			// ���ӱ��������ɵ�sql�ļ���š�
			Map<String, String> tableNoMap = new HashMap<String, String>();
			for (MainTableCfg mainCfg : mainTableCfgs) {
				if (mainCfg.getTableName().equalsIgnoreCase(tableName)) {
					TableStructure struct = getTableStructByMainTableCfg(
							mainCfg, tableNoMap);
					conf.setStruct(struct);
					break;
				}
			}
			if (conf.getStruct() == null) {
				for (MainTableCfg mainCfg : pubMainTableCfgs) {
					if (mainCfg.getTableName().equalsIgnoreCase(tableName)) {
						TableStructure struct = getTableStructByMainTableCfg(
								mainCfg, tableNoMap);
						conf.setStruct(struct);
						break;
					}
				}
			}
			// ����δ�������ӱ������Ԥ���������ӱ����ã�Ĭ�ϵ������á�
			if (conf.getStruct() == null) {
				TableStructure struct = new TableStructure();
				struct.setSubTables(new ArrayList<TableStructure>());
				struct.setTable(tableName);
				tableNoMap.put(tableName.toLowerCase(), "001"); //$NON-NLS-1$
				conf.setStruct(struct);
			}
			conf.setTableNoMap(tableNoMap);
			cfgs.add(conf);
		}
		return cfgs;
	}
	private List<MainTableCfg> getMainTableCfgs(IFolder cfgRoot) {
		IFolder subTableCfgsFolder = cfgRoot
				.getFolder(IInitDataRuleConst.TABLE_CFG_DIR);
		if (subTableCfgsFolder != null && subTableCfgsFolder.exists()) {
			List<MainTableCfg> list = new ArrayList<MainTableCfg>();
			try {
				IResource[] members = subTableCfgsFolder.members();
				for (IResource member : members) {
					if (member instanceof IFile) {
						IFile file = (IFile) member;
						MainTableCfg mtCfg = XStreamParser
								.getMainTableCfg(file);
						if (mtCfg != null) {
							list.add(mtCfg);
						}
					}
				}
			} catch (CoreException e) {
				WEBProjPlugin.getDefault().getLog().log(e.getStatus());
			}
			return list;
		} else {
			return new ArrayList<MainTableCfg>();
		}
	}

	private List<TableMapping> getMappingCfgs(IFolder root) {
		IFile mappingCfgFile = root.getFile(IInitDataRuleConst.MAPPING_CFG);
		List<TableMapping> moduleMappings = new ArrayList<TableMapping>();
		Properties mappingCfgs = new Properties();
		if (mappingCfgFile != null && mappingCfgFile.exists()) {
			try {
				final InputStream input = mappingCfgFile.getContents();
				mappingCfgs.load(input);
				input.close();
				for (Object key : mappingCfgs.keySet()) {
					TableMapping tm = new TableMapping();
					tm.setKey((String) key);
					tm.setValue(mappingCfgs.getProperty((String) key));
					moduleMappings.add(tm);
				}
			} catch (IOException e) {
				WEBProjPlugin
						.getDefault()
						.getLog()
						.log(new Status(IStatus.ERROR, WEBProjPlugin.PLUGIN_ID, e
								.getMessage(), e));
			} catch (CoreException e) {
				WEBProjPlugin.getDefault().getLog().log(e.getStatus());
			}
		}
		return moduleMappings;
	}

	private List<MainTableCfg> getPubMainTableCfgs() {
		return PluginInnerTableStructQueryService.getSingleton()
				.getCommonMainTableCfgs();
	}

	private List<TableMapping> getPubMappingCfgs() {
		List<TableMapping> mappings = new ArrayList<TableMapping>();
		Properties pubMappingCfgs = PluginInnerTableStructQueryService
				.getSingleton().getCommonMapping();
		if (pubMappingCfgs != null) {
			for (Object key : pubMappingCfgs.keySet()) {
				TableMapping tm = new TableMapping();
				tm.setKey((String) key);
				tm.setValue(pubMappingCfgs.getProperty((String) key));
				mappings.add(tm);
			}
		}
		return mappings;
	}

	public static TableStructure getTableStructByMainTableCfg(
			MainTableCfg mainCfg, Map<String, String> tableNoMap) {
		TableStructure struct = new TableStructure();
		struct.setTable(mainCfg.getTableName());
		struct.setSubTables(new ArrayList<TableStructure>());
		if (tableNoMap != null) {
			tableNoMap.put(mainCfg.getTableName().toLowerCase(),
					mainCfg.getSqlNo());
		}
		if (mainCfg.getChildren() == null) {
			return struct;
		}
		for (SubTableCfg subTableCfg : mainCfg.getChildren()) {
			processSubTableStructure(struct, subTableCfg, tableNoMap);
		}
		return struct;
	}

	/**
	 * �ݹ鴦�����ӱ�
	 * 
	 * @param struct
	 * @param subTableCfg
	 * @param tableNoMap
	 */
	private static void processSubTableStructure(TableStructure parentStruct,
			SubTableCfg subTableCfg, Map<String, String> tableNoMap) {

		TableStructure struct = new TableStructure();
		struct.setSubTables(new ArrayList<TableStructure>());
		struct.setForeignKey(subTableCfg.getFkColumn());
		struct.setTable(subTableCfg.getTableName());
		if (tableNoMap != null) {
			tableNoMap.put(subTableCfg.getTableName().toLowerCase(),
					subTableCfg.getSqlNo());
		}
		if (subTableCfg.getChildren() != null) {
			for (SubTableCfg subCfg : subTableCfg.getChildren()) {
				processSubTableStructure(struct, subCfg, tableNoMap);
			}
		}
		parentStruct.getSubTables().add(struct);
	}

	/**
	 * ��ȡ�����б�
	 * 
	 * @param dsName
	 * @return mlTableInfos
	 * @throws ConnectionException
	 */
	public static Map<String, List<String>> getMLTableInfo(String dsName)
			throws ConnectionException {

		Map<String, List<String>> mlTableMetaInfos = new HashMap<String, List<String>>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select distinct m.tableid as tableName, c.name as columnName" //$NON-NLS-1$
				+ " from md_column c inner join md_ormap m on c.id = m.columnid and m.attributeid in (select id from md_property where datatype='BS000010000100001058') " //$NON-NLS-1$
				+ " order by m.tableid, c.name"; //$NON-NLS-1$
		try {
			conn = ConnectionService.getConnection(dsName);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String tableName = rs.getString(1);
				String columnName = rs.getString(2).toLowerCase();
				List<String> list = mlTableMetaInfos.get(tableName
						.toLowerCase());
				if (list == null) {
					list = new ArrayList<String>();
					mlTableMetaInfos.put(tableName.toLowerCase(), list);
				}
				if (!list.contains(columnName)) {
					list.add(columnName);
				}
			}
		} catch (SQLException e) {
			WEBProjPlugin
					.getDefault()
					.getLog()
					.log(new Status(IStatus.ERROR, WEBProjPlugin.PLUGIN_ID, e
							.getMessage(), e));
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

		return mlTableMetaInfos;
	}
}
