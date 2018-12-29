package nc.uap.lfw.core.data;

import nc.uap.lfw.core.datamodel.IDatasetProvider;
import nc.uap.lfw.design.view.ClassPathProvider;
import uap.lfw.core.env.EnvProvider;

public class RefMdDataset extends Dataset implements IRefDataset {
	private static final long serialVersionUID = 1535975016041386754L;
	private String objMeta = null;
	
//	private String beanId;
	
	private MdDataset refDs;
	
	private boolean initialized = false;
	
	public String getBeanId() {
		return refDs.getBeanId();
	}

//	public void setBeanId(String beanId) {
//		this.beanId = beanId;
//	}

	/**
	 * 设置objMeta信息
	 * 
	 * @param objMeta
	 */
	public void setObjMeta(String objMeta) {
		this.objMeta = objMeta;
	}

	/**
	 * 获取objMeta的值
	 * 
	 * @return
	 */
	public String getObjMeta() {

		return this.objMeta;
	}
	
	public FieldSet getFieldSet() {
		MdDataset refMdDs = getRefDs();
		return refMdDs.getFieldSet();
	}

	private MdDataset getRefDs() {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		if(!initialized){
			initialized = true;
			refDs = MdDatasetPool.getDataset(objMeta);
			if(refDs == null){
				refDs = new MdDataset();
				refDs.setObjMeta(objMeta);
				refDs.initialized = true;
				Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
				IDatasetProvider provider = EnvProvider.getInstance().getDatasetProvider();
				provider.getMdDataset(refDs);
				Thread.currentThread().setContextClassLoader(currentLoader);
				MdDatasetPool.setDataset(objMeta, refDs);
			}
		}
		return refDs;
	}
	
	
	
	public Object clone() {
//		RefMdDataset ds = (RefMdDataset) super.clone();
//		ds.fieldSet = (FieldSet) this.fieldSet.clone();
//		ds.fieldSet.setDataSet(ds);// add by renxh
//		if (this.rowSetMap != null) {
//			ds.rowSetMap = new HashMap<String, RowSet>();
//			Iterator<Entry<String, RowSet>> it = this.rowSetMap.entrySet().iterator();
//			while (it.hasNext()) {
//				Entry<String, RowSet> entry = it.next();
//				RowSet newRs = (RowSet) entry.getValue().clone();
//				ds.rowSetMap.put(entry.getKey(), newRs);
//				newRs.setDataset(ds);
//			}
//		}
//		if(this.reqParameters != null)
//			ds.reqParameters = (ParameterSet) this.reqParameters.clone();
//		if(this.resParameters != null)
//			ds.resParameters = (ParameterSet) this.resParameters.clone();
		return this;
	}

	@Override
	public String getTableName() {
		return getRefDs().getTableName();
	}

	@Override
	public int getFieldCount() {
		return getRefDs().getFieldCount();
	}

	@Override
	public FieldRelations getFieldRelations() {
		return getRefDs().getFieldRelations();
	}

	@Override
	public String getVoMeta() {
		return getRefDs().getVoMeta();
	}

	@Override
	public String getUserDefProcessor() {
		return getRefDs().getUserDefProcessor();
	}
	
}
