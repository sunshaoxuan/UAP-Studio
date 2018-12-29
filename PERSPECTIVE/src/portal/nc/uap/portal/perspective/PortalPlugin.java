package nc.uap.portal.perspective;


import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.DefaultUIPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.portal.freemarker.preferences.IPreferenceConstants;

import org.eclipse.core.resources.IPathVariableChangeEvent;
import org.eclipse.core.resources.IPathVariableChangeListener;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.yonyou.uap.studio.lic.UAPStudioProduct;

/**
 * @author dingrf
 * 
 */
public class PortalPlugin extends DefaultUIPlugin implements IPreferenceConstants {
//	    private BundleContext bundleContext;
	    
	    public final static String ICONS_PATH = "icons/";
	    
	    public static final String ICON_BINDDATA     = "_icon_binddata";
		public static final String ICON_HTML     = "_icon_html";
		public static final String ICON_LIST     = "_icon_list";
		public static final String ICON_FORM     = "_icon_form";
		
	    public static ImageDescriptor getImageDescriptor(String path)
	    {
	        return AbstractUIPlugin.imageDescriptorFromPlugin("nc.uap.lfw.tools.portalperspective", path);
	    }

	    
		//The shared instance.
		private static PortalPlugin plugin;
		//Resource bundle.
		private ResourceBundle resourceBundle;
		
	    public PortalPlugin()
	    {
	    	super();
	    	plugin = this;
			try {
				resourceBundle = ResourceBundle.getBundle("nc.uap.portal.perspective.resources");
			} catch (MissingResourceException x) {
				resourceBundle = null;
			}
	    }

	    public void start(BundleContext context)
	        throws Exception
	    {
	    	UAPStudioProduct.LFW.validateLicenseThrowException(); 
//	    	StudioProduct.LFW.validateLicenseThrowException();
	    	super.start(context);
			
	        startPerspective();
	    }

		private void startPerspective() {
			ResourcesPlugin.getWorkspace().getPathVariableManager().addChangeListener(new IPathVariableChangeListener() {
                public void pathVariableChanged(IPathVariableChangeEvent event) {
                    if (WEBPersConstants.FIELD_NC_HOME.equals(
                                event.getVariableName())) {
                        //updateClassPath();
                    }
                }
            });
			
			getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent event) {
                    String newString = event.getNewValue().toString();

                    if (event.getProperty().equals(WEBPersConstants.FIELD_NC_HOME)) {
                        try {
                            getValueVariable(WEBPersConstants.FIELD_NC_HOME,
                                "The NC_HOME location").setValue(newString);

                            IPathVariableManager pathVarMgr = ResourcesPlugin.getWorkspace()
                                                                             .getPathVariableManager();
                            pathVarMgr.setValue(WEBPersConstants.FIELD_NC_HOME,
                                new Path(event.getNewValue().toString()));
                        } catch (CoreException e) {
                        	getDefault().logError(e);
                        }

                        
                    }

                    if (event.getProperty().equals(WEBPersConstants.FIELD_CLINET_IP)) {
                        try {
                            getValueVariable(WEBPersConstants.FIELD_CLINET_IP,
                                "CLINET_IP").setValue(newString);
                            setUrltoProperty();
                        } catch (CoreException e) {
                        	getDefault().logError(e);
                        }
                    }

                    if (event.getProperty()
                                 .equals(WEBPersConstants.FIELD_CLINET_PORT)) {
                        try {
                            getValueVariable(WEBPersConstants.FIELD_CLINET_PORT,
                                "CLINET_PORT").setValue(newString);
                            setUrltoProperty();
                        } catch (CoreException e) {
                        	getDefault().logError(e);
                        }
                    }
                }
            });
	        // Platform.getOS()
	        getPreferenceStore().setDefault(WEBPersConstants.FIELD_NC_HOME, "c:/nc_home");
	        getPreferenceStore()
	            .setDefault(WEBPersConstants.FIELD_CLINET_IP, "127.0.0.1");
	        getPreferenceStore().setDefault(WEBPersConstants.FIELD_CLINET_PORT, 80);
	        setUrltoProperty();
		}

	    public void stop(BundleContext context)
	        throws Exception
	    {
	        super.stop(context);
	        
			plugin = null;
	        resourceBundle = null;
//	        bundleContext = null;
	    }

//	    public BundleContext getBundleContext()
//	    {
//	        return bundleContext;
//	    }

	    public static String getResourceString(String key)
	    {
	        ResourceBundle bundle = PortalPlugin.getDefault().getResourceBundle();
	        return bundle == null ? key : bundle.getString(key);
	    }

		/**
		 * Returns the shared instance.
		 */
		public static MainPlugin getDefault() {
//			if(plugin == null)
//				return MainPlugin.getDefault().;
//			return plugin;
			return MainPlugin.getDefault();
		}

		/*
		 * ¼æÈÝ Freemark
		 */
//		public static PortalPlugin getInstance() {
//			return plugin;
//		}

		/**
		 * Returns the plugin's resource bundle,
		 */
		public ResourceBundle getResourceBundle() {
			return resourceBundle;
		}
		
		private IValueVariable getValueVariable(String varname, String description)
        	throws CoreException {
        IStringVariableManager vvManager = getVariableManager();
        IValueVariable var = vvManager.getValueVariable(varname);

        if (var == null) {
            var = vvManager.newValueVariable(varname, description);
            vvManager.addVariables(new IValueVariable[] { var });
        }

        return var;
    }


    private void setUrltoProperty() {
        try {
            IValueVariable var = getValueVariable(WEBPersConstants.FIELD_CLINET_IP,
                    "CLINET_IP");
            String ip = var.getValue();
            var = getValueVariable(WEBPersConstants.FIELD_CLINET_PORT, "CLINET_PORT");

            String port = var.getValue();
            System.setProperty("CLIENT_URL_MDE",
                MessageFormat.format("http://{0}:{1}", ip, port));
        } catch (CoreException e) {
           PortalPlugin.getDefault().logError(e.getMessage(),e);
        }
    }

    public static String[] getExModuleNames() {
        return MainPlugin.getVariableManager()
                        .getValueVariable(WEBPersConstants.FIELD_EX_MODULES)
                        .getValue().split(",");
    }

    public static URL makeImageURL(String prefix, String name) {
        String path = prefix + name;
        URL url = null;

        try {
            url = new URL(PortalPlugin.getDefault().getBundle().getEntry("/"), path);
        } catch (MalformedURLException e) {
            return null;
        }

        return url;
    }

    public static ImageDescriptor loadImage(String path, String imgName) {
        return ImageDescriptor.createFromURL(makeImageURL(path, imgName));
    }

    public static IStringVariableManager getVariableManager() {
        return VariablesPlugin.getDefault().getStringVariableManager();
    }

    public static String getExceptJarNames() {
        return getPreferenceStoreString(WEBPersConstants.EXCEPT_JAR_NC_HOME,
            "nc.bs.framework.tool.config.+.jar\ntestbill.+.jar\n.*PROXY.jar");
    }

    public static String[] getModuleNames() {
        return getPreferenceStoreString(WEBPersConstants.MODULES_NC_HOME,
            "uap/uapqe/uapbd").split("/");
    }

    public static String getPreferenceStoreString(String id, String defvalue) {
        String string = getDefault().getPreferenceStore().getString(id);

        return (string.length() > 1) ? string : defvalue;
    }

	/**
	 * Initializes the plugin preferences with default preference values for
	 * this plug-in.
	 */
	protected void initializeDefaultPluginPreferences() {
		Preferences prefs = getPluginPreferences();
		prefs.setDefault(HIGHLIGHT_RELATED_ITEMS, true);
		prefs.setDefault(COLOR_COMMENT, "170,0,0");
		prefs.setDefault(COLOR_TEXT, "0,0,0");
		prefs.setDefault(COLOR_INTERPOLATION, "255,0,128");
		prefs.setDefault(COLOR_DIRECTIVE, "0,0,255");
		prefs.setDefault(COLOR_STRING, "0,128,128");
		prefs.setDefault(COLOR_XML_COMMENT, "128,128,128");
		prefs.setDefault(COLOR_XML_TAG, "0,0,128");
		prefs.setDefault(COLOR_RELATED_ITEM, "255,255,128");
	}
    

}
