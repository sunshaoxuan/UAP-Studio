package nc.uap.lfw.build.pub.util.pdm.dbrecord;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.pub.util.pdm.CommonUtil;
import nc.uap.lfw.build.pub.util.pdm.SDPConnection;
import nc.uap.lfw.build.pub.util.pdm.SqlUtil;
import nc.uap.lfw.build.pub.util.pdm.vo.Table;
import nc.uap.lfw.build.pub.util.pdm.vo.TableField;

import org.apache.commons.io.IOUtils;

/**
 * sql�ű��Ĺ�����
 * 
 * @author mazqa
 * 
 */
public class SqlExporterUtils {

	/**
	 * �÷�������ָ��where����ִ�����ݿ��ѯ�������
	 * 
	 * @param tableVo
	 * @param whereCondition
	 * @param additionalCondition
	 * @param con
	 *            connection
	 * @return ����� ��ά���������
	 * @throws SQLException
	 */
	public static List<List<String>> getTableDataByWhereCondition(Table tableVo, String whereCondition,
			String additionalCondition, SDPConnection con) throws SQLException {
		List<List<String>> tableData = new ArrayList<List<String>>();
		Statement stmt = null;
		int colCount = tableVo.getTableFields().length;

		try {
			/* ����sql��ѯ��� */
			String queryClause = getQueryClause(tableVo, whereCondition, additionalCondition);

			/* ִ��sql��ѯ, �����ѯ��� */
			stmt = con.getPhysicalConnection().createStatement();
			ResultSet rs = stmt.executeQuery(queryClause);
			while (rs.next()) {
				/* ����ǰ�����¼ */
				List<String> tableRecord = new ArrayList<String>();

				for (int i = 0; i < colCount; i++) {
					Object colValueObj = null;
					if (isTableIndexString(tableVo, i))
						colValueObj = rs.getString(tableVo.getTableFields()[i].getFieldName());
					else
						colValueObj = rs.getObject(tableVo.getTableFields()[i].getFieldName());

					String colValue = null;
					if (colValueObj != null) {
						TableField field = tableVo.getTableFields()[i];
						if (SqlUtil.isNumber(field)) {
							/* '��ֵ��'�ֶ� */
							colValue = colValueObj.toString().trim();
						}

						if (SqlUtil.isString(field)) {
							/* '�ַ���'�ֶ� */

							String colValueTemp = colValueObj.toString();
							if (!" ".equalsIgnoreCase(colValueTemp)) {
								colValueTemp = CommonUtil.replaceAllString(colValueTemp.trim(), "'", "''");
							}
							colValue = "'" + colValueTemp + "'";
						}
					} else if (tableVo.getTableFields()[i].isNullAllowed()) {
						colValue = "null";
					} else {
						/* ������Ϊ�յ��ֶ�δȡ��ֵ, �׳��쳣 */
						String msg = "������" + tableVo.getTableName() + "\r\n";
						msg += "����: " + tableVo.getTableFields()[i].getFieldName() + "\r\n";
						msg += "������Ϊ��";
						MainPlugin.getDefault().logError(msg);
						throw new SQLException(msg);
					}
					tableRecord.add(colValue);
				}// for

				tableData.add(tableRecord);
			}// while
		} catch (SQLException sql_exception) {
			throw sql_exception;
		} finally {
			/* �ر�statement */
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException sql_exception) {
				throw sql_exception;
			}
		}

		return tableData;
	}

	/**
	 * �÷�������insert���֮�����ݲ���
	 * 
	 * @param table_vo
	 * @return
	 */
	private static String getSqlBefore(Table table_vo) {
		StringBuffer sb = new StringBuffer("");
		sb.append("insert into ");
		sb.append(table_vo.getTableName());
		sb.append("(");

		for (int i = 0; i < table_vo.getTableFields().length; i++) {
			sb.append(table_vo.getTableFields()[i].getFieldName());
			sb.append(",");
		}
		String s = sb.toString();
		String sqlInsertHead = s.substring(0, s.length() - 1) + ") values(";

		return sqlInsertHead;
	}

	/**
	 * �÷�������ָ��where����������where��������ȫ���ѯsql���
	 * 
	 * @param tableVo
	 * @param treated_where_condition
	 * @param additional_where_condition
	 * @return
	 */
	private static String getQueryClause(Table tableVo, String treated_where_condition,
			String additional_where_condition) {
		StringBuffer result = new StringBuffer("");

		result.append("select ");
		for (int i = 0; i < tableVo.getTableFields().length; i++) {
			result.append(tableVo.getTableFields()[i].getFieldName());
			result.append(", ");
		}// for
		result.delete(result.length() - 2, result.length());
		result.append(" from ");
		result.append(tableVo.getTableName());

		if (treated_where_condition != null) {
			result.append(" where ( ");
			result.append(treated_where_condition);
			result.append(" )");

			if (additional_where_condition != null) {
				result.append(" and ( ");
				result.append(additional_where_condition);
				result.append(" )");
			}
		} else {
			result.append(" where ( 1 != 1 )");
		}
		return result.toString();
	}

	/**
	 * �÷������ر��ָ�����Ƿ�Ϊ�ַ�����������
	 * 
	 * @param tableVO
	 * @param columnIndex
	 * @return
	 */
	private static boolean isTableIndexString(Table tableVO, int columnIndex) {
		boolean isNum = false;

		TableField fieldVO = tableVO.getTableFields()[columnIndex];
		switch (fieldVO.getDataType()) {
		case java.sql.Types.CHAR:
		case java.sql.Types.VARCHAR:
		case java.sql.Types.LONGVARCHAR:
			isNum = true;
			break;
		default:
			isNum = false;
			break;
		}
		return isNum;
	}

	/**
	 * �÷����������������д�뵽writer�������
	 * 
	 * @param tableVo
	 *            ��VO
	 * @param writer
	 *            ������������ⲿ�ر�
	 * @param sql_delimiter
	 *            sql�ָ���
	 * @param result_set
	 *            ���ݽ����
	 * @throws IOException д������ʱ���ܷ����쳣
	 */
	public static void outputResultToWriter(Table tableVo, Writer writer, String sql_delimiter,
			List<List<String>> result_set) throws IOException {
		/* ����sql�ļ� */

		/* �������"insert"�����ʽд��sql�ű��ļ� */
		for (int i = 0; i < result_set.size(); i++) {
			List<String> record = result_set.get(i);
			StringBuffer sb = new StringBuffer("");

			for (int j = 0; j < record.size(); j++) {
				String value = (String) record.get(j);
				sb.append(value);
				sb.append(",");
			}// for

			String s = sb.toString();
			String recordStr = s.substring(0, s.length() - 1); // ɾ����β�����','
			String rowData = getSqlBefore(tableVo) + recordStr + ")" + sql_delimiter + "\r\n";
			IOUtils.write(rowData, writer);

		}// for
	}

}
