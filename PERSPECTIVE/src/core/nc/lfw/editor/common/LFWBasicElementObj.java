package nc.lfw.editor.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;


/**
 * LFW基本组件
 * @author zhangxya
 *
 */
public abstract class LFWBasicElementObj  implements Cloneable, Serializable ,IPropertySource{

	private static final long serialVersionUID = 3477090827693251811L;
	public static final String PROP_ID = "element_ID";
	
	public static final String PROP_TARGET_CONNECTION ="target_connection";
	public static final String PROP_SOURCE_CONNECTION ="source_connection";
	private List<Connection> targetConnections = new ArrayList<Connection>();
	private List<Connection> sourceConnections = new ArrayList<Connection>();
	PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	private String id;
	
	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[0];
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
		
	}

    public void addPropertyChangeListener(PropertyChangeListener l) {
        listeners.addPropertyChangeListener(l);
    }

    protected void firePropertyChange(String prop, Object old, Object newValue) {
         listeners.firePropertyChange(prop, old, newValue);
    }
    
    protected void fireStructureChange(String prop, Object newValue) {
    	firePropertyChange(prop, null, newValue);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener l) {
        listeners.removePropertyChangeListener(l);
    }

	protected boolean isNull(String str){
		return str == null || str.trim().length() == 0;
	}
	
	
	protected Object clone() throws CloneNotSupportedException {
		Object o = super.clone();
		return o;
	}
	
	public void addSourceConn(Connection conn){
		sourceConnections.add(conn);
		fireStructureChange(PROP_SOURCE_CONNECTION,  conn);
	}
	public void addTargetConn(Connection conn){
		targetConnections.add(conn);
		fireStructureChange(PROP_TARGET_CONNECTION, conn);
		
	}
	public void removeSourceConn(Connection conn){
		sourceConnections.remove(conn);
		fireStructureChange(PROP_SOURCE_CONNECTION,conn);
	}
	public void removeTargetConn(Connection conn){
		targetConnections.remove(conn);
		fireStructureChange(PROP_TARGET_CONNECTION, conn);
		
	}
	public List<Connection> getSourceConnections() {
		return new ArrayList<Connection>(sourceConnections);
	}
	public List<Connection> getTargetConnections() {
		return new ArrayList<Connection>(targetConnections);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
