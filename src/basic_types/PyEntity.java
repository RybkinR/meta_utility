package basic_types;

import javax.swing.JPanel;

import code_porcessor.CodeManager;

public abstract class PyEntity {
	private String name;
	//obsolete
	private JPanel enSpecificPane;
	
	PyEntity(String name){
		this.name = name;
		enSpecificPane = new JPanel();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public abstract String getType();
	
	public abstract String pyCode();
	
	//obsolete
	JPanel getEnSpecificPane(){
		return enSpecificPane;
	}
	
	public abstract JPanel genSpecPane(CodeManager cm);

	@Override
	public String toString() {
		return getType() + " " + name;
	}
	
	
}
