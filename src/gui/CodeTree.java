package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import basic_types.PyClass;
import basic_types.PyEntity;
import basic_types.PyFunction;
import code_porcessor.CodeManager;
import code_porcessor.CodeProcessor;

public class CodeTree extends JFrame {

	/** 
	* 
	*/
	private static final long serialVersionUID = 1L;

	DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Code structure");

	private DefaultTreeModel model = new DefaultTreeModel(rootNode);

	private JTree structTree = new JTree(model);

	private JButton addButton = new JButton("Add function");
	
	private JButton addFolderButton = new JButton("Add class");
	
	private JButton delButton = new JButton("Delete entity");

	JScrollPane scrollPaneStructure = new JScrollPane(structTree);

	JPanel entitySpecificPane = new JPanel();
	
	JPanel actionPane = new JPanel();

	void addEntity(PyEntity ent) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ent);
		newNode.setAllowsChildren(false);
		rootNode.add(newNode);
		structTree.updateUI();
	}
	
	

	public CodeTree(String path) {
		CodeManager cm = new CodeManager();
		CodeProcessor.codeReader(path, cm);
		
		for(PyEntity en: cm.getEntities()){
			addEntity(en);
		}
		
		model.setAsksAllowsChildren(true);
		
		JMenuBar menuBar = new JMenuBar();
        
        JMenu genMenu = new JMenu("Generate code");
        
        genMenu.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
                CodeProcessor.codeWriter(cm, path);             
            }           
        });
         
        JMenu addClassMenu = new JMenu("Add class");
        
        addClassMenu.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
            	String name = JOptionPane.showInputDialog("Enter CLASS name");
            	cm.addEntity(new PyClass(name));
            	addEntity(new PyClass(name));
            	structTree.updateUI();
            }           
        });
        
        JMenu addFuncMenu = new JMenu("Add function");
        
        addFuncMenu.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
            	String name = JOptionPane.showInputDialog("Enter FUNCTION name");
            	cm.addEntity(new PyFunction(name));
                addEntity(new PyFunction(name));    
                structTree.updateUI();
            }           
        });
        
        JMenu delMenu = new JMenu("Delete selected");
        
        delMenu.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
            	TreePath path = structTree.getSelectionPath();
				if (path == null)
					return;
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				removeNode(selectedNode);
				if (selectedNode != null && selectedNode.getUserObject() instanceof PyEntity) {
					PyEntity pe = (PyEntity) selectedNode.getUserObject();
					cm.deleteEntity(pe.getName());
				}
				structTree.updateUI();
            }           
        });
        

		structTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = structTree.getSelectionPath();
				if (path == null)
					return;
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (selectedNode != null && selectedNode.getUserObject() instanceof PyEntity) {
					final PyEntity selectedEn = (PyEntity) selectedNode.getUserObject();
					entitySpecificPane.removeAll();
					entitySpecificPane.setLayout(new BoxLayout(entitySpecificPane, BoxLayout.PAGE_AXIS));
					entitySpecificPane.add(selectedEn.genSpecPane());
					
					actionPane.removeAll();
					
					JButton editButton = new JButton("Edit");
					entitySpecificPane.add(editButton);
				} else {
					entitySpecificPane.removeAll();
				}
				entitySpecificPane.updateUI();
			}
		});

		structTree.setEditable(false);
		

		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) structTree.getLastSelectedPathComponent();
				if (selNode.getUserObject() instanceof SportNutrition)
					removeNode(selNode);
			}
		});
		
		setSize(1000, 700);
		setVisible(true);
	}

	public DefaultMutableTreeNode searchNode(String nodeStr) {
		DefaultMutableTreeNode node = null;
		Enumeration e = rootNode.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			node = (DefaultMutableTreeNode) e.nextElement();
			if (nodeStr.equalsIgnoreCase(node.getUserObject().toString())
					&& node.getUserObject() instanceof SportNutrition) {
				return node;
			}
		}
		return null;
	}

	public void removeNode(DefaultMutableTreeNode selNode) {
		if (selNode == null) {
			return;
		}
		MutableTreeNode parent = (MutableTreeNode) (selNode.getParent());
		if (parent == null) {
			return;
		}
		MutableTreeNode toBeSelNode = getSibling(selNode);
		if (toBeSelNode == null) {
			toBeSelNode = parent;
		}
		TreeNode[] nodes = model.getPathToRoot(toBeSelNode);
		TreePath path = new TreePath(nodes);
		structTree.scrollPathToVisible(path);
		structTree.setSelectionPath(path);
		if (ContentSystem.listOfSN.remove(selNode.getUserObject())) {
			System.out.println(selNode.getUserObject().toString() + "was removed");
		}
		model.removeNodeFromParent(selNode);
	}

	public DefaultMutableTreeNode searchFolder(String folderName) {
		DefaultMutableTreeNode node = null;
		Enumeration e = rootNode.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			node = (DefaultMutableTreeNode) e.nextElement();
			if (folderName.equalsIgnoreCase(node.getUserObject().toString())
					&& node.getUserObject() instanceof String) {
				return node;
			}
		}
		return null;
	}

	private MutableTreeNode getSibling(DefaultMutableTreeNode selNode) {
		MutableTreeNode sibling = (MutableTreeNode) selNode.getPreviousSibling();
		if (sibling == null) {
			sibling = (MutableTreeNode) selNode.getNextSibling();
		}
		return sibling;
	}

}
