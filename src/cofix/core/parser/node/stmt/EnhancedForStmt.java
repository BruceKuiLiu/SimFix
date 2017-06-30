/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.core.parser.node.stmt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
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
import cofix.core.parser.node.expr.Svd;
import sun.org.mozilla.javascript.internal.ast.Loop;

/**
 * @author Jiajun
 * @datae Jun 23, 2017
 */
public class EnhancedForStmt extends Stmt {

	private Svd _varDecl = null;
	private Expr _expression = null;
	private Stmt _statement = null;
	
	private Stmt _statement_replace = null;
	
	/**
	 * EnhancedForStatement:
     *	for ( FormalParameter : Expression )
     *	                   Statement
	 */
	public EnhancedForStmt(int startLine, int endLine, ASTNode node) {
		this(startLine, endLine, node, null);
	}
	
	public EnhancedForStmt(int startLine, int endLine, ASTNode node, Node parent) {
		super(startLine, endLine, node, parent);
	}
	
	public void setParameter(Svd varDecl){
		_varDecl = varDecl;
	}
	
	public void setExpression(Expr expression){
		_expression = expression;
	}
	
	public void setBody(Stmt statement){
		_statement = statement;
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
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("for(");
		stringBuffer.append(_varDecl.toSrcString());
		stringBuffer.append(" : ");
		stringBuffer.append(_expression.toSrcString());
		if(_statement_replace != null){
			stringBuffer.append(_statement_replace.toSrcString());
		} else {
			stringBuffer.append(_statement.toSrcString());
		}
		return stringBuffer;
	}

	@Override
	public List<Literal> getLiterals() {
		List<Literal> list = _varDecl.getLiterals();
		list.addAll(_expression.getLiterals());
		if(_statement != null){
			list.addAll(_statement.getLiterals());
		}
		return list;
	}

	@Override
	public List<Variable> getVariables() {
		List<Variable> list = _varDecl.getVariables();
		list.addAll(_expression.getVariables());
		if(_statement != null){
			list.addAll(_statement.getVariables());
		}
		return list;
	}

	@Override
	public List<LoopStruct> getLoopStruct() {
		List<LoopStruct> list = new LinkedList<>();
		LoopStruct loopStruct = new LoopStruct(this, LoopStruct.KIND.EFOR);
		list.add(loopStruct);
		if(_statement != null){
			list.addAll(_statement.getLoopStruct());
		}
		return list;
	}
	
	@Override
	public List<CondStruct> getCondStruct() {
		List<CondStruct> list = new LinkedList<>();
		if(_statement != null){
			list.addAll(_statement.getCondStruct());
		}
		return list;
	}

	@Override
	public List<MethodCall> getMethodCalls() {
		List<MethodCall> list = _expression.getMethodCalls();
		if(_statement != null){
			list.addAll(_statement.getMethodCalls());
		}
		return list;
	}

	@Override
	public List<Operator> getOperators() {
		List<Operator> list = _expression.getOperators();
		if(_statement != null){
			list.addAll(_statement.getOperators());
		}
		return list;
	}
	
	@Override
	public List<OtherStruct> getOtherStruct() {
		List<OtherStruct> list = new LinkedList<>();
		if(_statement != null){
			list.addAll(_statement.getOtherStruct());
		}
		return list;
	}
}
