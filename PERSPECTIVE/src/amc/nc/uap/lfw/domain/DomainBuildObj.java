package nc.uap.lfw.domain;

import java.util.ArrayList;
import java.util.HashMap;

public class DomainBuildObj {

	private ArrayList moduleList;
	
	private HashMap<String,ArrayList<String>> dependMapping;
	
	private String domainId;
	
	

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public ArrayList getModuleList() {
		return moduleList;
	}

	public void setModuleList(ArrayList moduleList) {
		this.moduleList = moduleList;
	}

	public HashMap<String, ArrayList<String>> getDependMapping() {
		return dependMapping;
	}

	public void setDependMapping(HashMap<String, ArrayList<String>> dependMapping) {
		this.dependMapping = dependMapping;
	}
	
}
