package start;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.CodeTree;

public class Start {

	public static void main(String[] args) {
		String path = new String();
		JFileChooser fileopen = new JFileChooser();
		int ret = fileopen.showDialog(null, "Open file");                
		if (ret == JFileChooser.APPROVE_OPTION) {
			path = fileopen.getSelectedFile().getAbsolutePath();
		}
		
		CodeTree ct = new CodeTree(path);
	}

}
