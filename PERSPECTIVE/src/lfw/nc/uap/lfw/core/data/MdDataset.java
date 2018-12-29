package nc.uap.lfw.core.data;

import nc.uap.lfw.core.datamodel.IDatasetProvider;
import nc.uap.lfw.core.lifecycle.LifeCyclePhase;
import nc.uap.lfw.core.lifecycle.RequestLifeCycleContext;
import nc.uap.lfw.design.view.ClassPathProvider;
import uap.lfw.core.env.EnvProvider;




/**
 * 来自元数据的dataset
 * @author gd 2020-3-3
 *
 */
public class MdDataset  extends Dataset {
	private static final long serialVersionUID = 380090536121568068L;
	
	public static final String MASTER_TABLE = "masterTable";
	
	/* 元数据路径 */
	private String objMeta = null;
	
	private String beanId;
	
	public boolean initialized = false;
	
	private String masterObjMeta;
	
	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

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

	@Override
	public FieldSet getFieldSet() {
		FieldSet fs = super.getFieldSet();
		if(!initialized){
			initialized = true;
			fillByMd();
		}
		return fs;
	}
	
//	/**
//	 * 获取未初始化元数据前的Dataset
//	 * @return
//	 */
//	public FieldSet getOriFieldSet() {
//		if(initialized)
//			throw new LfwRuntimeException("already initialized");
//		return super.getFieldSet();
//	}

	protected void fillByMd() {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.dynrender);
		Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
		try{
			IDatasetProvider provider = EnvProvider.getInstance().getDatasetProvider();
			MdDataset ds = MdDatasetPool.getDataset(this.getObjMeta());
			if(ds == null){
				ds = new MdDataset();
				ds.setObjMeta(objMeta);
				ds.initialized = true;
				provider.getMdDataset(ds);
				
				MdDatasetPool.setDataset(objMeta, ds);
			}
			FieldSet fs = ds.getFieldSet();
			Field[] fields = fs.getFields();
			
			FieldSet currFs = super.getFieldSet();
			String masterTable = getMasterTable();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if(masterTable != null){
					String mt = (String) field.getExtendAttributeValue(MASTER_TABLE);
					if(mt != null && !mt.equals(masterTable))
						continue;
				}
				currFs.addField(field);
			}
			setCaption(ds.getCaption());
			setVoMeta(ds.getVoMeta());
			setBeanId(ds.getBeanId());
			setTableName(ds.getTableName());
			provider.mergeDataset(this);
		}
		finally{
			RequestLifeCycleContext.get().setPhase(phase);
			Thread.currentThread().setContextClassLoader(currentLoader);
		}
	}

	private String getMasterTable() {
		if(this.masterObjMeta != null && !this.masterObjMeta.equals("")){
			MdDataset ds = MdDatasetPool.getDataset(this.masterObjMeta);
			if(ds == null){
				IDatasetProvider provider = EnvProvider.getInstance().getDatasetProvider();
				ds = new MdDataset();
				ds.setObjMeta(masterObjMeta);
				ds.initialized = true;
				provider.getMdDataset(ds);
				
				MdDatasetPool.setDataset(masterObjMeta, ds);
			}
			return ds.getTableName();
		}
		return null;
	}

	@Override
	public void setFieldSet(FieldSet fieldSet) {
		FieldSet fs = super.getFieldSet();
		if(fs != null){
			int size = fieldSet.getFieldCount();
			for (int i = 0; i < size; i++) {
				fs.addField(fieldSet.getField(i));
			}
			fs.setDataSet(this);
		}
		else
			super.setFieldSet(fieldSet);
	}

	public String getMasterObjMeta() {
		return masterObjMeta;
	}

	public void setMasterObjMeta(String masterObjMeta) {
		this.masterObjMeta = masterObjMeta;
	}
}