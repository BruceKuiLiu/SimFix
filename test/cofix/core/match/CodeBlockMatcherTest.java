/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */
package cofix.core.match;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.Test;

import cofix.common.astnode.CodeBlock;
import cofix.common.astnode.Literal;
import cofix.common.astnode.MethodCall;
import cofix.common.astnode.Operator;
import cofix.common.astnode.Structure;
import cofix.common.astnode.Variable;
import cofix.common.code.search.CodeSearch;
import cofix.common.config.Constant;
import cofix.common.parser.ProjectInfo;
import cofix.common.util.JavaFile;
import cofix.common.util.Subject;

/**
 * @author Jiajun
 * @datae May 31, 2017
 */
public class CodeBlockMatcherTest {
	
	@Test
	public void test_chart_1() {
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("chart", 1, "/source", "/tests", "/build", "/build-tests");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() +"/org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java";
		int buggyLine = 1797;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
		
	}
	
	@Test
	public void test_chart_2() {
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("chart", 2, "/source", "/tests", "/build", "/build-tests");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/jfree/data/general/DatasetUtilities.java";
		int buggyLine = 752;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_chart_3() {
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("chart", 3, "/source", "/tests", "/build", "/build-tests");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/jfree/data/time/TimeSeries.java";
		int buggyLine = 1057;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_chart_7(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("chart", 7, "/source", "/tests", "/build", "/build-tests");
		ProjectInfo.init(subject);
		String file = subject.getHome() + "/source/org/jfree/data/time/TimePeriodValues.java";
		int buggyLine = 299;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_chart_11(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("chart", 11, "/source", "/tests", "/build", "/build-tests");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/jfree/chart/util/ShapeUtilities.java";
		int buggyLine = 275;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_chart_12(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("chart", 12, "/source", "/tests", "/build", "/build-tests");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/jfree/chart/plot/MultiplePiePlot.java";
		int buggyLine = 145;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_chart_20(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("chart", 20, "/source", "/tests", "/build", "/build-tests");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/jfree/chart/plot/ValueMarker.java";
		int buggyLine = 95;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_closure_14(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("closure", 14, "/src", "/test", "/build/classes", "/build/test");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/com/google/javascript/jscomp/ControlFlowAnalysis.java";
		int buggyLine = 767;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_closure_57(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("closure", 57, "/src", "/test", "/build/classes", "/build/test");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/com/google/javascript/jscomp/ClosureCodingConvention.java";
		int buggyLine = 197;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_closure_73(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("closure", 73, "/src", "/test", "/build/classes", "/build/test");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/com/google/javascript/jscomp/CodeGenerator.java";
		int buggyLine = 1045;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_closure_77(){
		// TODO : switch-case statement, lack of case "case '\0': sb.append("\\0"); break;"
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("closure", 77, "/src", "/test", "/build/classes", "/build/test");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/com/google/javascript/jscomp/CodeGenerator.java";
		int buggyLine = 966;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_lang_16(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("lang", 16, "/src/main/java", "/src/test/java", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/lang3/math/NumberUtils.java";
		int buggyLine = 458;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_lang_33(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("lang", 33, "/src/main/java", "/src/test/java", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/lang3/ClassUtils.java";
		int buggyLine = 907;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_lang_35(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("lang", 35, "/src/main/java", "/src/test/java", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/lang3/ArrayUtils.java";
		int buggyLine = 3295;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_lang_39(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("lang", 39, "/src/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/lang3/StringUtils.java";
		int buggyLine = 3676;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_lang_43(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("lang", 43, "/src/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/lang/text/ExtendedMessageFormat.java";
		int buggyLine = 421;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_lang_58(){
		// TODO : switch case , should be completed
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("lang", 58, "/src/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/lang/math/NumberUtils.java";
		int buggyLine = 452;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	

	@Test
	public void test_lang_59(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("lang", 59, "/src/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/lang/text/StrBuilder.java";
		int buggyLine = 885;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_lang_60(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("lang", 60, "/src/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/lang/text/StrBuilder.java";
		int buggyLine = 1673;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_5(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 5, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math3/complex/Complex.java";
		int buggyLine = 304;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_33(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 33, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math3/optimization/linear/SimplexTableau.java";
		int buggyLine = 338;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_35(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 35, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math3/genetics/ElitisticListPopulation.java";
		int buggyLine = 51;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_41(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 41, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/stat/descriptive/moment/Variance.java";
		int buggyLine = 520;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_49(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 49, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/linear/OpenMapRealVector.java";
		int buggyLine = 345;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_53(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 53, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/complex/Complex.java";
		int buggyLine = 153;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_59(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 59, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/util/FastMath.java";
		int buggyLine = 3482;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_63(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 63, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/util/MathUtils.java";
		int buggyLine = 417;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_70(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 70, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/analysis/solvers/BisectionSolver.java";
		int buggyLine = 72;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	@Test
	public void test_math_71(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 71, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/ode/nonstiff/EmbeddedRungeKuttaIntegrator.java";
		int buggyLine = 294;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_72(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 72, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/analysis/solvers/BrentSolver.java";
		int buggyLine = 115;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_75(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 75, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/stat/Frequency.java";
		int buggyLine = 303;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_79(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 79, "/src/main/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/util/MathUtils.java";
		int buggyLine = 1626;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}

	@Test
	public void test_math_98(){
		Constant.PROJECT_HOME = "testfile";
		Subject subject = new Subject("math", 98, "/src/java", "", "", "");
		ProjectInfo.init(subject);
		String file = subject.getHome() + subject.getSsrc() + "/org/apache/commons/math/linear/BigMatrixImpl.java";
		int buggyLine = 991;
		int lineRange = 10;
		check(file, buggyLine, lineRange);
	}
	
	
	private void check(String file, int buggyLine, int lineRange){
		CompilationUnit unit = (CompilationUnit) JavaFile.genASTFromSource(JavaFile.readFileToString(file), ASTParser.K_COMPILATION_UNIT);
		CodeSearch codeSearch = new CodeSearch(unit, buggyLine, lineRange);
		List<Statement> nodes = codeSearch.getASTNodes();
		CodeBlock codeBlock = new CodeBlock(unit, nodes);
		print(codeBlock);
	}
	
	private void print(CodeBlock codeBlock){
		System.out.println("----------------- Constant -----------------");
		for(Entry<Literal, Integer> entry : codeBlock.getConstants().entrySet()){
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		System.out.println("----------------- Variable -----------------");
		for(Entry<Variable, Integer> entry : codeBlock.getVariables().entrySet()){
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		System.out.println("----------------- Structure -----------------");
		for(Structure structure : codeBlock.getStructures()){
			System.out.println(structure);
		}
		
		System.out.println("----------------- Operator -----------------");
		for(Operator operator : codeBlock.getOperators()){
			System.out.println(operator.toString());
		}
		
		System.out.println("----------------- MethodCall -----------------");
		for(Entry<MethodCall, Integer> entry : codeBlock.getMethodCalls().entrySet()){
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

}
