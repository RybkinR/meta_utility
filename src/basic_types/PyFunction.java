package basic_types;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import code_porcessor.CodeManager;

public class PyFunction extends PyEntity{
	
	private ArrayList<String> params;
	private String data;
	
	public PyFunction(String name){
		super(name);
		params = new ArrayList<String>();
		data = new String();
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void addData(String data){
		this.data += "\n" + data;
	}
	
	public void setParams(ArrayList<String> params) {
		this.params = params;
	}

	
	public void addParam(String p){
		if(!params.contains(p))
			params.add(p);
	}
	
	public void deleteParam(String p){
		if(params.contains(p))
			params.remove(p);
	}
	
	public ArrayList<String> getParams(){
		return params;
	}
	
	public String getType(){
		return "Function";
	}
	
	public String pyCode(){
		String result = new String();
		
		result += "def " + this.getName() + "(";
		
		for(int i = 0; i < params.size(); i++){
			if(i < params.size() - 1){
				result += params.get(i) + ", ";
			}
			else{
				result += params.get(i);
			}
		}
		
		result += "):\n";
		
		if(!data.equals(null)) result += data + "\n";
		
		return result;
	}
	
	String pyCode(int n){
		
		String tabs = new String();
		for(int i = 0; i < n; i++){
			tabs += "\t";
		}
		
		String result = new String();
		
		result += tabs + "def " + this.getName() + "(";
		
		for(int i = 0; i < params.size(); i++){
			if(i < params.size() - 1){
				result += params.get(i) + ", ";
			}
			else{
				result += params.get(i);
			}
		}
		result += "):\n";

		if(!data.equals(null)) result += data + "\n";
		
		return result;
	}

	public JPanel genSpecPane(CodeManager cm) {
		
		JPanel res = new JPanel();
		
		res.setLayout(new GridLayout(2, 1));
		JLabel info = new JLabel();
		
		info.setText(pyCode());
		
		
		res.add(info);
		
		JPanel actionPane = new JPanel();
		actionPane.setLayout(new GridLayout(3, 1));
		JButton changeName = new JButton("Change name");
		JButton addParam = new JButton("Add param");
		JButton deleteParam = new JButton("Delete param");
		
		changeName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String prevName = getName();
				String name = JOptionPane.showInputDialog("Enter new name");
				cm.getFunction(prevName).setName(name);
				setName(name);
            	info.setText(pyCode());
            	res.updateUI();
			}
		});
		
		addParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String param = JOptionPane.showInputDialog("Enter parameter name");
				cm.getFunction(getName()).addParam(param);
				addParam(param);
            	info.setText(pyCode());
            	res.updateUI();
			}
		});
		
		deleteParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog("Enter parameter name");
				cm.getFunction(getName()).deleteParam(name);
				deleteParam(name);
            	info.setText(pyCode());
            	res.updateUI();
			}
		});
		
		actionPane.add(changeName);
		actionPane.add(addParam);
		actionPane.add(deleteParam);
		res.add(actionPane);
		return res;
	}
	
	public JPanel genSpecPane() {
		
		JPanel res = new JPanel();
		
		res.setLayout(new GridLayout(2, 1));
		JLabel info = new JLabel();
		
		info.setText(pyCode());
		
		
		res.add(info);
		
		JPanel actionPane = new JPanel();
		actionPane.setLayout(new GridLayout(3, 1));
		JButton changeName = new JButton("Change name");
		JButton addParam = new JButton("Add param");
		JButton deleteParam = new JButton("Delete param");
		
		changeName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog("Enter new name");
            	setName(name);
            	info.setText(pyCode());
            	res.updateUI();
			}
		});
		
		addParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String param = JOptionPane.showInputDialog("Enter parameter name");
            	addParam(param);
            	info.setText(pyCode());
            	res.updateUI();
			}
		});
		
		deleteParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog("Enter parameter name");
            	deleteParam(name);
            	info.setText(pyCode());
            	res.updateUI();
			}
		});
		
		actionPane.add(changeName);
		actionPane.add(addParam);
		actionPane.add(deleteParam);
		res.add(actionPane);
		return res;
	}
	
}
