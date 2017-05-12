package support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileWorker {
	public static ArrayList<String> fileReader(String path) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        ArrayList<String> lines = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
	}
	
	public static void fileWriter(String path, String text) throws FileNotFoundException{
        PrintWriter out = new PrintWriter(new File(path).getAbsoluteFile());
        try {
            out.print(text);
        } finally {
            out.close();
        }
	}
}
