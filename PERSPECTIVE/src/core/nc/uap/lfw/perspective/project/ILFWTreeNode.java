package nc.uap.lfw.perspective.project;

import java.io.File;

/**
 * Ê÷ÐÍ½Úµã
 * @author zhangxya
 *
 */
public interface ILFWTreeNode {
	public static final String PARENT_NODE_FOLDER = "PARENT_NODE_FOLDER";
	public static final String PARENT_PUB_REF_FOLDER = "PARENT_PUB_REF_FOLDER";
	public static final String NODE_FOLDER = "NODE_FOLDER";
	public static final String PUB_REF_FOLDER = "PUB_REF_FOLDER";

	public static final String PARENT_PUB_DS_FOLDER = "PARENT_PUB_DS_FOLDER";
	
	public static final String PARENT_PUB_WIDGET_FOLDER = "PARENT_PUB_WIdget_FOLDER";
	
	public static final String PAGEFLOW_FOLDER = "PAGEFLOW_FOLDER";
	
	public static final String PORTAL_DEFINE = "PORTAL_DEFINE";
	
	public static String PORTALDEFINE="PORTALDEFINE";
	public static String PORTALPAGE="PORTALPAGE";
	public static String PORTLET="PORTLET";
	public static String PORTLETS="PORTLETS";
	public static String MANAGERAPPS="MANAGERAPPS";
	public static String THEMES = "THEMES";
	public static String PLUGIN = "PLUGIN";
	public static String EVENTS = "EVENTS";
	public static String PORTAL = "PORTAL";
	public static String PORTALMANPAGE="PORTALMANPAGE";
	public static String PORTALUSERPAGE="PORTALUSERPAGE";
	public static String MANAEXTENPOINT="MANAEXTENPOINT";
	public static String PORTALMANAGE="PORTALMANAGE";
	public static String CMSMANAGE="CMSMANAGE";
	public static String PORTLETTHEMEEDIT="PORTLETTHEMEEDIT";
	public static String LAYOUTTHEME="LAYOUTTHEME";
	public static String POOLDSFOLDER = "POOLDSFOLDER";
	public static String POOLWIDGETFOLDER = "poolWidgetFolder";
	public static String POOLREFNODEFOLDER = "POOLREFNODEFOLDER";
	
	public static final String PROJECTS_FOLDER = "PROJECTS_FOLDER";
	public static final String METADATA_FOLDER = "METADATA_FOLDER";
	public static final String METADATA = "METADATA";
	public static final String UIS_FOLDER = "UIS_FOLDER";
	public static final String SRC_FOLDER = "SRC_FOLDER";
	public static final String WFM_FOLDER = "WFM_FOLDER";
	public static final String DOC_FOLDER = "DOC_FOLDER";
	public static final String FUNC_FOLDER = "FUNC_FOLDER";
	public static final String PRINT_FOLDER = "PRINT_FOLDER";
	public static final String CONFIG_FOLDER = "CONFIG_FOLDER";
	public static final String SERVICE_FOLDER = "SERVICE_FOLDER";
	public static final String MLR_FOLDER = "MLR_FOLDER";
	public static final String LANG_FOLDER = "LANG_FOLDER";
	public static final String JAVALANG_FOLDER = "JAVALANG_FOLDER";
	public static final String TEMPLATE_FOLDER = "TEMPLATE_FOLDER";
	public static final String QUERY_FOLDER = "QUERY_FOLDER";
	public static final String BUILD_FOLDER = "BUILD_FOLDER";
	public static final String APPLICATION_FOLDER = "APPLICATION_FOLDER";
	public static final String APPLICATION = "APPLICATION";
	public static final String MODEL_FOLDER = "MODEL_FOLDER";
	public static final String PDM_FOLDER = "PDM_FOLDER";
	public static final String SCRIPT_FOLDER = "SCRIPT_FOLDER";
	public static final String MAPPING_FOLDER = "MAPPING_FOLDER";
	public static final String BUILDDISK_FOLDER = "BUILDDISK_FOLDER";
	public static final String DOMAIN_BUILD_CONFIG = "DOMAIN_BUILD_CONFIG";
	public static final String BUILD_CONFIG = "BUILD_CONFIG";
//	public static final String MODEL = "MODEL";
	public static final String COMPONENT = "COMPONENT";
	public static final String WINDOW_COMPONENT = "WINDOW_COMPONENT";
	public static final String PUBVIEW_COMPONENT = "PUBVIEW_COMPONENT";
	public static final String WINDOW_FOLDER = "WINDOW_FOLDER";
	public static final String WINDOW = "WINDOW";
	public static final String VIEW = "VIEW";
	public static final String PUBLIC_VIEW_FOLDER = "PUBLIC_VIEW_FOLDER";
	public static final String PUBLIC_VIEW = "PUBLIC_VIEW";
	public static final String REFINFO = "REFINFO";
	
	public static final String WFM_FLWCATE = "WFM_FLWCATE";
	
	public File getFile();
	public void deleteNode();
	public String getIPathStr();
}
