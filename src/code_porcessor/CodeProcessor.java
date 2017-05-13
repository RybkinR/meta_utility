package code_porcessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import basic_types.PyClass;
import basic_types.PyEntity;
import basic_types.PyFunction;
import gui.CodeTree;
import support.FileWorker;

public class CodeProcessor {
	
	public static void codeReader(String path, CodeManager cm){
		try {
			ArrayList<String> code = FileWorker.fileReader(path);
			String line = new String();
			for(int i = 0; i < code.size(); i++){
				System.out.println(i);
				line = code.get(i);
				switch(lineType(line)){
				case "class":
					PyClass cl = new PyClass(getClassName(line));
					i = processClass(code, cl, i);
					cm.addEntity(cl);
					break;
				case "func":
					PyFunction func = new PyFunction(getFuncName(line));
					i = processFunction(code, func, i);
					cm.addEntity(func);
					break;
				default:
					continue;
				}
			}
		} catch (IOException e) {
			System.err.println("ERROR: failed to read file '" + path + "'");
			return;
		}
	}
	
	public static void codeWriter(CodeManager cm, String path){
		ArrayList<PyEntity> code = cm.getEntities();
		String result = new String();
		for(PyEntity ent: code){
			switch(ent.getType()){
			case "Class":
				PyClass cl = (PyClass) ent;
				result += cl.pyCode();
				break;
			case "Function":
				PyFunction func = (PyFunction) ent;
				result += func.pyCode();
				break;
			}
		}
		
		try {
			FileWorker.fileWriter(path, result);
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: failed to write in file '" + path + "'");
		}
	}
	
	private static int processClass(ArrayList<String> code, PyClass cl, int n){
		System.err.println("VITAL CHECK: " + code.get(n));
		n++;
		int offset = tabNum(code.get(n));
		for(; n < code.size(); ){
			String line = code.get(n);
			if(line.equals("")){
				n++;
				continue;
			}
			System.err.println(line);
			if(offset != tabNum(line)){
				System.err.println("Offset check: " + line);
				break;
			}
			
			switch(lineType(line)){
			case "field":
				cl.addField(getFieldName(line), getFieldValue(line));
				break;
			case "func":
				PyFunction func = new PyFunction(getFuncName(line));
				System.err.println("Check");
				n = processFunction(code, func, n);
				//System.err.println("VITAL CHECK: " + code.get(n));
				cl.addFunc(func);
				break;
			}
			n++;
			
		}
		
		return ++n;
	}
	
	private static int processFunction(ArrayList<String> code, PyFunction func, int n){
		int offset = tabNum(code.get(n));
		System.err.println(offset);
		func.setParams(getParams(code.get(n)));
		n++;
		for(; n < code.size(); ){
			String line = code.get(n);
			System.err.println("Current: " + tabNum(line));
			if(offset != tabNum(line)) break;
			func.addData(line);
			n++;
		}
		return ++n;
	}
		
	
	private static String getClassName(String line){
		String result = line;
		result = result.replaceAll(" ", "");
		result = result.replaceAll("\t", "");
		result = result.replaceAll("\n", "");
		result = result.replaceAll("class", "");
		result = result.replaceAll(":", "");
		return result;
		
	}
	
	private static String getFuncName(String line){
		String buf = line;
		String result = new String();
		
		buf = buf.replaceAll("def ", "");
		buf = buf.replaceAll(" ", "");
		buf = buf.replaceAll("\t", "");
		buf = buf.replaceAll("\n", "");
		
		for(int i = 0; i < buf.length(); i++){
			if(buf.charAt(i) == '(') break;
			result += buf.charAt(i);
		}
		
		return result;
		
	}
	
	private static ArrayList<String> getParams(String line){
		ArrayList<String> res = new ArrayList<String>();
		
		String buf = line;
		buf = buf.split("[(]")[1];
		buf = buf.split("[)]")[0];
		String[] params = buf.split(",");
		for(int i = 0; i < params.length; i++){
			res.add(params[i]);
		}
		return res;
	}
	
	private static String getFieldName(String line){
		String result = line.split("=")[0];
		result = result.replaceAll(" ", "");
		result = result.replaceAll("\t", "");
		return result;
	}
	
	private static String getFieldValue(String line){
		return line.split("=")[1];
	}
	
	
	
	private static String lineType(String line){
		if(isClass(line)) return "class";
		if(isField(line)) return "field";
		if(isFunction(line)) return "func";
		return "default";
	}
	
	private static boolean isClass(String line){
		System.out.println(line.contains("class "));
		System.out.println(line.endsWith(":"));
		if(line.contains("class ") && line.endsWith(":")) return true;
		return false;
	}
	
	private static boolean isFunction(String line){
		if(line.contains("def ") && line.endsWith("):")) return true;
		return false;
	}

	private static boolean isField(String line){
		Pattern p = Pattern.compile("[\\s]*[a-z]{1}[\\w]*[\\s]?=[\\s]?.+");
		Matcher m = p.matcher(line);  
		System.out.println("Check: " + line + " " + m.matches());
        return m.matches();  
	}
	
	private static int tabNum(String line){
		int n = 0;
		
		for(;n < line.length(); n++){
			if(!Character.isSpace(line.charAt(n))) break;
		}
		
		return n;
			
	}
}
