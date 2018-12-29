package uap.lfw.smartmw;

import java.net.URL;

public class LoaderHelper {

	public static URL[] getAllUrls(StartMode smart) {
		URL[] urls = null;
		switch(smart){
			case SMART:
				urls = new SmartLoaderUtil().getSmartURL();
				break;
			default:
				urls = getFullUrl();
		}
		return urls;
		
	}
	
	private static URL[] getFullUrl() {
		return null;
	}


}
