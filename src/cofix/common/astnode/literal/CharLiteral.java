/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.common.astnode.literal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;

import cofix.common.astnode.Expr;
import cofix.common.astnode.expr.Variable;
import cofix.core.adapt.Delta;
import cofix.core.adapt.Revision;

/**
 * @author Jiajun
 *
 */
public class CharLiteral extends Literal {

	private char _value;

	public CharLiteral(ASTNode node, char value) {
		_srcNode = node;
		_value = value;
	}

	@Override
	public Character getValue() {
		return _value;
	}

	@Override
	public Type getType() {
		AST ast = AST.newAST(AST.JLS8);
		return ast.newPrimitiveType(PrimitiveType.CHAR);
	}

	@Override
	public CharacterLiteral genAST() {
		AST ast = AST.newAST(AST.JLS8);
		CharacterLiteral literal = ast.newCharacterLiteral();
		literal.setCharValue(_value);
		return literal;
	}
	
	@Override
	public int hashCode() {
		return Character.valueOf(_value).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(!(obj instanceof CharLiteral)){
			return false;
		}
		CharLiteral other = (CharLiteral) obj;
		return _value == other.getValue().charValue();
	}
	
	@Override
	public String toString() {
		return "'" + _value + "'";
	}

	@Override
	public boolean matchType(Expr expr, Map<String, Type> allUsableVariables, List<Delta> modifications) {
		// exactly match
		if(expr instanceof CharLiteral){
			CharLiteral other = (CharLiteral) expr;
			if(_value != other.getValue()){
				Revision revision = new Revision(this);
				revision.setTar(expr);
				revision.setModificationComplexity(1);
				modifications.add(revision);
			}
			return true;
		} else if(expr != null){
			// type match
			Type type = expr.getType();
			if(type != null && type.toString().equals("char")){
				Revision revision = new Revision(this);
				revision.setTar(expr);
				revision.setModificationComplexity(1);
				modifications.add(revision);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Expr adapt(Expr tar, Map<String, Type> allUsableVarMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Variable> getVariables() {
		return new ArrayList<>();
	}

	@Override
	public void backup() {
		_backup = new CharLiteral(_srcNode, _value);
	}

	@Override
	public void restore() {
		this._value = ((CharLiteral)_backup).getValue();
		this._srcNode = _backup.getOriginalASTnode();
	}

}
