/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.common.node.metric;

import cofix.common.node.Node;

/**
 * @author Jiajun
 * @datae Jun 28, 2017
 */
public abstract class Feature {
	
	protected Node _node = null;
	
	protected Feature(Node node) {
		_node = node;
	}
}
