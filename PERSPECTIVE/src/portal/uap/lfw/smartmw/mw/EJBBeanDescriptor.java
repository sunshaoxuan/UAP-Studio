package uap.lfw.smartmw.mw;


/**
 * EJB描述符
 */
public class EJBBeanDescriptor implements CodeCreationRuleConstants, java.io.Serializable {

    private static final long serialVersionUID = 1515158541926916241L;

    public String beanName;

    public java.lang.String ejb_ref_name;

    public java.lang.String HomeClassName;

    public java.lang.String PrimaryKeyClassName;

    public java.lang.String RemoteInterfaceClassName;

    public boolean beSession;

    public boolean beStatelessSession;

    public java.lang.String Description;

    public EMVParameter envParameter[];

    public MethodTransectionType methodTransectionType[];

    public int maxInstance = 5000;

    public boolean beCmt;

    public boolean beCmp;

    /**
     * EJBBean 构造子注解。
     */
    public EJBBeanDescriptor() {
        super();
    }

    public void addEMVParameter(EMVParameter emv) {
        if (envParameter == null) {
            envParameter = new EMVParameter[1];
            envParameter[0] = emv;
        } else {
            EMVParameter[] tsTemp = envParameter;
            envParameter = new EMVParameter[tsTemp.length + 1];
            for (int i = 0; i < tsTemp.length; i++) {
                envParameter[i] = tsTemp[i];
            }
            envParameter[envParameter.length - 1] = emv;
        }
    }

    public void addMethodTransectionType(MethodTransectionType method) {
        methodTransectionType = (MethodTransectionType[]) Convertor.addObjectToArray(methodTransectionType, method);
    }

    public java.lang.String getDescription() {
        return Description;
    }

    public java.lang.String getEjb_ref_name() {
        return ejb_ref_name;
    }

    public EMVParameter getEMVParameter(String name) {
        return (EMVParameter) Convertor.getObjtFrmArry(envParameter, "ParameterName", name);
    }

    public java.lang.String getHomeClassName() {
        return HomeClassName;
    }

    public java.lang.String getHomeClassName_Local_() {
        int loc = HomeClassName.lastIndexOf(".");
        String str = HomeClassName.substring(0, loc + 1) + CodeSeperator + HomeClassName.substring(loc + 1);
        return str + LocalClassSuffix;

    }

  
    public java.lang.String getHome_LocalClassName() {

        StringBuffer homeLocal = new StringBuffer();
        String strBSPackName = BSEJBMapper.getInstance().getEjbPackage(beanName);
        if (strBSPackName.trim().length() > 0) {
            homeLocal.append(strBSPackName);
            homeLocal.append(".");
        }
        homeLocal.append(BSEJBMapper.getInstance().getEJBHome_Local(beanName));
        return homeLocal.toString();

    }

    public java.lang.String getHomeClassName_Remote_() {
        int loc = HomeClassName.lastIndexOf(".");
        String str = HomeClassName.substring(0, loc + 1) + CodeSeperator + HomeClassName.substring(loc + 1);
        loc = str.indexOf(".bs.");
        str = str.substring(0, loc) + ".ui." + str.substring(loc + 4);
        return str + RemoteClassSuffix;
    }

    public int getMaxInstance() {
        return maxInstance;
    }

    public MethodTransectionType getMethodTransectionType(String name) {
        return (MethodTransectionType) Convertor.getObjtFrmArry(methodTransectionType, "methodName", name);
    }

    public java.lang.String getPrimaryKeyClassName() {
        return PrimaryKeyClassName;
    }

    public java.lang.String getRemoteInterfaceClassName() {
        return RemoteInterfaceClassName;
    }

    public String getRemoteInterfaceClassName_Local_() {
        int loc = getRemoteInterfaceClassName().lastIndexOf(".");
        String str = getRemoteInterfaceClassName().substring(0, loc + 1) + CodeSeperator
                + getRemoteInterfaceClassName().substring(loc + 1);
        return str + LocalClassSuffix;
    }

    public String getRemoteInterfaceClassName_Remote_() {
        int loc = getRemoteInterfaceClassName().lastIndexOf(".");
        String str = getRemoteInterfaceClassName().substring(0, loc + 1) + CodeSeperator
                + getRemoteInterfaceClassName().substring(loc + 1);
        loc = str.indexOf(".bs.");
        str = str.substring(0, loc) + ".ui." + str.substring(loc + 4);
        return str + RemoteClassSuffix;
    }

    public boolean isCmp() {
        return beCmp;
    }

    public boolean isCmt() {
        return beCmt;
    }

    public boolean isSession() {
        return beSession;
    }

    public boolean isStatelessSession() {
        return beStatelessSession;
    }

    public void removeEMVParameter(String emv) {
        envParameter = (EMVParameter[]) Convertor.removeObjectFromArray(envParameter, "ParameterName", emv);
    }

    public void removeMethodTransectionType(String method) {
        methodTransectionType = (MethodTransectionType[]) Convertor.removeObjectFromArray(envParameter, "methodName",
                method);
    }

    public void setCmp(boolean newCmp) {
        beCmp = newCmp;
    }

    public void setCmt(boolean newCmt) {
        beCmt = newCmt;
    }

    public void setDescription(java.lang.String newDescription) {
        Description = newDescription;
    }

    public void setEjb_ref_name(java.lang.String newEjb_ref_name) {
        ejb_ref_name = newEjb_ref_name;
    }

    public void setHomeClassName(java.lang.String newHomeClassName) {
        HomeClassName = newHomeClassName;
    }

    public void setMaxInstance(int newMaxInstance) {
        maxInstance = newMaxInstance;
    }

    public void setPrimaryKeyClassName(java.lang.String newPrimaryKeyClassName) {
        PrimaryKeyClassName = newPrimaryKeyClassName;
    }

    public void setRemoteInterfaceClassName(java.lang.String newRemoteInterfaceClassName) {
        RemoteInterfaceClassName = newRemoteInterfaceClassName;
    }

    public void setSession(boolean newSession) {
        beSession = newSession;
    }

    public void setStatelessSession(boolean newStatelessSession) {
        beStatelessSession = newStatelessSession;
    }

    public boolean isSameConfig(EJBBeanDescriptor bd) {

        if (!HomeClassName.equals(bd.HomeClassName))
            return false;
        if (!PrimaryKeyClassName.equals(bd.PrimaryKeyClassName))
            return false;
        if (!RemoteInterfaceClassName.equals(bd.RemoteInterfaceClassName))
            return false;
        if (!Description.equals(bd.Description))
            return false;
        if (maxInstance != bd.maxInstance)
            return false;
        if (beCmt != bd.beCmt)
            return false;
        if (beCmp != bd.beCmp)
            return false;
        if (!arrayEMVParameterEqual(envParameter, bd.envParameter))
            return false;
        if (!arrayMTTEqual(methodTransectionType, bd.methodTransectionType))
            return false;
        return true;
    }
    
    

    public boolean isTheSameBean(EJBBeanDescriptor bd) {
        if (bd == null)
            return false;
        if (!beanName.equals(bd.beanName))
            return false;
        if (!ejb_ref_name.equals(bd.ejb_ref_name))
            return false;
        if (beSession != bd.beSession)
            return false;
        if (beStatelessSession != bd.beStatelessSession)
            return false;

        return true;
    }

    public boolean resetMethod(MethodTransectionType tranType) {
        return Convertor.replaceObjectInArray(methodTransectionType, "methodName", tranType);
    }
    
    private static boolean arrayEMVParameterEqual(EMVParameter[] arr1, EMVParameter[] arr2) {
        if (arr1 == null && arr2 == null)
            return true;
        if (arr1 == null || arr2 == null)
            return false;
        if (arr1.length != arr2.length)
            return false;
        for (int i = 0; i < arr1.length; i++) {
            EMVParameter aEMVP = arr1[i];
            int j;
            for (j = 0; j < arr2.length; j++) {
                if (aEMVP.equals(arr2[j]))
                    break;
            }
            if (j >= arr2.length)
                return false;
        }

        return true;
    }
    
    private static boolean arrayMTTEqual(MethodTransectionType[] arr1,
            MethodTransectionType[] arr2) {
        if (arr1 == null && arr2 == null)
            return true;
        if (arr1 == null || arr2 == null)
            return false;
        if (arr1.length != arr2.length)
            return false;
        for (int i = 0; i < arr1.length; i++) {
            MethodTransectionType aEMVP = arr1[i];
            int j;
            for (j = 0; j < arr2.length; j++) {
                if (aEMVP.equals(arr2[j]))
                    break;
            }
            if (j >= arr2.length)
                return false;
        }

        return true;
    }
}