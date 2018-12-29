package uap.lfw.smartmw.mw;

public class BSEJBMapper {
    public static final String EJB_BEAN_SUFFIX = "EjbBean";

    public static final String EJB_HOME_SUFFIX = "Home";

    public static final String EJB_HOME_LOCAL_SUFFIX = "Home_Local";

    public static final String EJB_OBJECT_SUFFIX = "EjbObject";

    protected static final String BUSINESS_INTF_PREFIX = "I";

    private static BSEJBMapper instance;

    static {
        instance = new BSEJBMapper();
    }

    public static BSEJBMapper getInstance() {
        return instance;
    }

    /**
     * 获取EJB BEAN的类名称
     */
    public String getEJBBean(String bsIntfFullName) {
        StringBuffer ejbBeanBuf = new StringBuffer();
        ejbBeanBuf.append(handleClsName(bsIntfFullName));
        ejbBeanBuf.append(EJB_BEAN_SUFFIX);
        return ejbBeanBuf.toString();
    }

    /**
     * 获取EJB HOME的类名称
     */
    public String getEJBHome(String bsIntfFullName) {
        StringBuffer ejbHomeBuf = new StringBuffer();
        ejbHomeBuf.append(handleIntfName(bsIntfFullName));
        ejbHomeBuf.append(EJB_HOME_SUFFIX);
        return ejbHomeBuf.toString();

    }

    /**
     * 获取EJB HOME的类名称
     */
    public String getEJBHome_Local(String bsIntfFullName) {

        StringBuffer ejbHomeBuf = new StringBuffer();
        ejbHomeBuf.append(handleClsName(bsIntfFullName));
        ejbHomeBuf.append(EJB_HOME_LOCAL_SUFFIX);
        return ejbHomeBuf.toString();

    }

    /**
     * 获取EJB HOME的类名称
     */
    public String getEJBObject(String bsIntfFullName) {

        StringBuffer ejbHomeBuf = new StringBuffer();
        ejbHomeBuf.append(handleIntfName(bsIntfFullName));
        //如果是BS则加上EjbObject后缀
        if (!bsIntfFullName.endsWith("BO")) {
            ejbHomeBuf.append(EJB_OBJECT_SUFFIX);
        }
        return ejbHomeBuf.toString();

    }

    /**
     * 获取EJB包名称
     */
    public String getEjbPackage(String bsIntfFullName) {

        int idx = bsIntfFullName.lastIndexOf(".");
        String bsIntfPackage = "";
        //如果没有包名，则没有
        if (-1 != idx) {
            bsIntfPackage = bsIntfFullName.substring(0, idx);
        }
        return bsIntfPackage;
    }

    /**
     * 对接口没有带I的接口进行处理,按照业务接口规范，为实现类提供名称
     * @return 处理过后的类名称
     */
    private String handleClsName(String bsIntfFullName) {
        int idx = bsIntfFullName.lastIndexOf(".");
        int len = bsIntfFullName.length();
        return bsIntfFullName.substring(idx + 1, len);

    }

    /**
     * 对接口没有带I的接口进行处理,按照业务接口规范，为接口提供名称
     * 已经不再使用
     * @return 处理过后的接口名称
     */
    private String handleIntfName(String bsIntfFullName) {
        int idx = bsIntfFullName.lastIndexOf(".");
        int len = bsIntfFullName.length();
        StringBuffer bsIntfName = new StringBuffer();

        if (bsIntfFullName.endsWith("BO")) {
            bsIntfName.append(bsIntfFullName.substring(idx + 1, len - 2));
        } else {
            bsIntfName.append(bsIntfFullName.substring(idx + 1, len));
        }

        return bsIntfName.toString();

    }

}
