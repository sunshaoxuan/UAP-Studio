package nc.uap.lfw.build.pub.util.pdm.vo;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.pub.util.pdm.SqlBuildRuleUtil;
import nc.uap.lfw.build.pub.util.pdm.itf.IDB2;
import nc.uap.lfw.build.pub.util.pdm.itf.IOracle;
import nc.uap.lfw.build.pub.util.pdm.itf.ISQLServer;
import nc.uap.lfw.build.pub.util.pdm.itf.ISql;

/**
 * ��VO��, ��Ӧ��Table���ݿ����
 * 
 * @author fanp
 */
public class TableVO implements ISql, ISQLServer, IDB2, IOracle {
	private String id;
	private String name;
	private String code;
	private String comment;
	private Map<String, ColumnVO> columns;
	private KeyVO primaryKey;
	private IndexVO[] indexs;

	public TableVO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public String getCodeDef() {
		if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
			return this.code.toLowerCase();
		else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
			return this.code.toUpperCase();
		else
			return this.code;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}

	public void setPrimaryKey(KeyVO key_vo) {
		this.primaryKey = key_vo;
	}

	public KeyVO getPrimaryKey() {
		return this.primaryKey;
	}

	public void setIndexs(List<IndexVO> v) {
		if (v != null) {
			indexs = v.toArray(new IndexVO[0]);
		} else
			indexs = null;
	}

	public IndexVO[] getIndexs() {
		return this.indexs;
	}

	private static boolean isPKColumn(String column_code, ColumnVO[] pk_columns) {
		if (pk_columns == null || pk_columns.length == 0)
			return false;

		for (int i = 0; i < pk_columns.length; i++) {
			ColumnVO column = pk_columns[i];
			if (column.getCode().equalsIgnoreCase(column_code))
				return true;
		}
		return false;
	}

	public String getColumnDef() {
		StringBuffer sb = new StringBuffer();

		Set<String> keys = columns.keySet();
		for (String coulmn_id : keys) {
			
			ColumnVO columnVO = (ColumnVO) columns.get(coulmn_id);
			
			// code
			String code = columnVO.getCode();
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				sb.append(code.toLowerCase());
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				sb.append(code.toUpperCase());
			else
				sb.append(code);
			sb.append(" ");
			
			// data type
			String data_type = columnVO.getDataType();
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				sb.append(data_type.toLowerCase());
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				sb.append(data_type.toUpperCase());
			sb.append(" ");
			
			// mandatary
			String nullable = columnVO.getMandatory();
			if (nullable != null && nullable.equalsIgnoreCase("1"))
				sb.append("not null");
			else
				sb.append("null");
			sb.append(" ");
			
			// default value
			String default_value = columnVO.getdefaultValue();
			// if(default_value != null && default_value.trim().length()>0)
			if (default_value != null) {
				sb.append(ISql.DDL_DEFAULT + " " + default_value);
				sb.append(" ");
			}
			sb.append("\r\n");
			
			// name
			String name = columnVO.getName();
			sb.append("/*" + name + "*/,");
			sb.append("\r\n");
		}

		return sb.toString();
	}

	public String getColumnDef_Oracle() {
		StringBuffer sb = new StringBuffer();

		Set<String> keys = columns.keySet();
		for (String coulmn_id : keys) {
			ColumnVO columnVO = (ColumnVO) columns.get(coulmn_id);

			// code
			String code = columnVO.getCode();
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				sb.append(code.toLowerCase());
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				sb.append(code.toUpperCase());
			else
				sb.append(code);
			sb.append(" ");

			// data type
			String data_type = columnVO.getDataType();
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				sb.append(data_type.toLowerCase());
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				sb.append(data_type.toUpperCase());
			sb.append(" ");

			// default value
			String default_value = columnVO.getdefaultValue();
			// if(default_value != null && default_value.trim().length()>0)
			if (default_value != null) {
				sb.append(ISql.DDL_DEFAULT + " " + default_value);
				sb.append(" ");
			}

			// mandatary
			String nullable = columnVO.getMandatory();
			if (nullable != null && nullable.equalsIgnoreCase("1"))
				sb.append("not null");
			else
				sb.append("null");
			sb.append(" ");
			sb.append("\r\n");

			// name
			String name = columnVO.getName();
			sb.append("/*" + name + "*/,");
			sb.append("\r\n");
		}

		return sb.toString();
	}

	public String getCustomizedColumnDef_SqlServer() {
		StringBuffer sb = new StringBuffer();

		sb.append(ISQLServer.DDL_SQLSERVER_TS + ",");
		sb.append("\r\n");
		// sb.append(ISQLServer.DDL_SQLSERVER_DR + ",");
		sb.append(ISQLServer.DDL_SQLSERVER_DR);
		sb.append("\r\n");

		return sb.toString();
	}

	public String getCustomizedColumnDef_DB2() {
		StringBuffer sb = new StringBuffer();

		sb.append(IDB2.DDL_DB2_TS + ",");
		sb.append("\r\n");
		// sb.append(IDB2.DDL_DB2_DR + ",");
		sb.append(IDB2.DDL_DB2_DR);
		sb.append("\r\n");

		return sb.toString();
	}

	public String getCustomizedColumnDef_Oracle() {
		StringBuffer sb = new StringBuffer();

		sb.append(IOracle.DDL_ORACLE_TS + ",");
		sb.append("\r\n");
		// sb.append(IOracle.DDL_ORACLE_DR + ",");
		sb.append(IOracle.DDL_ORACLE_DR);
		sb.append("\r\n");

		return sb.toString();
	}

	public String getPrimaryKeyDef_SqlServer() {
		if (primaryKey == null)
			return "";

		StringBuffer sb = new StringBuffer();
		sb.append(ISql.DDL_CONSTRAINT);
		sb.append(" ");
		String primaryKeyCode = null;
		if (getPrimaryKey().getConstraintName() != null && !getPrimaryKey().getConstraintName().trim().equalsIgnoreCase("")) {
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				primaryKeyCode = getPrimaryKey().getConstraintName().trim().toLowerCase();
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				primaryKeyCode = getPrimaryKey().getConstraintName().trim().toUpperCase();
		} else {
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				primaryKeyCode = ("PK_" + getCode()).toLowerCase();
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				primaryKeyCode = ("PK_" + getCode()).toUpperCase();
		}
		sb.append(primaryKeyCode);
		sb.append(" ");
		sb.append(ISql.DDL_PRIMARY_KEY);
		sb.append(" ");
		sb.append("(");
		if (primaryKey.getColumnDefs() != null) {
			ColumnVO[] columnVOs = primaryKey.getColumnDefs();
			for (int i = 0; i < columnVOs.length; i++) {
				String column_code = columnVOs[i].getCode();
				sb.append(column_code);
				sb.append(",");
			}
			sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(",")));
		}
		sb.append(")");
		sb.append(",");
		sb.append("\r\n");
		return sb.toString();
	}

	public String getPrimaryKeyDef_DB2() {
		if (primaryKey == null)
			return "";

		StringBuffer sb = new StringBuffer();
		sb.append(ISql.DDL_CONSTRAINT);
		sb.append(" ");

		String primaryKeyCode = null;
		if (getPrimaryKey().getConstraintName() != null && !getPrimaryKey().getConstraintName().trim().equalsIgnoreCase("")) {
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				primaryKeyCode = getPrimaryKey().getConstraintName().trim().toLowerCase();
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				primaryKeyCode = getPrimaryKey().getConstraintName().trim().toUpperCase();
		} else {
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				primaryKeyCode = ("PK_" + getCode()).toLowerCase();
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				primaryKeyCode = ("PK_" + getCode()).toUpperCase();
		}
		sb.append(primaryKeyCode);
		sb.append(" ");
		sb.append(ISql.DDL_PRIMARY_KEY);
		sb.append(" ");
		sb.append("(");
		if (primaryKey.getColumnDefs() != null) {
			ColumnVO[] columnVOs = primaryKey.getColumnDefs();
			for (int i = 0; i < columnVOs.length; i++) {
				String column_code = columnVOs[i].getCode();
				sb.append(column_code);
				sb.append(",");
			}
			sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(",")));
		}
		sb.append(")");
		sb.append(",");
		sb.append("\r\n");
		return sb.toString();
	}

	public String getPrimaryKeyDef_Oracle() {
		if (primaryKey == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(ISql.DDL_CONSTRAINT);
		sb.append(" ");
		String primaryKeyCode = null;
		if (getPrimaryKey().getConstraintName() != null && !getPrimaryKey().getConstraintName().trim().equalsIgnoreCase("")) {
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				primaryKeyCode = getPrimaryKey().getConstraintName().trim().toLowerCase();
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				primaryKeyCode = getPrimaryKey().getConstraintName().trim().toUpperCase();
		} else {
			if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_LOWERCASE)
				primaryKeyCode = ("PK_" + getCode()).toLowerCase();
			else if (SqlBuildRuleUtil.isScriptSensitive() == ISql.SCRIPT_UPPERCASE)
				primaryKeyCode = ("PK_" + getCode()).toUpperCase();
		}
		sb.append(primaryKeyCode);
		sb.append(" ");
		sb.append(ISql.DDL_PRIMARY_KEY);
		sb.append(" ");
		sb.append("(");
		if (primaryKey.getColumnDefs() != null) {
			ColumnVO[] columnVOs = primaryKey.getColumnDefs();
			for (int i = 0; i < columnVOs.length; i++) {
				String column_code = columnVOs[i].getCode();
				sb.append(column_code);
				sb.append(",");
			}
			sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(",")));
		}
		sb.append(")");
		sb.append(",");
		sb.append("\r\n");
		return sb.toString();
	}

	public void toHtml(String html_file) {
		StringBuffer table_declare = new StringBuffer();
		List<ColumnVO> alColumnComment = new ArrayList<ColumnVO>();
		BufferedWriter out = null;

		/*
		 * ################################## �����ʽ
		 * ###################################### <html> <head> <meta
		 * http-equiv="Content-Type" content="text/html; charset=GB2312">
		 * <title>%nc.vo.sdp.utility.parser.TableVO::getName%</title> <link
		 * href="nc_dd.css" type="text/css" rel="stylesheet"> </head>
		 * 
		 * <body>
		 * 
		 * <h3>��: test</h3>
		 * 
		 * <h3>ע��</h3> <table class="Grid" border="1" cellspacing="1"
		 * width="100%"> <tr> <td class="Header"><p align="left">��������,
		 * ����...</p></td> </tr> </table> <h3>�ֶ��嵥</h3> <table class="Grid"
		 * border="1" cellspacing="1" width="100%"> <tr> <td class="Header"><p
		 * align="center">����</p></td> <td class="Header"><p
		 * align="center">����</p></td> <td class="Header"><p
		 * align="center">�Ƿ�����</p></td> <td class="Header"><p
		 * align="center">����</p></td> <td class="Header"><p
		 * align="center">�Ƿ�Ϊ��</p></td> <td class="Header"><p
		 * align="center">Ĭ��ֵ</p></td> </tr> <tr> <td><p
		 * align="left">����ID</p></td> <td><p align="left">test_id</p></td>
		 * <td><p align="left">Y</p></td> <td><p align="left">char(20)</p></td>
		 * <td><p align="left">N</p></td> <td><p align="left"></p></td> </tr>
		 * </table>
		 * 
		 * <h3>�ؼ��ֶ�ҵ����</h3>
		 * 
		 * <table class="Grid" border="1" cellspacing="1" width="100%"> <tr> <td
		 * class="Header"><p align="center">����</p></td> <td class="Header"><p
		 * align="center">����</p></td> <td class="Header"><p
		 * align="center">����</p></td> </tr> <tr> <td><p
		 * align="left">����ID</p></td> <td><p align="left">test_id</p></td>
		 * <td><p align="left">�������ֶ�, ֵ��ƽ̨ID�ŷ������Զ�����</p></td> </tr> </table>
		 * 
		 * <h3>�����嵥</h3> <table class="Grid" border="1" cellspacing="1"
		 * width="100%"> <tr> <td class="Header"><p align="center">Ψһ��ʶ</p></td>
		 * <td class="Header"><p align="center">�Ƿ�Unique����</p></td> </tr> <tr>
		 * <td><p align="left">i_000008926</p></td> <td><p
		 * align="left">Y</p></td> </tr> </table> </body> </html>
		 * ###############
		 * #######################################################
		 * ###############
		 */
		table_declare.append("<html>");
		table_declare.append("<head>");
		table_declare.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\">");
		table_declare.append("<title>" + getName() + "</title>");
		table_declare.append("<link href=\"nc_dd.css\" type=\"text/css\" rel=\"stylesheet\">");
		table_declare.append("</head>");
		table_declare.append("<body>");

		table_declare.append("<h3>��: " + getName() + "</h3>");

		// ��ע��
		if (getComment() != null && !getComment().trim().equalsIgnoreCase("")) {
			table_declare.append("<h3>ע��</h3>");
			table_declare.append("<table class=\"Grid\" border=\"1\" cellspacing=\"1\" width=\"100%\">");
			table_declare.append("<tr>");
			table_declare.append("<td><p align=\"left\">" + getComment() + "</p></td>");
			table_declare.append("</tr>");
			table_declare.append("</table>");
		}

		// ����
		table_declare.append("<h3>�ֶ��嵥</h3>");
		table_declare.append("<table class=\"Grid\" border=\"1\" cellspacing=\"1\" width=\"100%\">");
		table_declare.append("<tr>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">����</p></td>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">����</p></td>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">�Ƿ�����</p></td>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">����</p></td>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">�Ƿ�Ϊ��</p></td>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">Ĭ��ֵ</p></td>");
		table_declare.append("</tr>");

		Map<String, ColumnVO> htColumns = getColumns();
		Set<String> e = htColumns.keySet();
		for (String column_id : e) {
			ColumnVO column_vo = (ColumnVO) htColumns.get(column_id);

			table_declare.append("<tr>");
			table_declare.append("<td><p align=\"left\">" + column_vo.getName() + "</p></td>");
			table_declare.append("<td><p align=\"left\">" + column_vo.getCode() + "</p></td>");

			if (getPrimaryKey() == null)
				table_declare.append("<td><p align=\"left\"></p></td>");
			else {
				if (isPKColumn(column_vo.getCode(), getPrimaryKey().getColumnDefs()))
					table_declare.append("<td><p align=\"left\">����</p></td>");
				else
					table_declare.append("<td><p align=\"left\"></p></td>");
			}

			table_declare.append("<td><p align=\"left\">" + column_vo.getDataType() + "</p></td>");
			if (column_vo.getMandatory() != null) {
				if (column_vo.getMandatory().equalsIgnoreCase("1"))
					table_declare.append("<td><p align=\"left\">�ǿ�</p></td>");
				else
					table_declare.append("<td><p align=\"left\"></p></td>");
			} else {
				table_declare.append("<td><p align=\"left\"></p></td>");
			}
			table_declare.append("<td><p align=\"left\">" + (column_vo.getdefaultValue() == null ? "" : column_vo.getdefaultValue()) + "</p></td>");
			table_declare.append("</tr>");

			// �Ƿ����"ҵ����"
			if (column_vo.getComment() != null || column_vo.getDescription() != null)
				alColumnComment.add(column_vo);
		}
			
		table_declare.append("</table>");

		// �ؼ��ֶ�ҵ�����б�
		table_declare.append("<h3>�ؼ��ֶ�ҵ����</h3>");
		table_declare.append("<table class=\"Grid\" border=\"1\" cellspacing=\"1\" width=\"100%\">");
		table_declare.append("<tr>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">�ֶ�����</p></td>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">�ֶδ���</p></td>");
		table_declare.append("<td class=\"Header\"><p align=\"center\">����</p></td>");
		table_declare.append("</tr>");

		Object[] objs = alColumnComment.toArray();
		for (int j = 0; j < objs.length; j++) {
			table_declare.append("<tr>");
			table_declare.append("<td><p align=\"left\">" + ((ColumnVO) objs[j]).getName() + "</p></td>");
			table_declare.append("<td><p align=\"left\">" + ((ColumnVO) objs[j]).getCode() + "</p></td>");
			table_declare.append("<td><p align=\"left\">" + ((ColumnVO) objs[j]).getComment() + "</p></td>");
			table_declare.append("</tr>");
		}// for
		table_declare.append("</table>");

		// �����б�
		IndexVO[] index_vos = getIndexs();
		if (index_vos != null && index_vos.length > 0) {
			table_declare.append("<h3>�����嵥</h3>");

			for (int p = 0; p < index_vos.length; p++) {
				IndexVO index = index_vos[p];
				ColumnVO[] index_columns = index.getColumnDefs();

				if (index_columns != null && index_columns.length > 0) {
					table_declare.append("<table class=\"Grid\" border=\"1\" cellspacing=\"1\" width=\"100%\">");

					// �����������"������"
					table_declare.append("<tr>");
					table_declare.append("<td class=\"Header\"><p align=\"center\">����</p></td>");
					table_declare.append("<td class=\"Header\"><p align=\"center\">�Ƿ�Unique����</p></td>");

					for (int q = 0; q < index_columns.length; q++) {
						table_declare.append("<td class=\"Header\"><p align=\"center\">������[" + (q + 1) + "]</p></td>");
					}
					table_declare.append("</tr>");

					// �����������"������"
					table_declare.append("<tr>");
					table_declare.append("<td><p align=\"left\">" + /*
																	 * index.getId
																	 * ()
																	 */index.getCode() + "</p></td>");
					if (index.isUnique())
						table_declare.append("<td><p align=\"left\">" + "Unique" + "</p></td>");
					else
						table_declare.append("<td><p align=\"left\"></p></td>");
					for (int m = 0; m < index_columns.length; m++) {
						table_declare.append("<td><p align=\"left\">" + index_columns[m].getCode() + "</p></td>");
					}
					table_declare.append("</tr>");
					table_declare.append("</table>");
				} else {
					// ���������ڿ�����, ҳ���ӡ������ʾ
					table_declare.append("<table class=\"Grid\" border=\"1\" cellspacing=\"1\" width=\"100%\">");
					table_declare.append("<tr>");
					table_declare.append("<td><p align=\"left\">����: ���������ڿ�����</p></td>");
					table_declare.append("</tr>");
					table_declare.append("</table>");
				}
			} // for
		}

		table_declare.append("</body>");
		table_declare.append("</html>");

		/* �����봫��TableVO��Ӧ��html�ļ� */
		try {
			File tableHTMLFile = new File(html_file);
			if (tableHTMLFile.exists())
				tableHTMLFile.delete();
			tableHTMLFile.createNewFile();
			out = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(tableHTMLFile))));
			out.write(table_declare.toString());
			out.flush();
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("����htmlsĿ¼�µĶ�ӦHTML�����ļ�ʧ��",ioe);
		} finally {
			try {
				out.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError("�ر�htmlsĿ¼�µĶ�ӦHTML�����ļ������ʧ��",ioe);
			}
		}
	}

	public Map<String, ColumnVO> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, ColumnVO> columns) {
		this.columns = columns;
	}
}
