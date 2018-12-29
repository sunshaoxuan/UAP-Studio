package nc.uap.lfw.editor.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

public final class BrowserProvider {
	public static Browser getBrowser(Composite parent) {
		Browser browser;
//		int ie = getIEVersion();
//		if(ie >= 9){
//			browser = new Browser(parent, SWT.NONE);
//		}
//		else
			browser = new Browser(parent, SWT.MOZILLA);
		return browser;
	}
	
//	private static int getIEVersion() {
//		int IEVersion = 0;
//		TCHAR key = new TCHAR (0, "Software\\Microsoft\\Internet Explorer", true);	//$NON-NLS-1$
//		int /*long*/ [] phkResult = new int /*long*/ [1];
//		if (OS.RegOpenKeyEx (OS.HKEY_LOCAL_MACHINE, key, 0, OS.KEY_READ, phkResult) == 0) {
//			int [] lpcbData = new int [1];
//			TCHAR buffer = new TCHAR (0, "svcVersion", true); //$NON-NLS-1$
//			int result = OS.RegQueryValueEx (phkResult [0], buffer, 0, null, (TCHAR) null, lpcbData);
//			if (result == 0) {
//				TCHAR lpData = new TCHAR (0, lpcbData [0] / TCHAR.sizeof);
//				result = OS.RegQueryValueEx (phkResult [0], buffer, 0, null, lpData, lpcbData);
//				if (result == 0) {
//					String versionString = lpData.toString (0, lpData.strlen ());
//					int index = versionString.indexOf ("."); //$NON-NLS-1$
//					if (index != -1) {
//						String majorString = versionString.substring (0, index);
//						try {
//							IEVersion = Integer.valueOf (majorString).intValue ();
//						} catch (NumberFormatException e) {
//							/* just continue, version-specific features will not be enabled */
//						}
//					}
//				}
//			}
//			OS.RegCloseKey (phkResult [0]);
//		}
//		return IEVersion;
//	}
}
