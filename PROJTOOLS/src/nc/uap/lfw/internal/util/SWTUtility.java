/*
 * Created on 2005-8-11
 * @author ºÎ¹ÚÓî
 */
package nc.uap.lfw.internal.util;

import nc.uap.lfw.core.WEBProjPlugin;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

/*
 * @author ºÎ¹ÚÓî
 */
public class SWTUtility {
    public static Display getStandardDisplay() {
		Display display;
		display = Display.getCurrent();
		if (display == null)
			display = Display.getDefault();
		return display;
	}
    
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return WEBProjPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
	}
	
	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow w = getActiveWorkbenchWindow();
		if (w != null) {
			return w.getActivePage();
		}
		return null;
	}
	
	
	
	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}
}
