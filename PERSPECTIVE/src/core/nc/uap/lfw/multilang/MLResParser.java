package nc.uap.lfw.multilang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.internal.core.Region;

public class MLResParser {
	
	 public static MLResElement[] parser(File file)
	  {
		 String content = null;
		try {
			content = FileUtilities.fetchFileContent(file, "GBK");
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		    final List list = new ArrayList();
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(content.toCharArray());
			parser.setResolveBindings(true);
			CompilationUnit unit = (CompilationUnit)parser.createAST(null);
			unit.accept(new ASTVisitor() {
				public boolean visit(StringLiteral node){
					String value = node.getLiteralValue();
			        int start = node.getStartPosition();
			        int len = node.getLength();
			        MLResElement ele = new MLResElement(value, start, len);
			        list.add(ele);
			        return super.visit(node);			        
				}
				
			});	
			return ((MLResElement[])list.toArray(new MLResElement[0]));
	  }

}
