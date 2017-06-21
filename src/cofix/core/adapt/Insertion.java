/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.core.adapt;

import java.util.Map;

import org.eclipse.jdt.core.dom.Type;

import cofix.common.astnode.Expr;

/**
 * @author Jiajun
 * @datae Jun 7, 2017
 */
public class Insertion extends Delta {

	public Insertion(Expr expr) {
		super(expr);
	}
	
	@Override
	public String toString() {
		String source = _expr.toString().trim();
		source = source.replace("\n", "\n-");
		return source;
	}

	@Override
	public boolean apply(Map<String, Type> allUsableVarMap) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean restore() {
		// TODO Auto-generated method stub
		return false;
	}

}
