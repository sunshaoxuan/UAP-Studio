package nc.uap.portal.wizards;

import org.osgi.framework.BundleContext;

import com.yonyou.uap.studio.lic.UAPStudioProduct;


import nc.uap.lfw.common.DefaultUIPlugin;
public class PortalProjPlugin extends DefaultUIPlugin{
	 private static PortalProjPlugin plugin;
	 /**
     * The constructor.
     */
    public PortalProjPlugin() {
        super();
        plugin = this;
    }
    
    /**
     * Returns the shared instance.
     */
    public static PortalProjPlugin getDefault() {
        return plugin;
    }
    public void start(BundleContext context) throws Exception {
    	UAPStudioProduct.LFW.validateLicenseThrowException(); 
        super.start(context);
    }
    
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }
}
