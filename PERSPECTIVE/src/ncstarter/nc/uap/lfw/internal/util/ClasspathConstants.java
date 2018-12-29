package nc.uap.lfw.internal.util;

import java.net.URL;

public final class ClasspathConstants {
    //common, can't  but public
    public static String SOURCE = "source/";
    public static String  CLASSES ="classes/";
    public static String  RESOURCES = "resources/";
    public static String LIB = "lib";
    
    
    //server
    public static String SERVER_CLASSES = "_classes/";
    public static String SERVER_LIB = "_lib";
    public static String SERVER_RESOURCES = "_resources/";
    
    public static String[] JAR_POSTFIX = new String[] { ".jar", ".zip"};
    
    public static String[] SRC_JAR_POSTFIX = new String[] {"_src.jar", "_src.zip"};    
    
    //private
    public static String PRIVATE_LIB = "META-INF/lib";
    public static String PRIVATE_CLASSES = "META-INF/classes/";
    public static String PRIVATE_RESOURCES = "META-INF/resources/";
    
    public static String MODULE_DEPLOY_XML = "META-INF/module.xml";
    
    //var
    public static String VAR_CLASSES = "var/classes/";
    public static String VAR_LIB = "var/lib";
    
    public static String EXTENSION_CLASSES="extension/classes";
    public static String EXTENSION_LIB="extension/lib";
    
    public static String ENABLE_HOT_DEPLOY_PROPERTY ="nc.enableHotDeploy";
    public static String DEFAULT_ENABLE_HOT_DEPLOY = "true";
    
    
    public static final URL[] EMPTY_URL_ARRAY = {};
    public static final String WLS_JNDI_CONTEXT = "weblogic.jndi.WLInitialContextFactory";
    public static final String INITIAL_CONTEXT_FACTORY_PROPERTY = "nc.jndi.InitialContextFactory";
    public static final String WAS_JNDI_CONTEXT = "com.ibm.websphere.naming.WsnInitialContextFactory";
}
