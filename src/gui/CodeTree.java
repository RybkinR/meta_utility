package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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

public class CodeTree{
	
	//final static String TEST_PATH = "C:\\Users\\Roman\\workspace\\Metaprogramming\\MetaData.py";
	
	private static final long serialVersionUID = 1L;
	
	JFrame frame = new JFrame("Meta utility");

	DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Code structure");

	private DefaultTreeModel model = new DefaultTreeModel(rootNode);

	private JTree structTree = new JTree(model);

	JScrollPane scrollPaneStructure = new JScrollPane(structTree);

	JPanel entitySpecificPane = new JPanel();
	
	void addEntity(PyEntity ent) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ent);
		newNode.setAllowsChildren(false);
		this.rootNode.add(newNode);
	}
	
	

	public CodeTree(String path) {
		model.setAsksAllowsChildren(true);
		CodeManager cm = new CodeManager();
		CodeProcessor.codeReader(path, cm);
		
		structTree.setRootVisible(true);
		
		for(PyEntity en: cm.getEntities()){
			System.out.println(en.getName());
			addEntity(en);
		}

		JMenuBar menuBar = new JMenuBar();
        
        JMenuItem genMenu = new JMenuItem("Generate code");
        
        genMenu.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
            	String newPath = new String();
        		JFileChooser fileopen = new JFileChooser();
        		int ret = fileopen.showDialog(null, "Open file");                
        		if (ret == JFileChooser.APPROVE_OPTION) {
        			newPath = fileopen.getSelectedFile().getAbsolutePath();
        		}
                CodeProcessor.codeWriter(cm, newPath);             
            }           
        });
         
        JMenuItem addClassMenu = new JMenuItem("Add class");
        
        addClassMenu.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
            	String name = JOptionPane.showInputDialog("Enter CLASS name");
            	cm.addEntity(new PyClass(name));
            	addEntity(new PyClass(name));
            	structTree.updateUI();
            }           
        });
        
        JMenuItem addFuncMenu = new JMenuItem("Add function");
        
        addFuncMenu.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
            	String name = JOptionPane.showInputDialog("Enter FUNCTION name");
            	cm.addEntity(new PyFunction(name));
                addEntity(new PyFunction(name));    
                structTree.updateUI();
            }           
        });
        
        JMenuItem delMenu = new JMenuItem("Delete selected");
        
        delMenu.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
            	TreePath path = structTree.getSelectionPath();
				if (path == null)
					return;
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				removeNode(selectedNode, cm);
				if (selectedNode != null && selectedNode.getUserObject() instanceof PyEntity) {
					PyEntity pe = (PyEntity) selectedNode.getUserObject();
					cm.deleteEntity(pe.getName());
				}
				structTree.updateUI();
            }           
        });
        
        menuBar.add(genMenu);
        menuBar.add(addClassMenu);
        menuBar.add(addFuncMenu);
        menuBar.add(delMenu);
        

		structTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = structTree.getSelectionPath();
				if (path == null)
					return;
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (selectedNode != null && selectedNode.getUserObject() instanceof PyEntity) {
					PyEntity selectedEn = (PyEntity) selectedNode.getUserObject();
					entitySpecificPane.removeAll();
					entitySpecificPane.add(selectedEn.genSpecPane(cm));
				} else {
					entitySpecificPane.removeAll();
				}
				entitySpecificPane.updateUI();
			}
		});

		
		structTree.setEditable(false);

		frame.setLayout(new GridLayout(1, 2));
		frame.add(scrollPaneStructure);
		frame.add(entitySpecificPane);
		frame.setJMenuBar(menuBar);
		frame.setSize(1000, 700);
		frame.setVisible(true);
	}

	
	public void removeNode(DefaultMutableTreeNode selNode, CodeManager cm) {
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
		PyEntity pe = (PyEntity) selNode.getUserObject();
		cm.deleteEntity(pe.getName());
		model.removeNodeFromParent(selNode);
	}

	private MutableTreeNode getSibling(DefaultMutableTreeNode selNode) {
		MutableTreeNode sibling = (MutableTreeNode) selNode.getPreviousSibling();
		if (sibling == null) {
			sibling = (MutableTreeNode) selNode.getNextSibling();
		}
		return sibling;
	}
	/*
	public static void main(String[] args) {
		
		CodeTree ct = new CodeTree(TEST_PATH);
	}
*/
}
