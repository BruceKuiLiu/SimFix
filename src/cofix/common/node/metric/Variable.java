/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.common.node.metric;

import org.eclipse.jdt.core.dom.Type;

import cofix.common.node.Node;

/**
 * @author Jiajun
 * @datae Jun 23, 2017
 */
public class Variable extends Feature {

	private String _name = null;
	private Type _type = null;
	
	public Variable(Node node, String name, Type type) {
		super(node);
		_name = name;
		_type = type;
	}
	
}
