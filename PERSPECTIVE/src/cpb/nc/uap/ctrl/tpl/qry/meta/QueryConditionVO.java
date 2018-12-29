package nc.uap.ctrl.tpl.qry.meta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.uap.ctrl.tpl.qry.operator.IOperator;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import uap.lfw.core.ml.LfwResBundle;

public class QueryConditionVO extends SuperVO{

	private static final long serialVersionUID = 7115091676594649877L;
	private String id;
    private String pkCorp;

    private String pkTemplet;
    private String fieldCode;
    private String fieldName;
    private String operaCode;
    private String operaName;

    private UFBoolean ifDefault;
    private UFBoolean ifUsed;
    private Integer dataType;
    private String consultCode;

    private UFBoolean ifSum;
    private UFBoolean ifGroup;
    private UFBoolean ifOrder;
    private Integer orderSequence;
    private Integer dispSequence;
    private Integer dispType;
    private String dispValue;
    private UFBoolean ifAutoCheck;
    private UFBoolean ifDataPower;
    private UFBoolean ifDesc;
    private UFBoolean ifImmobility;
    private UFBoolean ifMust;
    private UFBoolean isUserDef;
    private UFBoolean isSubIncluded;// 参照是否包含下级
    private Integer maxLength;
    private Integer returnType;
    private String tableCode;
    private String tableName;
    private String value;

    private String resid;

    private UFBoolean isCondition =UFBoolean.TRUE;//是否为条件!升级前为null的应认为true!反之为逻辑条件  2007-5-25 add by huangzg
    // V6 add at 2009-07-15
    private String prerestrict;// 预置限制条件
    private String guideline;// 指标条件,为双引擎服务
    
    //used for combobox translation
    private String[] itemIDs;
    private String nodecode;
    private DefaultConstEnum[] enums;
    //用于穿透查询等用户自定义的meta ewei+
    private String instrumentedsql;
    
    private Integer dr;
    
    private UFDateTime ts;
    
    // 是否是从元数据中获取
    // 只适用于通过元数据设计的查询模板中有极特殊的自定义QueryConditionVO
    private UFBoolean isFromMetaData = UFBoolean.TRUE;
    
    // 参照类型的where条件，只适用于代码使用，不会持久化到数据库
    private String where;
    
    // fields below add for V6 composite ref
    private UFBoolean isSysFuncRefUsed;// 是否使用系统函数参照
    private UFBoolean isAttrRefUsed;// 是否使用库表属性参照
    private UFBoolean ifNotMDCondition;// 是否元数据模板的非元数据条件
    
    private Integer limit;// 数目限制
    
    //=@>@>=@<@<=@like@
    //等于@大于@大于等于@小于@小于等于@相似@
    private static transient HashMap<String,String> keymap = new HashMap<String,String>();
    static {
        keymap.put("=", "UPT_Template-eq");
        keymap.put("==", "UPT_Template-eq");
        keymap.put(">", "UPT_Template-gt");
        keymap.put(">=", "UPT_Template-ge");
        keymap.put("<", "UPT_Template-lt");
        keymap.put("<=", "UPT_Template-le");
        keymap.put("like", "UPT_Template-like");
        keymap.put("not like", "UPT_Template-notlike");
        keymap.put("<>", "UPT_Template-ne");
        keymap.put("isnull", "UPT_Template-null");
        keymap.put("is null", "UPT_Template-null");
        keymap.put("isnotnull", "UPT_Template-notnull");
        keymap.put("is not null", "UPT_Template-notnull");
        keymap.put("include", "UPT_Template-include");
        keymap.put("in", "UPT_Template-eq");
        keymap.put("not in", "UPT_Template-ne");
        keymap.put("between", "UPT_Template-between");
        keymap.put("= ic", "UPT_Template-eq");
        keymap.put("between ic", "UPT_Template-between");
        keymap.put("like ic", "UPT_Template-like");
        keymap.put("llike ic", "UPT_Template-like");
        keymap.put("rlike ic", "UPT_Template-like");
        keymap.put("== ic", "UPT_Template-eq");
        keymap.put("!= ic", "UPT_Template-ne");
        keymap.put("<> ic", "UPT_Template-ne");
        keymap.put("in ic", "UPT_Template-eq");
        keymap.put("not in ic", "UPT_Template-ne");
        keymap.put("left like", "UPT_Template-llike");
        keymap.put("right like", "UPT_Template-rlike");
    }

    /**
     * 使用主键字段进行初始化的构造子.
     * 
     * 创建日期:(2001-6-14)
     */
    public QueryConditionVO() {
        initDefaultAttrs();
    }
    /**
     * 使用主键进行初始化的构造子.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param ??fieldNameForMethod??
     *            主键值
     */
    public QueryConditionVO(String newId) {

        // 为主键字段赋值:
        id = newId;
        initDefaultAttrs();
    }
    /**
     * 根据userVO修改用户定义的属性. 创建日期:(2002-11-15 15:54:25)
     * 
     * @return nc.vo.pub.query.QueryConditionVO
     */
    public void changeUserDefineAttr(QueryConditionVO userVO) {
        setDispSequence(userVO.getDispSequence());
        setDispValue(userVO.getDispValue());
        setIfDesc(userVO.getIfDesc());
        setIfGroup(userVO.getIfGroup());
        setIfImmobility(userVO.getIfImmobility());
        setIfOrder(userVO.getIfOrder());
        setIfSum(userVO.getIfSum());
        setIfUsed(userVO.getIfUsed());
        setOrderSequence(userVO.getOrderSequence());
        setValue(userVO.getValue());

    }
    /**
     * 根类Object的方法,克隆这个VO对象.
     * 
     * 创建日期:(2001-6-14)
     */
    public Object clone() {
    	return super.clone();
    }
    /**
     * 复制用户不能更改的属性. 创建日期:(2002-11-15 15:54:25)
     * 
     * @return nc.vo.pub.query.QueryConditionVO
     */
    public QueryConditionVO cloneWithDefaultAttr() {
        QueryConditionVO newVO = new QueryConditionVO();
        newVO.setConsultCode(getConsultCode());
        newVO.setDataType(getDataType());
        newVO.setDispType(getDispType());
        newVO.setFieldCode(getFieldCode());
        newVO.setFieldName(getFieldName());
        newVO.setId(getId());
        newVO.setIfAutoCheck(getIfAutoCheck());
        newVO.setIfDefault(getIfDefault());
        newVO.setIfMust(getIfMust());
        newVO.setIsUserDef(UFBoolean.valueOf(getUserDef()));
        newVO.setNewMaxLength(getMaxLength());
        newVO.setOperaCode(getOperaCode());
        newVO.setOperaName(getOperaName());
        newVO.setPkCorp(getPkCorp());
        newVO.setPkTemplet(getPkTemplet());
        newVO.setReturnType(getReturnType());
        newVO.setTableCode(getTableCode());
        newVO.setTableName(getTableName());
        newVO.setIfDataPower(getIfDataPower());
        newVO.setResid(getResid());
        newVO.setIsSubIncluded(isSubIncluded());
        newVO.setPrerestrict(getPrerestrict());
        newVO.setGuideline(getGuideline());
        newVO.setIsSysFuncRefUsed(getIsSysFuncRefUsed());
        newVO.setIsAttrRefUsed(getIsAttrRefUsed());
        newVO.setIfNotMDCondition(getIfNotMDCondition());
        newVO.setLimit(getLimit());
        return newVO;
    }
    /**
     * <p>
     * 需要在一个循环中访问的属性的名称数组.
     * <p>
     * 创建日期:(??Date??)
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getAttributeNames() {

        return new String[] { "pk_corp", "pk_templet", "field_code",
                "field_name", "opera_code", "opera_name", "value",
                "if_default", "if_used", "data_type", "if_immobility",
                "consult_code", "if_sum", "if_group", "if_order", "if_desc",
                "order_sequence", "max_length", "if_autocheck", "if_must","isSubIncluded",
                "disp_type", "return_type", "disp_value", "disp_sequence",
                "userdefflag", "table_code", "table_name", "if_datapower","isCondition",
                "reltablecode","relkeycode","prerestrict","guideline","isSysFuncRefUsed","isAttrRefUsed","if_notmdcondition","limit"};
    }
    /**
     * <p>
     * 根据一个属性名称字符串该属性的值.
     * <p>
     * 创建日期:(2002-10-10)
     * 
     * @param key
     *            java.lang.String
     */
    public Object getAttributeValue(String attributeName) {

        if (attributeName.equals("id")) {
            return id;
        } else if (attributeName.equals("pk_corp")) {
            return pkCorp;
        } else if (attributeName.equals("pk_templet")) {
            return pkTemplet;
        } else if (attributeName.equals("field_code")) {
            return fieldCode;
        } else if (attributeName.equals("field_name")) {
            return fieldName;
        } else if (attributeName.equals("opera_code")) {
            return operaCode;
        } else if (attributeName.equals("opera_name")) {
            return operaName;
        } else if (attributeName.equals("value")) {
            return value;
        } else if (attributeName.equals("if_default")) {
            return ifDefault;
        } else if (attributeName.equals("if_used")) {
            return ifUsed;
        } else if (attributeName.equals("data_type")) {
            return dataType;
        } else if (attributeName.equals("if_immobility")) {
            return ifImmobility;
        } else if (attributeName.equals("consult_code")) {
            return consultCode;
        } else if (attributeName.equals("if_sum")) {
            return ifSum;
        } else if (attributeName.equals("if_group")) {
            return ifGroup;
        } else if (attributeName.equals("if_order")) {
            return ifOrder;
        } else if (attributeName.equals("if_desc")) {
            return ifDesc;
        } else if (attributeName.equals("order_sequence")) {
            return orderSequence;
        } else if (attributeName.equals("max_length")) {
            return maxLength;
        } else if (attributeName.equals("if_autocheck")) {
            return ifAutoCheck;
        } else if (attributeName.equals("if_must")) {
            return ifMust;
        } else if (attributeName.equals("if_subincluded")) {
            return isSubIncluded;
        } else if (attributeName.equals("disp_type")) {
            return dispType;
        } else if (attributeName.equals("return_type")) {
            return returnType;
        } else if (attributeName.equals("disp_value")) {
            return dispValue;
        } else if (attributeName.equals("disp_sequence")) {
            return dispSequence;
        } else if (attributeName.equals("userdefflag")) {
            return isUserDef;
        } else if (attributeName.equals("table_code")) {
            return tableCode;
        } else if (attributeName.equals("table_name")) {
            return tableName;
        } else if (attributeName.equals("if_datapower")) {
            return ifDataPower;
        } else if (attributeName.equals("resid")) {
            return resid;
        }else if(attributeName.equals("iscondition")) {
        	return isCondition;
        }else if(attributeName.equals("instrumentsql")) {
        	return instrumentedsql;
        }else if (attributeName.equals("where")) {
        	return where;
        }else if (attributeName.equals("prerestrict")) {
        	return prerestrict;
        }else if (attributeName.equals("guideline")) {
        	return guideline;
        }else if (attributeName.equals("if_sysfuncrefused")) {
        	return isSysFuncRefUsed;
        }else if (attributeName.equals("if_attrrefused")) {
        	return isAttrRefUsed;
        }else if (attributeName.equals("if_notmdcondition")) {
        	return ifNotMDCondition;
        }else if (attributeName.equals("limit")) {
        	return limit;
        }
        return null;
    }
//    /**
//     * 此处插入方法说明. 创建日期:(2003-09-03 15:48:16)
//     * 
//     * @return java.lang.String[]
//     */
//    public String[] getComboItems() {
//        if (getDataType().intValue() != USERCOMBO)
//            return null;
//        if (comboItems == null || comboType.equals(COMBO_UNCHECK))
//            splitComboItems();
//        return comboItems;
//    }
//    /**
//     * 此处插入方法说明. 创建日期:(2003-09-03 15:35:53)
//     * 
//     * @return java.lang.String
//     */
//    public java.lang.String getComboType() {
//        if (getDataType().intValue() != USERCOMBO)
//            return COMBO_NULL;
//
//        if (comboType == null || comboType.equals(COMBO_UNCHECK))
//            splitComboItems();
//        if (comboType == null)
//            comboType = COMBO_NULL;
//        return comboType;
//    }
    /**
     * 属性consultCode的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getConsultCode() {
        return consultCode;
    }
    /**
     * 属性dataType的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return Integer
     */
    public Integer getDataType() {
        return dataType;
    }
    /**
     * 属性dispSequence的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return Integer
     */
    public Integer getDispSequence() {
        return dispSequence;
    }
    /**
     * 属性dispType的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return Integer
     */
    public Integer getDispType() {
        return dispType;
    }
    /**
     * 属性dispValue的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getDispValue() {
        return dispValue;
    }
    /**
     * 返回数值对象的显示名称.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return java.lang.String 返回数值对象的显示名称.
     */
    public String getEntityName() {

        return "QueryCondition";
    }
    /**
     * 属性fieldCode的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getFieldCode() {
        return fieldCode;
    }
    /**
     * 属性fieldName的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getFieldName() {
        return fieldName;
    }
    /**
     * 属性id的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getId() {
        return id;
    }
    /**
     * 属性ifAutoCheck的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfAutoCheck() {
        return ifAutoCheck;
    }
    /**
     * 属性isUseDataPower的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfDataPower() {
        if (ifDataPower == null)
            ifDataPower = UFBoolean.FALSE;
        return ifDataPower;
    }
    /**
     * 属性ifDefault的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfDefault() {
        return ifDefault;
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
     * 属性ifGroup的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfGroup() {
        return ifGroup;
    }
    /**
     * 属性ifImmobility的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfImmobility() {
        return ifImmobility;
    }
    /**
     * 属性ifMust的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfMust() {
        return ifMust;
    }
    /**
     * 属性ifOrder的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfOrder() {
        return ifOrder;
    }
    /**
     * 属性ifSum的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfSum() {
        return ifSum;
    }
    /**
     * 属性ifUsed的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return UFBoolean
     */
    public UFBoolean getIfUsed() {
        return ifUsed;
    }
    /**
     * 属性maxLength的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return Integer
     */
    public Integer getMaxLength() {
        return maxLength;
    }
    /**
     * 属性operaCode的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getOperaCode() {
        return operaCode;
    }
    /**
     * 属性operaName的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getOperaName() {
        return operaName;
    }
    
    /**
     * 返回操作符数组
     */
    public Operator[] getOperators() {
		String token = IOperator.TOKEN;
		if (StringUtil.isEmptyWithTrim(operaCode)
				|| StringUtil.isEmptyWithTrim(operaName)) {
			return null;
		} else {
			if (!operaCode.endsWith(token)) {
				operaCode = operaCode + token;
			}
			if (!operaName.endsWith(token)) {
				operaName = operaName + token;
			}
			String[] codes = operaCode.split(token);
			String[] names = operaName.split(token);
			List<Operator> list = new ArrayList<Operator>();
			for (int i = 0; i < codes.length; i++) {
				list.add(new Operator(codes[i], names[i]));
			}
			return list.toArray(new Operator[0]);
		}
	}
    
 /**
 * 创建日期:(2001-3-20 17:41:28)
     * 
     * @return nc.vo.pub.query.QueryOperaVO
     */
    public OperaVO[] getOperaVO() {
        if (operaCode != null && operaName != null) {
            if (!operaCode.endsWith("@"))
                operaCode = operaCode + "@";
            if (!operaName.endsWith("@"))
                operaName = operaName + "@";
            // opera_code
            List<String> codeItems = new ArrayList<String>();
            char[] charItems = operaCode.toCharArray();
            int index = 0;
            String code = "";
            while (index < charItems.length) {
                char ch = charItems[index];
                if (ch != '@') {
                    code = code + ch;
                    index++;
                } else {
                    codeItems.add(code);
                    code = "";
                    index = index + 1;
                }

            }
            //opera_name
            List<String>  nameItems = new ArrayList<String>();
            int bIndex = 0;
            int eIndex = 0;
            String name = "";
            while (eIndex >= 0) {
                eIndex = operaName.indexOf("@", eIndex + 1);
                if (eIndex > 0) {
                    name = operaName.substring(bIndex, eIndex);
                    bIndex = eIndex + 1;
                    nameItems.add(name);
                }
            }
            //Vector to operaVO[]
            OperaVO[] datas = new OperaVO[codeItems.size()];
            for (int i = 0; i < codeItems.size(); i++) {
                datas[i] = new OperaVO();
                datas[i].setOperaCode(codeItems.get(i).toString().trim());
                datas[i].setOperaName(nameItems.get(i).toString().trim());
            }
            return datas;
        } else
            return null;
    }
    /**
     * 属性orderSequence的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return Integer
     */
    public Integer getOrderSequence() {
        return orderSequence;
    }
    /**
     * 属性pkCorp的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getPkCorp() {
        return pkCorp;
    }
    /**
     * 属性pkTemplet的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getPkTemplet() {
        return pkTemplet;
    }
    /**
     * 返回对象标识,用来唯一定位对象.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getPrimaryKey() {

        return id;
    }
    /**
     * 属性returnType的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return Integer
     */
    public Integer getReturnType() {
        return returnType;
    }
    /**
     * 属性fieldCode的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getTableCode() {
        return tableCode;
    }
    /**
     * 属性fieldName的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getTableName() {
        return tableName;
    }
    /**
     * 此处插入方法说明. 创建日期:(2002-01-24 15:22:13)
     * 
     * @return UFBoolean
     */
    public boolean getUserDef() {

        if (isUserDef == null)
            return false;

        try {
            return isUserDef.booleanValue();
        } catch (Throwable e) {
            return false;
        }
    }
    /**
     * 属性value的Getter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @return String
     */
    public String getValue() {
        return value;
    }
    /**
     * 此处插入方法说明. 创建日期:(2003-09-08 15:31:08)
     */
    private void initDefaultAttrs() {
        setOperaCode("=@>@>=@<@<=@like@");
        setOperaName(LfwResBundle.getInstance().getStrByID(
                "_Template", "UPP_Template-000174")/*
                                                    * @res
                                                    * "等于@大于@大于等于@小于@小于等于@相似@"
                                                    */);

        setDataType(Integer.valueOf(0));
        setDispSequence(Integer.valueOf(100));
        setIfDefault(UFBoolean.TRUE);
        setIfUsed(UFBoolean.TRUE);
        setConsultCode("-99");
        setReturnType(Integer.valueOf(0));
        setDispType(Integer.valueOf(0));
        setIfMust(UFBoolean.FALSE);
        setIfAutoCheck(UFBoolean.FALSE);
        setIfOrder(UFBoolean.FALSE);
        setOrderSequence(Integer.valueOf(0));
        setIfGroup(UFBoolean.FALSE);
        setIfSum(UFBoolean.FALSE);
        setIfImmobility(UFBoolean.FALSE);
        setIsSubIncluded(UFBoolean.FALSE);
        setIsSysFuncRefUsed(UFBoolean.FALSE);
        setIsAttrRefUsed(UFBoolean.FALSE);
        setLimit(9999);
    }
    /**
     * 判断两个属性是否值相同,支持字符串,UFBoolean,Integer. 创建日期:(2002-11-15 16:27:32)
     * 
     * @return boolean
     * @param attrOld
     *            java.lang.Object
     * @param attrNew
     *            java.lang.Object
     */
    public static boolean isAttrEquals(Object attrOld, Object attrNew) {
        try {
            if (attrOld == null) {
                if (attrNew == null)
                    return true;
                else
                    return false;
            } else {
                if (attrNew == null)
                    return false;
            }

            if (attrOld instanceof String)
                if (((String) attrOld).equals((String) attrNew))
                    return true;

            if (attrOld instanceof UFBoolean) {
                boolean valueOld = ((UFBoolean) attrOld).booleanValue();
                boolean valueNew = ((UFBoolean) attrNew).booleanValue();

                if ((valueOld && valueNew) || ((!valueOld) && (!valueNew)))
                    return true;
            }
            if (attrOld instanceof Integer) {
                int valueOld = ((Integer) attrOld).intValue();
                int valueNew = ((Integer) attrNew).intValue();

                if (valueOld == valueNew)
                    return true;
            }

        } catch (Exception ex) {
        }
        return false;

    }
    /**
     * 判断用户不能更改的属性是否相同. 创建日期:(2002-11-15 15:54:25)
     * 
     * @return nc.vo.pub.query.QueryConditionVO
     */
    public boolean isDefaultAttrEquals(QueryConditionVO userVO) {
        if (!isAttrEquals(getConsultCode(), userVO.getConsultCode()))
            return false;
        if (!isAttrEquals(getDataType(), userVO.getDataType()))
            return false;
        if (!isAttrEquals(getDispType(), userVO.getDispType()))
            return false;
        if (!isAttrEquals(getFieldCode(), userVO.getFieldCode()))
            return false;
        if (!isAttrEquals(getFieldName(), userVO.getFieldName()))
            return false;
        //if (!isAttrEquals(getId(), userVO.getId()))
        //return false;
        if (!isAttrEquals(getIfAutoCheck(), userVO.getIfAutoCheck()))
            return false;
        if (!isAttrEquals(getIfDefault(), userVO.getIfDefault()))
            return false;
        if (!isAttrEquals(getIfMust(), userVO.getIfMust()))
            return false;
        if (!isAttrEquals(UFBoolean.valueOf(getUserDef()), UFBoolean.valueOf(userVO
                .getUserDef())))
            return false;
        if (!isAttrEquals(getMaxLength(), userVO.getMaxLength()))
            return false;
        if (!isAttrEquals(getOperaCode(), userVO.getOperaCode()))
            return false;
        if (!isAttrEquals(getOperaName(), userVO.getOperaName()))
            return false;
        if (!isAttrEquals(getPkCorp(), userVO.getPkCorp()))
            return false;
        //if (!isAttrEquals(getPkTemplet(), userVO.getPkTemplet()))
        //return false;
        if (!isAttrEquals(getReturnType(), userVO.getReturnType()))
            return false;
        if (!isAttrEquals(getTableCode(), userVO.getTableCode()))
            return false;
        if (!isAttrEquals(getTableName(), userVO.getTableName()))
            return false;
        if (!isAttrEquals(getInstrumentedsql(), userVO.getInstrumentedsql()))
            return false;

        return true;
    }
    /**
     * 此处插入方法说明. 创建日期:(2002-11-15 16:17:00)
     * 
     * @return boolean
     * @param compareVO
     *            nc.vo.pub.query.QueryConditionVO
     */
    public boolean isOneCondition(QueryConditionVO compareVO) {
        if (getFieldCode().equals(compareVO.getFieldCode())
                && getFieldName().equals(compareVO.getFieldName())) {
            if (getTableCode() != null) {
                if (compareVO.getTableCode() != null
                        && getTableCode().equals(compareVO.getTableCode()))
                    return true;
            } else {
                if (compareVO.getTableCode() == null
                        || compareVO.getTableCode().length() == 0)
                    return true;
            }

        }
        return false;
    }
    /**
     * 此处插入方法说明. 创建日期:(2002-01-24 15:22:13)
     * 
     * @return UFBoolean
     */
    public UFBoolean isUserDef() {
        return isUserDef;
    }
    /**
     * <p>
     * 对参数name对型的属性设置值.
     * <p>
     * 创建日期:(2002-10-10)
     * 
     * @param key
     *            java.lang.String
     */
    public void setAttributeValue(String name, Object value) {

        try {
            if (name.equals("id")) {
                id = (String) value;
            } else if (name.equals("pk_corp")) {
                pkCorp = (String) value;
            } else if (name.equals("pk_templet")) {
                pkTemplet = (String) value;
            } else if (name.equals("field_code")) {
                fieldCode = (String) value;
            } else if (name.equals("field_name")) {
                fieldName = (String) value;
            } else if (name.equals("opera_code")) {
                operaCode = (String) value;
            } else if (name.equals("opera_name")) {
                operaName = (String) value;
            } else if (name.equals("value")) {
                this.value = (String) value;
            } else if (name.equals("if_default")) {
                ifDefault = (UFBoolean) value;
            } else if (name.equals("if_used")) {
                ifUsed = (UFBoolean) value;
            } else if (name.equals("data_type")) {
                dataType = (Integer) value;
            } else if (name.equals("if_immobility")) {
                ifImmobility = (UFBoolean) value;
            } else if (name.equals("consult_code")) {
                consultCode = (String) value;
            } else if (name.equals("if_sum")) {
                ifSum = (UFBoolean) value;
            } else if (name.equals("if_group")) {
                ifGroup = (UFBoolean) value;
            } else if (name.equals("if_order")) {
                ifOrder = (UFBoolean) value;
            } else if (name.equals("if_desc")) {
                ifDesc = (UFBoolean) value;
            } else if (name.equals("order_sequence")) {
                orderSequence = (Integer) value;
            } else if (name.equals("max_length")) {
                maxLength = (Integer) value;
            } else if (name.equals("if_autocheck")) {
                ifAutoCheck = (UFBoolean) value;
            } else if (name.equals("if_must")) {
                ifMust = (UFBoolean) value;
            } else if(name.equals("if_subincluded")) {
            	isSubIncluded = (UFBoolean) value;
            } else if (name.equals("disp_type")) {
                dispType = (Integer) value;
            } else if (name.equals("return_type")) {
                returnType = (Integer) value;
            } else if (name.equals("disp_value")) {
                dispValue = (String) value;
            } else if (name.equals("disp_sequence")) {
                dispSequence = (Integer) value;
            } else if (name.equals("userdefflag")) {
                isUserDef = (UFBoolean) value;
            } else if (name.equals("table_code")) {
                tableCode = (String) value;
            } else if (name.equals("table_name")) {
                tableName = (String) value;
            } else if (name.equals("if_datapower")) {
                ifDataPower = (UFBoolean) value;
            } else if (name.equals("resid")) {
                resid = (String) value;
            } else if(name.equals("iscondition")) {
            	isCondition = (UFBoolean) value;
            } else if (name.equals("instrumentsql")) {
            	instrumentedsql= (String) value;
            } else if (name.equals("where")) {
            	where = (String)value;
            } else if (name.equals("prerestrict")) {
            	prerestrict = (String)value;
            } else if (name.equals("guideline")) {
            	guideline = (String)value;
            } else if (name.equals("if_sysfuncrefused")) {
            	isSysFuncRefUsed = (UFBoolean)value;
            } else if (name.equals("if_attrrefused")) {
            	isAttrRefUsed = (UFBoolean)value;
            } else if (name.equals("if_notmdcondition")) {
            	ifNotMDCondition = (UFBoolean)value;
            } else if (name.equals("limit")) {
            	limit = (Integer)value;
            } 
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "In QueryConditionVO.setAttributeValue()."
                            + LfwResBundle.getInstance()
                                    .getStrByID("_Template",
                                            "UPP_Template-000517")/*
                                                                   * @res
                                                                   * "赋值时类型转换错误!"
                                                                   */
                            + "name=" + name + " value=" + value + ".");
        }
    }
//    /**
//     * 此处插入方法说明. 创建日期:(2003-09-03 16:03:04)
//     * 
//     * @param items
//     *            java.lang.String[]
//     */
//    public void setComboItems(String[] items) {
//        comboItems = items;
//    }
//    /**
//     * 此处插入方法说明. 创建日期:(2003-09-03 15:35:53)
//     * 
//     * @param newComboType
//     *            java.lang.String
//     */
//    public void setComboType(java.lang.String newComboType) {
//        comboType = newComboType;
//    }
    /**
     * 属性consultCode的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newConsultCode
     *            String
     */
    public void setConsultCode(String newConsultCode) {

        consultCode = newConsultCode;
    }
    /**
     * 属性dataType的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newDataType
     *            Integer
     */
    public void setDataType(Integer newDataType) {

        dataType = newDataType;
    }
    /**
     * 属性dispSequence的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newdispSequence
     *            Integer
     */
    public void setDispSequence(Integer newDispSequence) {

        dispSequence = newDispSequence;
    }
    /**
     * 属性dispType的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newDispType
     *            Integer
     */
    public void setDispType(Integer newDispType) {

        dispType = newDispType;
    }
    /**
     * 属性dispValue的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newDispValue
     *            String
     */
    public void setDispValue(String newDispValue) {

        dispValue = newDispValue;
    }
    /**
     * 属性fieldCode的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newFieldCode
     *            String
     */
    public void setFieldCode(String newFieldCode) {

        fieldCode = newFieldCode;
    }
    /**
     * 属性fieldName的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newFieldName
     *            String
     */
    public void setFieldName(String newFieldName) {

        fieldName = newFieldName;
    }
    /**
     * 属性id的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newId
     *            String
     */
    public void setId(String newId) {

        id = newId;
    }
    /**
     * 属性ifAutoCheck的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfAutoCheck
     *            UFBoolean
     */
//    public void setIfAutocheck(UFBoolean newIfAutoCheck) {
//
//        ifAutoCheck = newIfAutoCheck;
//    }
    /**
     * 属性ifDataPower的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIsUseDataPower
     *            UFBoolean
     */
    public void setIfDataPower(UFBoolean newIsUseDataPower) {

        if (newIsUseDataPower == null)
            ifDataPower = UFBoolean.FALSE;
        else
            ifDataPower = newIsUseDataPower;
    }
    /**
     * 属性ifDefault的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfDefault
     *            UFBoolean
     */
    public void setIfDefault(UFBoolean newIfDefault) {

        ifDefault = newIfDefault;
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
     * 属性ifGroup的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfGroup
     *            UFBoolean
     */
    public void setIfGroup(UFBoolean newIfGroup) {

        ifGroup = newIfGroup;
    }
    /**
     * 属性ifImmobility的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfImmobility
     *            UFBoolean
     */
    public void setIfImmobility(UFBoolean newIfImmobility) {

        ifImmobility = newIfImmobility;
    }
    /**
     * 属性ifMust的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfMust
     *            UFBoolean
     */
    public void setIfMust(UFBoolean newIfMust) {

        ifMust = newIfMust;
    }
    /**
     * 属性ifOrder的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfOrder
     *            UFBoolean
     */
    public void setIfOrder(UFBoolean newIfOrder) {

        ifOrder = newIfOrder;
    }
    /**
     * 属性ifSum的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfSum
     *            UFBoolean
     */
    public void setIfSum(UFBoolean newIfSum) {

        ifSum = newIfSum;
    }
    /**
     * 属性ifUsed的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newIfUsed
     *            UFBoolean
     */
    public void setIfUsed(UFBoolean newIfUsed) {

        ifUsed = newIfUsed;
    }
    /**
     * 属性isUserDef的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newUserDef
     *            UFBoolean
     */
    public void setIsUserDef(UFBoolean newUserDef) {

        isUserDef = newUserDef;
    }
    /**
     * 属性maxLength的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newMaxLength
     *            Integer
     */
    public void setNewMaxLength(Integer newMaxLength) {

        maxLength = newMaxLength;
    }
    /**
     * 属性operaCode的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newOperaCode
     *            String
     */
    public void setOperaCode(String newOperaCode) {

        operaCode = newOperaCode;

        //the following lines are used to translate operaName
        if (operaCode != null && (operaCode = operaCode.trim()).length() > 0) {
        	operaName =  translateOperaCodeToName(newOperaCode);
        }
    }
    
    /**
	 * 将操作符编码字符串翻译成对应的名称字符串
	 * 
	 * @param newOperaCode
	 *            操作符编码字符串 形如 &quotlike@=@<@<=&quot
	 */
	public static String translateOperaCodeToName(String newOperaCode) {
		StringBuffer sb = new StringBuffer();
		String token = IOperator.TOKEN;
		String[] codes = newOperaCode.toLowerCase().split(token);
		for (String code : codes) {
			Object name = keymap.get(code.trim());
			if (name != null) {
				sb.append(LfwResBundle.getInstance()
						.getStrByID("_template", name.toString()));
			} else {
				sb.append(code.trim());
			}
			sb.append(token);
		}
// StringTokenizer st = new StringTokenizer(newOperaCode.toLowerCase(),
//		        delim, true);
//		while (st.hasMoreTokens()) {
//		    String tk = st.nextToken();
//		    if (tk.equals(delim))
//		        sb.append(delim);
//		    else {
//		        //format token
//		        tk = tk.trim();
//		        //replace " " with " ", suppose the most times of
//		        // replacement is not more than two.
//		        tk = tk.replaceAll("  ", " ");
//		        tk = tk.replaceAll("  ", " ");
//
//		        String key = (String) keymap.get(tk);
//		        if (key != null)
//		            sb.append(LfwResBundle.getInstance()
//		                    .getStrByID("_template", key));
//		        else
//		            sb.append(tk);
//		    }
//		}
		return sb.toString();
	}
	/**
     * 属性operaName的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newOperaName
     *            String
     */
    public void setOperaName(String newOperaName) {
    	operaName = newOperaName;
    }
    /**
     * 属性orderSequence的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newOrderSequence
     *            Integer
     */
    public void setOrderSequence(Integer newOrderSequence) {

        orderSequence = newOrderSequence;
    }
    /**
     * 属性pkCorp的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newPkCorp
     *            String
     */
    public void setPkCorp(String newPkCorp) {

        pkCorp = newPkCorp;
    }
    /**
     * 属性pkTemplet的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newPkTemplet
     *            String
     */
    public void setPkTemplet(String newPkTemplet) {

        pkTemplet = newPkTemplet;
    }
    /**
     * 设置对象标识,用来唯一定位对象.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param id
     *            String
     */
    public void setPrimaryKey(String newId) {

        id = newId;
    }
    /**
     * 属性returnType的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newReturnType
     *            Integer
     */
    public void setReturnType(Integer newReturnType) {

        returnType = newReturnType;
    }
    /**
     * 属性fieldCode的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newFieldCode
     *            String
     */
    public void setTableCode(String newTableCode) {

        tableCode = newTableCode;
    }
    /**
     * 属性fieldName的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newFieldName
     *            String
     */
    public void setTableName(String newTableName) {

        tableName = newTableName;
    }
    /**
     * 属性value的setter方法.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @param newValue
     *            String
     */
    public void setValue(String newValue) {

        value = newValue;
    }
//    /**
//     * 此处插入方法说明. 创建日期:(2003-09-03 15:53:39)
//     */
//    private void splitComboItems() {
//        boolean isRightCombo = false;
//        String consultCode = getConsultCode();
//        if (consultCode != null) {
//            try {
//                String[] items = nc.vo.bill.pub.MiscUtil.getStringTokens(
//                        consultCode, ",");
//                if (items.length > 1) {
//                    if (items[0].equalsIgnoreCase(COMBO_INDEX)
//                            || items[0].equalsIgnoreCase(COMBO_INDEX_CHAR)
//                            || items[0].equalsIgnoreCase(COMBO_STRING)) {
//                        isRightCombo = true;
//                        setComboType(items[0]);
//                        String[] cItems = new String[items.length - 1];
//                        System.arraycopy(items, 1, cItems, 0, items.length - 1);
//                        setComboItems(cItems);
//                    } else if (items[0].equalsIgnoreCase(COMBO_INDEX_X)
//                            || items[0].equalsIgnoreCase(COMBO_INDEX_CHAR_X)
//                            || items[0].equalsIgnoreCase(COMBO_STRING_X)) {
//                        isRightCombo = true;
//                        setComboType(items[0].substring(0,1));
//                        setItemIDs(new String[items.length - 1]);
//                        String[] cItems = new String[items.length - 1];
//
//                        System
//                                .arraycopy(items, 1, getItemIDs(), 0,
//                                        items.length - 1);
//                        for (int i = 0; i < getItemIDs().length; i++) {
//                            int pos = getItemIDs()[i].indexOf('=');
//                            if (pos > 0) {
//                                cItems[i] = getItemIDs()[i].substring(pos + 1);
//                                getItemIDs()[i] = getItemIDs()[i].substring(0, pos);
//                            } else
//                                cItems[i] = getItemIDs()[i];
//                        }
//                        setComboItems(cItems);
//                    } else if (items[0].equalsIgnoreCase(COMBO_INDEX_DBFIELD)
//                            || items[0]
//                                    .equalsIgnoreCase(COMBO_INDEX_CHAR_DBFIELD)
//                            || items[0].equalsIgnoreCase(COMBO_STRING_DBFIELD)) {
//                        if (items.length == 3) {
//                            String[] fitems = new DataDictionaryReader(
//                                    items[1], items[2])
//                                    .getQzsm();
//
//                            setComboItems(fitems);
//                            setComboType(items[0].substring(0,1));
//                        }
//                    }
//                }
//            } catch (Exception ex) {
//            }
//        }
//        if (!isRightCombo) {
//            setComboType(COMBO_NULL);
//            setComboItems(null);
//        }
//    }
    /**
     * 验证对象各属性之间的数据逻辑正确性.
     * 
     * 创建日期:(2001-6-14)
     * 
     * @exception nc.vo.pub.ValidationException
     *                如果验证失败,抛出 ValidationException,对错误进行解释.
     */
    public void validate() throws ValidationException {

        List<String> errFields = new ArrayList<String>(); // errFields record those null
        // fields that cannot be null.
        // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
        if (id == null) {
            errFields.add(new String("id"));
        }
        if (pkCorp == null) {
            errFields.add(new String("pkCorp"));
        }
        if (pkTemplet == null) {
            errFields.add(new String("pkTemplet"));
        }
        if (fieldCode == null) {
            errFields.add(new String("fieldCode"));
        }
        if (fieldName == null) {
            errFields.add(new String("fieldName"));
        }
        if (operaCode == null) {
            errFields.add(new String("operaCode"));
        }
        if (operaName == null) {
            errFields.add(new String("operaName"));
        }
        if (ifDefault == null) {
            errFields.add(new String("ifDefault"));
        }
        if (ifUsed == null) {
            errFields.add(new String("ifUsed"));
        }
        if (dataType == null) {
            errFields.add(new String("dataType"));
        }
        if (consultCode == null) {
            errFields.add(new String("consultCode"));
        }
        if (ifImmobility == null) {
            errFields.add(new String("ifImmobility"));
        }
        if (ifSum == null) {
            errFields.add(new String("ifSum"));
        }
        if (ifGroup == null) {
            errFields.add(new String("ifGroup"));
        }
        if (ifOrder == null) {
            errFields.add(new String("ifOrder"));
        }
        if (orderSequence == null) {
            errFields.add(new String("orderSequence"));
        }
        if (ifAutoCheck == null) {
            errFields.add(new String("ifAutoCheck"));
        }
        if (ifMust == null) {
            errFields.add(new String("ifMust"));
        }
        if (isSubIncluded == null) {
            errFields.add(new String("if_subincluded"));
        }
        if (dispType == null) {
            errFields.add(new String("disptype"));
        }
        if (returnType == null) {
            errFields.add(new String("returnType"));
        }
        if (dispSequence == null) {
            errFields.add(new String("dispSequence"));
        }
        // construct the exception message:
        StringBuffer message = new StringBuffer();
        message.append(LfwResBundle.getInstance().getStrByID(
                "common", "MC11")/* @res "下列字段不能为空" */
                + ":");
        if (errFields.size() > 0) {
            String[] temp = (String[]) errFields.toArray(new String[0]);
            message.append(temp[0]);
            for (int i = 1; i < temp.length; i++) {
                message.append(",");
                message.append(temp[i]);
            }
            // throw the exception:
            throw new LfwRuntimeException(message.toString());
        }
    }
    /**
     * @return Returns the resid.
     */
    public String getResid() {
        return resid;
    }
    /**
     * @param resid
     *            The resid to set.
     */
    public void setResid(String resid) {
        this.resid = resid;
    }
    public String getNodecode() {
        return nodecode;
    }
    public void setNodecode(String nodecode) {
        this.nodecode = nodecode;
    }
    public DefaultConstEnum[] getEnums() {
        return enums;
    }
    public void setEnums(DefaultConstEnum[] enums) {
        this.enums = enums;
    }
	/**
	 * @param itemIDs The itemIDs to set.
	 */
	private void setItemIDs(String[] itemIDs) {
		this.itemIDs = itemIDs;
	}
	/**
	 * @return Returns the itemIDs.
	 */
	public String[] getItemIDs() {
		return itemIDs;
	}
	/**
	 * @return Returns the dr.
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * @param dr The dr to set.
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * @return Returns the ts.
	 */
	public UFDateTime getTs() {
		return ts;
	}
	/**
	 * @param ts The ts to set.
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	/**
	 * @return Returns the isUserDef.
	 * 这个方法实质上和isUserDef()是一样的.只是因为
	 * isUserDef()不合javabean规范,序列化到
	 * 数据库时会有问题. 因此增加这样一个合乎规范的方法
	 */
	public UFBoolean getIsUserDef() {
		return isUserDef;
	}
	/**
	 * 参照是否包含下级
	 */
	public UFBoolean isSubIncluded() {
		return isSubIncluded;
	}
	/**
	 * 参照是否包含下级
	 */
	public UFBoolean getIsSubIncluded() {
		return isSubIncluded;
	}
	/**
	 * 参照是否包含下级
	 */
	public void setIsSubIncluded(UFBoolean isSubIncluded) {
		this.isSubIncluded = isSubIncluded;
	}
	/**
	 * @param ifAutoCheck The ifAutoCheck to set.
	 * 这个方法实质上和setIfAutocheck()是一样的.只是因为
	 * setIfAutocheck()不合javabean规范,序列化到
	 * 数据库时会有问题. 因此增加这样一个合乎规范的方法
	 * 
	 */
	public void setIfAutoCheck(UFBoolean ifAutoCheck) {
		this.ifAutoCheck = ifAutoCheck;
	}
	/**
	 * @param maxLength The maxLength to set.
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public UFBoolean getIsCondition() {
		return isCondition;
	}
	public void setIsCondition(UFBoolean isCondition) {
		this.isCondition = isCondition;
	}
	public void setInstrumentedsql(String instrumentedsql) {
		this.instrumentedsql = instrumentedsql;
	}
	public String getInstrumentedsql() {
		return instrumentedsql;
	}
	public UFBoolean isFromMetaData() {
		return isFromMetaData;
	}
	public void setFromMetaData(UFBoolean isFromMetaData) {
		this.isFromMetaData = isFromMetaData;
	}
	/**
	 * 参照类型的where条件，只适用于代码使用，不会持久化到数据库
	 */
	public String getWhere() {
		return where;
	}
	/**
	 * 参照类型的where条件，只适用于代码使用，不会持久化到数据库
	 */
	public void setWhere(String where) {
		this.where = where;
	}
	
	public String getPrerestrict() {
		return prerestrict;
	}
	
	public void setPrerestrict(String prerestrict) {
		this.prerestrict = prerestrict;
	}
	
	public String getGuideline() {
		return guideline;
	}
	
	public void setGuideline(String guideline) {
		this.guideline = guideline;
	}
	
	public UFBoolean getIsSysFuncRefUsed() {
		return isSysFuncRefUsed;
	}
	
	public void setIsSysFuncRefUsed(UFBoolean isSysFuncRefUsed) {
		this.isSysFuncRefUsed = isSysFuncRefUsed;
	}
	
	public UFBoolean getIsAttrRefUsed() {
		return isAttrRefUsed;
	}
	
	public void setIsAttrRefUsed(UFBoolean isAttrRefUsed) {
		this.isAttrRefUsed = isAttrRefUsed;
	}
	
	public UFBoolean getIfNotMDCondition() {
		return ifNotMDCondition;
	}
	
	public void setIfNotMDCondition(UFBoolean ifNotMDCondition) {
		this.ifNotMDCondition = ifNotMDCondition;
	}
	
	public boolean isNotMDCondition() {
		return ifNotMDCondition != null && ifNotMDCondition.booleanValue();
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}