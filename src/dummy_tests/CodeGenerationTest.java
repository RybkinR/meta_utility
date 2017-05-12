package dummy_tests;

import basic_types.PyClass;
import basic_types.PyFunction;

public class CodeGenerationTest {

	public static void main(String[] args) {
		PyClass cl = new PyClass("test");
		cl.addField("x", "1");
		cl.addField("y", "[]");
		
		cl.addFunc(new PyFunction("foo"));

		PyFunction func = new PyFunction("func1");
		func.addParam("a");
		func.setData("\t\tprint(1 + 1)");
		cl.addFunc(func);
		
		System.out.println(cl.pyCode());

	}

}
