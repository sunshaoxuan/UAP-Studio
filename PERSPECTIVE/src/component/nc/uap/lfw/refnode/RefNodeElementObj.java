package nc.uap.lfw.refnode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LFWUtility;
import nc.uap.lfw.core.ObjectComboPropertyDescriptor;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.config.RefNodeConf;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.LfwRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.SelfDefRefNode;
import nc.uap.lfw.lang.M_refnode;
import nc.uap.lfw.perspective.model.Constant;
import nc.uap.lfw.refnode.core.RefNodeEditor;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * 参照节点模型
 * @author zhangxya
 *
 */
public class RefNodeElementObj extends LfwElementObjWithGraph{
	private static final long serialVersionUID = 908307595793088095L;
	private String refRelationId;
	

	private String reftype;
	private String originalId;
	private String querySql;
	private BaseRefNode refnode;
	private String type;
	private String filterSql;
	private boolean isFromPool;
	public static final String PROP_UPDATE_CELL_PROPS = "update_cell_props"; //$NON-NLS-1$
	
	public String getRefRelationId() {
		return refRelationId;
	}

	public void setRefRelationId(String refRelationId) {
		this.refRelationId = refRelationId;
	}
	
	public boolean isFromPool() {
		return isFromPool;
	}
	
	public void setFromPool(boolean isFromPool) {
		this.isFromPool = isFromPool;
	}
	
	public String getReftype() {
		return reftype;
	}

	public void setReftype(String reftype) {
		this.reftype = reftype;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	
	public RefNodeElementObj(){
		super();
	}
	
	public RefNodeElementObj(String originalId){
		super();
		this.originalId = originalId;
	}

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BaseRefNode getRefnode() {
		return refnode;
	}

	public static final String PROP_REFNODE_ELEMENT ="refnode_element"; //$NON-NLS-1$
	public void setRefnode(BaseRefNode refnode) {
		this.refnode = refnode;
		fireStructureChange(PROP_REFNODE_ELEMENT,  refnode);
	}
	private NCRefNode ncrefnode;
	public NCRefNode getNcrefnode() {
		return ncrefnode;
	}

	public void setNcrefnode(NCRefNode ncrefnode) {
		this.ncrefnode = ncrefnode;
	}
	
	private LfwRefNode lfwrefnode;
	public LfwRefNode getLfwRefNode() {
		return lfwrefnode;
	}

	public void setLfwRefNode(LfwRefNode lfwrefnode) {
		this.lfwrefnode = lfwrefnode;
	}
	
	private BaseRefNode newrefnode;

	public BaseRefNode getNewrefnode() {
		return newrefnode;
	}

	public void setNewrefnode(BaseRefNode newrefnode) {
		this.newrefnode = newrefnode;
	}

	public static final String PROP_PATH = "element_PATH"; //$NON-NLS-1$
	public static final String PROP_TYPE = "element_TYPE"; //$NON-NLS-1$
	public static final String PROP_ID = "element_ID"; //$NON-NLS-1$
	public static final String PROP_WRITEDS = "element_WRITEDS"; //$NON-NLS-1$
	public static final String PROP_READDS = "element_READDS"; //$NON-NLS-1$
	public static final String PROP_NCREFNODE = "element_NCREFNODE"; //$NON-NLS-1$
	public static final String PROP_REFID = "element_REFID"; //$NON-NLS-1$
	public static final String PROP_READFIELDS = "element_READFIELDS"; //$NON-NLS-1$
	public static final String PROP_WRITEFIELDS= "element_WRITEFIELDS"; //$NON-NLS-1$
	public static final String PROP_MULTISEL = "element_MULTISEL"; //$NON-NLS-1$
	public static final String PROP_USERPOWER = "element_USERPOWER"; //$NON-NLS-1$
	public static final String PROP_ORGS = "element_ORGS"; //$NON-NLS-1$
	public static final String PROP_SELLEAFONLY = "element_SELLEAFONLY"; //$NON-NLS-1$
	public static final String PROP_PROCESSOR = "element_PROCESSOR"; //$NON-NLS-1$
	public static final String PROP_PAGEMETA = "element_PAGEMETA"; //$NON-NLS-1$
	public static final String PROP_REFMODEL = "element_REFMODEL"; //$NON-NLS-1$
	public static final String PROP_DATALISTENER = "element_DATALISTENER"; //$NON-NLS-1$
	public static final String PROP_PAGEMODEL = "element_PAGEMODEL"; //$NON-NLS-1$
	public static final String PROP_REFNODEDELEGATOR = "element_REFNODEDELEGATOR"; //$NON-NLS-1$
	public static final String PROP_ISREFRESH = "element_ISREFRESH"; //$NON-NLS-1$
	public static final String PROP_ISDIALOG = "element_ISDIALOG"; //$NON-NLS-1$
	public static final String PROP_ALLOWINPUT = "element_allowInput"; //$NON-NLS-1$
	public static final String PROP_CAPTION = "element_CAPTION"; //$NON-NLS-1$
	public static final String PROP_WIDTH = "element_width"; //$NON-NLS-1$
	public static final String PROP_HEIGHT = "element_height"; //$NON-NLS-1$
	
	
	public static String PROP_VISIBLEFIELDCODES = "visibleFieldCodes"; //$NON-NLS-1$
	public static String PROP_VISIBLEFIELDNAMES = "visibleFieldNames"; //$NON-NLS-1$
	public static String PROP_HIDDENFIELDCODES = "hiddenFieldCodes"; //$NON-NLS-1$
	public static String PROP_HIDDENFIELDNAMES = "hiddenFieldNames"; //$NON-NLS-1$
	public static String PROP_REFPKFIELD = "refPkField"; //$NON-NLS-1$
	public static String PROP_REFCODEFIELD = "refCodeField"; //$NON-NLS-1$
	public static String PROP_REFNAMEFIELD = "refNameField"; //$NON-NLS-1$
	public static String PROP_REFDATA = "refData"; //$NON-NLS-1$
	public static String PROP_QEURYSQL = "QuerySql"; //$NON-NLS-1$
	public static String PROP_FILTERSQL = "FilterSql"; //$NON-NLS-1$
	public static String PROP_FILTERFIELDS = "FilterFields"; //$NON-NLS-1$
	public static String PROP_REFTYPE = "RefType"; //$NON-NLS-1$
	public static String PROP_FIELDINTEX = "FieldIndex"; //$NON-NLS-1$
	
	public static String PROP_LFWREFCODE = "lfwRefCode"; //$NON-NLS-1$
	public static String PROP_MULTISELTREE = "multiSelTree"; //$NON-NLS-1$
	public static String PROP_SHOWSELRESULT = "showSelResult"; //$NON-NLS-1$
	public static String PROP_MULTISELRESULT = "multiSelResult"; //$NON-NLS-1$
	public static String PROP_PAGESELRESULT = "pageSelResult"; //$NON-NLS-1$
	
	//表格型,树型、树表型参照使用
	public static String PROP_TABLESTRING = "tableString"; //$NON-NLS-1$
	
	public static String PROP_STRPATCH = "strpatch"; //$NON-NLS-1$
	public static String PROP_FIXEDWHEREPART = "FixedWherePart"; //$NON-NLS-1$
	public static String PROP_GROUPPART = "GroupPart"; //$NON-NLS-1$
	public static String PROP_ORDERPART = "OrderPart"; //$NON-NLS-1$
	
	//树型/树表参照使用
	public static String PROP_CHILDFIELD = "childField"; //$NON-NLS-1$
	public static String PROP_FATHERFIELD = "FatherField"; //$NON-NLS-1$
	public static String PROP_ROOTNAME = "RootName"; //$NON-NLS-1$
	
	//树表型参照
	public static String PROP_CLASSREFCODEFIELD = "ClassRefCodeField"; //$NON-NLS-1$
	public static String PROP_CLASSREFNAMEFIELD= "ClassRefNameField"; //$NON-NLS-1$
	public static String PROP_CLASSFIELDCODES = "ClassFieldCodes"; //$NON-NLS-1$
	public static String PROP_CLASSJOINFIELD = "ClassJoinField"; //$NON-NLS-1$
	public static String PROP_DOCJOINFIELD = "DocJoinField"; //$NON-NLS-1$
	
	public static final String PROP_REF_TYPE_NC = "NC参照"; //$NON-NLS-1$
	public static final String PROP_REF_TYPE_SELF_DEF = "自定义参照"; //$NON-NLS-1$
	public static final String PROP_REF_TYPE_LFW = "新版参照"; //$NON-NLS-1$
	/**
	 * 获取所有的Nc参照编码
	 * @return
	 */
	private String[] getAllNcRefnode(){
		return LFWConnector.getAllNcRefNode();
	}
	
	private Map<String, Map<String, IRefNode>> allRefNodeMap = null;
	private Map<String, Map<String, IRefNode>> getAllRefNodeMap(){
		if(allRefNodeMap == null){
//			String ctx = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
//			allRefNodeMap = LFWConnector.getAllPoolRefNodes("/" + ctx);
		}
		return allRefNodeMap;
	}
	/**
	 * 获取公共池中所有的refnodeId
	 * @return
	 */
	private String[] getPoolRefNodeId(){
		List<String> reflist = new ArrayList<String>();
		Map<String, Map<String, IRefNode>> refnodeMap = getAllRefNodeMap();
		if(refnodeMap!=null){
			for (Iterator<String> it = refnodeMap.keySet().iterator(); it.hasNext();) {
				String itctx = it.next();
				Map<String, IRefNode> ctxMap = refnodeMap.get(itctx);
				for (Iterator<String> itonly = ctxMap.keySet().iterator(); itonly.hasNext();){
					String itonlyN = itonly.next();
					IRefNode refNode = ctxMap.get(itonlyN);
					if(refNode == null)
						continue;
					reflist.add(refNode.getId());
				}
			}
		}
		return (String[]) reflist.toArray(new String[reflist.size()]);
	}
	
	private String[] getAllDatasts(){
		List<String> datasetList = new ArrayList<String>();
		LfwView widget = LFWPersTool.getCurrentWidget();
		if(widget != null){
			Dataset[] datas =  widget.getViewModels().getDatasets();
			if(datas != null){
				for (int i = 0; i < datas.length; i++) {
					Dataset ds = datas[i];
					if(!(ds instanceof IRefDataset))
						datasetList.add(ds.getId());
				}
				datasetList.add(""); //$NON-NLS-1$
				return (String[])datasetList.toArray(new String[datasetList.size()]);
			}
			else return null;
		}
		else 
			return null;
	}
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		if(getType() == null){
			if(refnode instanceof NCRefNode){
				setType(PROP_REF_TYPE_NC);
				((NCRefNode)refnode).setReadDs("masterDs"); //$NON-NLS-1$
			}else if(refnode instanceof SelfDefRefNode){
				setType(PROP_REF_TYPE_SELF_DEF);
			}else if(refnode instanceof LfwRefNode){
				setType(PROP_REF_TYPE_LFW);
				((LfwRefNode)refnode).setReadDs("masterDs"); //$NON-NLS-1$
			}else{ 
				setType(PROP_REF_TYPE_SELF_DEF);
			}
		}
		if(getType().equals(PROP_REF_TYPE_SELF_DEF)){
			//prop = 1;
			if(refnode instanceof SelfDefRefNode){
				newrefnode = (SelfDefRefNode)refnode;
			}
			if(newrefnode == null){
				newrefnode = new SelfDefRefNode();
				newrefnode.setId(refnode.getId());
			}
			setRefnode(newrefnode);
		}else if(getType().equals(PROP_REF_TYPE_NC)){
			//prop = 2;
			if(refnode instanceof NCRefNode){
				ncrefnode = (NCRefNode) refnode;
			}
			if(ncrefnode == null){
				ncrefnode = new NCRefNode();
				ncrefnode.setId(refnode.getId());
				ncrefnode.setReadDs(null);
			}
			setRefnode(ncrefnode);
		}else if(getType().equals(PROP_REF_TYPE_LFW)){
			//prop = 3;
			if(refnode instanceof LfwRefNode){
				lfwrefnode = (LfwRefNode) refnode;
			}
			if(lfwrefnode == null){
				lfwrefnode = new LfwRefNode();
				lfwrefnode.setId(refnode.getId());
				lfwrefnode.setReadDs(null);
			}
			setRefnode(lfwrefnode);
		}
		
		if(this.isFromPool){
//			PropertyDescriptor pd = new ObjectComboPropertyDescriptor(PROP_TYPE, "参照类型", Constant.POOLREFTYPE);
//			pd.setCategory("基本");
//			al.add(pd);
		}else{
			PropertyDescriptor pd = new ObjectComboPropertyDescriptor(PROP_TYPE, M_refnode.RefNodeElementObj_0, Constant.REFTYPE);
			pd.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pd);
			
			PropertyDescriptor propCaption = new TextPropertyDescriptor(PROP_CAPTION,M_refnode.RefNodeElementObj_2);
			propCaption.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(propCaption);
		}
		PropertyDescriptor pds = new NoEditableTextPropertyDescriptor(PROP_ID, M_refnode.RefNodeElementObj_3);
		pds.setCategory(M_refnode.RefNodeElementObj_1);
		al.add(pds);
		
		
		
//		PropertyDescriptor pisDialog = new ComboBoxPropertyDescriptor(PROP_ISDIALOG,"是否Dialog", Constant.ISLAZY);
//		pisDialog.setCategory("基本");
//		al.add(pisDialog);
		
//		PropertyDescriptor pisFresh = new ComboBoxPropertyDescriptor(PROP_ISREFRESH,"是否刷新", Constant.ISLAZY);
//		pisFresh.setCategory("基本");
//		al.add(pisFresh);
		
		if((getType() != null && getType().equals(PROP_REF_TYPE_SELF_DEF)) || refnode instanceof SelfDefRefNode){
			PropertyDescriptor pd7 = new TextPropertyDescriptor(PROP_PATH,M_refnode.RefNodeElementObj_4);
			pd7.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pd7);
			PropertyDescriptor pdwidth = new TextPropertyDescriptor(PROP_WIDTH,M_refnode.RefNodeElementObj_5);
			pdwidth.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pdwidth);
			PropertyDescriptor pdheight = new TextPropertyDescriptor(PROP_HEIGHT,M_refnode.RefNodeElementObj_6);
			pdheight.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pdheight);
		}else{
			if(this.isFromPool){
//				PropertyDescriptor pd = new TextPropertyDescriptor(PROP_WRITEDS,"写入数据集");
//				pd.setCategory("基本");
//				al.add(pd);
			}else{
				PropertyDescriptor pdwidth = new TextPropertyDescriptor(PROP_WIDTH,M_refnode.RefNodeElementObj_7);
				pdwidth.setCategory(M_refnode.RefNodeElementObj_1);
				al.add(pdwidth);
				PropertyDescriptor pdheight = new TextPropertyDescriptor(PROP_HEIGHT,M_refnode.RefNodeElementObj_8);
				pdheight.setCategory(M_refnode.RefNodeElementObj_1);
				al.add(pdheight);
				PropertyDescriptor pd2 = new ObjectComboPropertyDescriptor(PROP_REFID,M_refnode.RefNodeElementObj_9, getPoolRefNodeId());
				pd2.setCategory(M_refnode.RefNodeElementObj_1);
				al.add(pd2);
				
				PropertyDescriptor pd = new ObjectComboPropertyDescriptor(PROP_WRITEDS,M_refnode.RefNodeElementObj_10, getAllDatasts());
				pd.setCategory(M_refnode.RefNodeElementObj_1);
				al.add(pd);
				PropertyDescriptor pd3 = new TextPropertyDescriptor(PROP_READFIELDS,M_refnode.RefNodeElementObj_11);
				pd3.setCategory(M_refnode.RefNodeElementObj_1);
				al.add(pd3);
				
				PropertyDescriptor pd4 = new TextPropertyDescriptor(PROP_WRITEFIELDS,M_refnode.RefNodeElementObj_12);
				pd4.setCategory(M_refnode.RefNodeElementObj_1);
				al.add(pd4);
			}
			PropertyDescriptor pd1 = new TextPropertyDescriptor(PROP_READDS,M_refnode.RefNodeElementObj_13);
			pd1.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pd1);
			
			PropertyDescriptor pd5 = new ComboBoxPropertyDescriptor(PROP_MULTISEL,M_refnode.RefNodeElementObj_14, Constant.ISLAZY);
			pd5.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pd5);
			
			PropertyDescriptor pd6 = new ComboBoxPropertyDescriptor(PROP_SELLEAFONLY,M_refnode.RefNodeElementObj_15, Constant.ISLAZY);
			pd6.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pd6);

			PropertyDescriptor pd8 = new TextPropertyDescriptor(PROP_PAGEMETA,M_refnode.RefNodeElementObj_16);
			pd8.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pd8);
			
			PropertyDescriptor pd9 = new TextPropertyDescriptor(PROP_PAGEMODEL,M_refnode.RefNodeElementObj_17);
			pd9.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pd9);
			
//			PropertyDescriptor pd10 = new TextPropertyDescriptor(PROP_REFMODEL, "参照模型类");
//			pd10.setCategory("基本");
//			al.add(pd10);
			
			PropertyDescriptor pd11 = new TextPropertyDescriptor(PROP_DATALISTENER, M_refnode.RefNodeElementObj_18);
			pd11.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pd11);
			
			PropertyDescriptor pddelegator = new TextPropertyDescriptor(PROP_REFNODEDELEGATOR, M_refnode.RefNodeElementObj_19);
			pddelegator.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pddelegator);
			
			PropertyDescriptor pisallowInput = new ComboBoxPropertyDescriptor(PROP_ALLOWINPUT,M_refnode.RefNodeElementObj_20, Constant.ISLAZY);
			pisallowInput.setCategory(M_refnode.RefNodeElementObj_1);
			al.add(pisallowInput);
			
			if((getType() != null && getType().equals(PROP_REF_TYPE_NC)) 
					|| refnode instanceof NCRefNode){
				PropertyDescriptor pd = new ComboBoxPropertyDescriptor(PROP_USERPOWER,M_refnode.RefNodeElementObj_21, Constant.ISLAZY);
				pd.setCategory(M_refnode.RefNodeElementObj_22);
				al.add(pd);
				
				pd = new ObjectComboPropertyDescriptor(PROP_NCREFNODE,M_refnode.RefNodeElementObj_23, getAllNcRefnode());
				pd.setCategory(M_refnode.RefNodeElementObj_22);
				al.add(pd);
				
				pd = new ComboBoxPropertyDescriptor(PROP_ORGS,M_refnode.RefNodeElementObj_24, Constant.ISLAZY);
				pd.setCategory(M_refnode.RefNodeElementObj_22);
				al.add(pd);
			}else if((getType() != null && getType().equals(PROP_REF_TYPE_LFW)) 
					|| refnode instanceof LfwRefNode){
				PropertyDescriptor pd = new ObjectComboPropertyDescriptor(PROP_LFWREFCODE,M_refnode.RefNodeElementObj_25, getAllNcRefnode());
				pd.setCategory(M_refnode.RefNodeElementObj_22);
				al.add(pd);
				
				pd = new ComboBoxPropertyDescriptor(PROP_MULTISELTREE,M_refnode.RefNodeElementObj_26, Constant.ISYES_NO);
				pd.setCategory(M_refnode.RefNodeElementObj_22);
				al.add(pd);
				
				pd = new ComboBoxPropertyDescriptor(PROP_SHOWSELRESULT,M_refnode.RefNodeElementObj_27, Constant.ISYES_NO);
				pd.setCategory(M_refnode.RefNodeElementObj_22);
				al.add(pd);
				
				pd = new ComboBoxPropertyDescriptor(PROP_MULTISELRESULT,M_refnode.RefNodeElementObj_28, Constant.ISYES_NO);
				pd.setCategory(M_refnode.RefNodeElementObj_22);
				al.add(pd);
				
				pd = new ComboBoxPropertyDescriptor(PROP_PAGESELRESULT,M_refnode.RefNodeElementObj_29, Constant.ISYES_NO);
				pd.setCategory(M_refnode.RefNodeElementObj_22);
				al.add(pd);
			}
		}
		return al.toArray(new IPropertyDescriptor[0]);
	}
	
	public void setPropertyValue(Object id, Object value) {
		WebElement webele = getWebElement();
		if(!canChange(webele)){
			return;
		}
		if(PROP_TYPE.equals(id)){
			setType((String)value);
		}
		else if(PROP_ID.equals(id)){
			refnode.setId((String)value);
			LFWBaseEditor.getActiveEditor().refreshTreeItem(refnode);
		}
		else if(PROP_REFNODEDELEGATOR.equals(id) && refnode instanceof RefNode){
			((RefNode)refnode).setRefnodeDelegator((String)value);
		}
		else if(PROP_PAGEMODEL.equals(id) && refnode instanceof RefNodeConf){
			((RefNodeConf)refnode).setPageModel((String)value);
		}
//		else if(PROP_REFMODEL.equals(id) && refnode instanceof RefNodeConf){
//			((RefNodeConf)refnode).setRefModel((String)value);
//		}
		else if(PROP_DATALISTENER.equals(id) && refnode instanceof RefNodeConf){
			((RefNodeConf)refnode).setDataListener((String)value);
		}
		else if(PROP_WRITEDS.equals(id) && refnode instanceof RefNode){
			((RefNode)refnode).setWriteDs((String)value);
		}
		else if(PROP_READDS.equals(id) && refnode instanceof RefNodeConf){
			((RefNodeConf)refnode).setReadDs((String)value);
		}
		else if(PROP_REFID.equals(id) && refnode instanceof RefNode){
			((RefNode)refnode).setRefId((String)value);
		}
		else if(PROP_CAPTION.equals(id)){
			String oldValue = refnode.getText();
			if((oldValue == null && value != null)  || (oldValue != null && value != null && !oldValue.equals(value))){
				refnode.setText((String)value);
				RefNodeEditor.getActiveEditor().refreshTreeItemText(refnode);
			}
		}
		else if(PROP_READFIELDS.equals(id) && refnode instanceof RefNode){
			((RefNode)refnode).setReadFields((String)value);
		}
		else if(PROP_WRITEFIELDS.equals(id) && refnode instanceof RefNode){
			((RefNode)refnode).setWriteFields((String)value);
		}
		else if(PROP_MULTISEL.equals(id) && refnode instanceof RefNodeConf){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			((RefNodeConf)refnode).setMultiSel(truevalue);
		}
//		else if(PROP_ISREFRESH.equals(id)){
//			boolean truevalue = false;
//			if((Integer)value == 0)
//				truevalue = true;
//			refnode.setRefresh(truevalue);
//		}
		else if(PROP_ALLOWINPUT.equals(id) && refnode instanceof RefNodeConf){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			((RefNodeConf)refnode).setAllowInput(truevalue);
		}
//		else if(PROP_ISDIALOG.equals(id)){
//			boolean truevalue = false;
//			if((Integer)value == 0)
//				truevalue = true;
//			refnode.setDialog(truevalue);
//		}
		else if(PROP_PAGEMETA.equals(id) && refnode instanceof RefNodeConf){
			((RefNodeConf)refnode).setPagemeta((String)value);
		}
		else if(PROP_USERPOWER.equals(id) && refnode instanceof NCRefNode){
			NCRefNode ncRefNode = (NCRefNode) refnode;
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ncRefNode.setUsePower(truevalue);
		}
		else if(PROP_ORGS.equals(id) && refnode instanceof NCRefNode){
			NCRefNode ncRefNode = (NCRefNode) refnode;
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ncRefNode.setOrgs(truevalue);
		}
		else if(PROP_SELLEAFONLY.equals(id) && refnode instanceof RefNodeConf){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			((RefNodeConf)refnode).setSelLeafOnly(truevalue);
		}
		else if(PROP_NCREFNODE.equals(id) && refnode instanceof NCRefNode){
			NCRefNode ref = (NCRefNode)refnode;
			ref.setRefcode((String)value);
		}
		else if(PROP_LFWREFCODE.equals(id) && refnode instanceof LfwRefNode){
			LfwRefNode ref = (LfwRefNode)refnode;
			ref.setLfwRefCode((String)value);
		}
		else if(PROP_MULTISELTREE.equals(id) && refnode instanceof LfwRefNode){
			LfwRefNode ref = (LfwRefNode)refnode;
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ref.setMultiSelTree(truevalue);
		}
		else if(PROP_SHOWSELRESULT.equals(id) && refnode instanceof LfwRefNode){
			LfwRefNode ref = (LfwRefNode)refnode;
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ref.setShowSelResult(truevalue);
		}
		else if(PROP_MULTISELRESULT.equals(id) && refnode instanceof LfwRefNode){
			LfwRefNode ref = (LfwRefNode)refnode;
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ref.setMultiSelResult(truevalue);
		}
		else if(PROP_PAGESELRESULT.equals(id) && refnode instanceof LfwRefNode){
			LfwRefNode ref = (LfwRefNode)refnode;
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ref.setPageSelResult(truevalue);
		}
//		else if(PROP_PROCESSOR.equals(id))
//			refnode.setProcessor((String)value);
		else if(PROP_PATH.equals(id) && refnode instanceof SelfDefRefNode){
			((SelfDefRefNode)refnode).setPath((String)value);
		}
		else if(PROP_WIDTH.equals(id) && refnode instanceof SelfDefRefNode){
			((SelfDefRefNode)refnode).setWidth((String)value);
		}
		else if(PROP_HEIGHT.equals(id) && refnode instanceof SelfDefRefNode){
			((SelfDefRefNode)refnode).setHeight((String)value);
		}
		else if(PROP_WIDTH.equals(id) && refnode instanceof SelfDefRefNode){
			((SelfDefRefNode)refnode).setWidth((String)value);
		}
		else if(PROP_HEIGHT.equals(id) && refnode instanceof SelfDefRefNode){
			((SelfDefRefNode)refnode).setHeight((String)value);
		}
		else if(PROP_WIDTH.equals(id) && !(refnode instanceof SelfDefRefNode)){
			((BaseRefNode)refnode).setDialogWidth((String)value);
		}
		else if(PROP_HEIGHT.equals(id) && !(refnode instanceof SelfDefRefNode)){
			((BaseRefNode)refnode).setDialogHeight((String)value);
		}
	}
	
	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public String getFilterSql() {
		return filterSql;
	}

	public void setFilterSql(String filterSql) {
		this.filterSql = filterSql;
	}

	public Object getPropertyValue(Object id) {
		if(PROP_TYPE.equals(id))
			return getType();
		if(PROP_ID.equals(id))
			return refnode.getId() == null?"":refnode.getId(); //$NON-NLS-1$
		else if(PROP_PAGEMODEL.equals(id) && refnode instanceof RefNodeConf){
			return ((RefNodeConf)refnode).getPageModel() == null?"":((RefNodeConf)refnode).getPageModel(); //$NON-NLS-1$
		}
		else if(PROP_REFNODEDELEGATOR.equals(id) && refnode instanceof RefNode){
			return ((RefNode)refnode).getRefnodeDelegator() == null?"":((RefNode)refnode).getRefnodeDelegator(); //$NON-NLS-1$
		}
		else if(PROP_DATALISTENER.equals(id) && refnode instanceof RefNodeConf)
			return ((RefNodeConf)refnode).getDataListener() == null?"":((RefNodeConf)refnode).getDataListener(); //$NON-NLS-1$
		else if(PROP_WRITEDS.equals(id) && refnode instanceof RefNode)
			return ((RefNode)refnode).getWriteDs() == null?"":((RefNode)refnode).getWriteDs(); //$NON-NLS-1$
		else if(PROP_READDS.equals(id) && refnode instanceof RefNodeConf)
			return ((RefNodeConf)refnode).getReadDs() == null?"":((RefNodeConf)refnode).getReadDs(); //$NON-NLS-1$
		else if(PROP_REFID.equals(id) && refnode instanceof RefNode)
			return ((RefNode)refnode).getRefId() == null?"":((RefNode)refnode).getRefId(); //$NON-NLS-1$
		else if(PROP_CAPTION.equals(id))
			return refnode.getText() == null?"":refnode.getText(); //$NON-NLS-1$
//		else if(PROP_REFMODEL.equals(id) && refnode instanceof RefNodeConf)
//			return ((RefNodeConf)refnode).getRefModel() == null? "":((RefNodeConf)refnode).getRefModel();
		else if(PROP_READFIELDS.equals(id) && refnode instanceof RefNode)
			return ((RefNode)refnode).getReadFields() == null?"":((RefNode)refnode).getReadFields(); //$NON-NLS-1$
		else if(PROP_WRITEFIELDS.equals(id) && refnode instanceof RefNode)
			return ((RefNode)refnode).getWriteFields() == null?"":((RefNode)refnode).getWriteFields(); //$NON-NLS-1$
		else if(PROP_MULTISEL.equals(id) && refnode instanceof RefNodeConf)
			return ((RefNodeConf)refnode).isMultiSel()? Integer.valueOf(0):Integer.valueOf(1);
//		else if(PROP_ISREFRESH.equals(id))
//			return (refnode.isRefresh() == true)? new Integer(0):new Integer(1);
		else if(PROP_ALLOWINPUT.equals(id) && refnode instanceof RefNodeConf)
			return ((RefNodeConf)refnode).isAllowInput()? Integer.valueOf(0):Integer.valueOf(1);
//		else if(PROP_ISDIALOG.equals(id))
//			return (refnode.isDialog() == true)? new Integer(0):new Integer(1);
		else if(PROP_SELLEAFONLY.equals(id) && refnode instanceof RefNodeConf)
			return ((RefNodeConf)refnode).isSelLeafOnly()? Integer.valueOf(0):Integer.valueOf(1);
//		else if(PROP_PROCESSOR.equals(id))
//			return refnode.getProcessor() == null?"":refnode.getProcessor();		
		else if(PROP_PAGEMETA.equals(id) && refnode instanceof RefNodeConf)
			return ((RefNodeConf)refnode).getPagemeta() == null?"":((RefNodeConf)refnode).getPagemeta(); //$NON-NLS-1$
		else if(PROP_NCREFNODE.equals(id)){
			NCRefNode ncref = (NCRefNode)refnode;
			return ncref.getRefcode() == null?"":ncref.getRefcode(); //$NON-NLS-1$
		}
		else if(PROP_USERPOWER.equals(id)){
			NCRefNode ncref = (NCRefNode)refnode;
			return ncref.isUsePower()? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(PROP_ORGS.equals(id)){
			NCRefNode ncref = (NCRefNode)refnode;
			return ncref.isOrgs()? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(PROP_LFWREFCODE.equals(id)){
			LfwRefNode lfwref = (LfwRefNode)refnode;
			return lfwref.getLfwRefCode() == null?"":lfwref.getLfwRefCode(); //$NON-NLS-1$
		}
		else if(PROP_MULTISELTREE.equals(id)){
			LfwRefNode lfwref = (LfwRefNode)refnode;
			return lfwref.isMultiSelTree()? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(PROP_SHOWSELRESULT.equals(id)){
			LfwRefNode lfwref = (LfwRefNode)refnode;
			return lfwref.isShowSelResult()? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(PROP_MULTISELRESULT.equals(id)){
			LfwRefNode lfwref = (LfwRefNode)refnode;
			return lfwref.isMultiSelResult()? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(PROP_PAGESELRESULT.equals(id)){
			LfwRefNode lfwref = (LfwRefNode)refnode;
			return lfwref.isPageSelResult()? Integer.valueOf(0):Integer.valueOf(1);
		}
		else if(PROP_PATH.equals(id)&& refnode instanceof SelfDefRefNode)
			return ((SelfDefRefNode)refnode).getPath() == null?"":((SelfDefRefNode)refnode).getPath(); //$NON-NLS-1$
		else if(PROP_WIDTH.equals(id) && refnode instanceof SelfDefRefNode)
			return ((SelfDefRefNode)refnode).getWidth() == null? "":((SelfDefRefNode)refnode).getWidth(); //$NON-NLS-1$
		else if(PROP_HEIGHT.equals(id) && refnode instanceof SelfDefRefNode)
			return ((SelfDefRefNode)refnode).getHeight() == null? "":((SelfDefRefNode)refnode).getHeight(); //$NON-NLS-1$
		else if(PROP_WIDTH.equals(id) && !(refnode instanceof SelfDefRefNode))
			return ((BaseRefNode)refnode).getDialogWidth() == null? "":((BaseRefNode)refnode).getDialogWidth(); //$NON-NLS-1$
		else if(PROP_HEIGHT.equals(id) &&!(refnode instanceof SelfDefRefNode))
			return ((BaseRefNode)refnode).getDialogHeight() == null? "":((BaseRefNode)refnode).getDialogHeight(); //$NON-NLS-1$
		else
			return null;
	}
	
	public void firePropUpdate(FormElement prop){
		fireStructureChange(PROP_UPDATE_CELL_PROPS, prop);
	}
	
	public WebElement getWebElement() {
		return refnode;
	}
	
}
