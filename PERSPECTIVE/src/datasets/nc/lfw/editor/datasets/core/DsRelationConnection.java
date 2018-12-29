package nc.lfw.editor.datasets.core;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;

/**
 * ds relation 
 * @author zhangxya
 *
 */
public class DsRelationConnection  extends Connection{
	
	private static final long serialVersionUID = 1794771154424833502L;
	
	public DsRelationConnection(LFWBasicElementObj source, LFWBasicElementObj target){
		super(source, target);
	}
}
