/**
 * 
 */
package nc.uap.lfw.core.log;

import java.io.PrintStream;

/**
 * @author chouhl
 *
 */
public class LfwSysOutWrapper {

	public static PrintStream getSysStream(){
//		PrintStream ps = System.out;
		PrintStream ps = null;
		return ps;
	}
	
	public static void println(String str){
		PrintStream ps = getSysStream();
//		ps.println(str);
	}
	
}
