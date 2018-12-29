package nc.uap.ctrl.tpl.qry.base;

public class PredicateConstants {

	// -------------Condition------------------------
	public final static String STRING_AND = "AND";

	public final static String STRING_OR = "OR";

	public final static String STRING_NOT = "NOT";

	public final static String STRING_DEDUCTIVE = "If-Then";

	public final static String STRING_IF = "If";

	public final static String STRING_THEN = "Then";

	public final static String STRING_ALL = "ALL";

	public final static String STRING_EXIST = "EXIST";

	// -------------Compare-------------------------
	public final static String STRING_EQ = "equal";

	public final static String STRING_EQ_LEFT = "left";

	public final static String STRING_EQ_RIGHT = "right";

	public final static String STRING_RANGE = "range";// 数值范围

	public final static String STRING_IN = "in";

	public final static String STRING_PATTERN = "pattern";// 正则表达式

	public final static String STRING_LIKE = "like";// 字符Like

	public final static String STRING_STRRANGE = "strRange";// 字符范围

	// -------------Position------------------------
	public final static String STRING_HEAD = "head";// 表头

	public final static String STRING_BODY = "body";// 表体

	public final static String STRING_LEFT = "left";// 左操作元素

	public final static String STRING_RIGHT = "right";// 右操作元素

	// -------------attribute----------------------
	public final static String STRING_TYPE = "type";

	public final static String STRING_VALUE = "value";

	// 表体的页签.其它类型也可以用之,但是可以自己设置些有意义的熟悉了
	public final static String STRING_TAG = "tag";

	// Pattern's mode ,not constraint,but recommend
	public final static String STRING_MODE = "mode";

	// case sesitive,not constraint,but recommend
	public final static String STRING_CASE_SENSITIVE = "careCase";

	// ------------- property,some maybe not useful----------------------------
	public final static String STRING_METAPROP = "Properties";

	public final static String STRING_FIELD_PROP = "field.property";// field定义在表头的字段上。

	public final static String STRING_FIELD_REF = "field.ref";

	public final static String STRING_COL_PROP = "column.property";// field定义在表体的字段上

	public final static String STRING_COL_REF = "column.ref";

	public final static String STRING_RULE_DEFINITION = "RuleDefinition";

	public final static String STRING_ENTITYOBJECT = "entityobject";

	public final static String STR_PREDICATE = " <Predicate> ";

	//-----------一些需要多语的描述--------------------
	
	public final static String AND_i18n = "AND";

	public final static String OR_i18n = "OR";

	public final static String NOT_i18n = "NOT";
	
}
