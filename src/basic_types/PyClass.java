package basic_types;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import code_porcessor.CodeManager;

public class PyClass extends PyEntity {
	
	private HashMap<String, String> fields;
	private ArrayList<PyFunction> funcs;
	
	public PyClass(String name){
		super(name);
		fields = new HashMap<String, String>();
		funcs = new ArrayList<PyFunction>();
	}

	public HashMap<String, String> getFields() {
		return fields;
	}

	public void setFields(HashMap<String, String> fields) {
		this.fields = fields;
	}
	
	public void addField(String name, String val){
		fields.put(name, val);
	}
	
	public void delField(String name){
		if(fields.containsKey(name)) fields.remove(name);
	}

	public ArrayList<PyFunction> getFuncs() {
		return funcs;
	}

	public void setFuncs(ArrayList<PyFunction> funcs) {
		this.funcs = funcs;
	}
	
	public void addFunc(PyFunction f){
		funcs.add(f);
	}
	
	public void delFunc(PyFunction f){
		if(funcs.contains(f)) funcs.remove(f);
	}
	
	public String getType(){
		return "Class";
	}

	public String pyCode() {
		String result = new String();
		
		result += "#NOTICE: Generated code. Implementation check is vital \n" + "class " + this.getName() + ":\n";
		
		Iterator it = fields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        result += "\t" + pair.getKey() + " = " + pair.getValue() + "\n";
	    }
	    
	    for(PyFunction f: funcs){
	    	result += f.pyCode(1);
	    }
	    
		return result;
	}

	@Override
	public JPanel genSpecPane(CodeManager cm) {
		JPanel res = new JPanel();
		
		res.setLayout(new GridLayout(2, 1));
		
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(this.getName());

		DefaultTreeModel model = new DefaultTreeModel(rootNode);

		JTree structTree = new JTree(model);
		
		loadTree(rootNode, structTree);
	    
	    JPanel classPane = new JPanel();
	    classPane.setLayout(new GridLayout(1, 2));
	    classPane.add(structTree);
	    
	    JPanel actionPane = new JPanel();
	    JButton changeName = new JButton("Change name");
	    JButton addFunction = new JButton("Add function");
	    JButton addField = new JButton("Add field");
	    JButton delButton = new JButton("Delete");
	    
	    changeName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String prevName = getName();
				String name = JOptionPane.showInputDialog("Enter new name");
            	cm.getClass(prevName).setName(name);
            	setName(name);
            	rootNode.setUserObject(name);
            	loadTree(rootNode, structTree);
            	res.updateUI();
			}
		});
	    
	    addFunction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog("Enter function name");
            	PyFunction f = new PyFunction(name);
            	cm.getClass(getName()).addFunc(f);
            	addFunc(f);
            	loadTree(rootNode, structTree);
            	res.updateUI();
			}
		});
	    
	    addField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = JOptionPane.showInputDialog("Enter field name and value (<name>=<value>)");
				if(isField(input)){
					cm.getClass(getName()).addField(input.split("=")[0], input.split("=")[1]);
					addField(input.split("=")[0], input.split("=")[1]);
				}
            	loadTree(rootNode, structTree);
            	res.updateUI();
			}
		});
	    
	    delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TreePath path = structTree.getSelectionPath();
				if (path == null)
					return;
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (selectedNode != null && selectedNode.getUserObject() instanceof PyFunction) {
					PyFunction pe = (PyFunction) selectedNode.getUserObject();
					cm.getClass(getName()).delFunc(pe);
					delFunc(pe);
					
				}
				else if(selectedNode != null && selectedNode.getUserObject() instanceof String){
					String s = (String)  selectedNode.getUserObject();
					cm.getClass(getName()).delField(s.split("=")[0]);
					delField(s.split("=")[0]);
				}
            	loadTree(rootNode, structTree);
            	res.updateUI();
			}
		});
	    actionPane.setLayout(new GridLayout(4,1));
	    actionPane.add(changeName);
	    actionPane.add(addFunction);
	    actionPane.add(addField);
	    actionPane.add(delButton);
	    
	    JPanel entityPane = new JPanel();
	    
	    
	    structTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = structTree.getSelectionPath();
				if (path == null)
					return;
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (selectedNode != null && selectedNode.getUserObject() instanceof PyFunction) {
					final PyFunction selectedEn = (PyFunction) selectedNode.getUserObject();
					entityPane.removeAll();
					entityPane.add(selectedEn.genSpecPane());
					entityPane.updateUI();
				}
				else{
					entityPane.removeAll();
				}
				entityPane.updateUI();
			}
		});

	    classPane.add(actionPane);
	    structTree.setEditable(false);
	    
	    res.add(classPane);
	    res.add(entityPane);
	    
		return res;
	}
	
	private void loadTree(DefaultMutableTreeNode rootNode, JTree structTree){
		rootNode.removeAllChildren();
		Iterator it = fields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        
	        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(pair.getKey() + "=" + pair.getValue());
			newNode.setAllowsChildren(false);
			rootNode.add(newNode);
			structTree.updateUI();
	    }
	    
	    for(PyFunction f: funcs){
	    	System.out.println(f.getData());
	    	DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(f);
			newNode.setAllowsChildren(false);
			rootNode.add(newNode);
			structTree.updateUI();
	    }
	}
	
	private static boolean isField(String line){
		Pattern p = Pattern.compile("[\\s]*[a-z]{1}[\\w]*[\\s]?=[\\s]?.+");
		Matcher m = p.matcher(line);  
		System.out.println("Check: " + line + " " + m.matches());
        return m.matches();  
	}
	
}
