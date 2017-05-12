package dummy_tests;

import java.util.ArrayList;
import java.util.Iterator;

public class ListTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> l = new ArrayList<String>();
		for(int i = 0; i < 10; i++){
			l.add("hi");
		}
		
		f(l);
		
		System.out.println(l.isEmpty());
		
	}
	
	static void f(ArrayList<String> l){
		for(int i = l.size()-1; i > -1; i--){
			System.out.println(l.remove(i));
		}
	}

}
