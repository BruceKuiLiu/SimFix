/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.core.modify;

import java.util.Map;

import org.eclipse.jdt.core.dom.Type;

import cofix.core.parser.node.Node;
import cofix.core.parser.node.Node.TYPE;

/**
 * @author Jiajun
 * @datae Jun 23, 2017
 */
public abstract class Modification {

	protected Node _node = null;
	protected TYPE _nodeType = TYPE.UNKNOWN;
	protected int _sourceID = -1;
	protected String _target = null;

	public Modification(Node node, int srcId, String target, TYPE changeNodeType){
		_node = node;
		_sourceID = srcId;
		_target = target;
		_nodeType = changeNodeType;
	}
	
	public TYPE getNodeType(){
		return _nodeType;
	}
	
	public Node getSrcNode(){
		return _node;
	}
	
	public int getSourceID(){
		return _sourceID;
	}
	
	public String getTargetString(){
		return _target;
	}
	
	public boolean apply(Map<String, Type> usableVars){
		return _node.adapt(this);
	}
	
	public boolean backup(){
		return _node.backup(this);
	}
	
	public boolean restore(){
		return _node.restore(this);
	}
	
	public boolean compatible(Modification modification){
		if(_node == modification._node && _sourceID == modification._sourceID){
			return false;
		}
		return true;
	}
	
}
