/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.core.match;

import java.util.Map;
import java.util.Map.Entry;

import cofix.common.parser.astnode.CodeBlock;
import cofix.common.parser.astnode.literal.Literal;

/**
 * @author Jiajun
 * @datae May 31, 2017
 */
public class LiteralMetric extends Metric {

	public LiteralMetric(float weight) {
		_weight = weight;
	}
	
	@Override
	public float getSimilarity(CodeBlock src, CodeBlock tar) {
		return _weight * getPureSimilarity(src, tar);
	}
	
	private float getPureSimilarity(CodeBlock src, CodeBlock tar){
		float similarity = 0.0f;
		
		Map<Literal, Integer> srcVarMap = src.getConstants();
		Map<Literal, Integer> tarVarMap = tar.getConstants();
		float count = 0f;
		for(Entry<Literal, Integer> entry : srcVarMap.entrySet()){
			Literal srcliteral = entry.getKey();
			count += entry.getValue();
			Integer tarCount = tarVarMap.get(srcliteral);
			if(tarCount != null){
				similarity += entry.getValue() > tarCount ? tarCount : entry.getValue();
			}
		}
		similarity *= 2.0f;
		for(Entry<Literal, Integer> entry : tarVarMap.entrySet()){
			count += entry.getValue();
		}
		if(count == 0){
			return 1.0f;
		}
		return similarity / count;
	}

}
