package nc.vo.pub;

public interface IErrorDict {
	 //32000 通用业务错误
	 public final static String ERR_BUSI_BILL_EXCEPTION = "-32000";
	 //32001 单据编码重复错误
	 public final static String ERR_BUSI_BILL_CODE_ALREADYEXIST = "-32001";
	 //32002 名称重复错误
	 public final static String ERR_BUSI_BILL_NAME_ALREADYEXIST = "-32002";
	 //32003 没有单据却调用UPDATE操作或者有单据却调用ADD操作
	 public final static String ERR_BUSI_BILL_OPER_CONFICT= "-32003";
}
