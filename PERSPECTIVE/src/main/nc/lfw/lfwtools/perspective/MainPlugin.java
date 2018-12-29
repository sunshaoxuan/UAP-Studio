package nc.lfw.lfwtools.perspective;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import nc.lfw.design.view.LFWConnector;
import nc.uap.lfw.common.DefaultUIPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.data.MdDatasetPool;

import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.IValueVariableListener;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uap.lfw.smartmw.StartEmbedTomcat;

import com.yonyou.uap.studio.lic.UAPStudioProduct;

/**
 * @author guoweic
 * 
 */
public class MainPlugin extends DefaultUIPlugin {
//	    private BundleContext bundleContext;

	  	private ResourceBundle resourceBundle;
	    
	    public final static String ICONS_PATH = "icons/";
	    
	    public static final String ICON_BINDDATA     = "_icon_binddata";
		public static final String ICON_HTML     = "_icon_html";
		public static final String ICON_LIST     = "_icon_list";
		public static final String ICON_FORM     = "_icon_form";
		
	    public static ImageDescriptor getImageDescriptor(String path)
	    {
	        return AbstractUIPlugin.imageDescriptorFromPlugin("nc.uap.lfw.tools.perspective", path);
	    }

	    
		//The shared instance.
		private static MainPlugin plugin;
		
		
	    public MainPlugin()
	    {
	    	plugin = this;
	    	resourceBundle = null;
	    }

	    public void start(BundleContext context)
	        throws Exception{
//	    	System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
//	    	System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
//	    	StudioProduct.LFW.validateLicenseThrowException();
	    	UAPStudioProduct.LFW.validateLicenseThrowException(); 
	    	super.start(context);
			
	    	new Thread(new Runnable() {
					@Override
					public void run() {
						StartEmbedTomcat.start();
					}
				}).start();
	    	 
	        startPerspective();	       	       
	    }

		private void startPerspective() {
			getVariableManager().addValueVariableListener(new IValueVariableListener() {
				@Override
				public void variablesRemoved(IValueVariable[] variables) {
					
				}
				
				@Override
				public void variablesChanged(IValueVariable[] variables) {
					if(variables[0].getName().equals("publish_successed")){
						String[] values = variables[0].getValue().split("@");
						if(values.length==2){
							LFWConnector.clearMDCacheById(values[1]);
							MdDatasetPool.clear();
						}
					}
//					for (IValueVariable val : variables) {
//						String key = val.getName();
//						if(key.equals(WEBPersConstants.FIELD_NC_HOME)){
//							String path = val.getValue();
//							if(path != null && !path.equals("")){
//								if(isUapHome(path))
//									StartEmbedTomcat.restart();
//							}
//						}
//					}
				}

				private boolean isUapHome(String path) {
					File f = new File(path);
					if(f.exists() && f.isDirectory()){
						File[] fs = f.listFiles();
						for (int i = 0; i < fs.length; i++) {
							if(fs[i].getName().equals("modules"))
								return true;
						}
					}
					return false;
				}
				
				@Override
				public void variablesAdded(IValueVariable[] variables) {
					System.out.println("1");
				}
			});
//			ResourcesPlugin.getWorkspace().getPathVariableManager().addChangeListener(new IPathVariableChangeListener() {
//                public void pathVariableChanged(IPathVariableChangeEvent event) {
//                    if (WEBPersConstants.FIELD_NC_HOME.equals(
//                                event.getVariableName())) {
//                        //updateClassPath();
//                    }
//                }
//            });
			
//			getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
//                public void propertyChange(PropertyChangeEvent event) {
//                    String newString = event.getNewValue().toString();
//
//                    if (event.getProperty().equals(WEBPersConstants.FIELD_NC_HOME)) {
//                        try {
//                            getValueVariable(WEBPersConstants.FIELD_NC_HOME,
//                                "The NC_HOME location").setValue(newString);
//
//                            IPathVariableManager pathVarMgr = ResourcesPlugin.getWorkspace()
//                                                                             .getPathVariableManager();
//                            pathVarMgr.setValue(WEBPersConstants.FIELD_NC_HOME,
//                                new Path(event.getNewValue().toString()));
//                        } catch (CoreException e) {
//                        	getDefault().logError(e);
//                        }
//
//                        
//                    }
//
//                    if (event.getProperty().equals(WEBPersConstants.FIELD_CLINET_IP)) {
//                        try {
//                            getValueVariable(WEBPersConstants.FIELD_CLINET_IP,
//                                "CLINET_IP").setValue(newString);
//                            setUrltoProperty();
//                        } catch (CoreException e) {
//                        	getDefault().logError(e);
//                        }
//                    }
//
//                    if (event.getProperty()
//                                 .equals(WEBPersConstants.FIELD_CLINET_PORT)) {
//                        try {
//                            getValueVariable(WEBPersConstants.FIELD_CLINET_PORT,
//                                "CLINET_PORT").setValue(newString);
//                            setUrltoProperty();
//                        } catch (CoreException e) {
//                        	getDefault().logError(e);
//                        }
//                    }
//                }
//            });
	        // Platform.getOS()
//	        getPreferenceStore().setDefault(WEBPersConstants.FIELD_NC_HOME, "c:/nc_home");
//	        getPreferenceStore()
//	            .setDefault(WEBPersConstants.FIELD_CLINET_IP, "127.0.0.1");
//	        getPreferenceStore().setDefault(WEBPersConstants.FIELD_CLINET_PORT, 80);
//	        setUrltoProperty();
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
	        ResourceBundle bundle = MainPlugin.getDefault().getResourceBundle();
	        return bundle == null ? key : bundle.getString(key);
	    }

		/**
		 * Returns the shared instance.
		 */
		public static MainPlugin getDefault() {
			return plugin;
		}

		/**
		 * Returns the plugin's resource bundle,
		 */
		public ResourceBundle getResourceBundle() {
			return resourceBundle;
		}
				
		public void setResourceBundle(ResourceBundle resourceBundle) {
			this.resourceBundle = resourceBundle;
		}

//		private IValueVariable getValueVariable(String varname, String description)
//        	throws CoreException {
//        IStringVariableManager vvManager = getVariableManager();
//        IValueVariable var = vvManager.getValueVariable(varname);
//
//        if (var == null) {
//            var = vvManager.newValueVariable(varname, description);
//            vvManager.addVariables(new IValueVariable[] { var });
//        }
//
//        return var;
//    }


//    private void setUrltoProperty() {
//        try {
//            IValueVariable var = getValueVariable(WEBPersConstants.FIELD_CLINET_IP,
//                    "CLINET_IP");
//            String ip = var.getValue();
//            var = getValueVariable(WEBPersConstants.FIELD_CLINET_PORT, "CLINET_PORT");
//
//            String port = var.getValue();
//            System.setProperty("CLIENT_URL_MDE",
//                MessageFormat.format("http://{0}:{1}", ip, port));
//        } catch (CoreException e) {
//        	MainPlugin.getDefault().logError(e.getMessage(),e);
//        }
//    }

    public static String[] getExModuleNames() {
        return WEBPersPlugin.getVariableManager()
                        .getValueVariable(WEBPersConstants.FIELD_EX_MODULES)
                        .getValue().split(",");
    }

    public static URL makeImageURL(String prefix, String name) {
        String path = prefix + name;
        URL url = null;

        try {
            url = new URL(MainPlugin.getDefault().getBundle().getEntry("/"), path);
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


}
