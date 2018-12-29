package nc.uap.lfw.multilang;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_multilang;
import nc.uap.lfw.tool.Helper;
import nc.uap.lfw.tool.UTFProperties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.jdt.internal.corext.refactoring.Checks;
import org.eclipse.jdt.internal.corext.refactoring.changes.TextChangeCompatibility;
import org.eclipse.jdt.internal.corext.refactoring.nls.KeyValuePair;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jface.text.Region;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

public class MLRRefactoring extends Refactoring {

	/** project */
	private IProject project;
	// /** 资源文件模块名称*/
	private IFolder folder;
	// /** 资源文件夹名称*/
	private List<MLResSubstitution> substitutionsList;
	// private MLResSubstitution rawSubstitutions[];
	private MLResSubstitution substitutions[];

	private MLResSubstitution[] rawSubstitutions = null;

	private String expType = "module"; //$NON-NLS-1$

	/** 当前PageNode */
	private PageNode currentPageNode;

	private String resourceHomePath;
	/** xml文件内容 */
	private Map<String, String> contents;
	/** xml文件原始内容 */
	private Map<String, String> oldContents;
	/** 已有多语资源Cache */
	/** page2 */
	private ExternalizeMLRWizardPage page;

	/** 源文件发生改变 */
	private boolean sourceFileChanged = false;

	/** 生成的key列表 */
	private List<String> newKeyList;

	private boolean flag;

	private String resModuleName = ""; //$NON-NLS-1$
	private String resFileName = "multiResource"; //$NON-NLS-1$

	private boolean ignoreRepeatedMLR = true;

	private Set<String> langModuleSet = new HashSet();

	private HashMap<String, UTFProperties> hmProps = new HashMap();
	
	private List<String> modifyFilePath;

	// private String prefix = "";

	private boolean includeChinese = true;
	private boolean includeLetter = false;
	private boolean includeDigit = false;
	private boolean includeOther = false;

	public MLRRefactoring(IProject project) {
		this.project = project;
	}

	public MLRRefactoring(IFolder folder) {
		this.folder = folder;
	}

	public String getResouceHomePath() {
		if (this.resourceHomePath == null) {
			String prefix = ""; //$NON-NLS-1$
			if (LfwCommonTool.isBCPProject(project)) {
				resourceHomePath = new StringBuilder(folder.getFullPath().segment(1)).append("/resources").toString(); //$NON-NLS-1$
			} else
				resourceHomePath = "/resources"; //$NON-NLS-1$
		}
		return this.resourceHomePath;
	}

	@SuppressWarnings("unchecked")
	public void createRawSubstitution(List<PageNode> pageNodes) {
		List list = new ArrayList();
		for (PageNode p : pageNodes) {
			if (!p.isFile())
				continue;
			File file = new File(p.getPath());

			try {
				List nodeList = new ArrayList();
				MLResElement[] eles = MLResParser.parser(file);
				if (this.contents == null)
					this.contents = new HashMap<String, String>();
				this.contents.put(p.getPath(),
						FileUtilities.fetchFileContent(file, "GBK")); //$NON-NLS-1$
				int count = (eles == null) ? 0 : eles.length;
				String eleName = file.getName();
				int index = eleName.lastIndexOf("."); //$NON-NLS-1$
				String prefix = ""; //$NON-NLS-1$
				if (index != -1)
					prefix = eleName.substring(0, index) + "-"; //$NON-NLS-1$
				else
					prefix = eleName + "-"; //$NON-NLS-1$
				for (int i = 0; i < count; ++i) {
					MLResSubstitution substitution = new MLResSubstitution(
							eles[i], Helper.stripQuotes(eles[i].getValue()));
					substitution.setPageNode(p);
					substitution.setPrefix(prefix);
					nodeList.add(substitution);
					list.add(substitution);
				}
				p.setSubstitutions((MLResSubstitution[]) nodeList
						.toArray(new MLResSubstitution[0]));
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
		rawSubstitutions = (MLResSubstitution[]) list
				.toArray(new MLResSubstitution[0]);
		substitutions = rawSubstitutions;
	}

	public void updateSubstitutionsByOption(boolean chinese, boolean letter,
			boolean digital, boolean otherSign) {
		List list = new ArrayList();
		int count = (this.rawSubstitutions == null) ? 0
				: this.rawSubstitutions.length;
		for (int i = 0; i < count; ++i) {
			MLResSubstitution sub = this.rawSubstitutions[i];
			String value = sub.getElement().getValue();
			if ((chinese) && (value.getBytes().length != value.length())) {
				list.add(sub);
			} else if ((letter) && (Helper.containsLetters(value))) {
				list.add(sub);
			} else if ((digital) && (Helper.containsDigist(value))) {
				list.add(sub);
			} else if ((otherSign) && (Helper.containsOtherSign(value)))
				list.add(sub);

		}
		this.substitutions = ((MLResSubstitution[]) list
				.toArray(new MLResSubstitution[0]));
		refreshSubstitutions();
	}

	protected boolean updateSameValueSubstitutions(MLResSubstitution sub) {
		boolean update = false;
		if (isIgnoreRepeatedMLR()) {
			String value = sub.getValue();
			int count = (this.substitutions == null) ? 0
					: this.substitutions.length;
			for (int i = 0; i < count; ++i) {
				MLResSubstitution temp = this.substitutions[i];
				if (value.equals(temp.getValue())) {
					update = true;
					temp.setEnglishValue(sub.getEnglishValue());
					temp.setTwValue(sub.getTwValue());
				}
			}
		}
		return update;
	}

	public void updateKey() {
		int count = (this.substitutions == null) ? 0
				: this.substitutions.length;
		UTFProperties props = (UTFProperties) this.hmProps
				.get(this.resModuleName);
		for (int i = 0; i < count; ++i) {
			MLResSubstitution sub = this.substitutions[i];
			sub.setKey(""); //$NON-NLS-1$
		}

		HashMap hm = new HashMap();
		for (int i = 0; i < count; ++i) {
			MLResSubstitution sub = this.substitutions[i];
			String value = sub.getValue();
			String key = null;
			if ((isIgnoreRepeatedMLR()) && (hm.containsKey(value))) {
				key = (String) hm.get(value);
				sub.setKey(key);
			} else {
				key = sub.createKey(this.substitutions, props);
				hm.put(value, key);
			}
		}
	}

	private void updateSubstitutions() {
		int count = (this.substitutions == null) ? 0
				: this.substitutions.length;
		HashMap<PageNode, List> nodemap = new HashMap<PageNode, List>();
		for (int i = 0; i < count; ++i) {
			MLResSubstitution sub = this.substitutions[i];
			PageNode pg = sub.getPageNode();
			if (nodemap.get(pg) == null) {
				nodemap.put(pg, new ArrayList<MLResSubstitution>());
			}
			nodemap.get(pg).add(sub);
			updateSubstitution(sub);
		}
		for (PageNode p : page.getPageNodes()) {
			if (nodemap.get(p) != null) {
				MLResSubstitution[] pagestitutions = (MLResSubstitution[]) nodemap
						.get(p).toArray(new MLResSubstitution[0]);
				p.setSubstitutions(pagestitutions);
			} else
				p.setSubstitutions(new MLResSubstitution[0]);
		}
	}

	protected void updateSubstitution(MLResSubstitution sub) {
		String value = sub.getValue();
		UTFProperties props = (UTFProperties) this.hmProps
				.get(this.resModuleName);
		sub.setLangModule(this.resModuleName);
		sub.setState(0);
		String langModule = this.resModuleName;
		String key = Helper.getKeyByValue(props, value);
		if (key == null) {
			Iterator iter = this.hmProps.keySet().iterator();
			while (iter.hasNext()) {
				String str = (String) iter.next();
				UTFProperties utfProp = (UTFProperties) this.hmProps.get(str);
				key = Helper.getKeyByValue(utfProp, value);
				if (key != null) {
					langModule = str;
					break;
				}
			}
		}
		if (key != null) {
			sub.setExtKey(key);
			sub.setExtLangModule(langModule);
			sub.setState(1);
		} else {
			sub.setExtKey(""); //$NON-NLS-1$
			sub.setExtLangModule(""); //$NON-NLS-1$
		}
	}

	private void refreshPrefix() {
		// int count = (this.substitutions == null) ? 0
		// : this.substitutions.length;
		// for (int i = 0; i < count; ++i)
		// this.substitutions[i].setPrefix(this.prefix);
	}

	protected void refreshSubstitutions() {
		Set langModules = new HashSet(this.langModuleSet);
		langModules.add(this.resModuleName);
		this.hmProps = Helper.getSimpchnMLResPropsHM(getProject(),
				getResouceHomePath(), new ArrayList(langModules));
		// refreshPrefix();
		updateSubstitutions();
		updateKey();
	}

	public void updateSubPostion(PageNode pageNode) {
		PageNode p = null;
		if (pageNode != null)
			p = pageNode;
		else if (currentPageNode != null)
			p = currentPageNode;
		if (p == null)
			return;
		int count = substitutions != null ? substitutions.length : 0;
		// 更新位置信息
		for (int i = 0; i < count; i++) {
			if (substitutions[i].getPageNode() != p)
				continue;
			if (substitutions[i].getState() == 4) {
				// String value = ProjConstants.I18NNAME + "=\"" +
				// substitutions[i].getSelectKey() + "\"";;
				// String value = substitutions[i].getSelectKey();
				// substitutions[i].getElement().setFPosition(this.contents.get(p.getPath()).indexOf(value),
				// value.length());
			} else if (substitutions[i].getState() == 5) {
				// String value = substitutions[i].getSelectKey();
				// substitutions[i].getElement().setFPosition(this.contents.get(p.getPath()).indexOf(value),
				// value.length());
			} else {
				// String newKey = ProjConstants.I18NNAME + "=\"" +
				// substitutions[i].getRealKey() + "\"";
				// String newKey = substitutions[i].getSelectKey();
				// substitutions[i].getElement().setFPosition(this.contents.get(p.getPath()).indexOf(newKey),
				// newKey.length());
			}
		}
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		// TODO Auto-generated method stub
		return new RefactoringStatus();
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		try {
			// RefactoringStatus localRefactoringStatus1;
			pm.beginTask(M_multilang.MLRRefactoring_0, 3);
			RefactoringStatus result = new RefactoringStatus();
			result.merge(Checks.validateModifiesFiles(getAllFilesToModify(),
					getValidationContext()));
			if ((!(needModifyPropFile())) && (!(needModifySourceFile())))
				result.addFatalError(M_multilang.MLRRefactoring_1);
			try {
				IFile fsimpchn = getResBoundleFile("simpchn"); //$NON-NLS-1$
				if ((fsimpchn.exists())
						&& (!(fsimpchn.getCharset().equalsIgnoreCase("UTF-16")))) { //$NON-NLS-1$
					fsimpchn.setCharset("UTF-16", pm); //$NON-NLS-1$
				}

				IFile ftradchn = getResBoundleFile("tradchn"); //$NON-NLS-1$
				if (ftradchn.exists()) {
					ftradchn.setCharset("UTF-16", pm); //$NON-NLS-1$
				}

				IFile fenglis = getResBoundleFile("english"); //$NON-NLS-1$
				if (fenglis.exists()) {
					fenglis.setCharset("UTF-16", pm); //$NON-NLS-1$
				}

			} catch (Exception e) {
				result.addFatalError(e.getClass() + ":" + e.getMessage()); //$NON-NLS-1$
			}
			return result;
		} finally {
			pm.done();
		}
	}

	private IFile[] getAllFilesToModify() {
		List list = new ArrayList();
		if (needModifySourceFile()) {
			// IResource resource = this.cu.getResource();
			// if (resource.exists())
			// list.add((IFile)resource);
		}

		if (needModifyPropFile()) {
			IFile fsimpchn = getResBoundleFile("simpchn"); //$NON-NLS-1$
			if (fsimpchn.exists())
				list.add(fsimpchn);

			IFile ftradchn = getResBoundleFile("tradchn"); //$NON-NLS-1$
			if (ftradchn.exists())
				list.add(ftradchn);

			IFile fenglis = getResBoundleFile("english"); //$NON-NLS-1$
			if (fenglis.exists())
				list.add(fenglis);
		}

		return ((IFile[]) list.toArray(new IFile[0]));
	}

	public IFile getResBoundleFile(String langCode) {
		return Helper.getResBoundleFile(getProject(), getResouceHomePath(),
				langCode, this.resModuleName, this.resFileName);
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		try {
			CompositeChange compositechange;
			pm.beginTask("", 3); //$NON-NLS-1$
			CompositeChange change = new CompositeChange(M_multilang.MLRRefactoring_2);

			if (needModifyPropFile()) {
				addResFileModifyChange(change, "simpchn"); //$NON-NLS-1$
				addResFileModifyChange(change, "english"); //$NON-NLS-1$
				addResFileModifyChange(change, "tradchn"); //$NON-NLS-1$
			}

			pm.worked(1);
			if (needModifySourceFile()) {
				createModifySourceChange(change, pm);
			}

			pm.worked(1);
			compositechange = change;
			return compositechange;
		} finally {
			pm.done();
		}
	}

	private boolean needModifySourceFile() {
		boolean b = false;
		int count = (this.substitutions == null) ? 0
				: this.substitutions.length;
		for (int i = 0; i < count; ++i) {
			MLResSubstitution sub = this.substitutions[i];
			if (sub.getState() == 2)
				continue;
			b = true;
			break;
		}

		return b;
	}

	private boolean needModifyPropFile() {
		boolean b = false;
		int count = (this.substitutions == null) ? 0
				: this.substitutions.length;
		for (int i = 0; i < count; ++i) {
			MLResSubstitution sub = this.substitutions[i];
			if (sub.getState() == 0) {
				b = true;
				break;
			}
		}

		return b;
	}

	private void addResFileModifyChange(CompositeChange change, String langCode)
			throws CoreException {
		CreateResFileChange cfc = createFileChange(langCode);
		if (cfc != null) {
			change.add(cfc);
		} else {
			TextFileChange tfc = createTextFileChange(langCode);
			change.add(tfc);
		}
	}

	private void createModifySourceChange(CompositeChange change,
			IProgressMonitor pm) throws CoreException {

		for (PageNode p : page.getPageNodes()) {
			if (!p.isFile())
				continue;
			MLResSubstitution[] nodesitution = p.getSubstitutions();
			int count = (nodesitution == null) ? 0 : nodesitution.length;
			if (count == 0)
				continue;

			IFile ifile = p.getFile();
			if(modifyFilePath==null) modifyFilePath = new ArrayList<String>();
			modifyFilePath.add(p.getPath());
			ICompilationUnit cu = JavaCore.createCompilationUnitFrom(ifile);

			TextChange c = new CompilationUnitChange(M_multilang.MLRRefactoring_3
					+ cu.getElementName(), cu);
			MultiTextEdit multiTextEdit = new MultiTextEdit();
			c.setEdit(multiTextEdit);

			String accessName = createImportForAccessor(multiTextEdit, cu);

			for (int i = 0; i < count; ++i) {
				MLResSubstitution sub = nodesitution[i];
				if (sub.getState() == 2)
					continue;
				String langModule = sub.getRealLangModule();
				String key = sub.getRealKey();
				String value = sub.getValue();
				Region position = sub.getElement().getPosition();
				String argStr = sub.getElement().getArgStr();
				String source = ""; //$NON-NLS-1$
				if ((argStr == null) || (argStr.trim().length() == 0))
					source = accessName + ".getStrByID(\"" + langModule //$NON-NLS-1$
							+ "\", \"" + key + "\")/*" //$NON-NLS-1$ //$NON-NLS-2$
							+ Helper.unwindEscapeChars(value) + "*/"; //$NON-NLS-1$
				else
					source = accessName + ".getStrByID(\"" + langModule //$NON-NLS-1$
							+ "\", \"" + key + "\", null, " + argStr + ")/*" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ Helper.unwindEscapeChars(value) + "*/"; //$NON-NLS-1$

				TextChangeCompatibility.addTextEdit(
						c,
						M_multilang.MLRRefactoring_4 + value + "\" ", //$NON-NLS-2$
						new ReplaceEdit(position.getOffset(), position
								.getLength(), source));
			}
			change.add(c);
		}
	}

	private String createImportForAccessor(MultiTextEdit parent,
			ICompilationUnit cu) throws CoreException {
		IJavaElement ele = cu.getParent().getParent();
//		String mlJC = "nc.vo.ml.NCLangRes4VoTransl"; //$NON-NLS-1$
//		String mStr = ".getNCLangRes()"; //$NON-NLS-1$
//		if (ele instanceof IPackageFragmentRoot) {
//			IPackageFragmentRoot pr = (IPackageFragmentRoot) ele;
//			String prName = pr.getElementName();
//			if (prName.equalsIgnoreCase("private")) { //$NON-NLS-1$
//				mlJC = "nc.bs.ml.NCLangResOnserver"; //$NON-NLS-1$
//				mStr = ".getInstance()"; //$NON-NLS-1$
//			} else if (prName.equalsIgnoreCase("client")) { //$NON-NLS-1$
//				mlJC = "nc.ui.ml.NCLangRes"; //$NON-NLS-1$
//				mStr = ".getInstance()"; //$NON-NLS-1$
//			}
//		}
		String mlJC = "uap.lfw.core.ml.LfwResBundle"; 
		String mStr = ".getInstance()"; 
		IJavaProject javaProject = JavaCore.create(getProject());
		IType mlJava = javaProject.findType(mlJC);

		String fullyQualifiedName = mlJava.getFullyQualifiedName();
		ImportRewrite importRewrite = CodeStyleConfiguration
				.createImportRewrite(cu, true);

		String nameToUse = importRewrite.addImport(fullyQualifiedName);
		TextEdit edit = importRewrite.rewriteImports(null);
		parent.addChild(edit);
		return nameToUse + mStr;
	}

	private TextFileChange createTextFileChange(String langCode)
			throws CoreException {
		TextFileChange change = new TextFileChange(M_multilang.MLRRefactoring_5,
				getResBoundleFile(langCode));
		MyPropertyFileDocumentModel model = new MyPropertyFileDocumentModel(
				change.getCurrentDocument(new NullProgressMonitor()));
		int count = (this.substitutions == null) ? 0
				: this.substitutions.length;
		List keyList = new ArrayList();
		for (int i = 0; i < count; ++i) {
			MLResSubstitution sub = this.substitutions[i];
			String key = sub.getKeyWithPrefix();
			if (sub.getState() == 0) {
				if (keyList.contains(key))
					continue;
				keyList.add(key);
				String value = getSubValue(sub, langCode);
				KeyValuePair curr = new KeyValuePair(key,
						Helper.unwindEscapeChars(value));
				InsertEdit edit = model.insert(curr);
				TextChangeCompatibility.addTextEdit(change, M_multilang.MLRRefactoring_6 + key + "=" //$NON-NLS-2$
						+ value, edit);
			}
		}
		return change;
	}

	private String getSubValue(MLResSubstitution sub, String langCode) {
		String value = sub.getValue();
		if (langCode.equals("english")) //$NON-NLS-1$
			value = sub.getEnglishValue();
		else if (langCode.equals("tradchn")) //$NON-NLS-1$
			value = sub.getTwValue();
		return value;
	}

	private CreateResFileChange createFileChange(String langCode) {
		IFile file = getResBoundleFile(langCode);

		String LineDelimiter = Helper.getLineDelimiterPreference(getProject());
		CreateResFileChange cfc = null;
		if (!(file.exists())) {
			int count = (this.substitutions == null) ? 0
					: this.substitutions.length;
			StringBuilder sb = new StringBuilder();
			List keyList = new ArrayList();
			for (int i = 0; i < count; ++i) {
				MLResSubstitution sub = this.substitutions[i];
				String key = sub.getKeyWithPrefix();
				if (sub.getState() == 0) {
					if (keyList.contains(key))
						continue;
					keyList.add(key);
					String value = getSubValue(sub, langCode);
					sb.append(key).append("=") //$NON-NLS-1$
							.append(Helper.unwindEscapeChars(value))
							.append(LineDelimiter);
				}
			}
			String charset = "UTF-16"; //$NON-NLS-1$
			String name = "创建资源文件:"; //$NON-NLS-1$
			if (langCode.equals("english")) { //$NON-NLS-1$
				charset = "UTF-16"; //$NON-NLS-1$
				name = M_multilang.MLRRefactoring_7;
			} else if (langCode.equals("tradchn")) { //$NON-NLS-1$
				charset = "UTF-16"; //$NON-NLS-1$
				name = M_multilang.MLRRefactoring_8;
			} else if (langCode.equals("simpchn")) { //$NON-NLS-1$
				name = M_multilang.MLRRefactoring_9;
			}
			cfc = new CreateResFileChange(file.getFullPath(), sb.toString(),
					charset);
			cfc.setName(name + file.getFullPath().toOSString());
		}
		return cfc;
	}

	public String getName() {
		return (new StringBuilder(M_multilang.MLRRefactoring_10)).toString();
	}

	public static MLRRefactoring create(IProject project) {
		return new MLRRefactoring(project);
	}

	public boolean isIgnoreRepeatedMLR() {
		return this.ignoreRepeatedMLR;
	}

	public void setIgnoreRepeatedMLR(boolean ignoreRepeatedMLR) {
		this.ignoreRepeatedMLR = ignoreRepeatedMLR;
	}

	public IProject getProject() {
		return project;
	}

	public IFolder getFolder() {
		return folder;
	}

	public void setFolder(IFolder folder) {
		this.folder = folder;
	}

	public PageNode getCurrentPageNode() {
		return currentPageNode;
	}

	public void setCurrentPageNode(PageNode currentPageNode) {
		this.currentPageNode = currentPageNode;
	}

	public Map<String, String> getContents() {
		return contents;
	}

	public void setContents(Map<String, String> contents) {
		this.contents = contents;
	}

	public String getPrefix() {
		if (currentPageNode != null)
			return currentPageNode.getPrefix();
		return null;
	}

	public void setPrefix(String prefix) {
		if (currentPageNode != null) {
			String old = currentPageNode.getPrefix();
			currentPageNode.setPrefix(prefix);
			if (!old.equals(prefix)) {
				refreshPrefix();
				updateSubPostion(null);
				page.refreshSoruseView();
			}
		}
	}

	public ExternalizeMLRWizardPage getPage() {
		return page;
	}

	public void setPage(ExternalizeMLRWizardPage page) {
		this.page = page;
	}

	public MLResSubstitution[] getSubstitutions() {
		return substitutions;
	}

	public void setSubstitutions(MLResSubstitution[] substitutions) {
		this.substitutions = substitutions;
	}

	public boolean isIncludeChinese() {
		return this.includeChinese;
	}

	public boolean isIncludeLetter() {
		return this.includeLetter;
	}

	public boolean isIncludeDigit() {
		return this.includeDigit;
	}

	public boolean isIncludeOther() {
		return this.includeOther;
	}

	public String getResModuleName() {
		return resModuleName;
	}

	public void setResModuleName(String resModuleName) {
		this.resModuleName = resModuleName;
	}

	public List<String> getModifyFilePath() {
		return modifyFilePath;
	}

	public String getResFileName() {
		return resFileName;
	}

	public void setResFileName(String resFileName) {
		this.resFileName = resFileName;
	}
	
	

}
