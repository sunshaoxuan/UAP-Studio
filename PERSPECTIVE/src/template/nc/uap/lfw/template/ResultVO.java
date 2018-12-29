package nc.uap.lfw.template;

public class ResultVO {

	private String step;
	private String resource;
	private String result;
	
	public ResultVO(String step,String resource,String result){
		this.step = step;
		this.resource = resource;
		this.result = result;	
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
