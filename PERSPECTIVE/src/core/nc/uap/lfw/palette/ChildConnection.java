package nc.uap.lfw.palette;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;

/**
 * 创建ds fieldRelation的子关联
 * @author zhangxya
 *
 */
public class ChildConnection  extends Connection{
	
	private static final long serialVersionUID = 1794771154424833502L;

	public ChildConnection(LFWBasicElementObj source, LFWBasicElementObj target){
		super(source, target);
	}
}
