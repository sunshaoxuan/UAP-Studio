package nc.uap.lfw.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import nc.uap.lfw.common.DefaultUIPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.lfw.lang.M_core;

import org.eclipse.core.resources.IPathVariableChangeEvent;
import org.eclipse.core.resources.IPathVariableChangeListener;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.framework.BundleContext;

import com.yonyou.uap.studio.lic.UAPStudioProduct;

public class WEBProjPlugin extends DefaultUIPlugin {
    public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$
    private static WEBProjPlugin plugin;
    public static final String PLUGIN_ID = "com.yonyou.studio.web.project";

    /**
     * The constructor.
     */
    public WEBProjPlugin() {
        super();
        plugin = this;
    }


    private void updateClassPath() {
        if (MessageDialog.openConfirm(null, M_core.WEBProjPlugin_0,
                    M_core.WEBProjPlugin_1)) {
            ProjCoreUtility.updateWorkspaceClasspath();
        }
    }

    private IValueVariable getValueVariable(String varname, String description)
        throws CoreException {
        IStringVariableManager vvManager = LfwCommonTool.getVariableManager();
        IValueVariable var = vvManager.getValueVariable(varname);

        if (var == null) {
            var = vvManager.newValueVariable(varname, description);
            vvManager.addVariables(new IValueVariable[] { var });
        }

        return var;
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception {
//    	StudioProduct.LFW.validateLicenseThrowException();
    	UAPStudioProduct.LFW.validateLicenseThrowException(); 
    	
//    	ScanNewVersionTask task = new ScanNewVersionTask();
//        Thread thread = new Thread(task);
//        thread.start();
        
        super.start(context);
        ResourcesPlugin.getWorkspace().getPathVariableManager().addChangeListener(new IPathVariableChangeListener() {
                public void pathVariableChanged(IPathVariableChangeEvent event) {
                    if (WEBProjConstants.FIELD_NC_HOME.equals(
                                event.getVariableName())) {
                        //updateClassPath();
                    }
                }
            });
        getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent event) {
                    String newString = event.getNewValue().toString();

                    if (event.getProperty().equals(WEBProjConstants.FIELD_NC_HOME)) {
                        try {
                            getValueVariable(WEBProjConstants.FIELD_NC_HOME,
                                M_core.WEBProjPlugin_2).setValue(newString);

                            IPathVariableManager pathVarMgr = ResourcesPlugin.getWorkspace()
                                                                             .getPathVariableManager();
                            pathVarMgr.setValue(WEBProjConstants.FIELD_NC_HOME,
                                new Path(event.getNewValue().toString()));
                        } catch (CoreException e) {
                        	getDefault().logError(e);
                        }

                        updateClassPath();
                    }

                    if (event.getProperty().equals(WEBProjConstants.FIELD_CLINET_IP)) {
                        try {
                            getValueVariable(WEBProjConstants.FIELD_CLINET_IP,
                                M_core.WEBProjPlugin_3).setValue(newString);
                            setUrltoProperty();
                        } 
                        catch (CoreException e) {
                        	getDefault().logError(e);
                        }
                    }

                    if (event.getProperty()
                                 .equals(WEBProjConstants.FIELD_CLINET_PORT)) {
                        try {
                            getValueVariable(WEBProjConstants.FIELD_CLINET_PORT,
                                M_core.WEBProjPlugin_4).setValue(newString);
                            setUrltoProperty();
                        } catch (CoreException e) {
                            getDefault().logError(e);
                        }
                    }
                }
            });
        // Platform.getOS()
        getPreferenceStore().setDefault(WEBProjConstants.FIELD_NC_HOME, "c:/nc_home"); //$NON-NLS-1$
        getPreferenceStore()
            .setDefault(WEBProjConstants.FIELD_CLINET_IP, "127.0.0.1"); //$NON-NLS-1$
        getPreferenceStore().setDefault(WEBProjConstants.FIELD_CLINET_PORT, 80);
        setUrltoProperty();
        
        
    }

    private void setUrltoProperty() {
        try {
            IValueVariable var = getValueVariable(WEBProjConstants.FIELD_CLINET_IP,
                    M_core.WEBProjPlugin_5);
            String ip = var.getValue();
            var = getValueVariable(WEBProjConstants.FIELD_CLINET_PORT, M_core.WEBProjPlugin_6);

            String port = var.getValue();
            System.setProperty("CLIENT_URL_MDE", //$NON-NLS-1$
                MessageFormat.format("http://{0}:{1}", ip, port)); //$NON-NLS-1$
        } catch (CoreException e) {
            WEBProjPlugin.getDefault().logError(e.getMessage(),e);
        }
    }


    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     */
    public static WEBProjPlugin getDefault() {
        return plugin;
    }


    public static URL makeImageURL(String prefix, String name) {
        String path = prefix + name;
        URL url = null;

        try {
            url = new URL(WEBProjPlugin.getDefault().getBundle().getEntry("/"), path); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            return null;
        }

        return url;
    }

    public static ImageDescriptor loadImage(String path, String imgName) {
        return ImageDescriptor.createFromURL(makeImageURL(path, imgName));
    }

    public static String getExceptJarNames() {
        return getPreferenceStoreString(WEBProjConstants.EXCEPT_JAR_NC_HOME,
            "nc.bs.framework.tool.config.+.jar\ntestbill.+.jar\n.*PROXY.jar"); //$NON-NLS-1$
    }

    public static String[] getModuleNames() {
        return getPreferenceStoreString(WEBProjConstants.MODULES_NC_HOME,
            "uap/uapqe/uapbd").split("/"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static String getPreferenceStoreString(String id, String defvalue) {
        String string = getDefault().getPreferenceStore().getString(id);

        return (string.length() > 1) ? string : defvalue;
    }
}
