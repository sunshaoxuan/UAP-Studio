package nc.uap.ctrl.tpl.qry.meta;


import java.util.ArrayList;
import java.util.List;

import nc.uap.ctrl.tpl.qry.operator.IOperatorConstants;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.lang.UFBoolean;
import uap.lfw.core.ml.LfwResBundle;

public class ConditionVO extends nc.vo.pub.ValueObject implements
        IQueryConstants {

    public final static String SEPARATOR_INNER = ":"; //冒号
    public final static String SEPARATOR_OUTTER = ";"; //分号

    private boolean logic = true; //各个条件之间的与或关系,缺省为‘与’.
    private boolean noLeft = true; //有无左括号,缺省为‘无’.
    private boolean noRight = true; //有无右括号,缺省为‘无’.
    private UFBoolean ifDesc;
    private UFBoolean ifSysFunc; //是否系统函数,固定值条件设置要使用

    private String tableCode; //表编码
    private String tableName; //表名称
    private String fieldCode; //字段编码(带表名)
    private String fieldName; //字段名称
    private String operaCode; //操作符编码, 是否应统一定义操作符编码?
    private String operaName; //操作符名称,固定值条件设置要使用
    private String value; //数据取值
    private String comboType = COMBO_NULL;//下拉框返回类型
    private RefResultVO refResult; //参照结果,refCode, refName, refPk, object

    private int dataType; //数据类型
    private int orderSequence; //排列顺序
    private int comboIndex; //下拉框的选中的index
    
    private boolean fixCondition = false;

    private static final long serialVersionUID = -6061741910913318960L;

    public final String DISP_AND = 
                    LfwResBundle.getInstance().getStrByID(
                            "_Template", "UPP_Template-000515")/* @res "并且" */;
    public final String DISP_OR = 
                    LfwResBundle.getInstance().getStrByID(
                            "_Template", "UPP_Template-000516")/* @res "或者" */;

    /**
     * ConditionVO 构造子注解.
     */
    public ConditionVO() {
        super();
    }
    /**
     * ConditionVO 构造子,直接从查询模版VO生成一个查询条件VO. 注意:不包括操作符operaCode和取值Value
     */
    public ConditionVO(QueryConditionVO queryVO) {
        super();
        setFieldCode(queryVO.getFieldCode());
        setFieldName(queryVO.getFieldName());
        setTableCode(queryVO.getTableCode());
        setTableName(queryVO.getTableName());
        setDataType(queryVO.getDataType().intValue());
        setIfDesc(queryVO.getIfDesc());
        setOrderSequence(queryVO.getOrderSequence().intValue());
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-12 14:44:08)
     * 
     * @return java.lang.String
     */
    public String getChStr() {
        return getChStr(false);
    }
    /**
     * 此处插入方法说明. 创建日期:(2002-01-24 11:40:56)
     * 
     * @return java.lang.String
     * @param conditions
     *            nc.vo.pub.query.ConditionVO[]
     */
    public String getCHStr(ConditionVO[] conditions) {
        if ((conditions == null) || (conditions.length == 0))
            return null;

        String chStr = "";

        if (conditions != null)
            for (int i = 0; i < conditions.length; i++)
                chStr += conditions[i].getChStr();
        chStr = chStr.trim();

        //去掉第一个and/or
        if (chStr.indexOf(" ") > 0)
            chStr = chStr.substring(chStr.indexOf(" "));
        else
            chStr = null;

        return chStr;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-12 14:44:08)
     * 
     * @return java.lang.String
     */
    public String getChStr(boolean isMultiTable) {
        String str = "";
        if (logic)
            str += " " + DISP_AND;
        else
            str += " " + DISP_OR;

        if (noLeft)
            str += "";
        else
            str += " (";

        if (isMultiTable) {
          str += " (" + tableName + "." + fieldName ;
//            str += " ("
//                    + nc.ui.pub.ClientEnvironment.getInstance().getModuleLang()
//                            .getString(tableName + "." + fieldName);
        }
        else {
            str += " (" + fieldName ;
//            str += " ("
//                    + nc.ui.pub.ClientEnvironment.getInstance().getModuleLang()
//                            .getString(fieldName);
        }
        if (operaCode.equalsIgnoreCase(IOperatorConstants.ISNULL)
                || operaCode.equals(IOperatorConstants.ISNULL_INCLUDE_SPACE))
            str += " "
                    + LfwResBundle.getInstance().getStrByID(
                            "_Template", "UPP_Template-000511")/* @res "为空" */
                    + ")";
        else if (operaCode.equalsIgnoreCase(IOperatorConstants.ISNOTNULL)
                || operaCode.equals(IOperatorConstants.ISNOTNULL_INCLUDE_SPACE))
            str += " "
                    + LfwResBundle.getInstance().getStrByID(
                            "_Template", "UPP_Template-000512")/* @res "不为空" */
                    + ")";
        else {
            str += " " + operaName;
            if (operaCode.trim().equalsIgnoreCase("in"))
                str += " (" + value + "))";

            else if ((dataType == INTEGER) || (dataType == DECIMAL)
                    || (dataType == USERCOMBO && comboType.equals(COMBO_INDEX)))
                str += " " + value + ")";
            else
                str += " '" + value + "')";
        }
        if (noRight)
            str += "";
        else
            str += " )";

        return str;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-10-17 11:17:21)
     * 
     * @return int
     */
    public int getComboIndex() {
        return comboIndex;
    }
    /**
     * 此处插入方法说明. 创建日期:(2003-11-03 13:04:27)
     * 
     * @return int
     */
    public String getComboType() {
        return comboType;
    }
    /**
     * 创建日期:(2001-4-25 16:45:19)
     * 
     * @return int
     */
    public int getDataType() {
        return dataType;
    }
    /**
     * 返回数值对象的显示名称.
     * 
     * 创建日期:(2001-2-15 14:18:08)
     * 
     * @return java.lang.String 返回数值对象的显示名称.
     */
    public String getEntityName() {
        return null;
    }
    /**
     * 创建日期:(2001-4-25 16:07:15)
     * 
     * @return int
     */
    public String getFieldCode() {
        return fieldCode;
    }
    /**
     * 创建日期:(2001-5-21 15:53:09)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFieldName() {
        return fieldName;
    }
    /**
     * @return String
     */
    public String getFieldNoT() {
        if (fieldCode != null && fieldCode.length() != 0) {
            int index = fieldCode.indexOf(".");
            String field = null;
            if (index >= 0)
                field = fieldCode.substring(index + 1, fieldCode.length());
            else
                field = fieldCode;
            return field;
        }
        else
            return null;

    }
    /**
     * 属性ifDesc的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfDesc() {
        return ifDesc;
    }
    /**
     * 属性ifSysFunc的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfSysFunc() {
        return ifSysFunc;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-5 12:55:07)
     * 
     * @return boolean
     */
    public boolean getLogic() {
        return logic;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-5 12:55:07)
     * 
     * @return boolean
     */
    public boolean getNoLeft() {
        return noLeft;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-5 12:55:07)
     * 
     * @return boolean
     */
    public boolean getNoRight() {
        return noRight;
    }
    /**
     * 创建日期:(2001-4-25 16:45:08)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOperaCode() {
        return operaCode;
    }
    /**
     * 创建日期:(2001-4-25 16:45:08)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOperaName() {
        return operaName;
    }
    /**
     * 创建日期:(2001-4-25 16:45:19)
     * 
     * @return int
     */
    public int getOrderSequence() {
        return orderSequence;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-8-17 9:11:07)
     * 
     * @return nc.vo.pub.query.RefResultVO
     */
    public RefResultVO getRefResult() {
        return refResult;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-12 14:44:08)
     * 
     * @return java.lang.String
     */
    public String getSQLStr() {
        return getSQLStr(false);
    }
    /**
     * 此处插入方法说明. 创建日期:(2002-01-24 11:40:56)
     * 
     * @return java.lang.String
     * @param conditions
     *            nc.vo.pub.query.ConditionVO[]
     */
    public String getSQLStr(ConditionVO[] conditions) {
        if ((conditions == null) || (conditions.length == 0))
            return null;
        return getWhereSQL(conditions);
//        String str = "";
//        for (int i = 0; i < conditions.length; i++)
//            str += conditions[i].getSQLStr();
//
//        //去掉第一个and/or
//        if (str.length() > 3) {
//            try {
//                str = str.substring(str.indexOf(" ", 1));
//            }
//            catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            str = null;
//        }
//
//        return str;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-12 14:44:08)
     * 
     * @return java.lang.String
     */
    private String getSQLStr(boolean isNullWithSpace) {
        StringBuffer str = new StringBuffer();
        if (logic)
            str.append(" and");
        else
            str.append(" or");

        if (!noLeft)
            str.append(" (");

        boolean isAllNull = false;
        boolean isAllNotNull = false;
        if (operaCode.trim().equalsIgnoreCase(IOperatorConstants.ISNULL_INCLUDE_SPACE)
                || (isNullWithSpace && operaCode.trim().equalsIgnoreCase(
                        IOperatorConstants.ISNULL)))
            isAllNull = true;
        else if (operaCode.trim().equalsIgnoreCase(IOperatorConstants.ISNOTNULL_INCLUDE_SPACE)
                || (isNullWithSpace && operaCode.trim().equalsIgnoreCase(
                		IOperatorConstants.ISNOTNULL)))
            isAllNotNull = true;
        if (isAllNull && !isDigital())
        str.append(" ((" + fieldCode + "='~') or (ltrim(rtrim("
                + fieldCode + ")) = ''))");
        else if (isAllNotNull && !isDigital())
            str.append(" ((" + fieldCode + "<>'~') and (ltrim(rtrim("
                    + fieldCode + ")) <> ''))");
        else {
            String strOpera = operaCode.trim();
            if (isAllNull){
            	strOpera = IOperatorConstants.ISNULL;
            }else if (isAllNotNull){
            	strOpera = IOperatorConstants.ISNOTNULL;
            }else if (operaCode.trim().equals(IOperatorConstants.EQ2)){
            	strOpera = IOperatorConstants.EQ;
            }

            String newValue = value;
            if (operaCode.equalsIgnoreCase("like") && value.indexOf("%") < 0)
                newValue = "%" + newValue + "%";
            if (!isDigital())
                newValue = "'" + newValue + "'";

            str.append(" (" + fieldCode + " " + strOpera);
            if ((!isNullWithSpace && (strOpera.equalsIgnoreCase(IOperatorConstants.ISNULL) || strOpera
                    .equalsIgnoreCase(IOperatorConstants.ISNOTNULL)))) {
                str.append(")");
            }
            else if (operaCode.trim().equalsIgnoreCase("in")||operaCode.trim().indexOf(" in")!=-1) {
                if (value.startsWith("(") && value.endsWith(")")) { //对于in
                                                                    // 的操作符,若value在()里面,则不用判断数据类型
                    str.append(" " + value + ")");
                }
                else {
                    str.append(" (" + newValue + "))");
                }
            }else if(operaCode.trim().equalsIgnoreCase("between")) {//对于介于操作处理;value为由,分开的字符串组成
            	
            	//between and 的处理
//            	if(value.startsWith("(") && value.endsWith(")")) {
//            		String[] values =value.substring(1, value.length()-1).split(",");
//            		str.append(" "+values[0]+" and "+ values[1] +")");
//            	}else {//若value不在()里面，则需要判断数据类型..between and 也允许字符 
//            		String[] values =value.split(",");
//            		if(isDigital()) {
//            			str.append(" "+values[0]+" and "+ values[1] +")");	
//            		}else {
//            			str.append(" '"+values[0]+"' and '"+ values[1] +"')");
//            		}
//            	}
            	//用>= 和 <= 来处理
            	String[] values = value.split(",");
            	str.delete(str.length()-7, str.length());
            	boolean firstEff = false;
            	if(!values[0].equals(IOperatorConstants.ISNULL_INCLUDE_SPACE)) {
            		firstEff = true;
            		str.append(">="+getSqlValue(values[0]));            		
            	}
            	if(!values[1].equals(IOperatorConstants.ISNULL_INCLUDE_SPACE)) {
            		if(firstEff)
            			str.append(" and "+ fieldCode +" <= "+getSqlValue(values[1]));
            		else
            			str.append(" <= "+ getSqlValue(values[1]));
            	}
            	str.append(")");
            }
            
            else //其他操作符
            {
                str.append(" " + newValue + ")");
            }
        }
        if (!noRight)
            str.append(" )");
        return str.toString();
    }
    private String getSqlValue(String value) {
    	if(!isDigital())
    		return "'"+value+"'";
    	else return value;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-12 14:44:08)
     * 
     * @return java.lang.String
     */
    public String getSQLStrForNull() {
        return getSQLStr(true);
    }
    /**
     * 创建日期:(2001-4-25 16:07:15)
     * 
     * @return int
     */
    public String getTableCodeForMultiTable() {
        return tableCode;
    }
    /**
     * @return String
     */
    public String getTableName() {
        if (fieldCode != null && fieldCode.length() != 0) {
            int index = fieldCode.indexOf(".");
            String tableName = null;
            if (index >= 0)
                tableName = fieldCode.substring(0, index);
            return tableName;
        }
        else
            return null;

    }
    /**
     * 创建日期:(2001-5-21 15:53:09)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTableNameForMultiTable() {
        return tableName;
    }
    /**
     * 创建日期:(2001-4-25 16:48:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getValue() {
        return value;
    }
    /**
     * 此处插入方法说明. 创建日期:(2002-01-24 11:40:56)
     * 
     * @return java.lang.String
     * @param conditions
     *            nc.vo.pub.query.ConditionVO[]
     */
    public String getWhereSQL(ConditionVO[] conditions) {
		if ((conditions == null) || (conditions.length == 0))
            return null;
		List<ConditionVO> fixConditions = new ArrayList<ConditionVO>();
		List<ConditionVO> customConditions = new ArrayList<ConditionVO>();
		for (ConditionVO vo : conditions)
		{
			if(vo.isFixCondition())
			{
				fixConditions.add(vo);
			}
			else
			{
				customConditions.add(vo);
			}
		}
    	if(!fixConditions.isEmpty()&&!customConditions.isEmpty())
    	{
    		return getWhereSqlHelper(fixConditions.toArray(new ConditionVO[0])) + " and ("+
    		getWhereSqlHelper(customConditions.toArray(new ConditionVO[0])) +")";
    	}
    	else if(fixConditions.isEmpty()&&!customConditions.isEmpty())
    	{
    		return getWhereSqlHelper(customConditions.toArray(new ConditionVO[0]));
    	}
    	else if(!fixConditions.isEmpty()&&customConditions.isEmpty())
    	{
    		return getWhereSqlHelper(fixConditions.toArray(new ConditionVO[0]));
    	}
    	return null;
    }
	private String getWhereSqlHelper(ConditionVO[] conditions) {
		if ((conditions == null) || (conditions.length == 0))
            return null;

        String str = "";
        for (int i = 0; i < conditions.length; i++)
            str += conditions[i].getSQLStr();

        //去掉第一个and/or
        if (str.length() > 3) {
            try {
                str = str.substring(str.indexOf(" ", 1));
            }
            catch (Throwable e) {
            	LfwLogger.error(e);
            }
        }
        else {
            str = null;
        }

        return str;
	}
    /**
     * 此处插入方法说明. 创建日期:(2004-4-7 16:38:07)
     * 
     * @return boolean
     */
    public boolean isDigital() {
        if (dataType == INTEGER || dataType == DECIMAL
                || (dataType == USERCOMBO && comboType.equals(COMBO_INDEX)))
            return true;

        return false;
    }
    /**
     * 此处插入方法说明. 创建日期:(2003-10-27 10:46:38)
     * 
     * @return nc.vo.pub.query.ConditionVO[]
     * @param strConditions
     *            java.lang.String
     */
    public static ConditionVO[] parseString(String strConditions) {
        if (strConditions == null || strConditions.length() == 0) {
            return null;
        }
        java.util.StringTokenizer st = new java.util.StringTokenizer(
                strConditions, SEPARATOR_OUTTER, false);

        //逻辑关系
        int count = st.countTokens();
        ConditionVO[] conditions = new ConditionVO[count];
        for (int i = 0; i < count; i++) {
            conditions[i] = parseStringToOne(st.nextToken());
        }
        return conditions;
    }
    /**
     * 此处插入方法说明. 创建日期:(2003-10-27 10:53:38)
     * 
     * @return nc.vo.pub.query.ConditionVO
     * @param strCon
     *            java.lang.String
     */
    private static ConditionVO parseStringToOne(String describer) {
        if (describer == null || describer.length() == 0) {
            return null;
        }
        try {
            ConditionVO condition = new ConditionVO();
            java.util.StringTokenizer st = new java.util.StringTokenizer(
                    describer, SEPARATOR_INNER, false);

            //逻辑关系
            String boolVal = st.nextToken();
            condition.setLogic(boolVal.equals("Y"));
            //有无左括号
            boolVal = st.nextToken();
            condition.setNoLeft(boolVal.equals("Y"));
            //有无右括号
            boolVal = st.nextToken();
            condition.setNoRight(boolVal.equals("Y"));

            //是否降序
            boolVal = st.nextToken();
            condition.setIfDesc(UFBoolean.valueOf((boolVal.equals("Y"))));
            //是否系统函数
            boolVal = st.nextToken();
            condition.setIfSysFunc(UFBoolean.valueOf(boolVal.equals("Y")));

            //表编码
            String str = st.nextToken();
            condition.setTableCode(str.equals("null") ? null : str);
            //表名称
            str = st.nextToken();
            condition.setTableName(str.equals("null") ? null : str);
            //字段编码(带表名)
            str = st.nextToken();
            condition.setFieldCode(str.equals("null") ? null : str);
            //字段名称
            str = st.nextToken();
            condition.setFieldName(str.equals("null") ? null : str);
            //操作符编码
            str = st.nextToken();
            condition.setOperaCode(str.equals("null") ? null : str);
            //操作符名称
            str = st.nextToken();
            condition.setOperaName(str.equals("null") ? null : str);
            //数据取值
            str = st.nextToken();
            condition.setValue(str.equals("null") ? null : str);
            //数据取值
            str = st.nextToken();
            condition.setComboType(str.equals("null") ? COMBO_NULL : str);

            //参照结果
            RefResultVO ref = new RefResultVO();
            str = st.nextToken();
            ref.setRefCode(str.equals("null") ? null : str);
            str = st.nextToken();
            ref.setRefName(str.equals("null") ? null : str);
            str = st.nextToken();
            ref.setRefPK(str.equals("null") ? null : str);
            if (ref.getRefCode() != null || ref.getRefName() != null
                    || ref.getRefPK() != null)
                condition.setRefResult(ref);

            //数据类型
            condition.setDataType(Integer.parseInt(st.nextToken()));
            //排列顺序
            condition.setOrderSequence(Integer.parseInt(st.nextToken()));
            //下拉框的选中的index
            condition.setComboIndex(Integer.parseInt(st.nextToken()));

            return condition;
        }
        catch (Exception e) {
        	LfwLogger.error(e);
            return null;
        }
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-10-17 11:16:55)
     * 
     * @param index
     *            int
     */
    public void setComboIndex(int index) {
        comboIndex = index;
    }
    /**
     * 此处插入方法说明. 创建日期:(2003-11-03 13:04:27)
     * 
     * @param newComboType
     *            int
     */
    public void setComboType(String newComboType) {
        if (newComboType == null)
            comboType = COMBO_NULL;
        else
            comboType = newComboType;
    }
    /**
     * 创建日期:(2001-4-25 16:45:19)
     * 
     * @param newDataType
     *            int
     */
    public void setDataType(int newDataType) {
        dataType = newDataType;
    }
    /**
     * 创建日期:(2001-4-25 16:07:15)
     * 
     * @param newM_fieldCode
     *            int
     */
    public void setFieldCode(String newFieldCode) {
        fieldCode = newFieldCode;
    }
    /**
     * 创建日期:(2001-5-21 15:53:09)
     * 
     * @param newFieldName
     *            java.lang.String
     */
    public void setFieldName(java.lang.String newFieldName) {
        fieldName = newFieldName;
    }
    /**
     * 属性ifDesc的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfDesc
     *            UFBoolean
     */
    public void setIfDesc(UFBoolean newIfDesc) {

        ifDesc = newIfDesc;
    }
    /**
     * 属性ifSysFunc的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfSysFunc
     *            UFBoolean
     */
    public void setIfSysFunc(UFBoolean newIfSysFunc) {

        ifSysFunc = newIfSysFunc;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-5 12:56:27)
     * 
     * @param b
     *            boolean
     */
    public void setLogic(boolean b) {
        logic = b;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-5 12:56:27)
     * 
     * @param b
     *            boolean
     */
    public void setNoLeft(boolean b) {
        noLeft = b;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-7-5 12:56:27)
     * 
     * @param b
     *            boolean
     */
    public void setNoRight(boolean b) {
        noRight = b;
    }
    /**
     * 创建日期:(2001-4-25 16:45:08)
     * 
     * @param newOpera
     *            java.lang.String
     */
    public void setOperaCode(java.lang.String newOpera) {
        operaCode = newOpera;
    }
    /**
     * 创建日期:(2001-4-25 16:45:08)
     * 
     * @param newOpera
     *            java.lang.String
     */
    public void setOperaName(java.lang.String newOpera) {
        operaName = newOpera;
    }
    /**
     * 创建日期:(2001-4-25 16:45:19)
     * 
     * @param newDataType
     *            int
     */
    public void setOrderSequence(int newOrder) {
        orderSequence = newOrder;
    }
    /**
     * 此处插入方法说明. 创建日期:(2001-8-17 9:10:08)
     * 
     * @param result
     *            nc.vo.pub.query.RefResultVO
     */
    public void setRefResult(RefResultVO result) {
        refResult = result;
    }
    /**
     * 创建日期:(2001-4-25 16:07:15)
     * 
     * @param newM_fieldCode
     *            int
     */
    public void setTableCode(String newTableCode) {
        tableCode = newTableCode;
    }
    /**
     * 创建日期:(2001-5-21 15:53:09)
     * 
     * @param newFieldName
     *            java.lang.String
     */
    public void setTableName(java.lang.String newTableName) {
        tableName = newTableName;
    }
    /**
     * 创建日期:(2001-4-25 16:48:29)
     * 
     * @param newValueLower
     *            java.lang.String
     */
    public void setValue(java.lang.String newValue) {
        value = newValue;
    }
    /**
     * 此处插入方法说明. 创建日期:(2003-10-27 10:45:53)
     * 
     * @return java.lang.String
     * @param vos
     *            nc.vo.pub.query.ConditionVO[]
     */
    public static String toStringInfo(ConditionVO[] vos) {
        if (vos == null || vos.length == 0)
            return null;
        StringBuffer sbuffer = new StringBuffer();
        for (int i = 0; i < vos.length; i++) {
            sbuffer.append(toStringOneInfo(vos[i]));
            sbuffer.append(SEPARATOR_OUTTER);
        }
        return sbuffer.toString();

    }
    /**
     * 此处插入方法说明. 创建日期:(2003-10-27 10:45:53)
     * 
     * @return java.lang.String
     * @param vos
     *            nc.vo.pub.query.ConditionVO[]
     */
    private static String toStringOneInfo(ConditionVO con) {
        StringBuffer strBuf = new StringBuffer();
        String space = SEPARATOR_INNER;

        //逻辑关系
        String boolVal = con.getLogic() ? "Y" : "N";
        strBuf.append(boolVal + space);
        //没有左括号
        boolVal = con.getNoLeft() ? "Y" : "N";
        strBuf.append(boolVal + space);
        //没有右括号
        boolVal = con.getNoRight() ? "Y" : "N";
        strBuf.append(boolVal + space);
        //是否降序
        boolVal = (con.getIfDesc() != null && con.getIfDesc().booleanValue()) ? "Y"
                : "N";
        strBuf.append(boolVal + space);
        //是否系统函数
        boolVal = (con.getIfSysFunc() != null && con.getIfSysFunc()
                .booleanValue()) ? "Y" : "N";
        strBuf.append(boolVal + space);

        //表编码,表名称,字段编码,字段名称,操作符编码,操作符名称,数据取值
        strBuf.append((con.getTableCodeForMultiTable() == null || con
                .getTableCodeForMultiTable().length() == 0) ? "null" : con
                .getTableCodeForMultiTable());
        strBuf.append(space);
        strBuf.append((con.getTableNameForMultiTable() == null || con
                .getTableNameForMultiTable().length() == 0) ? "null" : con
                .getTableNameForMultiTable());
        strBuf.append(space);
        strBuf.append((con.getFieldCode() == null || con.getFieldCode()
                .length() == 0) ? "null" : con.getFieldCode());
        strBuf.append(space);
        strBuf.append((con.getFieldName() == null || con.getFieldName()
                .length() == 0) ? "null" : con.getFieldName());
        strBuf.append(space);
        strBuf.append((con.getOperaCode() == null || con.getOperaCode()
                .length() == 0) ? "null" : con.getOperaCode());
        strBuf.append(space);
        strBuf.append((con.getOperaName() == null || con.getOperaName()
                .length() == 0) ? "null" : con.getOperaName());
        strBuf.append(space);
        strBuf
                .append((con.getValue() == null || con.getValue().length() == 0) ? "null"
                        : con.getValue());
        strBuf.append(space);
        strBuf.append((con.getComboType() == null || con.getComboType()
                .length() == 0) ? "null" : con.getComboType());
        strBuf.append(space);

        //参照结果,refCode, refName, refPk
        if (con.getRefResult() == null)
            strBuf.append("null:null:null:");
        else {
            RefResultVO ref = con.getRefResult();
            strBuf.append((ref.getRefCode() == null || ref.getRefCode()
                    .length() == 0) ? "null" : ref.getRefCode());
            strBuf.append(space);
            strBuf.append((ref.getRefName() == null || ref.getRefName()
                    .length() == 0) ? "null" : ref.getRefName());
            strBuf.append(space);
            strBuf
                    .append((ref.getRefPK() == null || ref.getRefPK().length() == 0) ? "null"
                            : ref.getRefPK());
            strBuf.append(space);
        }

        //数据类型,排列顺序,下拉框的选中的index
        strBuf.append(con.getDataType() + space + con.getOrderSequence()
                + space + con.getComboIndex() + space);

        return strBuf.toString();
    }
    /**
     * 验证对象各属性之间的数据逻辑正确性.
     * 
     * 创建日期:(2001-2-15 11:47:35)
     * 
     * @exception nc.vo.pub.ValidationException
     *                如果验证失败,抛出 ValidationException,对错误进行解释.
     */
    public void validate() throws nc.vo.pub.ValidationException {
    }
    
    /* (non-Javadoc)
     * @see nc.vo.pub.ValueObject#clone()
     */
    //应 余大英 的要求加上 
    @Override
    public Object clone() {
    	ConditionVO vo = (ConditionVO) super.clone();
    	if(vo.getRefResult()!=null)
    	{
    		vo.setRefResult((RefResultVO) vo.getRefResult().clone());
    	}
    	return vo;
    }
	public boolean isFixCondition() {
		return fixCondition;
	}
	public void setFixCondition(boolean fixCondition) {
		this.fixCondition = fixCondition;
	}
}