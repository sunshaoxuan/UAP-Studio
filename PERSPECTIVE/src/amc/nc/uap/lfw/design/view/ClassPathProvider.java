package nc.uap.lfw.design.view;

public class ClassPathProvider {
	private static ClassLoader cl;
	public static ClassLoader getClassLoader(){
		return cl;
	}
	
	public static void setClassLoader(ClassLoader cld) {
		cl = cld;
	}
}
