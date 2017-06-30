/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.core.parser.node.stmt;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;

import cofix.core.metric.CondStruct;
import cofix.core.metric.Literal;
import cofix.core.metric.LoopStruct;
import cofix.core.metric.MethodCall;
import cofix.core.metric.Operator;
import cofix.core.metric.OtherStruct;
import cofix.core.metric.Variable;
import cofix.core.modify.Modification;
import cofix.core.parser.node.Node;
import cofix.core.parser.node.expr.Expr;

/**
 * @author Jiajun
 * @datae Jun 23, 2017
 */
public class SynchronizedStmt extends Stmt {

	private Expr _expression = null;
	private Blk _blk = null;
	
	/**
	 * SynchronizedStatement:
     *	synchronized ( Expression ) Block
	 */
	public SynchronizedStmt(int startLine, int endLine, ASTNode node) {
		this(startLine, endLine, node, null);
	}

	public SynchronizedStmt(int startLine, int endLine, ASTNode node, Node parent) {
		super(startLine, endLine, node, parent);
	}
	
	public void setExpression(Expr expression){
		_expression = expression;
	}
	
	public void setBlock(Blk blk){
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
	public StringBuffer toSrcString() {
		StringBuffer stringBuffer = new StringBuffer("synchronized(");
		stringBuffer.append(_expression.toSrcString());
		stringBuffer.append(")");
		stringBuffer.append(_blk.toSrcString());
		return stringBuffer;
	}

	@Override
	public List<Literal> getLiterals() {
		List<Literal> list = _expression.getLiterals();
		list.addAll(_blk.getLiterals());
		return list;
	}

	@Override
	public List<Variable> getVariables() {
		List<Variable> list = _expression.getVariables();
		list.addAll(_blk.getVariables());
		return list;
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
