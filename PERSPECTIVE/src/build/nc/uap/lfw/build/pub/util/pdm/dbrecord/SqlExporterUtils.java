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
 * sql脚本的工具类
 * 
 * @author mazqa
 * 
 */
public class SqlExporterUtils {

	/**
	 * 该方法根据指定where条件执行数据库查询并结果集
	 * 
	 * @param tableVo
	 * @param whereCondition
	 * @param additionalCondition
	 * @param con
	 *            connection
	 * @return 结果集 二维结果集数据
	 * @throws SQLException
	 */
	public static List<List<String>> getTableDataByWhereCondition(Table tableVo, String whereCondition,
			String additionalCondition, SDPConnection con) throws SQLException {
		List<List<String>> tableData = new ArrayList<List<String>>();
		Statement stmt = null;
		int colCount = tableVo.getTableFields().length;

		try {
			/* 构造sql查询语句 */
			String queryClause = getQueryClause(tableVo, whereCondition, additionalCondition);

			/* 执行sql查询, 处理查询结果 */
			stmt = con.getPhysicalConnection().createStatement();
			ResultSet rs = stmt.executeQuery(queryClause);
			while (rs.next()) {
				/* 处理当前主表记录 */
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
							/* '数值型'字段 */
							colValue = colValueObj.toString().trim();
						}

						if (SqlUtil.isString(field)) {
							/* '字符型'字段 */

							String colValueTemp = colValueObj.toString();
							if (!" ".equalsIgnoreCase(colValueTemp)) {
								colValueTemp = CommonUtil.replaceAllString(colValueTemp.trim(), "'", "''");
							}
							colValue = "'" + colValueTemp + "'";
						}
					} else if (tableVo.getTableFields()[i].isNullAllowed()) {
						colValue = "null";
					} else {
						/* 不允许为空的字段未取到值, 抛出异常 */
						String msg = "表名：" + tableVo.getTableName() + "\r\n";
						msg += "列名: " + tableVo.getTableFields()[i].getFieldName() + "\r\n";
						msg += "不允许为空";
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
			/* 关闭statement */
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
	 * 该方法生成insert语句之非数据部分
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
	 * 该方法根据指定where条件、附加where条件构造全表查询sql语句
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
	 * 该方法返回表的指定列是否为字符型数据类型
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
	 * 该方法将结果集的数据写入到writer输出流中
	 * 
	 * @param tableVo
	 *            表VO
	 * @param writer
	 *            输出流，须在外部关闭
	 * @param sql_delimiter
	 *            sql分隔符
	 * @param result_set
	 *            数据结果集
	 * @throws IOException 写入数据时可能发生异常
	 */
	public static void outputResultToWriter(Table tableVo, Writer writer, String sql_delimiter,
			List<List<String>> result_set) throws IOException {
		/* 创建sql文件 */

		/* 将结果以"insert"语句形式写入sql脚本文件 */
		for (int i = 0; i < result_set.size(); i++) {
			List<String> record = result_set.get(i);
			StringBuffer sb = new StringBuffer("");

			for (int j = 0; j < record.size(); j++) {
				String value = (String) record.get(j);
				sb.append(value);
				sb.append(",");
			}// for

			String s = sb.toString();
			String recordStr = s.substring(0, s.length() - 1); // 删除结尾多余的','
			String rowData = getSqlBefore(tableVo) + recordStr + ")" + sql_delimiter + "\r\n";
			IOUtils.write(rowData, writer);

		}// for
	}

}
