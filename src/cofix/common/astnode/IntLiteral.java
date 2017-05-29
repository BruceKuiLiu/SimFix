/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package cofix.common.astnode;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.NumberLiteral;

public class IntLiteral extends Literal{

	private Integer _value = 0;
	
	public IntLiteral(int value) {
		_value = value;
	}
	
	@Override
	public Integer getValue() {
		return _value;
	}
	
	@Override
	public Class getType() {
		return int.class;
	}
	
	@Override
	public NumberLiteral genAST() {
		AST ast = AST.newAST(AST.JLS8);
		return ast.newNumberLiteral(String.valueOf(_value));
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(_value).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(!(obj instanceof IntLiteral)){
			return false;
		}
		IntLiteral other = (IntLiteral)obj;
		return this._value == other.getValue();
	}
	
	@Override
	public String toString() {
		return String.valueOf(_value);
	}
	
}
