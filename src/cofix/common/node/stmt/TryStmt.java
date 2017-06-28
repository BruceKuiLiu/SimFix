/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.common.node.stmt;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;

import cofix.common.node.Node;
import cofix.common.node.metric.CondStruct;
import cofix.common.node.metric.Literal;
import cofix.common.node.metric.MethodCall;
import cofix.common.node.metric.Operator;
import cofix.common.node.metric.OtherStruct;
import cofix.common.node.metric.LoopStruct;
import cofix.common.node.metric.Variable;
import cofix.common.node.modify.Modification;

/**
 * @author Jiajun
 * @datae Jun 23, 2017
 */
public class TryStmt extends Stmt {

	private Blk _blk = null;
	
	/**
	 * TryStatement:
     *	try [ ( Resources ) ]
     *	    Block
     *	    [ { CatchClause } ]
     *	    [ finally Block ]
	 */
	public TryStmt(int startLine, int endLine, ASTNode node) {
		this(startLine, endLine, node, null);
	}

	public TryStmt(int startLine, int endLine, ASTNode node, Node parent) {
		super(startLine, endLine, node, parent);
	}
	
	public void setBody(Blk blk){
		_blk = blk;
	}
	
	@Override
	public boolean match(Node node, Map<String, Type> allUsableVariables, List<Modification> modifications) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean adapt(Modification modification) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean restore(Modification modification) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean backup(Modification modification) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Literal> getLiterals() {
		return _blk.getLiterals();
	}

	@Override
	public List<Variable> getVariables() {
		return _blk.getVariables();
	}

	@Override
	public List<LoopStruct> getLoopStruct() {
		return _blk.getLoopStruct();
	}
	
	@Override
	public List<CondStruct> getCondStruct() {
		return _blk.getCondStruct();
	}

	@Override
	public List<MethodCall> getMethodCalls() {
		return _blk.getMethodCalls();
	}

	@Override
	public List<Operator> getOperators() {
		return _blk.getOperators();
	}

	@Override
	public List<OtherStruct> getOtherStruct() {
		return _blk.getOtherStruct();
	}
}
