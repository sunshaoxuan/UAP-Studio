package nc.uap.portal.freemarker.model.interpolation;



public class NullFragment extends NameFragment {

	public NullFragment() {
		super(0, "");
	}

	public boolean isStartFragment() {
		return true;
	}
}