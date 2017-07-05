/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.core.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.Type;

import com.sun.org.apache.xalan.internal.xsltc.compiler.NodeTest;

import cofix.common.util.Pair;
import cofix.core.metric.NewFVector;
import cofix.core.metric.Variable;
import cofix.core.metric.Variable.USE_TYPE;
import cofix.core.modify.Deletion;
import cofix.core.modify.Insertion;
import cofix.core.modify.Modification;
import cofix.core.parser.NodeUtils;
import cofix.core.parser.node.CodeBlock;
import cofix.core.parser.node.Node;
import cofix.core.parser.node.Node.TYPE;
import cofix.core.parser.node.expr.SName;
import cofix.core.parser.node.stmt.BreakStmt;
import cofix.core.parser.node.stmt.ContinueStmt;
import cofix.core.parser.node.stmt.ReturnStmt;
import cofix.core.parser.node.stmt.ThrowStmt;

/**
 * @author Jiajun
 * @datae Jun 29, 2017
 */
public class CodeBlockMatcher {

	public static double getSimilarity(CodeBlock buggyCode, CodeBlock codeBlock){
		NewFVector buggy = buggyCode.getFeatureVector();
		NewFVector other = codeBlock.getFeatureVector();
		return buggy.computeSimilarity(other, NewFVector.ALGO.COSINE);
	}
	
	public static List<Modification> match(CodeBlock buggyBlock, CodeBlock similarBlock, Map<String, Type> allUsableVariables){
		List<Modification> modifications = new LinkedList<>();
		// match variables first
		Map<String, String> varTrans = matchVariables(buggyBlock.getVariables(), similarBlock.getVariables());
		
		for(Entry<String, String> entry : varTrans.entrySet()){
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		
		List<Node> bNodes = buggyBlock.getParsedNode();
		List<Node> sNodes = similarBlock.getParsedNode();
		
		Map<Integer, Integer> match = new HashMap<>();
		Map<Integer, Integer> reverseMatch = new HashMap<>();
		for(int i = 0; i < bNodes.size(); i++){
			Node buggyNode = bNodes.get(i);
			for(int j = 0; j < sNodes.size(); j++){
				if(reverseMatch.containsKey(j)){
					continue;
				}
				Node simNode = sNodes.get(j);
				List<Modification> tmp = new LinkedList<>();
				if(buggyNode.match(simNode, varTrans, allUsableVariables, tmp)){
					match.put(i, j);
					reverseMatch.put(j, i);
					modifications.addAll(tmp);
				}
			}
		}
		
		// insert nodes at buggy code site
		for(int j = 0; j < sNodes.size(); j++){
			if(!reverseMatch.containsKey(j)){
				Node tarNode = sNodes.get(j);
				Map<SName, Pair<String, String>> record = NodeUtils.tryReplaceAllVariables(tarNode, varTrans, allUsableVariables);
				if(record == null){
					continue;
				}
				int nextMatchIndex = -1;
				for(int index = j; index < sNodes.size(); index++){
					if(reverseMatch.containsKey(index)){
						nextMatchIndex = index;
						break;
					}
				}
				if(nextMatchIndex == -1){
					int last = bNodes.size() - 1;
					for(; last >= 0; last --){
						Node node = bNodes.get(last);
						if(!(node instanceof ReturnStmt) && !(node instanceof ThrowStmt) && !(node instanceof BreakStmt) && !(node instanceof ContinueStmt)){
							break;
						}
					}
					nextMatchIndex = last >= 0 ? last : 0;
				}
				NodeUtils.replaceVariable(record);
				String target = tarNode.toSrcString().toString();
				Insertion insertion = new Insertion(buggyBlock, nextMatchIndex, target, TYPE.UNKNOWN);
				modifications.add(insertion);
				NodeUtils.restoreVariables(record);
			}
		}
		
		// delete nodes at buggy code site
		if(buggyBlock.getParsedNode().size() > 2){
			for(int i = 0; i < bNodes.size(); i++){
				if(!match.containsKey(i)){
					modifications.add(new Deletion(buggyBlock, i, null, TYPE.UNKNOWN));
				}
			}
		}
		
		return modifications;
	}
	
	private static Map<String, String> matchVariables(List<Variable> bVars, List<Variable> sVars){
		
		Map<Variable, List<USE_TYPE>> bMap = new HashMap<>();
		for(Variable variable : bVars){
			List<USE_TYPE> list = bMap.get(variable);
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(variable.getUseType());
			bMap.put(variable, list);
		}
			
		Map<Variable, List<USE_TYPE>> sMap = new HashMap<>();
		for(Variable variable : sVars){
			List<USE_TYPE> list = sMap.get(variable);
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(variable.getUseType());
			sMap.put(variable, list);
		}
		
		Map<String, Integer> buggyNameMap = new HashMap<>();
		Map<Integer, String> reverseBuggyNameMap = new HashMap<>(); 
		int i = 0;
		for(Variable variable : bMap.keySet()){
			buggyNameMap.put(variable.getName(), i);
			reverseBuggyNameMap.put(i, variable.getName());
			i++;
		}
		Map<String, Integer> simNameMap = new HashMap<>();
		Map<Integer, String> reverseSimNameMap = new HashMap<>();
		int j = 0;
		for(Variable variable : sMap.keySet()){
			simNameMap.put(variable.getName(), j);
			reverseSimNameMap.put(j, variable.getName());
			j++;
		}
		if(i < 1 || j < 1){
			return new HashMap<>();
		}
		double[][] similarityTable = new double[j][i];
		
		for(Entry<Variable, List<USE_TYPE>> sim : sMap.entrySet()){
			String simName = sim.getKey().getName();
			for(Entry<Variable, List<USE_TYPE>> buggy : bMap.entrySet()){
				String buggyName = buggy.getKey().getName();
				Double similarity = 0.7 * LCS(sim.getValue(), buggy.getValue()) + 0.1 * NodeUtils.nameSimilarity(simName, buggyName) + 0.2 * NodeUtils.typeSimilarity(sim.getKey().getType(), buggy.getKey().getType());
				if(similarity > 0.5){
					similarityTable[simNameMap.get(simName)][buggyNameMap.get(buggyName)] = similarity;
				}
			}
		}
		
		
		return tryMatch(similarityTable, reverseSimNameMap, reverseBuggyNameMap);
	}
	private static Map<String, String> tryMatch(double[][] similarityTable, Map<Integer, String> reverseSimNameMap, Map<Integer, String> reverseBuggyNameMap){
		Map<String, String> matchTable = new HashMap<>();
		int rowGuard = similarityTable.length;
		int colGuard = similarityTable[0].length;
		while(true){
			double currentBiggest = 0.1;
			int row = 0;
			int colum = 0;
			for(int i = 0; i < rowGuard; i++){
				for(int j = 0; j < colGuard; j++){
					if(similarityTable[i][j] > currentBiggest){
						currentBiggest = similarityTable[i][j];
						row = i;
						colum = j;
					}
				}
			}
			if(currentBiggest > 0.1){
				matchTable.put(reverseSimNameMap.get(row), reverseBuggyNameMap.get(colum));
				for(int j = 0; j < colGuard; j++){
					similarityTable[row][j] = 0;
				}
				for(int i = 0; i < rowGuard; i++){
					similarityTable[i][colum] = 0;
				}
			} else {
				break;
			}
		}
		return matchTable;
	}
	
	private static double LCS(List<USE_TYPE> first, List<USE_TYPE> snd){
		int firstLen = first.size();
		int sndLen = snd.size();
		if(firstLen == 0 || sndLen == 0){
			return 0;
		}
		int[][] score = new int[firstLen + 1][sndLen + 1];
		for(int i = 0; i < firstLen; i++){
			for(int j = 0; j < sndLen; j++){
				if(first.get(i).equals(snd.get(j))){
					score[i + 1][j + 1] = score[i][j] + 1; 
				} else {
					score[i + 1][j + 1] = Math.max(score[i + 1][j], score[i][j + 1]);
				}
			}
		}
		double s = (double)(score[firstLen][sndLen] * 2) / (double)(firstLen + sndLen);
		return s;
	}
	
}
