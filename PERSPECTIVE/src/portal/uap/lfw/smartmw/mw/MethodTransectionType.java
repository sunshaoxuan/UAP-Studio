package uap.lfw.smartmw.mw;


public class MethodTransectionType implements java.io.Serializable {

	private static final long serialVersionUID = 7847796943945100259L;

	public String methodName = null;

	public String methodTransectionType = "Support";

	public String methodIsolateLevel = "TRANSECTION_READ_COMMITED";

	public int methodId = 0;

	private final static String[] transectionType = { "Not Supported",
			"Required", "Support", "Request New", "Mandatory", "NEVER", "N/A" };

	private final static String isolateType[] = { "TRANSECTION_SERIALIZABLE",
			"TRANSECTION_READ_COMMITED", "TRANSECTION_READ_UNCOMMITED",
			"TRANSECTION_REPEATABLE_READ", "N/A" };

	private final static int isolateTable[] = {
			IMethodTransectionType.TRANSECTION_SERIALIZABLE,
			IMethodTransectionType.TRANSECTION_READ_COMMITED,
			IMethodTransectionType.TRANSECTION_READ_UNCOMMITED,
			IMethodTransectionType.TRANSECTION_REPEATABLE_READ,
			IMethodTransectionType.TRANSECTION_NONE };

	public MethodTransectionType() {
		super();
	}

	public int getIsolateLevel() throws Exception {
		for (int i = 0; i < isolateType.length; i++) {
			if (methodIsolateLevel.equals(isolateType[i]))
				return isolateTable[i];
		}
		throw new Exception(methodName + " with invalid isolation level:"
				+ methodIsolateLevel);
	}

	/*
	 * NOT_SUPPORTED = 0 REQUIRED = 1 SUPPORT = 2 REQUEST_NEW =3 MANDATORY = 4;
	 */
	public int getTransectionType() throws Exception {
		for (int i = 0; i < transectionType.length; i++) {
			if (transectionType[i].equals(methodTransectionType))
				return i;
		}
		throw new Exception(methodName + " with invalid transaction type:"
				+ methodTransectionType);
	}

	public boolean equals(MethodTransectionType mtt) {
		if (mtt == null || methodName == null || mtt.methodName == null)
			return false;
		if (!methodName.equals(mtt.methodName))
			return false;
		if (!methodTransectionType.equals(mtt.methodTransectionType))
			return false;
		if (!methodIsolateLevel.equals(mtt.methodIsolateLevel))
			return false;
		if (methodId != mtt.methodId)
			return false;

		return true;
	}

	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + (methodName == null ? 0 : methodName.hashCode());
		hash = hash * 31 + methodTransectionType.hashCode();
		hash = hash * 31 + methodIsolateLevel.hashCode();
		hash = hash * 31 + methodId;
		return hash;
	}
}