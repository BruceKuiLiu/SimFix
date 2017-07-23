/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.internal.core.SourceField;
import org.junit.runner.Result;

import com.sun.org.apache.bcel.internal.classfile.SourceFile;
import com.sun.org.apache.xpath.internal.operations.Mod;

import cofix.common.config.Constant;
import cofix.common.inst.Instrument;
import cofix.common.inst.MethodInstrumentVisitor;
import cofix.common.junit.runner.JUnitEngine;
import cofix.common.junit.runner.JUnitRuntime;
import cofix.common.junit.runner.OutStream;
import cofix.common.localization.AbstractFaultlocalization;
import cofix.common.localization.ManualLocator;
import cofix.common.run.Runner;
import cofix.common.util.JavaFile;
import cofix.common.util.Pair;
import cofix.common.util.Status;
import cofix.common.util.Subject;
import cofix.core.match.CodeBlockMatcher;
import cofix.core.modify.Modification;
import cofix.core.modify.Revision;
import cofix.core.parser.NodeUtils;
import cofix.core.parser.node.CodeBlock;
import cofix.core.parser.node.Node;
import cofix.core.parser.search.BuggyCode;
import cofix.core.parser.search.SimpleFilter;
import javassist.bytecode.analysis.ControlFlow.Block;

/**
 * @author Jiajun
 * @datae Jun 20, 2017
 */
public class Repair {

	private AbstractFaultlocalization _localization = null;
	private Subject _subject = null;
	private List<String> _failedTestCases = null;
	private Map<Integer, Set<Pair<String, String>>> _passedTestCasesMap = null;
	public Repair(Subject subject, AbstractFaultlocalization fLocalization) {
		_localization = fLocalization;
		_subject = subject;
		_failedTestCases = fLocalization.getFailedTestCases();
		_passedTestCasesMap = new HashMap<>();
//		try {
//			computeMethodCoverage();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	private void computeMethodCoverage() throws IOException{
		JUnitRuntime runtime = new JUnitRuntime(_subject);
		String src = _subject.getHome() + _subject.getSsrc();
		MethodInstrumentVisitor methodInstrumentVisitor = new MethodInstrumentVisitor();
		Instrument.execute(src, methodInstrumentVisitor);
		
		if(!Runner.compileSubject(_subject)){
			System.err.println("Build project failed!");
			System.exit(0);
		}
		
		System.out.println("Passed test classes : " + _localization.getPassedTestCases().size());
		for(String test : _localization.getPassedTestCases()){
			String[] testStr = test.split("#");
			String clazz = testStr[0];
			String methodName = testStr[1];
			OutStream outStream = new OutStream();
			Result result = JUnitEngine.getInstance(runtime).test(clazz, methodName, new PrintStream(outStream));
			if(result.getFailureCount() > 0){
				System.out.println("Error : Passed test cases running failed ! => " + clazz);
				System.exit(0);
			}
			for(Integer method : outStream.getOut()){
				Set<Pair<String, String>> tcases = _passedTestCasesMap.get(method);
				if(tcases == null){
					tcases = new HashSet<>();
				}
				tcases.add(new Pair<String, String>(clazz, methodName));
				_passedTestCasesMap.put(method, tcases);
			}
		}
		// restore source file
		_subject.restore();
	}
	
//	private void computeMethodCoverage() throws IOException{
//		JUnitRuntime runtime = new JUnitRuntime(_subject);
//		String src = _subject.getHome() + _subject.getSsrc();
//		MethodInstrumentVisitor methodInstrumentVisitor = new MethodInstrumentVisitor();
//		Instrument.execute(src, methodInstrumentVisitor);
//		
//		if(!Runner.compileSubject(_subject)){
//			System.err.println("Build project failed!");
//			System.exit(0);
//		}
//		
//		System.out.println("Passed test classes : " + _localization.getPassedTestCases().size());
//		for(String test : _localization.getPassedTestCases()){
//			String[] testStr = test.split("#");
//			String clazz = testStr[0];
//			String methodName = testStr[1];
//			OutStream outStream = new OutStream();
//			Result result = JUnitEngine.getInstance(runtime).test(clazz, methodName, new PrintStream(outStream));
//			if(result.getFailureCount() > 0){
//				System.out.println("Error : Passed test cases running failed ! => " + clazz);
//				System.exit(0);
//			}
//			for(Integer method : outStream.getOut()){
//				Set<Pair<String, String>> tcases = _passedTestCasesMap.get(method);
//				if(tcases == null){
//					tcases = new HashSet<>();
//				}
//				tcases.add(new Pair<String, String>(clazz, methodName));
//				_passedTestCasesMap.put(method, tcases);
//			}
//		}
//		// restore source file
//		_subject.restore();
//	}
	
//	public Status splitFix(Timer timer){
//		Status status = Status.FAILED;
//		for(int i = 0; i < _failedTestCases.size(); i++){
//			String failedTest = _failedTestCases.get(i);
//			String[] clazzAndMethod = failedTest.split("::");
//			if(clazzAndMethod.length < 2){
//				System.out.println("Failed test case format error : " + failedTest);
//				System.exit(0);
//			}
//			String clazz = clazzAndMethod[0].replace(".", "/");
//			String testFile = _subject.getHome() + _subject.getSsrc() + "/" + clazz + ".java";
//			String testBin = _subject.getHome() + _subject.getTbin() + "/" + clazz + ".class";
//			
//		}
//		
//		return status;
//	}

	public Status fix(Timer timer) throws IOException{
		String src = _subject.getHome() + _subject.getSsrc();
		List<Pair<String, Integer>> locations = _localization.getLocations(100);
		int correct = 0;
		Set<String> haveTryBuggySourceCode = new HashSet<>();
		Status status = Status.FAILED;
		Set<String> patches = new HashSet<>();
		for(Pair<String, Integer> loc : locations){
			_subject.restore();
			System.out.println(loc.getFirst() + "," + loc.getSecond());
			
			String file = _subject.getHome() + _subject.getSsrc() + "/" + loc.getFirst().replace(".", "/") + ".java";
			String binFile = _subject.getHome() + _subject.getSbin() + "/" + loc.getFirst().replace(".", "/") + ".class";
			// get buggy code block
			CodeBlock buggyblock = BuggyCode.getBuggyCodeBlock(file, loc.getSecond());
			Integer methodID = buggyblock.getWrapMethodID(); 
			if(methodID == null){
				logMessage(new Date(System.currentTimeMillis()).toString() + " : " + loc.getFirst() + "," + loc.getSecond() + "=>Find no block");
				System.out.println("Find no block!");
				continue;
			}
			logMessage(loc.getFirst() + "," + loc.getSecond());
			List<CodeBlock> buggyBlockList = new LinkedList<>();
			buggyBlockList.addAll(buggyblock.reduce());
			buggyBlockList.add(buggyblock);
			
			for(CodeBlock oneBuggyBlock : buggyBlockList){
				String currentBlockString = oneBuggyBlock.toSrcString().toString();
				if(haveTryBuggySourceCode.contains(currentBlockString)){
					continue;
				}
				haveTryBuggySourceCode.add(currentBlockString);
				_subject.restore();
				Pair<Integer, Integer> range = oneBuggyBlock.getLineRangeInSource();
				Set<String> haveTryPatches = new HashSet<>();
				// get all variables can be used at buggy line
				Map<String, Type> usableVars = NodeUtils.getUsableVarTypes(file, loc.getSecond());
				// search candidate similar code block
				SimpleFilter simpleFilter = new SimpleFilter(oneBuggyBlock);
				
				List<Pair<CodeBlock, Double>> candidates = simpleFilter.filter(src, 0.3);
				List<String> source = null;
				try {
					source = JavaFile.readFileToList(file);
				} catch (IOException e1) {
					System.err.println("Failed to read file to list : " + file);
					continue;
				}
				int i = 1;
	//			Set<String> already = new HashSet<>();
				for(Pair<CodeBlock, Double> similar : candidates){
					// try top 100 candidates
					if(i > 100){
						break;
					}
					System.out.println("=====================" + (i++) +"==============================");
					System.out.println(similar.getFirst().toSrcString().toString());
					// compute transformation
					List<Modification> modifications = CodeBlockMatcher.match(oneBuggyBlock, similar.getFirst(), usableVars);
					Map<String, Set<Node>> already = new HashMap<>();
					// try each transformation first
					List<Set<Modification>> list = new ArrayList<>();
					list.addAll(consistantModification(modifications));
					for(Modification modification : modifications){
						String modify = modification.toString();
						Set<Node> tested = already.get(modify);
						if(tested != null){
							if(tested.contains(modification.getSrcNode())){
								continue;
							} else {
								tested.add(modification.getSrcNode());
							}
						} else {
							tested = new HashSet<>();
							tested.add(modification.getSrcNode());
							already.put(modify, tested);
						}
						Set<Modification> set = new HashSet<>();
						set.add(modification);
						list.add(set);
					}
					
					List<Modification> legalModifications = new ArrayList<>();
					while(true){
						for(Set<Modification> modifySet : list){
							if(timer.timeout()){
								return Status.TIMEOUT;
							}
							for(Modification modification : modifySet){
								modification.apply(usableVars);
							}
							
							String replace = oneBuggyBlock.toSrcString().toString();
							if(haveTryPatches.contains(replace)){
								System.out.println("already try ...");
								for(Modification modification : modifySet){
									modification.restore();
								}
								if(legalModifications != null){
									for(Modification modification : modifySet){
										legalModifications.add(modification);
									}
								}
								continue;
							}
							
							System.out.println("========");
							System.out.println(replace);
							System.out.println("========");
							
							haveTryPatches.add(replace);
							try {
								JavaFile.sourceReplace(file, source, range.getFirst(), range.getSecond(), replace);
							} catch (IOException e) {
								System.err.println("Failed to replace source code.");
								continue;
							}
							try {
								FileUtils.forceDelete(new File(binFile));
							} catch (IOException e) {
							}
							
							// validate correctness of patch
							switch (validate(oneBuggyBlock)) {
							case COMPILE_FAILED:
//								haveTryPatches.remove(replace);
								break;
							case SUCCESS:
								String correctPatch = oneBuggyBlock.toSrcString().toString().replace("\\s*|\t|\r|\n", "");
								if(patches.contains(correctPatch)){
									continue;
								}
								patches.add(correctPatch);
								correct ++;
								dumpPatch("Find a patch", file, range, oneBuggyBlock.toSrcString().toString());
								String target = Constant.HOME + "/patch/" + _subject.getName() + "/" + _subject.getId();
								File tarFile = new File(target);
								if(!tarFile.exists()){
									tarFile.mkdirs();
								}
								File sourceFile = new File(file);
								FileUtils.copyFile(sourceFile, new File(target + "/" + correct + "_" + sourceFile.getName()));
								status = Status.SUCCESS;
								if(correct == Constant.PATCH_NUM){
									return Status.SUCCESS;
								}
								break; //remove passed revision
							case TEST_FAILED:
								if(legalModifications != null){
									for(Modification modification : modifySet){
										legalModifications.add(modification);
									}
								}
							}
							for(Modification modification : modifySet){
								modification.restore();
							}
						}
						if(legalModifications == null){
							break;
						}
						list = combineModification(legalModifications);
						legalModifications = null;
					}
				}
			}
		}
		return status;
	}
	
	private void logMessage(String message){
		JavaFile.writeStringToFile("result.log", new Date(System.currentTimeMillis()).toString() + " " + message + "\n", true);
	}
	
	private void dumpPatch(String message, String file, Pair<Integer, Integer> codeRange, String text){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\n----------------------------------------\n----------------------------------------\n");
		stringBuffer.append(message + " : [" + file + "=>" + codeRange.getFirst() + "," + codeRange.getSecond() + "]\n");
		stringBuffer.append(text);
		SimpleDateFormat simpleFormat=new SimpleDateFormat("yy/MM/dd HH:mm"); 
		stringBuffer.append("\nTime : " + simpleFormat.format(new Date()) + "\n");
		stringBuffer.append("----------------------------------------\n");
		System.out.println(stringBuffer.toString());
		JavaFile.writeStringToFile("result.log", stringBuffer.toString(), true);
	}
	
	
	private List<Set<Modification>> consistantModification(List<Modification> modifications){
		List<Set<Modification>> result = new LinkedList<>();
		List<Modification> revisions = new ArrayList<>();
		for(Modification modification : modifications){
			if(modification instanceof Revision){
				revisions.add(modification);
			}
		}
		
		for(int i = 0; i < revisions.size(); i++){
			Set<Modification> consistant = new HashSet<>();
			Modification modification = revisions.get(i);
			consistant.add(modification);
			for(int j = i+1; j < revisions.size(); j++){
				Modification other = revisions.get(j);
				if(modification.compatible(other) && modification.getTargetString().equals(other.getTargetString())){
					consistant.add(other);
				}
			}
			if(consistant.size() > 0){
				result.add(consistant);
			}
		}
		
		return result;
	}
	private List<Set<Modification>> combineModification(List<Modification> modifications){
		List<Set<Modification>> list = new ArrayList<>();
		int length = modifications.size();
		if(length == 0){
			return list;
		}
		int[][] incompatibleMap = new int[length][length];
		for(int i = 0; i < length; i++){
			for(int j = i; j < length; j++){
				if(i == j){
					incompatibleMap[i][j] = 1;
				} else if(modifications.get(i).compatible(modifications.get(j))){
					incompatibleMap[i][j] = 0;
					incompatibleMap[j][i] = 0;
				} else {
					incompatibleMap[i][j] = 1;
					incompatibleMap[i][j] = 1;
				}
			}
		}
		List<Set<Integer>> baseSet = new ArrayList<>();
		for(int i = 0; i < modifications.size(); i++){
			Set<Integer> set = new HashSet<>();
			set.add(i);
			baseSet.add(set);
		}
		
		List<Set<Integer>> expanded = expand(incompatibleMap, baseSet, 2, 3);
		for(Set<Integer> set : expanded){
			Set<Modification> combinedModification = new HashSet<>();
			for(Integer integer : set){
				combinedModification.add(modifications.get(integer));
			}
			list.add(combinedModification);
		}
		
		return list;
	}
	
	private List<Set<Integer>> expand(int[][] incompatibleTabe, List<Set<Integer>> baseSet, int currentSize, int upperbound){
		List<Set<Integer>> rslt = new LinkedList<>();
		if(currentSize > upperbound){
			return rslt;
		}
		int length = incompatibleTabe.length;
		for(Set<Integer> base : baseSet){
			int minIndex = 0;
			for(Integer integer : base){
				if(integer > minIndex){
					minIndex = integer;
				}
			}
			
			for(minIndex ++; minIndex < length; minIndex ++){
				boolean canExd = true;
				for(Integer integer : base){
					if(incompatibleTabe[minIndex][integer] == 1){
						canExd = false;
						break;
					}
				}
				if(canExd){
					Set<Integer> expanded = new HashSet<>(base);
					expanded.add(minIndex);
					rslt.add(expanded);
				}
			}
		}
		
		if(rslt.size() > 0){
			rslt.addAll(expand(incompatibleTabe, rslt, currentSize + 1, upperbound));
		}
		
		return rslt;
	}
	
	private ValidateStatus validate(CodeBlock buggyBlock){
		
		if(!Runner.compileSubject(_subject)){
			System.err.println("Build failed !");
			return ValidateStatus.COMPILE_FAILED;
		}
		
		// validate patch using failed test cases
		for(String testcase : _failedTestCases){
			String[] testinfo = testcase.split("::");
			if(!Runner.testSingleTest(_subject, testinfo[0], testinfo[1])){
				return ValidateStatus.TEST_FAILED;
			}
		}
		
		dumpPatch("Pass Single Test", "", new Pair<Integer, Integer>(0, 0), buggyBlock.toSrcString().toString());
		
		if(!Runner.runTestSuite(_subject)){
			return ValidateStatus.TEST_FAILED;
		}
		
		return ValidateStatus.SUCCESS;
	}
	
	private enum ValidateStatus{
		COMPILE_FAILED,
		TEST_FAILED,
		SUCCESS
	}

}
