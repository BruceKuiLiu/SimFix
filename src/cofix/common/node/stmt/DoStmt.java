/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.common.node.stmt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;

import cofix.common.node.Node;
import cofix.common.node.expr.Expr;
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
public class DoStmt extends Stmt {

	private Stmt _stmt = null;
	private Expr _expression = null;
	
	/**
	 * DoStatement:
     *	do Statement while ( Expression ) ;
	 */
	public DoStmt(int startLine, int endLine, ASTNode node) {
		this(startLine, endLine, node, null);
	}

	public DoStmt(int startLine, int endLine, ASTNode node, Node parent) {
		super(startLine, endLine, node, parent);
	}
	
	public void setBody(Stmt stmt){
		_stmt = stmt;
	}
	
	public void setExpression(Expr expression){
		_expression = expression;
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
		List<Literal> list = _expression.getLiterals();
		if(_stmt != null){
			list.addAll(_stmt.getLiterals());
		}
		return list;
	}

	@Override
	public List<Variable> getVariables() {
		List<Variable> list = _expression.getVariables();
		if(_stmt != null){
			list.addAll(_stmt.getVariables());
		}
		return list;
	}

	@Override
	public List<LoopStruct> getLoopStruct() {
		List<LoopStruct> list = new LinkedList<>();
		LoopStruct loopStruct = new LoopStruct(this, LoopStruct.KIND.DO);
		list.add(loopStruct);
		if(_stmt != null){
			list.addAll(_stmt.getLoopStruct());
		}
		return list;
	}
	
	@Override
	public List<CondStruct> getCondStruct() {
		List<CondStruct> list = new ArrayList<>();
		if(_stmt != null){
			list.addAll(_stmt.getCondStruct());
		}
		return list;
	}

	@Override
	public List<MethodCall> getMethodCalls() {
		List<MethodCall> list = _expression.getMethodCalls();
		if(_stmt != null){
			list.addAll(_stmt.getMethodCalls());
		}
		return list;
	}

	@Override
	public List<Operator> getOperators() {
		List<Operator> list = _expression.getOperators();
		if(_stmt != null){
			list.addAll(_stmt.getOperators());
		}
		return list;
	}
	
	@Override
	public List<OtherStruct> getOtherStruct() {
		List<OtherStruct> list = new LinkedList<>();
		if(_stmt != null){
			list.addAll(_stmt.getOtherStruct());
		}
		return list;
	}
	
}