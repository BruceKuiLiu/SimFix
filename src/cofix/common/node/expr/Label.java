/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.common.node.expr;

import org.eclipse.jdt.core.dom.ASTNode;

import cofix.common.node.Node;

/**
 * @author Jiajun
 * @datae Jun 23, 2017
 */
public abstract class Label extends Expr {

	/**
	 * Name:
     *	SimpleName
     *	QualifiedName
	 */
	public Label(int startLine, int endLine, ASTNode node) {
		super(startLine, endLine, node);
	}

}
