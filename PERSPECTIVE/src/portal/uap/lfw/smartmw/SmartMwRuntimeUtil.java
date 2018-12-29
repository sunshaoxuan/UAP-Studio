package uap.lfw.smartmw;

public class SmartMwRuntimeUtil {
	public static String getUapHome() {
		String uapHome = System.getProperty("nc.lfw.location");
		return uapHome;
	}
}
