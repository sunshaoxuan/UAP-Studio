package nc.uap.lfw.build.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;

import org.apache.commons.io.IOUtils;



public class CommonUtils {
	
	/**
	 * ÷¥––√¸¡Ó°£
	 * 
	 * @param cmds
	 * @param inputWriter
	 * @param errorWriter
	 * @return
	 * @throws IOException
	 */
	public static int executeCommand(String[] cmds, Writer inputWriter, Writer errorWriter) throws IOException{
		return executeCommand(cmds, null, inputWriter, errorWriter);
	}
	
	/**
	 * For the case that: the command operation failed but the exit code is 0, and the error stream contains the failure message.
	 * 
	 * @param cmds
	 * @param directory
	 * @param inputWriter
	 * @param errorWriter
	 * @return
	 * @throws IOException
	 */
	public static int executeCommand(String[] cmds, File directory, Writer inputWriter, Writer errorWriter) throws IOException{
		//Test whether the commands contains "cmd.exe"...
		String osName = System.getProperty("os.name");
		List<String> cmdList = new ArrayList<String>();
		if (osName.equals("Windows NT")) {
			cmdList.add("cmd.exe");
			cmdList.add("/C");
		} else if (osName.equals("Windows 2000") || osName.equals("Windows 2003") || osName.equals("Windows XP")) {
			cmdList.add("CMD.exe");
			cmdList.add("/C");
		} else if (osName.equals("Windows 95")) {
			cmdList.add("command.com");
			cmdList.add("/C");
		}
		String[] temp = new String[cmdList.size() + cmds.length];
		System.arraycopy(cmdList.toArray(new String[0]), 0, temp, 0, cmdList.size());
		System.arraycopy(cmds, 0, temp, cmdList.size(), cmds.length);
		
		ProcessBuilder processBuilder = new ProcessBuilder(temp);
		processBuilder.directory(directory);
		Process process = processBuilder.start();
		MessageCollector inputMsgCollector = new MessageCollector(process.getInputStream(), inputWriter);
		inputMsgCollector.start();
		MessageCollector errorMsgCollector = new MessageCollector(process.getErrorStream(), errorWriter);
		errorMsgCollector.start();
		
		int exitCode = 0;
		try {
			exitCode = process.waitFor();
			inputMsgCollector.join();
			errorMsgCollector.join();
			return exitCode;
		} catch (InterruptedException e) {
			MainPlugin.getDefault().logError(null, e);
			return -1;
		}
	}

	static class MessageCollector extends Thread{
		
		Writer writer;
		
		private InputStream is;
		
		public MessageCollector(InputStream is, Writer writer) {
			this.writer = writer;
			this.is = is;
		}

		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			try {
				while((line = br.readLine()) != null){
					writer.write(line);
					writer.write(IOUtils.LINE_SEPARATOR);
				}
			} catch (IOException e) {
				MainPlugin.getDefault().logError("Read ");
			}
		}
	}

}
